package com.lwt.tavern.Interceptor;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.lwt.tavern.common.UserHolder;
import com.lwt.tavern.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.lwt.tavern.common.RedisConstants.LOGIN_TOKEN_KEY;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("authorization");
        String json = stringRedisTemplate.opsForValue().get(LOGIN_TOKEN_KEY + token);
        UserDTO user = JSONUtil.toBean(json, UserDTO.class);
        if (BeanUtil.isEmpty(user)){
            return false;
        }
        UserHolder.saveUser(user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
