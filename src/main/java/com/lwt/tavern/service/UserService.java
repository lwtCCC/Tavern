package com.lwt.tavern.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.user.LoginDTO;
import com.lwt.tavern.dto.user.ResetPasswordDTO;
import com.lwt.tavern.entity.User;

public interface UserService extends IService<User> {

    Result loginByUsername(LoginDTO loginDTO);

    Result loginByPhone(LoginDTO loginDTO);

    Result register(LoginDTO loginDTO);

    Result sendCode(String phone);

    Result queryUserById(Long id);

    Result sendCodeRegister(String phone);

    Result me();

    Result resetSendCode(String phone);

    Result verify(LoginDTO loginDTO);

    Result resetPassword(ResetPasswordDTO resetPasswordDTO);

    Result resetProfile(String profile);

    Result deleteMe();
}
