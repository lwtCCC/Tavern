package com.lwt.tavern.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.MessageDTO;
import com.lwt.tavern.entity.Message;
import com.lwt.tavern.mapper.MessageMapper;
import com.lwt.tavern.service.MessageService;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
    @Override
    public Result like(MessageDTO messageDTO) {



        return null;
    }
}
