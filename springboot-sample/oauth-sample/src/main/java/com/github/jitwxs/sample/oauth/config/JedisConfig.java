package com.github.jitwxs.sample.oauth.config;

import com.github.jitwxs.sample.oauth.utils.jedis.JedisClientPool;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

/**
 * @author jitwxs
 * @date 2018/4/19 23:05
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "redis.standalone")
public class JedisConfig {
    private String host;

    private Integer port;

    private String password;

    @Bean
    public JedisClientPool jedisClientPool() {
        JedisClientPool jedisClientPool = new JedisClientPool();
        jedisClientPool.setJedisPool(jedisPool());

        return jedisClientPool;
    }

    @Bean
    public JedisPool jedisPool() {
        if(StringUtils.isEmpty(password)) {
            return new JedisPool(new GenericObjectPoolConfig(), host, port, Protocol.DEFAULT_TIMEOUT);
        } else {
            return new JedisPool(new GenericObjectPoolConfig(), host, port, Protocol.DEFAULT_TIMEOUT, password, Protocol.DEFAULT_DATABASE, null);
        }
    }
}