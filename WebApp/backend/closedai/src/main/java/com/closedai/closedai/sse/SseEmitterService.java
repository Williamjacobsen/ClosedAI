package com.closedai.closedai.sse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseEmitterService {

    /** keep a thread-safe list of live connections */
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    /** create a new emitter and register life-cycle callbacks */
    public SseEmitter create() {
        // 30-minute timeout; tweak as you like
        SseEmitter emitter = new SseEmitter(30 * 60 * 1_000L);

        emitters.add(emitter);

        emitter.onCompletion(()   -> emitters.remove(emitter));
        emitter.onTimeout   (()   -> emitters.remove(emitter));
        emitter.onError     (e    -> emitters.remove(emitter));

        return emitter;
    }

    /** push a message to every still-alive emitter */
    public void broadcast(String json) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("response").data(json));
            } catch (IOException | IllegalStateException ex) {
                emitter.complete();
            }
        }
    }
}
