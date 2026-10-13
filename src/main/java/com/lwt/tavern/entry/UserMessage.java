package com.lwt.tavern.entry;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`user_message`")
public class UserMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long sendId;
    private Integer messageType;
    private String title;
    private String content;
    private Integer targetType;
    private Long targetId;
    private boolean read;
    private LocalDateTime createTime;
}
