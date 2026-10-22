package com.lwt.tavern.controller;

import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.user.LoginDTO;
import com.lwt.tavern.dto.user.ResetPasswordDTO;
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
    @PostMapping("/login/code")
    public Result sendCode(@RequestParam("phone") String phone){
        return userService.sendCode(phone);
    }

    /**
     * 根据手机号发送验证码(注册用)
     * @param phone 手机号
     * @return 带有验证码的消息
     */
    @PostMapping("/register/code")
    public Result sendRegisterCode(@RequestParam("phone") String phone){
        return userService.sendRegisterCode(phone);
    }

    /**
     * 发送验证码（修改密码用的）
     * @param phone 手机号码
     * @return code
     */
    @PostMapping("/reset/code")
    public Result resetSendCode(@RequestBody String phone){
        return userService.resetSendCode(phone);
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
     * 展示自己的信息
     * @return 返回自己的详细信息，但是不包括是否删除
     */
    @PostMapping("/me")
    public Result me(){
        return userService.me();
    }

    /**
     * 根据id查询用户
     * @param id 前端传来的id
     * @return 该用户的信息
     */
    @GetMapping("/{id}")
    public Result queryUserById(@PathVariable("id") Long id){
        return userService.queryUserById(id);
    }

    /**
     * 修改密码的身份验证
     * @param loginDTO 手机号和验证码
     * @return 是否成功
     */
    @PostMapping("/reset/verify")
    public Result verify(@RequestBody LoginDTO loginDTO){
        return userService.verify(loginDTO);
    }

    /**
     * 修改密码
     * @param resetPasswordDTO 包含新密码和确认密码
     * @return 完整的用户信息，以覆盖旧的
     */
    @PutMapping("/reset/password")
    public Result resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO){
        return userService.resetPassword(resetPasswordDTO);
    }

    /**
     * 修改简介
     * @param profile 简介内容
     * @return 用户基础信息
     */
    @PutMapping("/reset/profile")
    public Result resetProfile(@RequestBody String profile){
        return userService.resetProfile(profile);
    }

    /**
     * 注销
     * @return 把自己删除
     */
    @DeleteMapping("/me")
    public Result deleteMe(){
        return userService.deleteMe();
    }

}
