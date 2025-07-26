package com.closedai.closedai.chatsystem.history;

import java.util.List;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.closedai.closedai.session.SessionService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ChatHistoryController {

    private final SessionService sessionService;
    private final ChatHistoryRepository chatHistoryRepository;
    private final ChatHistoryMapper chatHistoryMapper;

    public ChatHistoryController(
            SessionService sessionService,
            ChatHistoryRepository chatHistoryRepository,
            ChatHistoryMapper chatHistoryMapper
    ) {
        this.sessionService = sessionService;
        this.chatHistoryRepository = chatHistoryRepository;
        this.chatHistoryMapper = chatHistoryMapper;
    }

    @GetMapping("/get-chat-history")
    public List<ChatHistoryDTO> test(
            @CookieValue(value = "SESSION_ID", required = true) String cookieSessionId,
            HttpServletResponse response
    ) {
        String sessionId = sessionService.getOrCreateSession(cookieSessionId, response).getSessionId();

        List<ChatHistoryDTO> messages = chatHistoryMapper.toDto(chatHistoryRepository.findAllBySessionId(sessionId));

        return messages;
    }

}
