package com.closedai.closedai.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.closedai.closedai.session.SessionService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class SseController {

    private final static Logger logger = LoggerFactory.getLogger(SseController.class);

    private final SseService sseService;
    private final SessionService sessionService;

    public SseController(SseService sseService, SessionService sessionService) {
        this.sseService = sseService;
        this.sessionService = sessionService;
    }
    
    @GetMapping(
        path="/get-response", 
        produces=MediaType.TEXT_EVENT_STREAM_VALUE
    )
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public SseEmitter subscribe(
        @CookieValue(value="SESSION_ID", required=true) String cookieSessionId,
        HttpServletResponse response
    ) {

        String sessionId = sessionService.getOrCreateSession(cookieSessionId, response).getSessionId();

        logger.info("Handling SSE for User Session ID = " + sessionId);

        SseEmitter emitter = sseService.addEmitter(sessionId);

        sseService.sendSse(sessionId, "Connection Established...");
        
        return emitter;
    }

}
