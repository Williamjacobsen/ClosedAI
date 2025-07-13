package com.closedai.closedai.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisSubscriberConfig {

    private static final String CHANNEL = "response_channel";

    @Bean
    @SuppressWarnings("unused")
    MessageListenerAdapter responseAdapter(ResponseSubscriber sub) {
        // "onMessage" is the method we implemented above
        return new MessageListenerAdapter(sub, "onMessage");
    }

    @Bean
    @SuppressWarnings("unused")
    RedisMessageListenerContainer container(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter responseAdapter) {

        RedisMessageListenerContainer c = new RedisMessageListenerContainer();
        c.setConnectionFactory(connectionFactory);
        c.addMessageListener(responseAdapter, new PatternTopic(CHANNEL));
        return c;
    }
}
