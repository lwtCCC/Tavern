package com.lwt.tavern.controller;

import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.shop.ShopDTO;
import com.lwt.tavern.dto.shop.ShopWithUpdateDTO;
import com.lwt.tavern.query.ShopQuery;
import com.lwt.tavern.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class ShopController {

    private final ShopService shopService;

    /**
     * 分页功能实现
     * @param shopQuery
     * @return
     */
    @GetMapping("/page")
    public Result pageShops(ShopQuery shopQuery) {
        return shopService.pageShops(shopQuery);
    }

    /**
     * 查看商品详情
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public Result shopDetail(@PathVariable Long id) {
        return shopService.shopDetail(id);
    }

    /**
     * 我发布过的商品
     * @param shopQuery
     * @return
     */
    @GetMapping("/page/me")
    public Result pageShopsWithMe(ShopQuery shopQuery) {
        return shopService.pageShopsWithMe(shopQuery);
    }

    //删除
    @PutMapping("/delete")
    public Result deleteShop(@RequestBody ShopDTO shopDTO) {
        return shopService.deleteShop(shopDTO);
    }

    //修改
    @PutMapping("/delete")
    public Result updateShop(@RequestBody ShopWithUpdateDTO shopWithUpdateDTO) {
        return shopService.updateShop(shopWithUpdateDTO);
    }

    //发布
    @PostMapping("/public")
    public Result publicShop(@RequestBody ShopWithUpdateDTO shopWithUpdateDTO) {
        return shopService.publicShop(shopWithUpdateDTO);
    }

    //购买
    /*@PostMapping("/buy")
    public Result addOrder(@RequestBody ShopDTO shopDTO) {
        return
    }*/

}
