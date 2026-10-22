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
public class LikeRedisService {
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    private DefaultRedisScript<Long> likeScript;

    @PostConstruct
    public void init() {
        likeScript = new DefaultRedisScript<>();
        likeScript.setLocation(new ClassPathResource("lua/like.lua"));
        likeScript.setResultType(Long.class);
    }

    /**
     * 执行点赞操作，返回 1=状态变更，0=幂等无变化
     */
    public Long execute(Long userId, Long entityId, Short type, Boolean isLike) {
        String key = "like:" + type + ":" + entityId;//like:0:
        String countKey = BOOK_DETAIL_KEY + entityId;

        if (type == 0){
            countKey = BOOK_DETAIL_KEY + entityId;
        }
        
        return redisTemplate.execute(
            likeScript,
            Arrays.asList(key, countKey),
            userId.toString(),
            isLike ? "1" : "0",
            String.valueOf(System.currentTimeMillis())
        );
    }
}