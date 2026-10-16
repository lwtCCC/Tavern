package com.lwt.tavern.vo;

import com.lwt.tavern.entry.Book;
import com.lwt.tavern.entry.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
     * 封面
     */
    private String cover;
    /**
     * 作者
     */
    private User user;
    /**
     * 书的内容(分段)
     */
    private List<BookContentBlockVO> bookContentBlockVO;
}
