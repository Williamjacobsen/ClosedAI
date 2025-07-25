package com.closedai.closedai.session;

import java.time.Duration;
import java.util.UUID;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Transactional(readOnly = true)
    public SessionEntity getSession(String sessionId) {
        
        if (sessionId != null) {
            SessionEntity existing = sessionRepository.findBySessionId(sessionId);
            if (existing != null) {
                sessionRepository.updateLastActive(existing.getSessionId());
                return existing;
            }
        }

        return null;
    }

    @Transactional
    private SessionEntity createSession(HttpServletResponse response) {
        
        String newSessionId = UUID.randomUUID().toString();
        SessionEntity newSession = new SessionEntity(newSessionId);
        sessionRepository.save(newSession);

        ResponseCookie cookie = ResponseCookie.from("SESSION_ID", newSessionId)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(Duration.ofDays(3650))
            .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return newSession;
    }

    @Transactional
    public SessionEntity getOrCreateSession(String sessionId, HttpServletResponse response) {
        
        SessionEntity session = getSession(sessionId);

        if (session != null) {
            return session;
        }

        return createSession(response);
    }

}
