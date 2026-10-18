package com.lwt.tavern.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.common.UserHolder;
import com.lwt.tavern.dto.EntityStarDTO;
import com.lwt.tavern.entity.EntityStar;
import com.lwt.tavern.mapper.EntityStarMapper;
import com.lwt.tavern.service.EntityStarService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EntityStarServiceImpl extends ServiceImpl<EntityStarMapper, EntityStar> implements EntityStarService {


    @Override
    public Result actionStar(EntityStarDTO entityStarDTO) {

        if (entityStarDTO.getStatus() != null && entityStarDTO.getStatus() == 1) {
            //删除
            remove(
                    new LambdaQueryWrapper<EntityStar>()
                            .eq(EntityStar::getEntityId,entityStarDTO.getEntityId())
                            .eq(EntityStar::getUserId, UserHolder.getUser().getId())
            );
        } else {
            //添加
            EntityStar entityStar = BeanUtil.copyProperties(entityStarDTO, EntityStar.class);
            entityStar.setCreateTime(LocalDateTime.now());
            entityStar.setUpdateTime(LocalDateTime.now());
            save(entityStar);
        }

        return Result.success();
    }
}
