package com.lwt.tavern.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.entity.GameModel;

public interface GameModelService extends IService<GameModel> {
    Result getGameModel();

    Result getGameModel(Long id);
}
