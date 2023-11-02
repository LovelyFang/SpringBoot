package com.tsxy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SpringBootExampleTestApplicationTests {

    /**
     * 会把所有都当做对象, 混合别的可能会有问题
     */
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Test
    void testSpringBootRedisTemplateClass() {

        String cacheKey = "Action";

        ValueOperations cacheValue = redisTemplate.opsForValue();
        cacheValue.set(cacheKey, "action", 1, TimeUnit.MILLISECONDS);
        System.out.println(cacheValue.get(cacheKey));

        SetOperations cacheSet = redisTemplate.opsForSet();
        Set<String> cacheSetValue = new HashSet<>();
        cacheSetValue.add("1");
        cacheSet.add(cacheKey, cacheSetValue);
        System.out.println(cacheSet.members(cacheKey));
        cacheSet.remove(cacheKey, cacheSetValue);
    }

    @Test
    void testSpringBootStringRedisTemplateClass() {

        String cacheKey = "Action";
        ValueOperations<String, String> cacheValue = stringRedisTemplate.opsForValue();

        cacheValue.set(cacheKey, "action", 6, TimeUnit.SECONDS);
        System.out.println(cacheValue.get(cacheKey));

        cacheKey = "redisSetCacheKey";
        SetOperations<String, String> cacheSet = stringRedisTemplate.opsForSet();
        cacheSet.add(cacheKey, "1");
        System.out.println(cacheSet.members(cacheKey));
        cacheSet.remove(cacheKey, "1");


    }

}
