package com.lwt.tavern.controller;

import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.EntityLikeDTO;
import com.lwt.tavern.dto.EntityStarDTO;
import com.lwt.tavern.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entity")
@RequiredArgsConstructor
public class InteractionController {

    private final EntityLikeService entityLikeService;
    private final EntityStarService entityStarService;

    @PostMapping("/like")
    public Result actionLike(@RequestBody EntityLikeDTO entityLikeDTO) {
        return entityLikeService.actionLike(entityLikeDTO);
    }

    @PostMapping("/star")
    public Result actionStar(@RequestBody EntityStarDTO entityStarDTO) {
        return entityStarService.actionStar(entityStarDTO);
    }

}
