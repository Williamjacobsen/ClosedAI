package com.closedai.closedai.sse;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseService {
    
    /** TODO:
     * Have a list of emitters
     * send event to client with session_id
     */

    SseEmitter emitter;

    public SseEmitter createEmitter() {

        emitter = new SseEmitter(30_000L);

        return emitter;
    }

    public void sendSingleEvent(String data) {

        try {
            emitter.send("Hello World!");
            //emitter.complete(); // close connection
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

    }

}
