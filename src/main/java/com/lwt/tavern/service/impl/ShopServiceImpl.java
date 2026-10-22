package com.lwt.tavern.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.common.SnowFlake;
import com.lwt.tavern.common.UserHolder;
import com.lwt.tavern.dto.PageDTO;
import com.lwt.tavern.dto.shop.ShopDTO;
import com.lwt.tavern.dto.shop.ShopOrderDTO;
import com.lwt.tavern.entity.*;
import com.lwt.tavern.mapper.*;
import com.lwt.tavern.query.PageQuery;
import com.lwt.tavern.service.GameModelService;
import com.lwt.tavern.service.ShopService;
import com.lwt.tavern.vo.BasicBookVO;
import com.lwt.tavern.vo.BasicShopVO;
import com.lwt.tavern.vo.ShopVO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {

    private final SnowFlake shopSnowFlake;
    private final UserMapper userMapper;
    private final ShopMapper shopMapper;
    private final EntityLikeMapper entityLikeMapper;
    private final EntityStarMapper entityStarMapper;
    private final GameModelService gameModelService;
    private final SnowFlake shopOrderFlake;

    public ShopServiceImpl(@Qualifier("shopSnowFlake") SnowFlake shopSnowFlake,
                           UserMapper userMapper,
                           ShopMapper shopMapper, EntityLikeMapper entityLikeMapper,
                           EntityStarMapper entityStarMapper,
                           GameModelService gameModelService,
                           @Qualifier("shopOrderFlake") SnowFlake shopOrderFlake) {
        this.shopSnowFlake = shopSnowFlake;
        this.userMapper = userMapper;
        this.shopMapper = shopMapper;
        this.entityLikeMapper = entityLikeMapper;
        this.entityStarMapper = entityStarMapper;
        this.gameModelService = gameModelService;
        this.shopOrderFlake = shopOrderFlake;
    }

    @Override
    public Result pageShops(PageQuery pageQuery) {

        Page<Shop> page = Page.of(pageQuery.getPageNo(), pageQuery.getPageSize());

        LambdaQueryChainWrapper<Shop> eq = lambdaQuery()
                .orderByDesc(pageQuery.getIsHot()?Shop::getHot:Shop::getCreateTime);

        page(page,eq);

        List<Shop> shopList = page.getRecords();
        List<Long> entityIds = shopList.stream().map(Shop::getId).toList();

        Set<Long> userIds = shopList.stream().map(Shop::getUserId).collect(Collectors.toSet());

        Map<Long, String> userMap = userMapper.selectByIds(userIds).stream().collect(Collectors.toMap(User::getId, User::getUsername));

        //组装
        List<BasicShopVO> voList = shopList.stream().map(shop -> {
            BasicShopVO vo = BeanUtil.copyProperties(shop, BasicShopVO.class);
            //作者信息填充
            String username = userMap.get(vo.getUserId());
            vo.setUsername(username);
            //TODO 图片
            return vo;
        }).toList();

        //6.拿到最后一项id，传给前端
        Long maxId = voList.get(voList.size() - 1).getId();
        //TODO 放redis里，热点和时间是通用的
        //8.整合其他数据，如用户数据，点赞数据，收藏数据，观看数数据
        PageDTO<BasicShopVO> pageDTO = new PageDTO<>(page.getTotal(), page.getPages(), maxId, voList);
        return Result.success(pageDTO);
    }

    @Override
    public Result shopDetail(Long id) {
        //TODO 放redis
        Shop shop = lambdaQuery().eq(Shop::getId, id).one();
        if (BeanUtil.isEmpty(shop)) {
            return Result.fail("页面丢失");
        }
        ShopVO shopVO = BeanUtil.copyProperties(shop, ShopVO.class);
        User user = userMapper.selectById(shop.getUserId());
        if (BeanUtil.isEmpty(user)) {
            return Result.fail("商家信息丢失");
        }
        shopVO.setUsername(user.getUsername());
        shopVO.setAvatar(user.getAvatar());
        return Result.success(shopVO);
    }

    @Override
    public Result deleteShop(Long id) {
        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            return Result.fail("请重新登录");
        }

        Shop one = lambdaQuery()
                .eq(Shop::getUserId, userId)
                .eq(Shop::getId, id).one();

        if (one == null) {
            return Result.fail("该商品不存在");
        }

        removeById(id);

        return Result.success();
    }

    @Override
    public Result updateShop(ShopDTO shopDTO) {

        if (shopDTO == null) {
            return Result.fail("");
        }

        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            return Result.fail("");
        }
        if (!userId.equals(shopDTO.getUserId())) {
            return Result.fail("");
        }
        Shop shop = BeanUtil.copyProperties(shopDTO, Shop.class);
        shop.setUpdateTime(LocalDateTime.now());
        updateById(shop);
        return Result.success();
    }

    @Override
    public Result publicShop(ShopDTO shopDTO) {
        if (shopDTO == null) {
            return Result.fail("");
        }

        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            return Result.fail("");
        }

        if (!userId.equals(shopDTO.getUserId())) {
            return Result.fail("");
        }
        if (shopDTO.getUnitPrice() <= 0) {
            return Result.fail("");
        }
        if (shopDTO.getStock() <= 0) {
            return Result.fail("");
        }
        if (shopDTO.getMinLevel() <= 0) {
            return Result.fail("");
        }
        Shop shop = BeanUtil.copyProperties(shopDTO, Shop.class);
        long id = shopSnowFlake.nextId();
        shop.setId(id);
        save(shop);
        return Result.success();
    }

    @Override
    public Result pageShopsWithMe(PageQuery pageQuery) {
        Page<Shop> page = Page.of(pageQuery.getPageNo(), pageQuery.getPageSize());

        Long userId = UserHolder.getUser().getId();
        LambdaQueryChainWrapper<Shop> eq = lambdaQuery()
                .eq(Shop::getUserId, userId)
                .orderByDesc(pageQuery.getIsHot()?Shop::getHot:Shop::getCreateTime);

        page(page,eq);

        List<Shop> shopList = page.getRecords();
        List<Long> entityIds = shopList.stream().map(Shop::getId).toList();

        User user = userMapper.selectById(userId);

        //组装
        List<BasicShopVO> voList = shopList.stream().map(shop -> {
            BasicShopVO vo = BeanUtil.copyProperties(shop, BasicShopVO.class);
            //作者信息填充
            String username = user.getUsername();
            vo.setUsername(username);
            //TODO 图片
            return vo;
        }).toList();

        //6.拿到最后一项id，传给前端
        Long maxId = voList.get(voList.size() - 1).getId();
        //TODO 放redis里，热点和时间是通用的
        //8.整合其他数据，如用户数据，点赞数据，收藏数据，观看数数据
        PageDTO<BasicShopVO> pageDTO = new PageDTO<>(page.getTotal(), page.getPages(), maxId, voList);
        return Result.success(pageDTO);
    }

    @Override
    public Result addOrder(ShopOrderDTO shopOrderDTO) {

        //获取资金
        Long userId = UserHolder.getUser().getId();
        GameModel gameModel = gameModelService.query().eq("user_id", userId).one();
        //获取商品
        Shop shop = query().eq("id", shopOrderDTO.getShopId()).one();

        //商品状态
        Short status = shop.getStatus();
        if (status == 1) {
            return Result.fail("该商品已下架");
        }
        //商品库存
        Integer stock = shop.getStock();
        if (stock < 1) {
            return Result.fail("已经卖完了");
        }
        Integer quantity = shopOrderDTO.getQuantity();
        if (quantity < 1) {
            return Result.fail("");
        }
        if (quantity > stock) {
            return Result.fail("");
        }
        //购买资格
        Integer minLevel = shop.getMinLevel();
        Integer level = gameModel.getLevel();
        if (level < minLevel) {
            return Result.fail("等级不够");
        }

        Long gold = gameModel.getGold();
        Long unitPrice = shop.getUnitPrice();

        long total = unitPrice * quantity;
        if (gold < total) {
            return Result.fail("没有足够的金币");
        }

        gameModel.setGold(gold - total);
        gameModel.setUpdateTime(LocalDateTime.now());
        gameModelService.save(gameModel);

        shop.setStock(stock - quantity);
        if (stock.equals(quantity)) {
            shop.setStatus((short) 1);
        }

        save(shop);

        return Result.success();
    }


}
