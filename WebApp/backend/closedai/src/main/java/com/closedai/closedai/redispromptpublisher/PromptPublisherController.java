package com.closedai.closedai.redispromptpublisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/prompt")
public class PromptPublisherController {

    @Autowired
    private RedisPublisher redisPublisher;

    // ObjectMapper is the standard Jackson JSON serializer/deserializer
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/send")
    public String sendPrompt(@RequestBody PromptRequest request) {
        try {
            // Convert Java object to JSON string safely
            String message = objectMapper.writeValueAsString(request);

            // Publish to Redis
            redisPublisher.publish(message);

            return "Prompt published!";
        } catch (JsonProcessingException e) {
            return "Failed to serialize message: " + e.getMessage();
        }
    }
}
