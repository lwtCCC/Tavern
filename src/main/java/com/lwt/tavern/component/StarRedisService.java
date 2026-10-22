package com.lwt.tavern.component;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.lwt.tavern.common.RedisConstants.BOOK_DETAIL_KEY;

@Component
public class StarRedisService {
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    private DefaultRedisScript<Long> starScript;

    @PostConstruct
    public void init() {
        starScript = new DefaultRedisScript<>();
        starScript.setLocation(new ClassPathResource("lua/star.lua"));
        starScript.setResultType(Long.class);
    }

    /**
     * 执行点赞操作，返回 1=状态变更，0=幂等无变化
     */
    public Long execute(Long userId, Long entityId, Short type, Boolean isStar) {
        String relationKey = "star:" + type + ":" + entityId;
        String countKey = BOOK_DETAIL_KEY + entityId;
        
        return redisTemplate.execute(
                starScript,
            Arrays.asList(relationKey, countKey),
            userId.toString(),
            isStar ? "1" : "0",
            String.valueOf(System.currentTimeMillis())
        );
    }
}