package com.lwt.tavern.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.entity.EntityView;

public interface EntityViewService extends IService<EntityView> {
    Result addView(Long id);
}
