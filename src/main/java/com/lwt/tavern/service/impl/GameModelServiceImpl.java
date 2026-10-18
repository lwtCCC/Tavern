package com.lwt.tavern.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.common.UserHolder;
import com.lwt.tavern.dto.user.UserDTO;
import com.lwt.tavern.entity.GameModel;
import com.lwt.tavern.mapper.GameModelMapper;
import com.lwt.tavern.service.GameModelService;
import com.lwt.tavern.vo.GameModelVO;
import com.lwt.tavern.vo.GameModelWithMeVO;
import org.springframework.stereotype.Service;

@Service
public class GameModelServiceImpl extends ServiceImpl<GameModelMapper, GameModel> implements GameModelService {


    @Override
    public Result getGameModel() {
        //1.获取登录用户信息
        UserDTO user = UserHolder.getUser();
        if (BeanUtil.isEmpty(user)) {
            return Result.fail("登录超时");
        }
        //2.获取该用户id
        Long userId = user.getId();
        //3.使用该id获取游戏模型
        GameModel ga = query().eq("user_id", userId).one();
        if (BeanUtil.isEmpty(ga)) {
            return Result.success("没有查询到游戏模型");
        }
        //4.把模型转换成vo返回给前端
        GameModelWithMeVO gameModelWithMeVO = BeanUtil.copyProperties(ga, GameModelWithMeVO.class);
        return Result.success(gameModelWithMeVO);
    }

    @Override
    public Result getGameModel(Long id) {
        //1.使用用户id查询用户的游戏模型
        GameModel gameModel = query().eq("user_id", id).one();
        //2.判断非空
        if (BeanUtil.isEmpty(gameModel)) {
            return Result.fail("");
        }
        //3.转换成其他用户的游戏模型vo
        GameModelVO gameModelVO = BeanUtil.copyProperties(gameModel, GameModelVO.class);
        return Result.success(gameModelVO);
    }
}
