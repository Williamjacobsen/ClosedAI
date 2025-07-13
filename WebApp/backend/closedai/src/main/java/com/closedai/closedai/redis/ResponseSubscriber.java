package com.closedai.closedai.redis;

import java.nio.charset.StandardCharsets;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import com.closedai.closedai.sse.SseEmitterService;

@Component
public class ResponseSubscriber implements MessageListener {

    private final SseEmitterService sse;

    public ResponseSubscriber(SseEmitterService sse) {
        this.sse = sse;
    }

    /** called by Spring Data Redis whenever a new pub/sub message arrives */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);
        sse.broadcast(payload);               // push straight out over SSE
    }
}
