package com.lwt.tavern.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.EntityLikeDTO;
import com.lwt.tavern.entity.EntityLike;

public interface EntityLikeService extends IService<EntityLike> {
    Result actionLike(EntityLikeDTO entityLikeDTO);
}
