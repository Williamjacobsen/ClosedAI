package com.closedai.closedai.redis;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/redis")
public class RedisController {
    
    private final RedisPublisher redisPublisher; // dependency injection

    public RedisController(RedisPublisher redisPublisher) {
        this.redisPublisher = redisPublisher;
    }

    @PostMapping("/publish")
    public String publishMessage(@RequestParam String channel, @RequestParam String message) {
        redisPublisher.publish(channel, message);
        return "Published to " + channel;
    }
    
}
