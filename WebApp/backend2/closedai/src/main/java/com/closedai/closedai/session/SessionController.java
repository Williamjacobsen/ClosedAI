package com.closedai.closedai.session;

import java.time.Duration;
import java.util.UUID;

import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class SessionController {

    private final SessionRepository sessionRepository;

    public SessionController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/session")
    public SessionResponseDTO getOrCreateSession(
        @CookieValue(value="SESSION_ID", required=false) String cookieSessionId,
        HttpServletResponse response
    ) {
        
        if (cookieSessionId != null)
        {
            SessionEntity session = sessionRepository.findBySessionId(cookieSessionId);
            if (session != null) {
                sessionRepository.updateLastActive(session.getSessionId());

                return new SessionResponseDTO(session.getSessionId());
            }
        }

        String newSessionId = UUID.randomUUID().toString();
        SessionEntity sessionEntity = new SessionEntity(newSessionId);
        sessionRepository.save(sessionEntity);

        ResponseCookie cookie = ResponseCookie.from("SESSION_ID", newSessionId)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(Duration.ofDays(3650))
            .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return new SessionResponseDTO(newSessionId);
    }
    
}
