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
import com.lwt.tavern.entity.delete.DeleteComment;
import com.lwt.tavern.mapper.CommentMapper;
import com.lwt.tavern.mapper.EntityLikeMapper;
import com.lwt.tavern.mapper.UserMapper;
import com.lwt.tavern.query.PageCommentQuery;
import com.lwt.tavern.service.CommentService;
import com.lwt.tavern.service.DeleteCommentService;
import com.lwt.tavern.service.UserService;
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

    private final UserService userService;
    private final EntityLikeMapper entityLikeMapper;
    private final SnowFlake commentSnowFlake;
    private final DeleteCommentService deleteCommentService;
    private final UserMapper userMapper;

    public CommentServiceImpl(EntityLikeMapper entityLikeMapper,
                              @Qualifier("commentSnowFlake") SnowFlake commentSnowFlake,
                              DeleteCommentService deleteCommentService,
                              UserService userService,UserMapper userMapper) {
        this.userService = userService;
        this.entityLikeMapper = entityLikeMapper;
        this.commentSnowFlake = commentSnowFlake;
        this.deleteCommentService = deleteCommentService;
        this.userMapper = userMapper;
    }

    @Override
    public Result getRootComment(PageCommentQuery query) {

        //分页获取
        //先通过热度获取实体ids
        Page<Comment> page = Page.of(0, 10);

        LambdaQueryChainWrapper<Comment> eq;
        //评论表的设计
        /*
          评论表的设计
          id，主要作用是作为的子评论的parentId
          entityId 关联的大实体，也就是段落和商品
          userId，方便查找自己的评论和用来关联评论人信息
          root，为0就是跟评论，子评论的root指向跟评论的id
          content是内容
          createTime是发布时间
          parentId指向实际回复人，当前评论为根时，parentId为0
          likeCount为点赞数
          commentCount为子评论数，统计方法为查找和自己id一样的root评论’
          hot就是热度
         */

        eq = lambdaQuery()
                .eq(Comment::getRoot,query.getId())
                .eq(Comment::getEntityId,query.getEntityId())
                .orderByDesc(query.getIsHot()?Comment::getHot:Comment::getCreateTime);

        page(page, eq);

        List<Comment> comments = page.getRecords();

        //实体id集合,每条评论的id
        List<Long> ids = comments.stream().map(Comment::getId).toList();

        //涉及到的所用用户id
        Set<Long> userIds = comments.stream().map(Comment::getUserId).collect(Collectors.toSet());

        Map<Long, User> userMap = userMapper
                .selectByIds(userIds)
                .stream().collect(Collectors.toMap(User::getId, u->u));

        List<EntityLike> entityLikes = entityLikeMapper.selectList(
                new LambdaQueryWrapper<EntityLike>()
                        .eq(EntityLike::getUserId, UserHolder.getUser().getId())
                        .in(EntityLike::getEntityId, ids)
        );

        // 点赞过的文章id列表
        List<Long> likeList = entityLikes.stream().map(EntityLike::getEntityId).toList();

        //组装
        List<CommentVO> voList = comments.stream().map(book -> {
            CommentVO vo = BeanUtil.copyProperties(book, CommentVO.class);
            Long id = vo.getId();
            //作者信息填充
            User user = userMap.get(vo.getUserId());
            vo.setUsername(user.getUsername());
            vo.setUserId(user.getId());
            vo.setAvatar(user.getAvatar());

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

        comment.setCreateTime(LocalDateTime.now());
        comment.setHot(0L);
        comment.setLikeCount(0L);
        save(comment);
        List<CommentVO> voList = commentDTO.getVoList();
        CommentVO e = BeanUtil.copyProperties(comment, CommentVO.class);
        User user = userService.query().eq("id", userId).one();
        e.setUsername(user.getUsername());
        e.setAvatar(user.getAvatar());
        voList.add(e);
        return Result.success(voList);
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
