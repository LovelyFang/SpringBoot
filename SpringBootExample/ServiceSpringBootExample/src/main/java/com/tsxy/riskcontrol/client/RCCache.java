package com.tsxy.riskcontrol.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import com.tsxy.riskcontrol.client.model.RCData;
import com.tsxy.riskcontrol.client.util.HashUtil;
import com.tsxy.riskcontrol.client.util.SecTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * 给予jedis封装的缓存操作工具累
 * @Author Liu_df
 * @Date 2024/9/29 14:28
 */
@Component
public class RCCache {

    @Autowired
    @Qualifier("rcJedisPool")
    private JedisPool jedisPool;

    public RCCache() {
        // empty
    }

    public RCCache(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public String mapGet(String key, String field) {
        Jedis jedis = jedisPool.getResource();

        try {
            return jedis.hget(key, field);
        } finally {
            jedis.close();
        }
    }

    /**
     * 对于每个RCData，都会有一个锚定的时间戳 该时间戳用于登记在分布式节点范围内同一时间窗口的数据
     */
    public RCData getWithTimeMark(String cacheSource, long expireTime) {

        String baseKey = baseKey(cacheSource);
        String realCacheKey = cacheKey(baseKey);
        String timeCacheKey = timeCacheKey(baseKey);
        String stickCacheKey = stickCacheKey(baseKey);

        try (Jedis jedis = jedisPool.getResource()) {
            long currentTimeTs = SecTimeUtil.currentSec();
            String timePoint = String.valueOf(currentTimeTs);
            jedis.setnx(timeCacheKey, timePoint);

            Pipeline pipe = jedis.pipelined();

            Response<String> timeInCacheResp = pipe.get(timeCacheKey);
            Response<String> touchTimesInCacheResp = pipe.get(realCacheKey);
            Response<Set<String>> stickTimesInCacheResp = pipe.smembers(stickCacheKey);

            pipe.sync();

            String timeInCache = timeInCacheResp.get();
            long timePointInCache = NumberUtils.toLong(timeInCache, currentTimeTs);
            if (timePointInCache == currentTimeTs) {

                // 时间戳是当前节点设置的，需要设置过期时间
                jedis.expireAt(timeCacheKey, currentTimeTs + expireTime);
            }
            return new RCData(baseKey, timePointInCache, NumberUtils.toInt(touchTimesInCacheResp.get(), 0), stickTimesInCacheResp.get(), expireTime);
        }
    }

    public long ensureTouchTs(String baseKey, long touchTs, long expireTime) {

        long exactTouchTs = touchTs;

        try (Jedis jedis = jedisPool.getResource()) {
            String timeCacheKey = timeCacheKey(baseKey);
            String timepoint = String.valueOf(touchTs);
            long retVal = jedis.setnx(timeCacheKey, timepoint);
            // 表示已经存在
            if (retVal == 0) {
                String touchTsInCache = jedis.get(timeCacheKey);
                if (StringUtils.isEmpty(touchTsInCache)) {
                    // 仅存在于毫秒级的巧合事件
                    return ensureTouchTs(baseKey, touchTs, expireTime);
                }

                exactTouchTs = NumberUtils.toLong(touchTsInCache, -1);
            } else {
                // 初始创建，需要设置过期时间
                jedis.expireAt(timeCacheKey, expireTime);
                // 重置计时和计数器
                String dataCache = cacheKey(baseKey);
                jedis.set(dataCache, "0");
                jedis.expireAt(dataCache, expireTime);
            }

            return exactTouchTs;
        }
    }

    public String baseKey(String cacheKey) {
        return HashUtil.md5(cacheKey);
    }

    class PipeHolder {
        Jedis jedis;
        Pipeline pipe;
        List<Consumer<Boolean>> delayActionList = new ArrayList<>();


        public PipeHolder(Jedis jedis) {
            this.jedis = jedis;
            this.pipe = jedis.pipelined();
        }

        void close() {
            try {
                pipe.sync();
                pipe.close();
                delayActionList.forEach(new Consumer<Consumer<Boolean>>() {
                    @Override
                    public void accept(Consumer<Boolean> action) {
                        action.accept(true);
                    }
                });
            } finally {
                jedis.close();
            }
        }
    }
    private static ThreadLocal<PipeHolder> pipeTL = new ThreadLocal<>();
    public void startPipe() {
        pipeTL.set(new PipeHolder(jedisPool.getResource()));
    }

    public void endPipe() {
        try {
            pipeTL.get().close();
        } finally {
            pipeTL.remove();
        }
    }

    public void inc(String baseKey, long deltaInc, final Consumer<Long> consumer) {
        PipeHolder pipeH = pipeTL.get();

        String cacheKey = cacheKey(baseKey);
        if(pipeH == null) {
            Jedis resource = jedisPool.getResource();

            try {
                long retVal = resource.incrBy(cacheKey, deltaInc);
                consumer.accept(retVal);
            } finally {
                resource.close();
            }
        } else {
            final Response<Long> resp = pipeH.pipe.incrBy(cacheKey, deltaInc);
            pipeH.delayActionList.add(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean _flag) {
                    consumer.accept(resp.get());
                }
            });
        }
    }

    public void loadAndRefresh(final RCData rcData) {
        PipeHolder pipeH = pipeTL.get();

        String stickCacheKey = stickCacheKey(rcData.getBaseKey());
        if(pipeH == null) {
            Jedis resource = jedisPool.getResource();
            try {
                Set<String> set = resource.smembers(stickCacheKey);
                rcData.resetStickTimeSet(set);
                rcData.recheck();
            } finally {
                resource.close();
            }
        } else {
            final Response<Set<String>> setResp = pipeH.pipe.smembers(stickCacheKey);
            pipeH.delayActionList.add(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean _flag) {
                    rcData.resetStickTimeSet(setResp.get());
                    rcData.recheck();
                }});
        }
    }

    public void resetExpireTime(String baseKey, long banEndTime) {
        PipeHolder pipeH = pipeTL.get();

        String cacheKey = cacheKey(baseKey);
        if(pipeH == null) {
            Jedis resource = jedisPool.getResource();
            try {
                resource.expireAt(cacheKey, banEndTime);
            } finally {
                resource.close();
            }
        } else {
            pipeH.pipe.expireAt(cacheKey, banEndTime);
        }
    }

    String[] strArrTmp = new String[0];
    public void refreshStickTimes(String baseKey, long stickKeepEndTime, Set<String> stickTimeSets) {
        String stickCacheKey = stickCacheKey(baseKey);

        PipeHolder pipeH = pipeTL.get();

        if(pipeH == null) {
            try (Jedis resource = jedisPool.getResource()) {
                Pipeline pipe = resource.pipelined();
                pipe.sadd(stickCacheKey, stickTimeSets.toArray(strArrTmp));
                pipe.expireAt(stickCacheKey, stickKeepEndTime);
                pipe.sync();
            }
        } else {
            pipeH.pipe.sadd(stickCacheKey, stickTimeSets.toArray(strArrTmp));
            pipeH.pipe.expireAt(stickCacheKey, stickKeepEndTime);
        }
    }

    private String stickCacheKey(String baseKey) {
        return "rc" + ":" + "stick" + ":" + baseKey;
    }

    private String timeCacheKey(String baseKey) {
        return "rc" + ":" + "time" + ":" + baseKey;
    }

    private String cacheKey(String baseKey) {
        return "rc:" + baseKey;
    }
}