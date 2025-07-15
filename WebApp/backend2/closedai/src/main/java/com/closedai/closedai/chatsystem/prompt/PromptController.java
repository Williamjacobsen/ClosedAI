package com.closedai.closedai.chatsystem.prompt;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.closedai.closedai.redis.RedisPublisher;
import com.closedai.closedai.session.SessionService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/prompt")
public class PromptController {

    private final SessionService sessionService;
    private final RedisPublisher redisPublisher;

    public PromptController(
        SessionService sessionService,
        RedisPublisher redisPublisher
    ) {
        this.sessionService = sessionService;
        this.redisPublisher = redisPublisher;
    }
    
    @PostMapping("/send")
    public ResponseEntity<String> prompt(
        @RequestBody PromptRequestDTO requestBody,
        @CookieValue(name = "SESSION_ID", required = false) String cookieSessionId,
        HttpServletResponse response
    ) {

        String prompt = requestBody.getPrompt();

        String sessionId = sessionService.getOrCreateSession(cookieSessionId, response).getSessionId();

        redisPublisher.publish("prompt_channel", prompt);

        return ResponseEntity.ok(String.format("Received prompt from session { session = %s, prompt = %s }", sessionId, prompt));
    }

}
