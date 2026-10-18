package com.lwt.tavern.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.common.SnowFlake;
import com.lwt.tavern.common.UserHolder;
import com.lwt.tavern.dto.shop.ShopDTO;
import com.lwt.tavern.dto.shop.ShopWithUpdateDTO;
import com.lwt.tavern.entity.Shop;
import com.lwt.tavern.mapper.ShopMapper;
import com.lwt.tavern.query.ShopQuery;
import com.lwt.tavern.service.ShopService;
import com.lwt.tavern.vo.ShopBasicVO;
import com.lwt.tavern.vo.ShopVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {

    private final SnowFlake shopSnowFlake;

    public ShopServiceImpl(SnowFlake shopSnowFlake) {
        this.shopSnowFlake = shopSnowFlake;
    }

    @Override
    public Result pageShops(ShopQuery shopQuery) {
        Page<Shop> page = Page.of(shopQuery.getPageNo(), shopQuery.getPageSize());
        page.addOrder(OrderItem.desc("hot"));
        page(page);

        List<Shop> records = page.getRecords();
        List<ShopBasicVO> shopVOS = BeanUtil.copyToList(records, ShopBasicVO.class);
        return Result.success(shopVOS);
    }

    @Override
    public Result shopDetail(Long id) {
        Shop shop = lambdaQuery().eq(Shop::getId, id).one();
        ShopVO shopVO = BeanUtil.copyProperties(shop, ShopVO.class);
        return Result.success(shopVO);
    }

    @Override
    public Result pageShopsWithMe(ShopQuery shopQuery) {

        Page<Shop> page = Page.of(shopQuery.getPageNo(), shopQuery.getPageSize());
        page.addOrder(OrderItem.desc("hot"));

        LambdaQueryChainWrapper<Shop> eq = lambdaQuery()
                .eq(Shop::getUserId, UserHolder.getUser().getId());
        page(page,eq);

        List<Shop> records = page.getRecords();
        List<ShopBasicVO> shopVOS = BeanUtil.copyToList(records, ShopBasicVO.class);
        return Result.success(shopVOS);
    }

    @Override
    public Result deleteShop(ShopDTO shopDTO) {
        Long userId = UserHolder.getUser().getId();

       lambdaUpdate()
                .eq(Shop::getUserId, userId)
                .eq(Shop::getId, shopDTO.getId())
               .update();

       return Result.success();
    }

    @Override
    public Result updateShop(ShopWithUpdateDTO shopWithUpdateDTO) {
        Shop shop = BeanUtil.copyProperties(shopWithUpdateDTO, Shop.class);
        updateById(shop);

        return Result.success();
    }

    @Override
    public Result publicShop(ShopWithUpdateDTO shopWithUpdateDTO) {

        Shop shop = BeanUtil.copyProperties(shopWithUpdateDTO, Shop.class);
        long id = shopSnowFlake.nextId();
        shop.setId(id);
        save(shop);
        return Result.success(shop);
    }


}
