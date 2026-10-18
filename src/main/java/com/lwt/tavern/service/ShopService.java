package com.lwt.tavern.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.shop.ShopDTO;
import com.lwt.tavern.dto.shop.ShopWithUpdateDTO;
import com.lwt.tavern.entity.Shop;
import com.lwt.tavern.query.ShopQuery;

public interface ShopService extends IService<Shop> {
    Result pageShops(ShopQuery shopQuery);

    Result shopDetail(Long id);

    Result pageShopsWithMe(ShopQuery shopQuery);

    Result deleteShop(ShopDTO shopDTO);

    Result updateShop(ShopWithUpdateDTO shopWithUpdateDTO);

    Result publicShop(ShopWithUpdateDTO shopWithUpdateDTO);
}
