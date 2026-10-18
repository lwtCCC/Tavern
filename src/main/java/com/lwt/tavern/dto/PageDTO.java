package com.lwt.tavern.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageDTO<T> {
    /**
     * 总条数
     */
    private Long total;
    /**
     * 总页数
     */
    private Long pages;
    /**
     * 最后一项id
     */
    private Long id;
    /**
     * 数据集合
     */
    private List<T> list;
}
