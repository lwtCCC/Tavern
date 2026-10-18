package com.lwt.tavern.query;

import lombok.Data;

/**
 * 分页查询的实体
 */
@Data
public class PageQuery {
    /**
     * 当前页码,默认第一页
     */
    private Long pageNo=1L;
    /**
     * 每页大小，默认每页10个
     */
    private Long pageSize=10L;
    /**
     * 上次查询的最大值，用于优化深度分页
     */
    private Long maxId;
    /**
     * 是否升序
     */
    private Boolean isAsc=true;
}
