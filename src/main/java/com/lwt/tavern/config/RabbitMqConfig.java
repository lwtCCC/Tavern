package com.lwt.tavern.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    //点赞
    public static final String LIKE_EXCHANGE = "like.exchange";
    public static final String LIKE_KEY = "like.key";
    public static final String LIKE_QUEUE = "like.queue";

    //收藏
    public static final String STAR_EXCHANGE = "star.exchange";
    public static final String STAR_KEY = "star.key";
    public static final String STAR_QUEUE = "star.queue";

    @Bean
    public DirectExchange likeExchange() {
        return new DirectExchange(LIKE_EXCHANGE, true, false);
    }

    @Bean
    public Queue likeQueue() {
        return QueueBuilder.durable(LIKE_QUEUE).build();
    }

    @Bean
    public Binding likeBinding(Queue likeQueue, DirectExchange likeExchange) {
        return BindingBuilder.bind(likeQueue).to(likeExchange).with(LIKE_KEY);
    }


    @Bean
    public DirectExchange starExchange() {
        return new DirectExchange(STAR_EXCHANGE, true, false);
    }

    @Bean
    public Queue starQueue() {
        return QueueBuilder.durable(STAR_QUEUE).build();
    }

    @Bean
    public Binding starBinding(Queue starQueue, DirectExchange starExchange) {
        return BindingBuilder.bind(starQueue).to(starExchange).with(STAR_KEY);
    }

    // 消息体用 JSON 序列化
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
