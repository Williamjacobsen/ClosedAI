package com.closedai.closedai.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Configuration
public class RedisConfig {

    private void crashIfNoConnection(JedisPool jedisPool) {

        try (Jedis jedis = jedisPool.getResource()) {
            
            if (!"PONG".equalsIgnoreCase(jedis.ping())) {
                throw new RuntimeException("Unable to connect to Redis: Invalid PING response");
            }

        } catch (Exception e) {
            throw new RuntimeException("Unable to connect to Redis at startup: " + e.getMessage(), e);
        }

    }

    @Bean
    public JedisPool jedisPool(
        @Value("${spring.data.redis.host}") String redisHost,
        @Value("${spring.data.redis.port}") int redisPort
    ) {
        JedisPool jedisPool = new JedisPool(redisHost, redisPort);

        crashIfNoConnection(jedisPool);

        return jedisPool;
    }
    
}
