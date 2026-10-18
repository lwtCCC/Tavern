package com.lwt.tavern.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.common.UserHolder;
import com.lwt.tavern.dto.EntityLikeDTO;
import com.lwt.tavern.entity.EntityLike;
import com.lwt.tavern.mapper.EntityLikeMapper;
import com.lwt.tavern.service.EntityLikeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EntityLikeServiceImpl extends ServiceImpl<EntityLikeMapper,EntityLike> implements EntityLikeService {
    @Override
    public Result actionLike(EntityLikeDTO entityLikeDTO) {

        if (entityLikeDTO.getStatus() != null && entityLikeDTO.getStatus() == 1) {
            //删除
            remove(
                    new LambdaQueryWrapper<EntityLike>()
                            .eq(EntityLike::getEntityId,entityLikeDTO.getEntityId())
                            .eq(EntityLike::getUserId, UserHolder.getUser().getId())
            );
        } else {
            //添加
            EntityLike entityLike = BeanUtil.copyProperties(entityLikeDTO, EntityLike.class);
            entityLike.setCreateTime(LocalDateTime.now());
            entityLike.setUpdateTime(LocalDateTime.now());
            save(entityLike);
        }

        return Result.success();
    }
}
