package com.lwt.tavern.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.component.LikeRedisService;
import com.lwt.tavern.dto.EntityLikeDTO;
import com.lwt.tavern.entity.EntityLike;
import com.lwt.tavern.mapper.EntityLikeMapper;
import com.lwt.tavern.service.EntityLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntityLikeServiceImpl extends ServiceImpl<EntityLikeMapper,EntityLike> implements EntityLikeService {

    private final StringRedisTemplate redisTemplate;
    private final LikeRedisService likeRedisService;

    @Override
    public Result actionLike(EntityLikeDTO entityLikeDTO) {


        return Result.success();
    }
}
