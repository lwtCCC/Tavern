package com.lwt.tavern.dto;

import com.lwt.tavern.vo.CommentVO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CommentDTO {
    /**
     * 实体id
     */
    private Long entityId;
    /**
     * 根评论
     */
    private Long root;
    /**
     * 父评论
     */
    private Long parentId;
    /**
     * 评论内容
     */
    private String content;
    private List<CommentVO> voList;
}
