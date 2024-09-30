package com.tsxy.riskcontrol.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import com.tsxy.riskcontrol.client.model.RCStatus;
import com.tsxy.riskcontrol.client.model.RCThreshold;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

/**
 * 通过异步加载的方式对combKey（module:identifier:key）的风控阈值项进行缓存管理
 * 缓存时效: 1min
 * @Author Liu_df
 * @Date 2024/9/29 14:40
 */
@Component
public class RCConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RCCache rcCache;

    @Value("${rc.threshold.cache.refresh.interval:10}")
    private int cacheRefreshInterval = 10;

    private Cache<String, List<RCThreshold>> cacheHolder;

    private Cache<String, RCStatus> statusCacheHolder;

    public RCConfig() {
        // empty
    }

    public RCConfig(RCCache rcCache, int cacheRefreshInterval) {
        this.rcCache = rcCache;
        this.cacheRefreshInterval = cacheRefreshInterval;
    }

    @PostConstruct
    public void init() {
        cacheHolder = CacheBuilder.newBuilder()
                .refreshAfterWrite(cacheRefreshInterval, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<RCThreshold>>(){
                    @Override
                    public List<RCThreshold> load(String key) throws Exception {
                        return loadThresholds(key);
                    }
                });

        statusCacheHolder = CacheBuilder.newBuilder()
                .refreshAfterWrite(cacheRefreshInterval, TimeUnit.SECONDS)
                .build(new CacheLoader<String, RCStatus>(){
                    @Override
                    public RCStatus load(String key) throws Exception {
                        return loadRCStatus(key);
                    }
                });
    }

    public List<RCThreshold> thresholds(String module, String identifier, String key) {
        final String combKey = module + ":" + identifier + ":" + key;
        try {
            return cacheHolder.get(combKey, new Callable<List<RCThreshold>>() {
                @Override
                public List<RCThreshold> call() throws Exception {
                    return loadThresholds(combKey);
                }
            });
        } catch (ExecutionException e) {
            logger.warn("error while retrieve rcthresholds from rmeote cache.", e);
        }

        return Collections.emptyList();
    }

    private List<RCThreshold> loadThresholds(String combKey) {
        if(!combKey.contains(":")) {
            throw new IllegalArgumentException("the combKey should include char ':' but get: " + combKey);
        }

        String[] keyval = combKey.split(":", 2);
        String json = rcCache.mapGet("rc.conf." + keyval[0], keyval[1]);

        if(!StringUtils.isEmpty(json)) {
            List<RCThreshold> thresholds = jsonToArr(json);

            List<RCThreshold> resultList = new ArrayList<RCThreshold>();

            for(RCThreshold rct : thresholds) {
                if(rct.assureConfig(combKey)) {
                    resultList.add(rct);
                }
            }

            logger.info("load rc config for key: {}, value: {}", combKey, resultList);
            return resultList;
        }

        return Collections.emptyList();
    }

    private List<RCThreshold> jsonToArr(String json) {
        return JSON.parseArray(json, RCThreshold.class);
    }

    private RCStatus loadRCStatus(String combKey) {
        if(!combKey.contains(":")) {
            throw new IllegalArgumentException("the combKey should include char ':' but get: " + combKey);
        }

        String[] keyval = combKey.split(":", 2);
        String val = rcCache.mapGet("rc.status." + keyval[0], keyval[1]);

        // hset rc.status.<moduole>, <identifier>, [0, 1, 2]

        if(!StringUtils.isEmpty(val)) {
            return RCStatus.fromCode(val);
        }

        return RCStatus.None;
    }

    public RCStatus getStatus(String module, String identifier) {
        final String combKey = module + ":" + identifier;
        try {
            return statusCacheHolder.get(combKey, new Callable<RCStatus>() {
                @Override
                public RCStatus call() throws Exception {
                    return loadRCStatus(combKey);
                }
            });
        } catch (ExecutionException e) {
            logger.warn("error while retrieve rcthresholds from rmeote cache.", e);
        }

        return RCStatus.LogOnly;
    }
}
