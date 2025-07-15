package com.closedai.closedai.redis;

import java.time.Duration;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

@Component
public class RedisSubscriber {

    private final JedisPool jedisPool;

    public RedisSubscriber(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @PostConstruct
    public void init() {
        new Thread(() -> {

            while (true) {
                try (Jedis jedis = jedisPool.getResource()) {

                    jedis.subscribe(new JedisPubSub() {

                        @Override
                        public void onMessage(String channel, String message) {
                            System.out.println("Received from " + channel + ": " + message);
                        }

                    }, "prompt_channel", "response_channel");

                } catch (Exception e) {
                    System.err.println("Redis subscription error: " + e.getMessage());

                    try {
                        Thread.sleep(Duration.ofSeconds(5));
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }

                }
            }

        }).start();
    }
    
}
