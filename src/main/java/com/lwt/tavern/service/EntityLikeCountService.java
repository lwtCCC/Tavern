package com.lwt.tavern.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.entity.EntityLikeCount;

public interface EntityLikeCountService extends IService<EntityLikeCount> {
    Result getLikeCount(Long id);
}
