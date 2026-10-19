package com.lwt.tavern.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.entity.DeleteUser;
import com.lwt.tavern.mapper.DeleteUserMapper;
import com.lwt.tavern.service.DeleteUserService;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserServiceImpl extends ServiceImpl<DeleteUserMapper, DeleteUser> implements DeleteUserService {
}
