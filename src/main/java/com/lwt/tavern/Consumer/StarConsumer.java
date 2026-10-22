package com.lwt.tavern.Consumer;

import cn.hutool.core.bean.BeanUtil;
import com.lwt.tavern.dto.MessageDTO;
import com.lwt.tavern.entity.EntityStar;
import com.lwt.tavern.service.EntityStarService;
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
public class StarConsumer {

    private final EntityStarService entityStarService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            name = STAR_QUEUE
                    ),
                    exchange = @Exchange(
                            name = STAR_EXCHANGE
                    ),
                    key = STAR_KEY
            )
    )
    public void saveLike(List<String> messageList) {
        List<MessageDTO> messageDTOS = BeanUtil.copyToList(messageList, MessageDTO.class);

        List<EntityStar> list = messageDTOS.stream().map(messageDTO -> EntityStar.builder()
                .entityId(messageDTO.getId())
                .userId(messageDTO.getUserId())
                .entityType(messageDTO.getType())
                .isStar(messageDTO.getAction() ? (short) 0 : (short) 1)
                .updateTime(messageDTO.getTimestamp())
                .build()).toList();
        entityStarService.saveBatch(list);
    }
}
