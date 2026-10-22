package com.lwt.tavern.Consumer;

import cn.hutool.core.bean.BeanUtil;
import com.lwt.tavern.dto.MessageDTO;
import com.lwt.tavern.entity.EntityLike;
import com.lwt.tavern.service.EntityLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lwt.tavern.config.RabbitMqConfig.*;

@Service
@RequiredArgsConstructor
public class LikeConsumer {

    private final EntityLikeService entityLikeService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            name = LIKE_QUEUE
                    ),
                    exchange = @Exchange(
                            name = LIKE_EXCHANGE
                    ),
                    key = LIKE_KEY
            )
    )
    public void saveLike(List<String> messageList) {
        List<MessageDTO> messageDTOS = BeanUtil.copyToList(messageList, MessageDTO.class);

        List<EntityLike> list = messageDTOS.stream().map(messageDTO -> EntityLike.builder()
                .entityId(messageDTO.getId())
                .userId(messageDTO.getUserId())
                .entityType(messageDTO.getType())
                .isLike(messageDTO.getAction() ? (short) 0 : (short) 1)
                .updateTime(messageDTO.getTimestamp())
                .build()).toList();
        entityLikeService.saveBatch(list);
    }
}
