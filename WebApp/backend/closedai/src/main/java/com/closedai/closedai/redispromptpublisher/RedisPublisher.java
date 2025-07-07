package com.closedai.closedai.redispromptpublisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service // Marks this class as a Spring-managed service (i.e., a business logic component)
public class RedisPublisher {

    @Autowired // Spring will inject the Redis template here (used to talk to Redis)
    private StringRedisTemplate redisTemplate;

    private static final String CHANNEL = "prompt_channel"; // Redis channel name

    // Publishes the message to the Redis channel
    public void publish(String message) {
        redisTemplate.convertAndSend(CHANNEL, message); // Redis pub/sub command
        System.out.println("Published: " + message); // For logging/debugging
    }
}
