package com.closedai.closedai.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

@Component
public class RedisPublisher {

    private final Jedis jedis;

    public RedisPublisher(
        @Value("${spring.data.redis.host}") String redisHost,
        @Value("${spring.data.redis.port}") int redisPort
    ) {
        this.jedis = new Jedis(redisHost, redisPort);
    }

    public void publish(String channel, String message) {
        jedis.publish(channel, message);
    }
    
}
