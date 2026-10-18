package com.lwt.tavern.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.entity.EntityStarCount;

public interface EntityStarCountService extends IService<EntityStarCount> {
    Result getStarCount(Long id);
}
