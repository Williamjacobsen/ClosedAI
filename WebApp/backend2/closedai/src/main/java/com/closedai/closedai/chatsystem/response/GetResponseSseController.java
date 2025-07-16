package com.closedai.closedai.chatsystem.response;

import org.springframework.stereotype.Component;

import com.closedai.closedai.sse.SseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GetResponseSseController {
    
    private final SseService sseService;

    public GetResponseSseController(SseService sseService) {
        this.sseService = sseService;
    }

    public record messageToJson(String sessionId, String response) {}

    /** Called from RedisSubscriber */
    public void process(String message) {
        System.out.println("Processing response: " + message);

        ObjectMapper objectMapper = new ObjectMapper();
        messageToJson data;
        try {
            data = objectMapper.readValue(message, messageToJson.class);        
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println(String.format(
            "Processing response JSON: { sessionId = %s, response = %s }", 
            data.sessionId(), data.response()
            ));

        sseService.sendSse(data.sessionId(), data.response());
    }

}
