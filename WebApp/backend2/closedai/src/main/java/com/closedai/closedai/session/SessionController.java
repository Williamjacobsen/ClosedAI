package com.closedai.closedai.session;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/session")
    public SessionResponseDTO getOrCreateSession(
        @CookieValue(value="SESSION_ID", required=false) String cookieSessionId,
        HttpServletResponse response
    ) {
        
        SessionEntity session = sessionService.getOrCreateSession(cookieSessionId, response);

        return new SessionResponseDTO(session.getSessionId());
    }
    
}
