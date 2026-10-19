package com.lwt.tavern.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageCommentQuery {
    /**
     *  实体id
     */
    private Long id;
    /**
     * 上次查询的最大值，用于优化深度分页
     */
    private Long maxId;
    /**
     * 是否升序
     */
    private Boolean isAsc=true;
}
