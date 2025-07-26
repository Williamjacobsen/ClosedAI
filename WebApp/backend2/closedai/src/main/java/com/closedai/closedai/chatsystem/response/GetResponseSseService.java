package com.closedai.closedai.chatsystem.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.closedai.closedai.chatsystem.history.ChatHistoryService;
import com.closedai.closedai.chatsystem.history.MessageType;
import com.closedai.closedai.sse.SseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GetResponseSseService {

    private final static Logger logger = LoggerFactory.getLogger(GetResponseSseService.class);

    private final SseService sseService;
    private final ChatHistoryService chatHistoryService;

    public GetResponseSseService(SseService sseService, ChatHistoryService chatHistoryService) {
        this.sseService = sseService;
        this.chatHistoryService = chatHistoryService;
    }

    public record messageToJson(String sessionId, String chatSessionName, String response) {

    }

    /**
     * Called from RedisSubscriber
     */
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

        chatHistoryService.addToChatHistory(data.sessionId, data.response, data.chatSessionName, MessageType.RESPONSE);

        sseService.sendSse(data.sessionId(), data.response());
    }

}
