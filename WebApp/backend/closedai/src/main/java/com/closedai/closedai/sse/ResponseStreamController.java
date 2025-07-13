package com.closedai.closedai.sse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
public class ResponseStreamController {

    private final SseEmitterService sse;

    public ResponseStreamController(SseEmitterService sse) {
        this.sse = sse;
    }

    @GetMapping("/responses")
    public SseEmitter stream() {
        return sse.create();
    }
}
