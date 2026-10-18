package com.lwt.tavern.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.entity.EntityStarCount;
import com.lwt.tavern.mapper.EntityStarCountMapper;
import com.lwt.tavern.service.EntityStarCountService;
import org.springframework.stereotype.Service;

@Service
public class EntityStarCountServiceImpl extends ServiceImpl<EntityStarCountMapper,EntityStarCount> implements EntityStarCountService {
    @Override
    public Result getStarCount(Long id) {
        EntityStarCount one = lambdaQuery().eq(EntityStarCount::getEntityId, id).one();
        return Result.success(one.getStarCount());
    }
}
