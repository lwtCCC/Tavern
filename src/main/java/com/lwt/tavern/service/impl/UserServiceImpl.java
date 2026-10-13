package com.lwt.tavern.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.common.JWTUtil;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.entry.dto.LoginDTO;
import com.lwt.tavern.entry.dto.UserDTO;
import com.lwt.tavern.entry.User;
import com.lwt.tavern.mapper.UserMapper;
import com.lwt.tavern.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.lwt.tavern.common.RedisConstants.LOGIN_CODE_KEY;
import static com.lwt.tavern.common.RedisConstants.LOGIN_TOKEN_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final StringRedisTemplate stringRedisTemplate;

    private final JWTUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    /**
     * 用户名密码登录
     * @param loginDTO 登录用的dto
     * @return token
     */
    @Override
    public Result loginByUsername(LoginDTO loginDTO) {
        //1.获取用户名和密码
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        //2.校验是否为空
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.fail("用户名或密码不能为空");
        }
        //3.查询数据库是否存在
        User user = query().eq("username", username).one();
        //密码加密比较
        if (BeanUtil.isEmpty(user) || !passwordEncoder.matches(password,user.getPassword())){
            return Result.fail("该用户不存在，请先注册");
        }
        //4.生成token
        String token = jwtUtil.createToken(user.getId(), user.getUsername());
        //5.将用户和token放入redis
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        stringRedisTemplate.opsForValue().set(LOGIN_TOKEN_KEY + token, JSONUtil.toJsonStr(userDTO));
        //6.返回token
        return Result.success(token);
    }

    /**
     * 电话号码登录
     * @param loginDTO 登录dto
     * @return token
     */
    @Override
    public Result loginByPhone(LoginDTO loginDTO) {
        //1.获取手机号和验证码
        String phone = loginDTO.getPhone();
        String code = loginDTO.getCode();
        //2.判断非空
        if (StrUtil.isBlank(phone) || StrUtil.isBlank(code)){
            return Result.fail("用户名或密码不能为空");
        }
        //3.和redis的验证码比较
        String codeORI = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY+phone);
        if (codeORI != null && !codeORI.equals(code)){
            return Result.fail("验证码错误");
        }

        //4.删除原有验证码
        Boolean delete = stringRedisTemplate.delete(LOGIN_CODE_KEY + phone);
        //5.查询用户
        User user = query().eq("phone", phone).one();
        //6.生成token
        String token = jwtUtil.createToken(user.getId(), user.getUsername());
        //7.将用户和token放入redis
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        stringRedisTemplate.opsForValue().set(LOGIN_TOKEN_KEY + token, JSONUtil.toJsonStr(userDTO));

        return Result.success(token);
    }

    /**
     * 发送验证码
     * @param phone 手机号
     * @return 验证码
     */
    @Override
    public Result sendCode(String phone) {
        //1.校验手机号
        if (StrUtil.isBlank(phone)){
            return Result.fail("手机号错误");
        }

        //2.生成验证码
        String code = RandomUtil.randomNumbers(4);

        //3.在redis存储验证码
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY+phone,code);

        //4.发送验证码
        return Result.success(code);
    }

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return 用户信息
     */
    @Override
    public Result queryUserById(Integer id) {
        //1.校验id是否为空
        if (id == null){
            return Result.fail("id不合法");
        }
        //2.查询id用户
        User user = query().eq("id", id).one();
        //3.校验用户是否为空
        if (user == null){
            //直接返回空
            return Result.success();
        }
        //4.转dto
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        //5.返回dto
        return Result.success(userDTO);
    }

    /**
     * 注册功能
     * @param loginDTO 登录用dto
     * @return token
     */
    @Override
    public Result register(LoginDTO loginDTO) {
        //1.获取手机号，密码，验证码
        String phone = loginDTO.getPhone();
        String password = loginDTO.getPassword();
        String code = loginDTO.getCode();
        //2.非空判断
        if (StrUtil.isBlank(phone) || StrUtil.isBlank(password) || StrUtil.isBlank(code)){
            return Result.fail("手机号或者密码不能为空");
        }

        if (StrUtil.isBlank(code)){
            return Result.fail("验证码不能为空");
        }

        //3.校验验证码
        String codeORI = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        if (!code.equals(codeORI)){
            return Result.fail("验证码不同，请重新输入");
        }
        //4.写入数据库
        //5.随机生成一个用户名
        String username = RandomUtil.randomString(8);
        User user = BeanUtil.copyProperties(loginDTO, User.class);
        //用户名
        user.setUsername(username);
        //id
        //创建时间
        user.setCreateTime(LocalDateTime.now());
        //更新时间
        user.setUpdateTime(LocalDateTime.now());

        save(user);
        //6.生成token
        String token = jwtUtil.createToken(user.getId(), user.getUsername());
        //7.将用户和token放入redis
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        stringRedisTemplate.opsForValue().set(LOGIN_TOKEN_KEY + token, JSONUtil.toJsonStr(userDTO));
        //8.返回token
        return Result.success(token);
    }
}
