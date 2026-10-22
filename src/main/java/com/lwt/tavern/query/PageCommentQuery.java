package com.lwt.tavern.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageCommentQuery {
    /**
     * 评论id，没有的话默认0
     */
    private Long id;
    /**
     *  实体id
     */
    private Long entityId;
    /**
     * 上次查询的最大值，用于优化深度分页
     */
    private Long maxId;

    /**
     * 段落或者商店商店0 评论1
     */
    private Integer type;

    private Boolean isHot;
    /**
     * 是否升序
     */
    private Boolean isAsc = false;
}
