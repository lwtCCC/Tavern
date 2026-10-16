package com.lwt.tavern.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 书的查询条件实体
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BookQuery extends PageQuery{
    /**
     * 书名
     */
    private String bookName;
    /**
     * 作者
     */
    private String username;
    /**
     * 描述
     */
    private String description;
}
