package jit.wxs.demo.config;

import jit.wxs.demo.utils.jedis.JedisClientPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

/**
 * @author jitwxs
 * @date 2018/4/19 23:05
 */
@Configuration
public class JedisConfig {
    @Value("${redis.standalone.host}")
    private String STANDALONE_HOST;

    @Value("${redis.standalone.port}")
    private Integer STANDALONE_PORT;

    @Value("${redis.standalone.password}")
    private String STANDALONE_PASSWORD;

    @Bean
    public JedisClientPool jedisClientPool() {
        JedisClientPool jedisClientPool = new JedisClientPool();
        jedisClientPool.setJedisPool(jedisPool());

        return jedisClientPool;
    }

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool(new GenericObjectPoolConfig(), STANDALONE_HOST, STANDALONE_PORT,
                Protocol.DEFAULT_TIMEOUT, STANDALONE_PASSWORD, Protocol.DEFAULT_DATABASE, null);
    }
}