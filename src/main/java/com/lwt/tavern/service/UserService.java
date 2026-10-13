package com.lwt.tavern.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.entry.dto.LoginDTO;
import com.lwt.tavern.entry.User;

public interface UserService extends IService<User> {

    Result loginByUsername(LoginDTO loginDTO);

    Result loginByPhone(LoginDTO loginDTO);

    Result register(LoginDTO loginDTO);

    Result sendCode(String phone);

    Result queryUserById(Integer id);
}
