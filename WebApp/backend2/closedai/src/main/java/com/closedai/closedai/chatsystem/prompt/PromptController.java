package com.closedai.closedai.chatsystem.prompt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.closedai.closedai.chatsystem.history.ChatHistoryService;
import com.closedai.closedai.redis.RedisPublisher;
import com.closedai.closedai.session.SessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/prompt")
public class PromptController {

    private final static Logger logger = LoggerFactory.getLogger(PromptController.class);
    
    private final SessionService sessionService;
    private final RedisPublisher redisPublisher;
    private final ChatHistoryService chatHistoryService;

    public PromptController(
        SessionService sessionService,
        RedisPublisher redisPublisher,
        ChatHistoryService chatHistoryService
    ) {
        this.sessionService = sessionService;
        this.redisPublisher = redisPublisher;
        this.chatHistoryService = chatHistoryService;
    }

    public record promptRequest(String chatSessionName, String prompt) {};

    private String toJson(String chatSessionName, String prompt) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(new promptRequest(chatSessionName, prompt));
    }
    
    @PostMapping("/send")
    public ResponseEntity<String> sendPrompt(
        @RequestBody PromptRequestDTO requestBody,
        @CookieValue(name = "SESSION_ID", required = false) String cookieSessionId,
        HttpServletResponse response
    ) {
        logger.info(requestBody.toString());

        String sessionId = sessionService.getOrCreateSession(cookieSessionId, response).getSessionId();

        String prompt = requestBody.getPrompt();
        String chatSessionName = requestBody.getChatSessionName();

        chatHistoryService.addToChatHistory(sessionId, prompt, chatSessionName);

        String json;
        try {
            json = toJson(chatSessionName, prompt);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Invalid JSON input");
        }

        // TODO: chatSessionName should be tied to sessionId

        redisPublisher.publish("prompt_channel", json);

        return ResponseEntity.ok(requestBody.toString());
    }

}
