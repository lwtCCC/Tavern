package com.lwt.tavern.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.entity.EntityLikeCount;
import com.lwt.tavern.mapper.EntityLikeCountMapper;
import com.lwt.tavern.service.EntityLikeCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntityLikeCountServiceImpl extends ServiceImpl<EntityLikeCountMapper,EntityLikeCount> implements EntityLikeCountService {


    @Override
    public Result getLikeCount(Long id) {

        EntityLikeCount one = lambdaQuery().eq(EntityLikeCount::getEntityId, id).one();

        return Result.success(one.getLikeCount());
    }


}
