package com.closedai.closedai.redis;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisPublisher {

    private final JedisPool jedisPool;

    public RedisPublisher(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public void publish(String channel, String message) {
        try (Jedis jedis = jedisPool.getResource()) {

            if (isNullOrEmpty(message)) {
                System.out.println("Channel or message is null. Publish aborted.");
                return;
            }

            jedis.publish(channel, message);
        }
    }
    
}
