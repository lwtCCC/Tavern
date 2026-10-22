package com.lwt.tavern.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.MessageDTO;
import com.lwt.tavern.entity.Message;

public interface MessageService extends IService<Message> {
    Result like(MessageDTO messageDTO);
}
