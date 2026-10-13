package com.lwt.tavern.controller;

import com.lwt.tavern.common.Result;
import com.lwt.tavern.entry.dto.LoginDTO;
import com.lwt.tavern.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * 根据手机号发送验证码
     * @param phone 手机号
     * @return 带有验证码的消息
     */
    @PostMapping("/code")
    public Result sendCode(@RequestParam("phone") String phone){
        return userService.sendCode(phone);
    }

    /**
     * 注册功能
     * @param loginDTO 登录信息
     * @return 返回token
     */
    @PostMapping("/register")
    public Result register(@RequestBody LoginDTO loginDTO){
        return userService.register(loginDTO);
    }

    /**
     * 用户密码登录
     * @return token
     */
    @PostMapping("/loginByUsername")
    public Result loginByUsername(@RequestBody LoginDTO loginDTO){
        return userService.loginByUsername(loginDTO);
    }

    /**
     * 手机验证码登录
     * @return token
     */
    @PostMapping("/loginByPhone")
    public Result loginByPhone(@RequestBody LoginDTO loginDTO){
        return userService.loginByPhone(loginDTO);
    }

    /**
     * 根据id查询用户
     * @param id 前端传来的id
     * @return 该用户的信息
     */
    @GetMapping("/{id}")
    public Result queryUserById(@PathVariable("id") Integer id){
        return userService.queryUserById(id);
    }
}
