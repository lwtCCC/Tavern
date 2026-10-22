package com.lwt.tavern.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.dto.user.LoginDTO;
import com.lwt.tavern.dto.user.ResetPasswordDTO;
import com.lwt.tavern.entity.User;

public interface UserService extends IService<User> {

    Result sendCode(String phone);

    Result sendRegisterCode(String phone);

    Result resetSendCode(String phone);

    Result register(LoginDTO loginDTO);

    Result loginByUsername(LoginDTO loginDTO);

    Result loginByPhone(LoginDTO loginDTO);

    Result me();

    Result queryUserById(Long id);

    Result verify(LoginDTO loginDTO);

    Result resetPassword(ResetPasswordDTO resetPasswordDTO);

    Result resetProfile(String profile);

    Result deleteMe();
}
