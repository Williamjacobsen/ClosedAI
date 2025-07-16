package com.closedai.closedai.redis;

import java.time.Duration;

import org.springframework.stereotype.Component;

import com.closedai.closedai.chatsystem.response.GetResponseSseController;

import jakarta.annotation.PostConstruct;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

@Component
public class RedisSubscriber {

    private final JedisPool jedisPool;
    private final GetResponseSseController handleResponse;

    public RedisSubscriber(JedisPool jedisPool, GetResponseSseController handleResponse) {
        this.jedisPool = jedisPool;
        this.handleResponse = handleResponse;
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
                            handleResponse.process(message);
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
