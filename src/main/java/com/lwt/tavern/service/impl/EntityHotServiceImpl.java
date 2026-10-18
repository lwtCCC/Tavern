package com.lwt.tavern.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.entity.EntityHot;
import com.lwt.tavern.mapper.EntityHotMapper;
import com.lwt.tavern.service.EntityHotService;
import org.springframework.stereotype.Service;

@Service
public class EntityHotServiceImpl extends ServiceImpl<EntityHotMapper, EntityHot> implements EntityHotService {
}
