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
     * 排序字段
     */
    private String sortBy="hot";
    /**
     * 是否降序
     */
    private Boolean isAsc;
}
