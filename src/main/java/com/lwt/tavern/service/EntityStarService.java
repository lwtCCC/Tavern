package com.lwt.tavern.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.EntityStarDTO;
import com.lwt.tavern.entity.EntityStar;

public interface EntityStarService extends IService<EntityStar> {
    Result actionStar(EntityStarDTO entityStarDTO);
}
