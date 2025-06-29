package com.closedai.closedai.redisqueue;

import java.util.Map;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RedisQueueService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisQueueService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public String generateNextId() {
        Long nextId = redisTemplate.opsForValue().increment("prompt:id:counter");
        return String.valueOf(nextId);
    }

    public void pushPrompt(String id, String content) {
        try {
            Map<String, String> data = Map.of("id", id, "content", content);
            String json = objectMapper.writeValueAsString(data);
            redisTemplate.opsForList().leftPush("prompt_queue", json);
            System.out.println("Pushed prompt to Redis queue.");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to enqueue prompt", e);
        }
    }

    public String getResponse(String id) {
        return redisTemplate.opsForValue().get("response:" + id);
    }
}
