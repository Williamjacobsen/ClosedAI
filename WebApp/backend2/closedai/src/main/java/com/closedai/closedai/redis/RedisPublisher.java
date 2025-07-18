package com.closedai.closedai.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisPublisher {

    private final static Logger logger = LoggerFactory.getLogger(RedisPublisher.class);

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
                logger.error("Channel or message is null. Publish aborted.");
                return;
            }

            jedis.publish(channel, message);
        }
    }
    
}
