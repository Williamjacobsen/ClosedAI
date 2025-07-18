package com.closedai.closedai.chatsystem.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.closedai.closedai.sse.SseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GetResponseSseController {
    
    private final static Logger logger = LoggerFactory.getLogger(GetResponseSseController.class);

    private final SseService sseService;

    public GetResponseSseController(SseService sseService) {
        this.sseService = sseService;
    }

    public record messageToJson(String sessionId, String chatSessionName, String response) {}

    /** Called from RedisSubscriber */
    public void process(String message) {
        logger.info("Processing response: " + message);

        ObjectMapper objectMapper = new ObjectMapper();
        messageToJson data;
        try {
            data = objectMapper.readValue(message, messageToJson.class);        
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return;
        }

        sseService.sendSse(data.sessionId(), data.response());
    }

}
