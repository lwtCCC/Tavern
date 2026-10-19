package com.lwt.tavern.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.entity.EntityHot;
import com.lwt.tavern.mapper.EntityHotMapper;
import com.lwt.tavern.service.EntityHotService;
import org.springframework.stereotype.Service;

@Service
public class EntityHotServiceImpl extends ServiceImpl<EntityHotMapper, EntityHot> implements EntityHotService {


    @Override
    public Result getHot(Long id) {

        //根据点赞数，阅览量，被搜索次数，收藏数计算热度

        return null;
    }
}
