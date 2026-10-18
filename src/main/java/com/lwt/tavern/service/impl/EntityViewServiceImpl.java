package com.lwt.tavern.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.entity.EntityView;
import com.lwt.tavern.mapper.EntityViewMapper;
import com.lwt.tavern.service.EntityViewService;
import org.springframework.stereotype.Service;

@Service
public class EntityViewServiceImpl extends ServiceImpl<EntityViewMapper,EntityView> implements EntityViewService {
    @Override
    public Result addView(Long id) {

        lambdaUpdate()
                .setSql("view = view + 1")
                .eq(EntityView::getEntityId, id);

        return Result.success();
    }
}
