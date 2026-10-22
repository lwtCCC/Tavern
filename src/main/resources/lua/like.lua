-- 根据行为来判断是否点赞
-- 1是有改变，0是保持不变

-- 点赞key
local like_key = KEYS[1]
-- 点赞数量key
local like_count_key = KEYS[2]
-- 用户id
local user_id = ARGV[1]
-- 行为：0不点赞，1点赞
local action = tonumber(ARGV[2])
-- 时间戳
local time = ARGV[3]

-- 查找该用户是否有点赞过该文章的记录
local is_like = redis.call('HEXISTS', like_key, user_id)

-- 如果点赞过，那么再点赞也没用，不取消点赞是因为行为是点赞
if action == 1 then
    -- 实现幂等
    if is_like == 1 then
        return 0
    end
    redis.call('HSET', like_key, user_id, time)
    redis.call('INCR', like_count_key, 'likeCount')
    return 1
else
    if is_liked == 0 then
        return 0
    end
    redis.call('HDEL', like_key, user_id)
    redis.call('DECR', like_count_key,'likeCount')
    return 1
end