package com.lwt.tavern.controller;

import com.lwt.tavern.common.Result;
import com.lwt.tavern.service.GameModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game_model")
@RequiredArgsConstructor
public class GameModelController {

    private final GameModelService gameModelService;

    @GetMapping("/me")
    public Result getGameModel(){
        return gameModelService.getGameModel();
    }

    @GetMapping("/other/{id}")
    public Result getGameModel(@PathVariable Long id){
        return gameModelService.getGameModel(id);
    }
}
