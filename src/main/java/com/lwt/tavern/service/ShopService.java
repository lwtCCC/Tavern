package com.lwt.tavern.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.shop.ShopDTO;
import com.lwt.tavern.dto.shop.ShopOrderDTO;
import com.lwt.tavern.entity.Shop;
import com.lwt.tavern.query.PageQuery;

public interface ShopService extends IService<Shop> {
    Result pageShops(PageQuery pageQuery);

    Result shopDetail(Long id);

    Result deleteShop(Long id);

    Result updateShop(ShopDTO shopDTO);

    Result publicShop(ShopDTO shopDTO);

    Result pageShopsWithMe(PageQuery pageQuery);

    Result addOrder(ShopOrderDTO shopOrderDTO);
}
