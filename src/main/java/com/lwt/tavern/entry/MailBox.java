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
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("`mailbox`")
public class MailBox implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private String mailContent;
    private LocalDateTime sendTime;
    private Long sendId;
    private boolean deletedUser;
    private boolean deletedSend;
}
