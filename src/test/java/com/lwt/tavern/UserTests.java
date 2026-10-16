package com.lwt.tavern;

import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.user.LoginDTO;
import com.lwt.tavern.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTests {

    @Autowired
    private UserService userService;

    @Test
    void testSendCode() {
        Result result = userService.sendCode("14762386203");
        System.out.println("验证码为: "+result.getData().toString());
    }

    @Test
    void testSendCodeRegister() {
        Result result = userService.sendCodeRegister("14762386203");
        System.out.println("验证码为: "+result.getData().toString());
    }

    @Test
    void testRegister(){
        LoginDTO loginDTO = new LoginDTO(null, "14762386203", "lwt200555","3276");
        Result result = userService.register(loginDTO);
        System.out.println("注册结果,返回token: "+result.getErrorMsg());
    }

    @Test
    void testLoginByUsername(){
        LoginDTO loginDTO = new LoginDTO("USER_vB8OYC83", "14762386203", "lwt200555","3276");
        Result result = userService.loginByUsername(loginDTO);
        System.out.println("注册结果,返回token: "+result.getData().toString());
    }

    @Test
    void testLoginByPhone(){
        LoginDTO loginDTO = new LoginDTO("USER_vB8OYC83", "14762386203", "lwt200555","3276");
        Result result = userService.loginByPhone(loginDTO);
        System.out.println("注册结果,返回token: "+result.getData().toString());
    }

    @Test
    void testQueryUserById(){
        Result result = userService.queryUserById(16216714514432L);
        System.out.println("查询结果,返回用户: "+result.getData());
    }
}
