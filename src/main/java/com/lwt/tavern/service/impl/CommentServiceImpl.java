package com.lwt.tavern.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.common.SnowFlake;
import com.lwt.tavern.common.UserHolder;
import com.lwt.tavern.dto.CommentDTO;
import com.lwt.tavern.dto.PageDTO;
import com.lwt.tavern.entity.*;
import com.lwt.tavern.mapper.CommentMapper;
import com.lwt.tavern.mapper.EntityLikeCountMapper;
import com.lwt.tavern.mapper.EntityLikeMapper;
import com.lwt.tavern.mapper.UserMapper;
import com.lwt.tavern.query.PageCommentQuery;
import com.lwt.tavern.service.CommentService;
import com.lwt.tavern.service.DeleteCommentService;
import com.lwt.tavern.service.EntityHotCommentService;
import com.lwt.tavern.vo.CommentVO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {


    private final CommentMapper commentMapper;
    private final EntityHotCommentService entityHotCommentService;
    private final UserMapper userMapper;
    private final EntityLikeMapper entityLikeMapper;
    private final EntityLikeCountMapper entityLikeCountMapper;
    private final SnowFlake commentSnowFlake;
    private final DeleteCommentService deleteCommentService;

    public CommentServiceImpl(CommentMapper commentMapper,
                              EntityHotCommentService entityHotCommentService,
                              UserMapper userMapper, EntityLikeMapper entityLikeMapper,
                              EntityLikeCountMapper entityLikeCountMapper,
                              @Qualifier("commentSnowFlake") SnowFlake commentSnowFlake, DeleteCommentService deleteCommentService) {
        this.commentMapper = commentMapper;
        this.entityHotCommentService = entityHotCommentService;
        this.userMapper = userMapper;
        this.entityLikeMapper = entityLikeMapper;
        this.entityLikeCountMapper = entityLikeCountMapper;
        this.commentSnowFlake = commentSnowFlake;
        this.deleteCommentService = deleteCommentService;
    }

    @Override
    public Result getRootComment(PageCommentQuery query) {

        //分页获取
        //先通过热度获取实体ids
        Page<EntityHotComment> page = Page.of(0, 10);

        LambdaQueryChainWrapper<EntityHotComment> eq = entityHotCommentService.lambdaQuery()
                .eq(EntityHotComment::getEntityId, query.getId());

        entityHotCommentService.page(page, eq);

        List<EntityHotComment> records = page.getRecords();
        //实体id集合,每条评论的id
        List<Long> ids = records.stream().map(EntityHotComment::getId).toList();

        //相关评论列表
        List<Comment> comments = commentMapper.selectByIds(ids);

        //涉及到的所用用户id
        Set<Long> userIds = comments.stream().map(Comment::getUserId).collect(Collectors.toSet());

        Map<Long, String> userMap = userMapper
                .selectByIds(userIds)
                .stream().collect(Collectors.toMap(User::getId, User::getUsername));

        List<EntityLike> entityLikes = entityLikeMapper.selectList(
                new LambdaQueryWrapper<EntityLike>()
                        .eq(EntityLike::getUserId, UserHolder.getUser().getId())
                        .in(EntityLike::getEntityId, ids)
        );

        // 点赞过的文章id列表
        List<Long> likeList = entityLikes.stream().map(EntityLike::getEntityId).toList();

        //点赞数
        Map<Long, Long> likeCountMap = entityLikeCountMapper
                .selectByIds(ids)
                .stream()
                .collect(Collectors.toMap(EntityLikeCount::getEntityId, EntityLikeCount::getLikeCount));

        //组装
        List<CommentVO> voList = comments.stream().map(book -> {
            CommentVO vo = BeanUtil.copyProperties(book, CommentVO.class);
            Long id = vo.getId();
            //作者信息填充
            String username = userMap.get(vo.getUserId());
            vo.setUsername(username);

            //点赞数填充
            Long likeCount = likeCountMap.get(id);
            vo.setLikeCount(likeCount);

            //是否点赞
            vo.setLike(likeList.contains(id) ? 1 : 0);
            return vo;
        }).toList();

        //6.拿到最后一项id，传给前端
        Long maxId = voList.get(voList.size() - 1).getId();

        //8.整合其他数据，如用户数据，点赞数据，收藏数据，观看数数据
        PageDTO<CommentVO> pageDTO = new PageDTO<>(page.getTotal(), page.getPages(), maxId, voList);
        return Result.success(pageDTO);
    }

    @Override
    public Result addComment(CommentDTO commentDTO) {
        Long userId = UserHolder.getUser().getId();

        Comment comment = BeanUtil.copyProperties(commentDTO, Comment.class);
        comment.setUserId(userId);

        long id = commentSnowFlake.nextId();
        comment.setId(id);

        if (comment.getRoot() == 0) {
            comment.setParentId(0L);
        }

        comment.setCreateTime(LocalDateTime.now());
        save(comment);
        return Result.success();
    }

    @Override
    public Result deleteComment(Long id) {

        Long userId = UserHolder.getUser().getId();
        Comment one = lambdaQuery().eq(Comment::getUserId, userId).eq(Comment::getId, id).one();
        if (one == null) {
            return Result.fail("");
        }
        DeleteComment deleteComment = BeanUtil.copyProperties(one, DeleteComment.class);
        deleteCommentService.save(deleteComment);
        LambdaQueryChainWrapper<Comment> eq = lambdaQuery().eq(Comment::getUserId, userId).eq(Comment::getId, id);
        remove(eq);
        return Result.success();
    }
}
