package com.tsxy.riskcontrol.client.autoconfig;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

/**
 * @Author Liu_df
 * @Date 2024/9/29 14:21
 */

@Component("rcJedisPoolConfig")
public class RcJedisPoolConfig {

    @Value("${rc.redis.host:}")
    private String host;
    @Value("${rc.redis.port:6379}")
    private int port;
    @Value("${rc.redis.password:}")
    private String password;
    @Value("${rc.redis.database:0}")
    private int database;
    @Value("${rc.redis.timeout:1000}")
    private int timeout;
    @Value("${rc.switch.accessable:true}")
    private String rcAccessable;

    @Bean
    public JedisPool rcJedisPool() {
        if(rcAccessable==null || "false".equals(rcAccessable)){
            return null;
        }
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

        if(StringUtils.isEmpty(password)) {
            password = null;
        }

        JedisPool jedisPool = new JedisPool(poolConfig, host, port, timeout, password, database);

        return jedisPool;
    }
}