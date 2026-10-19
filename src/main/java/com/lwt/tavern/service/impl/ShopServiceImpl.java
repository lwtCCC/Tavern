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
import com.lwt.tavern.service.EntityHotService;
import com.lwt.tavern.service.GameModelService;
import com.lwt.tavern.service.ShopService;
import com.lwt.tavern.service.UserService;
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
    private final EntityHotService entityHotService;
    private final UserMapper userMapper;
    private final ShopMapper shopMapper;
    private final EntityLikeMapper entityLikeMapper;
    private final EntityStarMapper entityStarMapper;
    private final EntityStarCountMapper entityStarCountMapper;
    private final EntityLikeCountMapper entityLikeCountMapper;
    private final EntityViewMapper entityViewMapper;
    private final GameModelService gameModelService;
    private final SnowFlake shopOrderFlake;

    public ShopServiceImpl(@Qualifier("shopSnowFlake") SnowFlake shopSnowFlake,
                           EntityHotService entityHotService, UserMapper userMapper,
                           ShopMapper shopMapper, EntityLikeMapper entityLikeMapper,
                           EntityStarMapper entityStarMapper,
                           EntityStarCountMapper entityStarCountMapper,
                           EntityLikeCountMapper entityLikeCountMapper,
                           EntityViewMapper entityViewMapper,
                           GameModelService gameModelService,
                           @Qualifier("shopOrderFlake") SnowFlake shopOrderFlake) {
        this.shopSnowFlake = shopSnowFlake;
        this.entityHotService = entityHotService;
        this.userMapper = userMapper;
        this.shopMapper = shopMapper;
        this.entityLikeMapper = entityLikeMapper;
        this.entityStarMapper = entityStarMapper;
        this.entityStarCountMapper = entityStarCountMapper;
        this.entityLikeCountMapper = entityLikeCountMapper;
        this.entityViewMapper = entityViewMapper;
        this.gameModelService = gameModelService;
        this.shopOrderFlake = shopOrderFlake;
    }

    @Override
    public Result pageShopsByHot(PageQuery pageQuery) {

        Page<EntityHot> page = Page.of(pageQuery.getPageNo(), pageQuery.getPageSize());

        LambdaQueryChainWrapper<EntityHot> eq = entityHotService
                .lambdaQuery()
                .eq(EntityHot::getEntityType, 1).orderByDesc(EntityHot::getHot);

        entityHotService.page(page,eq);

        List<EntityHot> records = page.getRecords();
        List<Long> entityIds = records.stream().map(EntityHot::getEntityId).toList();

        List<Shop> shopList = shopMapper.selectByIds(entityIds);

        Set<Long> userIds = shopList.stream().map(Shop::getUserId).collect(Collectors.toSet());

        Map<Long, String> userMap = userMapper.selectByIds(userIds).stream().collect(Collectors.toMap(User::getId, User::getUsername));


        List<EntityLike> entityLikes = entityLikeMapper.selectList(
                new LambdaQueryWrapper<EntityLike>()
                        .eq(EntityLike::getUserId, UserHolder.getUser().getId())
                        .in(EntityLike::getEntityId, entityIds)
        );

        // 点赞过的文章id列表
        List<Long> likeList = entityLikes.stream().map(EntityLike::getEntityId).toList();

        List<EntityStar> entityStars = entityStarMapper.selectList(
                new LambdaQueryWrapper<EntityStar>()
                        .eq(EntityStar::getUserId, UserHolder.getUser().getId())
                        .in(EntityStar::getEntityId, entityIds)
        );

        //收藏过的id列表
        List<Long> starList = entityStars.stream().map(EntityStar::getEntityId).toList();

        //点赞数
        Map<Long, Long> likeCountMap = entityLikeCountMapper
                .selectByIds(entityIds)
                .stream()
                .collect(Collectors.toMap(EntityLikeCount::getEntityId, EntityLikeCount::getLikeCount));

        //收藏数
        Map<Long, Long> starCountMap = entityStarCountMapper
                .selectByIds(entityIds).stream()
                .collect(Collectors.toMap(EntityStarCount::getEntityId, EntityStarCount::getStarCount));

        //浏览量
        Map<Long, Long> viewCountMap = entityViewMapper
                .selectByIds(entityIds)
                .stream()
                .collect(Collectors.toMap(EntityView::getEntityId, EntityView::getView));

        //组装
        List<BasicShopVO> voList = shopList.stream().map(shop -> {
            BasicShopVO vo = BeanUtil.copyProperties(shop, BasicShopVO.class);
            Long id = vo.getId();
            //作者信息填充
            String username = userMap.get(vo.getUserId());
            vo.setUsername(username);

            //点赞数填充
            Long likeCount = likeCountMap.get(id);
            Long starCount = starCountMap.get(id);
            Long viewCount = viewCountMap.get(id);
            vo.setLikeCount(likeCount);
            vo.setStarCount(starCount);
            vo.setViewCount(viewCount);

            //是否点赞
            vo.setLike(likeList.contains(id) ? 1 : 0);
            //是否收藏
            vo.setStar(starList.contains(id) ? 1 : 0);
            return vo;
        }).toList();

        //6.拿到最后一项id，传给前端
        Long maxId = voList.get(voList.size() - 1).getId();

        //8.整合其他数据，如用户数据，点赞数据，收藏数据，观看数数据
        PageDTO<BasicShopVO> pageDTO = new PageDTO<>(page.getTotal(), page.getPages(), maxId, voList);
        return Result.success(pageDTO);
    }

    @Override
    public Result shopDetail(Long id) {
        Shop shop = lambdaQuery().eq(Shop::getId, id).one();
        //TODO 图片
        ShopVO shopVO = BeanUtil.copyProperties(shop, ShopVO.class);
        return Result.success(shopVO);
    }

    @Override
    public Result deleteShop(Long id) {
        Long userId = UserHolder.getUser().getId();

        Shop one = lambdaQuery()
                .eq(Shop::getUserId, userId)
                .eq(Shop::getId, id).one();

        if (one == null) {
            return Result.fail("该商品不存在");
        }

        return Result.success();
    }

    @Override
    public Result updateShop(ShopDTO shopDTO) {
        Shop shop = BeanUtil.copyProperties(shopDTO, Shop.class);
        updateById(shop);
        return Result.success();
    }

    @Override
    public Result publicShop(ShopDTO shopDTO) {
        Shop shop = BeanUtil.copyProperties(shopDTO, Shop.class);
        long id = shopSnowFlake.nextId();
        shop.setId(id);
        save(shop);
        return Result.success();
    }

    @Override
    public Result pageShopsByTime(PageQuery pageQuery) {

        //1.设置查询参数，关联热度表
        Page<Shop> page = Page.of(pageQuery.getPageNo(), pageQuery.getPageSize());
        //设置查询条件
        LambdaQueryChainWrapper<Shop> eq = lambdaQuery()
                .orderByDesc(Shop::getCreateTime);
        page(page,eq);

        //4.获取相关的实体id
        List<Long> entityIds = page.getRecords().stream().map(Shop::getId).toList();
        //获取书（实体）
        List<Shop> shopList = page.getRecords();

        //再通过书获取用户id集合和点赞和收藏和阅览
        Set<Long> userIds = shopList.stream().map(Shop::getUserId).collect(Collectors.toSet());

        //userId::username
        Map<Long, String> userMap = userMapper
                .selectByIds(userIds).stream().collect(Collectors.toMap(User::getId, User::getUsername));

        List<EntityLike> entityLikes = entityLikeMapper.selectList(
                new LambdaQueryWrapper<EntityLike>()
                        .eq(EntityLike::getUserId, UserHolder.getUser().getId())
                        .in(EntityLike::getEntityId, entityIds)
        );

        // 点赞过的文章id列表
        List<Long> likeList = entityLikes.stream().map(EntityLike::getEntityId).toList();

        List<EntityStar> entityStars = entityStarMapper.selectList(
                new LambdaQueryWrapper<EntityStar>()
                        .eq(EntityStar::getUserId, UserHolder.getUser().getId())
                        .in(EntityStar::getEntityId, entityIds)
        );

        //收藏过的id列表
        List<Long> starList = entityStars.stream().map(EntityStar::getEntityId).toList();

        //点赞数
        Map<Long, Long> likeCountMap = entityLikeCountMapper
                .selectByIds(entityIds)
                .stream()
                .collect(Collectors.toMap(EntityLikeCount::getEntityId, EntityLikeCount::getLikeCount));

        //收藏数
        Map<Long, Long> starCountMap = entityStarCountMapper
                .selectByIds(entityIds).stream()
                .collect(Collectors.toMap(EntityStarCount::getEntityId, EntityStarCount::getStarCount));

        //浏览量
        Map<Long, Long> viewCountMap = entityViewMapper
                .selectByIds(entityIds)
                .stream()
                .collect(Collectors.toMap(EntityView::getEntityId, EntityView::getView));

        //组装
        List<BasicBookVO> voList = shopList.stream().map(book -> {
            BasicBookVO vo = BeanUtil.copyProperties(book, BasicBookVO.class);
            Long id = vo.getId();
            //作者信息填充
            String username = userMap.get(vo.getUserId());
            vo.setUsername(username);

            //点赞数填充
            Long likeCount = likeCountMap.get(id);
            Long starCount = starCountMap.get(id);
            Long viewCount = viewCountMap.get(id);
            vo.setLikeCount(likeCount);
            vo.setStarCount(starCount);
            vo.setViewCount(viewCount);

            //是否点赞
            vo.setLike(likeList.contains(id) ? 1 : 0);
            //是否收藏
            vo.setStar(starList.contains(id) ? 1 : 0);
            return vo;
        }).toList();

        //6.拿到最后一项id，传给前端
        Long maxId = voList.get(voList.size() - 1).getId();

        //8.整合其他数据，如用户数据，点赞数据，收藏数据，观看数数据
        PageDTO<BasicBookVO> pageDTO = new PageDTO<>(page.getTotal(), page.getPages(), maxId, voList);
        return Result.success(pageDTO);
    }

    @Override
    public Result pageShopsByHotWithMe(PageQuery pageQuery) {
        Page<EntityHot> page = Page.of(pageQuery.getPageNo(), pageQuery.getPageSize());

        Long userId = UserHolder.getUser().getId();
        String username = userMapper.selectById(userId).getUsername();

        LambdaQueryChainWrapper<EntityHot> eq = entityHotService
                .lambdaQuery()
                .eq(EntityHot::getUserId, UserHolder.getUser().getId())
                .eq(EntityHot::getEntityType, 1).orderByDesc(EntityHot::getHot);

        entityHotService.page(page,eq);

        List<EntityHot> records = page.getRecords();
        List<Long> entityIds = records.stream().map(EntityHot::getEntityId).toList();

        List<Shop> shopList = shopMapper.selectByIds(entityIds);


        List<EntityLike> entityLikes = entityLikeMapper.selectList(
                new LambdaQueryWrapper<EntityLike>()
                        .eq(EntityLike::getUserId, UserHolder.getUser().getId())
                        .in(EntityLike::getEntityId, entityIds)
        );

        // 点赞过的文章id列表
        List<Long> likeList = entityLikes.stream().map(EntityLike::getEntityId).toList();

        List<EntityStar> entityStars = entityStarMapper.selectList(
                new LambdaQueryWrapper<EntityStar>()
                        .eq(EntityStar::getUserId, UserHolder.getUser().getId())
                        .in(EntityStar::getEntityId, entityIds)
        );

        //收藏过的id列表
        List<Long> starList = entityStars.stream().map(EntityStar::getEntityId).toList();

        //点赞数
        Map<Long, Long> likeCountMap = entityLikeCountMapper
                .selectByIds(entityIds)
                .stream()
                .collect(Collectors.toMap(EntityLikeCount::getEntityId, EntityLikeCount::getLikeCount));

        //收藏数
        Map<Long, Long> starCountMap = entityStarCountMapper
                .selectByIds(entityIds).stream()
                .collect(Collectors.toMap(EntityStarCount::getEntityId, EntityStarCount::getStarCount));

        //浏览量
        Map<Long, Long> viewCountMap = entityViewMapper
                .selectByIds(entityIds)
                .stream()
                .collect(Collectors.toMap(EntityView::getEntityId, EntityView::getView));

        //组装
        List<BasicShopVO> voList = shopList.stream().map(shop -> {
            BasicShopVO vo = BeanUtil.copyProperties(shop, BasicShopVO.class);
            Long id = vo.getId();
            //作者信息填充
            vo.setUsername(username);

            //点赞数填充
            Long likeCount = likeCountMap.get(id);
            Long starCount = starCountMap.get(id);
            Long viewCount = viewCountMap.get(id);
            vo.setLikeCount(likeCount);
            vo.setStarCount(starCount);
            vo.setViewCount(viewCount);

            //是否点赞
            vo.setLike(likeList.contains(id) ? 1 : 0);
            //是否收藏
            vo.setStar(starList.contains(id) ? 1 : 0);
            return vo;
        }).toList();

        //6.拿到最后一项id，传给前端
        Long maxId = voList.get(voList.size() - 1).getId();

        //8.整合其他数据，如用户数据，点赞数据，收藏数据，观看数数据
        PageDTO<BasicShopVO> pageDTO = new PageDTO<>(page.getTotal(), page.getPages(), maxId, voList);
        return Result.success(pageDTO);
    }

    @Override
    public Result pageShopsByTimeWithMe(PageQuery pageQuery) {
        //1.设置查询参数，关联热度表
        Page<Shop> page = Page.of(pageQuery.getPageNo(), pageQuery.getPageSize());
        Long userId = UserHolder.getUser().getId();
        String username = userMapper.selectById(userId).getUsername();

        //设置查询条件
        LambdaQueryChainWrapper<Shop> eq = lambdaQuery()
                .eq(Shop::getUserId, UserHolder.getUser().getId())
                .orderByDesc(Shop::getCreateTime);
        page(page,eq);

        //4.获取相关的实体id
        List<Long> entityIds = page.getRecords().stream().map(Shop::getId).toList();
        //获取书（实体）
        List<Shop> shopList = page.getRecords();

        List<EntityLike> entityLikes = entityLikeMapper.selectList(
                new LambdaQueryWrapper<EntityLike>()
                        .eq(EntityLike::getUserId, UserHolder.getUser().getId())
                        .in(EntityLike::getEntityId, entityIds)
        );

        // 点赞过的文章id列表
        List<Long> likeList = entityLikes.stream().map(EntityLike::getEntityId).toList();

        List<EntityStar> entityStars = entityStarMapper.selectList(
                new LambdaQueryWrapper<EntityStar>()
                        .eq(EntityStar::getUserId, UserHolder.getUser().getId())
                        .in(EntityStar::getEntityId, entityIds)
        );

        //收藏过的id列表
        List<Long> starList = entityStars.stream().map(EntityStar::getEntityId).toList();

        //点赞数
        Map<Long, Long> likeCountMap = entityLikeCountMapper
                .selectByIds(entityIds)
                .stream()
                .collect(Collectors.toMap(EntityLikeCount::getEntityId, EntityLikeCount::getLikeCount));

        //收藏数
        Map<Long, Long> starCountMap = entityStarCountMapper
                .selectByIds(entityIds).stream()
                .collect(Collectors.toMap(EntityStarCount::getEntityId, EntityStarCount::getStarCount));

        //浏览量
        Map<Long, Long> viewCountMap = entityViewMapper
                .selectByIds(entityIds)
                .stream()
                .collect(Collectors.toMap(EntityView::getEntityId, EntityView::getView));

        //组装
        List<BasicBookVO> voList = shopList.stream().map(book -> {
            BasicBookVO vo = BeanUtil.copyProperties(book, BasicBookVO.class);
            Long id = vo.getId();
            //作者信息填充
            vo.setUsername(username);

            //点赞数填充
            Long likeCount = likeCountMap.get(id);
            Long starCount = starCountMap.get(id);
            Long viewCount = viewCountMap.get(id);
            vo.setLikeCount(likeCount);
            vo.setStarCount(starCount);
            vo.setViewCount(viewCount);

            //是否点赞
            vo.setLike(likeList.contains(id) ? 1 : 0);
            //是否收藏
            vo.setStar(starList.contains(id) ? 1 : 0);
            return vo;
        }).toList();

        //6.拿到最后一项id，传给前端
        Long maxId = voList.get(voList.size() - 1).getId();

        //8.整合其他数据，如用户数据，点赞数据，收藏数据，观看数数据
        PageDTO<BasicBookVO> pageDTO = new PageDTO<>(page.getTotal(), page.getPages(), maxId, voList);
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
        Long unitPrice = shop.getPrice();

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
