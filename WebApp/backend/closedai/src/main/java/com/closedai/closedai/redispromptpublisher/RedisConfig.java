package com.closedai.closedai.redispromptpublisher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration // Marks this class as containing configuration (beans, templates, etc.)
public class RedisConfig {

    // Registers a RedisTemplate that Spring can inject wherever needed
    // This template is used to interact with Redis using Strings (as keys and values)
    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }
}
