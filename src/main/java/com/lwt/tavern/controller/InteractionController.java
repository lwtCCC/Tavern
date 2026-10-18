package com.lwt.tavern.controller;

import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.EntityLikeDTO;
import com.lwt.tavern.dto.EntityStarDTO;
import com.lwt.tavern.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/InteractionController")
@RequiredArgsConstructor
public class InteractionController {

    private final EntityLikeService entityLikeService;
    private final EntityStarService entityStarService;
    private final EntityLikeCountService entityLikeCountService;
    private final EntityStarCountService entityStarCountService;
    private final EntityViewService entityViewService;

    @GetMapping("/like/count/{id}")
    public Result getLikeCount(@PathVariable Long id) {
        return entityLikeCountService.getLikeCount(id);
    }

    @GetMapping("/star/count/{id}")
    public Result getStarCount(@PathVariable Long id) {
        return entityStarCountService.getStarCount(id);
    }

    @PostMapping("/like/act")
    public Result actionLike(@RequestBody EntityLikeDTO entityLikeDTO) {
        return entityLikeService.actionLike(entityLikeDTO);
    }

    @PostMapping("/like/act")
    public Result actionStar(@RequestBody EntityStarDTO entityStarDTO) {
        return entityStarService.actionStar(entityStarDTO);
    }

    @PostMapping("/view")
    public Result addView(@RequestBody Long id) {
        return entityViewService.addView(id);
    }

}
