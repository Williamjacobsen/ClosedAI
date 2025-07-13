package com.closedai.closedai.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

@Component
public class RedisSubscriber {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @PostConstruct // runs in a new thread after the app is started
    public void init() {

        new Thread(() -> {

            try (
                Jedis jedis = new Jedis(redisHost, redisPort)
            ) {
                
                jedis.subscribe(new JedisPubSub() {
                    
                    @Override
                    public void onMessage(String channel, String message) {
                        System.out.println("Received from " + channel + ": " + message);
                    }

                }, "prompt_channel", "response_channel");

            } catch (Exception e) {
                System.err.println("Redis subscription error: " + e.getMessage());
            }

        }).start();

    }

}
