package com.lwt.tavern.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.jsontype.PolymorphicTypeValidator;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        GenericJacksonJsonRedisSerializer jsonSerializer = createJsonSerializer();

        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * 创建 JSON 序列化器，启用类型信息以便对象能正确反序列化回原始类型。
     */
    private GenericJacksonJsonRedisSerializer createJsonSerializer() {
        // 限制允许反序列化的类型范围，避免安全风险
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.lwt.tavern.")   // 你的项目包名
                .allowIfSubType("java.util.")         // 集合类型
                .allowIfSubType("java.time.")         // 时间类型
                .allowIfSubType("java.lang.")         // 基本包装类
                .build();

        return GenericJacksonJsonRedisSerializer.builder()
                .enableDefaultTyping(ptv)             // 启用类型信息，并限定范围
                .typePropertyName("@class")           // 保持与旧版一致的属性名
                .build();
    }
}
