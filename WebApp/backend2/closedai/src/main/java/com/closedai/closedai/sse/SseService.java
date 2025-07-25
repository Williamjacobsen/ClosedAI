package com.closedai.closedai.sse;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseService {

    private static final Logger logger = LoggerFactory.getLogger(SseService.class);
    
    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>(); // Thread safe compared to a regular hashmap (race conditions etc.) 

    public SseEmitter addEmitter(String sessionId) {

        SseEmitter emitter = new SseEmitter(600_000L);

        emitter.onCompletion(() -> {
            emitters.remove(sessionId);
            logger.info("SSE completed for sessionId: {}", sessionId);
        });
        emitter.onTimeout(() -> emitters.remove(sessionId));
        emitter.onError((_) -> emitters.remove(sessionId));

        emitters.put(sessionId, emitter);
        return emitter;
    }

    public boolean sendSse(String sessionId, String message) {
        
        SseEmitter emitter = emitters.get(sessionId);

        if (emitter == null) {
            logger.error("Emitter is null for sessionId = " + sessionId);
            return false;
        }

        try {
            emitter.send(
                SseEmitter.event()
                    .name("response")
                    .data(message)
                );

        } catch (IOException e) { 

            emitter.completeWithError(e);
            emitters.remove(sessionId);

            return false;
        }
        
        return true;
    }

    public void removeEmitter(String sessionId) {
        emitters.remove(sessionId);
    }

}
