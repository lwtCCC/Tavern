package com.lwt.tavern.controller;

import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.shop.ShopDTO;
import com.lwt.tavern.dto.shop.ShopOrderDTO;
import com.lwt.tavern.query.PageQuery;
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
     * @param pageQuery
     * @return
     */
    @GetMapping("/page/hot")
    public Result pageShopsByHot(PageQuery pageQuery) {
        return shopService.pageShopsByHot(pageQuery);
    }

    /**
     * 分页功能实现
     * @param pageQuery
     * @return
     */
    @GetMapping("/page/time")
    public Result pageShopsByTime(PageQuery pageQuery) {
        return shopService.pageShopsByTime(pageQuery);
    }

    /**
     * 分页功能实现
     * @param pageQuery
     * @return
     */
    @GetMapping("/page/hot/me")
    public Result pageShopsByHotWithMe(PageQuery pageQuery) {
        return shopService.pageShopsByHotWithMe(pageQuery);
    }

    /**
     * 分页功能实现
     * @param pageQuery
     * @return
     */
    @GetMapping("/page/time/me")
    public Result pageShopsByTimeWithMe(PageQuery pageQuery) {
        return shopService.pageShopsByTimeWithMe(pageQuery);
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

    //删除
    @PutMapping("/delete")
    public Result deleteShop(@RequestBody Long id) {
        return shopService.deleteShop(id);
    }

    //修改
    @PutMapping("/update")
    public Result updateShop(@RequestBody ShopDTO shopDTO) {
        return shopService.updateShop(shopDTO);
    }

    //发布
    @PostMapping("/public")
    public Result publicShop(@RequestBody ShopDTO shopDTO) {
        return shopService.publicShop(shopDTO);
    }

    //购买
    @PostMapping("/buy")
    public Result addOrder(@RequestBody ShopOrderDTO shopOrderDTO) {
        return shopService.addOrder(shopOrderDTO);
    }

}
