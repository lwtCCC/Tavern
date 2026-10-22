package com.lwt.tavern.controller;

import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.MessageDTO;
import com.lwt.tavern.entity.Message;
import com.lwt.tavern.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/book")
    public Result like(@RequestBody MessageDTO messageDTO) {
        return messageService.like(messageDTO);
    }

    //系统消息
    /*@PostMapping()
    public Result sendMessage(@RequestBody ){
        return userMessageService
    }*/

    //查看

    //删除

    //私信发送
}
