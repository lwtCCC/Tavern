package com.lwt.tavern.vo;

import com.lwt.tavern.entry.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookVO {
    /**
     * 书本id
     */
    private Long id;
    /**
     * 书名
     */
    private String bookName;
    /**
     * 热度
     */
    private Long hot;
    /**
     * 阅览量
     */
    private Long viewCount;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 点赞数量
     */
    private Long likeCount;
    /**
     * 收藏数量
     */
    private Long starCount;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 逻辑删除
     */
    private Short deleted;
    /**
     * 封面
     */
    private String cover;
    /**
     * 作者
     */
    private User user;
}
