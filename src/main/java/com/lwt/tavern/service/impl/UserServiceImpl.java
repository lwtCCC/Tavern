package com.lwt.tavern.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.lwt.tavern.common.JWTUtil;
import com.lwt.tavern.common.Result;
import com.lwt.tavern.common.SnowFlake;
import com.lwt.tavern.common.UserHolder;
import com.lwt.tavern.dto.user.LoginDTO;
import com.lwt.tavern.dto.user.ResetPasswordDTO;
import com.lwt.tavern.dto.user.UserDTO;
import com.lwt.tavern.entity.DeleteUser;
import com.lwt.tavern.entity.User;
import com.lwt.tavern.mapper.UserMapper;
import com.lwt.tavern.service.DeleteUserService;
import com.lwt.tavern.service.UserService;
import com.lwt.tavern.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.lwt.tavern.common.CommonConstants.USER_PREFIX;
import static com.lwt.tavern.common.RedisConstants.*;

@Slf4j
@Service

public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final StringRedisTemplate stringRedisTemplate;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final SnowFlake userSnowFlake;
    private final DeleteUserService deleteUserService;

    public UserServiceImpl(
            StringRedisTemplate stringRedisTemplate,
            JWTUtil jwtUtil,
            PasswordEncoder passwordEncoder,
            @Qualifier("userSnowFlake") SnowFlake userSnowFlake, DeleteUserService deleteUserService){
        this.stringRedisTemplate = stringRedisTemplate;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userSnowFlake = userSnowFlake;
        this.deleteUserService = deleteUserService;
    }

    /**
     * 发送验证码
     * @param phone 手机号
     * @return 验证码
     */
    @Override
    public Result sendCode(String phone) {
        return getCode(LOGIN_CODE_KEY,phone,LOGIN_CODE_KEY_TTL,TimeUnit.SECONDS);
    }

    /**
     * 发送验证码(注册验证码)
     * @param phone 手机号
     * @return 验证码
     */
    @Override
    public Result sendCodeRegister(String phone) {
        return getCode(REGISTER_CODE_KEY,phone,REGISTER_CODE_KEY_TTL,TimeUnit.SECONDS);
    }

    @Override
    public Result me() {
        //1.获取自己的id
        UserDTO user = UserHolder.getUser();
        if (BeanUtil.isEmpty(user)) {
            return Result.fail("登录超时，请重新登录");
        }
        Long id = user.getId();
        //2.查询数据库获取用户详细信息
        User queryUser = query().eq("id", id).one();
        if (BeanUtil.isEmpty(queryUser)) {
            return Result.fail("该用户不存在");
        }
        //3.基础 信息封装成 vo 给前端
        UserVO userVO = BeanUtil.copyProperties(queryUser, UserVO.class);
        return Result.success(userVO);
    }

    @Override
    public Result resetSendCode(String phone) {
        return getCode(RESET_CODE_KEY,phone,RESET_CODE_KEY_TTL,TimeUnit.SECONDS);
    }

    @Override
    public Result verify(LoginDTO loginDTO) {
        //1.获取手机号和验证码
        String phone = loginDTO.getPhone();
        String code = loginDTO.getCode();
        //2.判断非空
        if (StrUtil.isBlank(phone) || StrUtil.isBlank(code)){
            return Result.fail("用户名或密码不能为空");
        }
        //3.和redis的验证码比较
        String codeORI = stringRedisTemplate.opsForValue().get(RESET_CODE_KEY+phone);
        if (codeORI != null && !codeORI.equals(code)){
            return Result.fail("验证码错误");
        }

        //4.删除原有验证码
        Boolean delete = stringRedisTemplate.delete(RESET_CODE_KEY + phone);
        if (!delete){
            System.out.println("验证码删除失败");
        }
        //返回成功
        return Result.success(true);
    }

    @Override
    public Result resetPassword(ResetPasswordDTO resetPasswordDTO) {

        UserDTO userDTO = UserHolder.getUser();
        Long id = userDTO.getId();

        if (BeanUtil.isEmpty(id)){
            return Result.fail("");
        }

        if (BeanUtil.isEmpty(resetPasswordDTO)){
            return Result.fail("");
        }
        String newPassword = resetPasswordDTO.getNewPassword();
        String confirmPassword = resetPasswordDTO.getConfirmPassword();

        if (!newPassword.equals(confirmPassword)){
            return Result.fail("");
        }

        User user = query().eq("id", id).one();
        String oldPassword = user.getPassword();
        String encodePassword = passwordEncoder.encode(newPassword);
        if (Objects.equals(encodePassword, oldPassword)){
            return Result.fail("");
        }

        user.setPassword(encodePassword);
        save(user);
        return Result.success();
    }

    @Override
    public Result resetProfile(String profile) {
        UserDTO userDTO = UserHolder.getUser();
        Long id = userDTO.getId();
        if (BeanUtil.isEmpty(id)){
            return Result.fail("");
        }
        User user = query().eq("id", id).one();

        if (StrUtil.isBlank(profile)){
            user.setProfile("该用户暂无留言");
            save(user);
        }

        user.setProfile(profile);
        save(user);
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        return Result.success(userVO);
    }

    @Override
    public Result deleteMe() {
        Long id = UserHolder.getUser().getId();
        User user = query().eq("id", id).one();
        DeleteUser deleteUser = BeanUtil.copyProperties(user, DeleteUser.class);
        deleteUserService.save(deleteUser);
        remove(
                new LambdaQueryWrapper<User>()
                        .eq(User::getId, id)
        );

        return Result.success();
    }


    private Result getCode(String prefix,String phone,Long ttl,TimeUnit timeUnit) {
        //1.校验手机号
        if (StrUtil.isBlank(phone)){
            return Result.fail("手机号错误");
        }
        //2.检查验证码是否存在
        String oldCode = stringRedisTemplate.opsForValue().get(prefix + phone);
        if (StrUtil.isNotBlank(oldCode)){
            //如果已经存在，则直接返回
            return Result.success(oldCode);
        }
        //3.生成验证码
        String code = RandomUtil.randomNumbers(4);
        //4.在redis存储验证码
        stringRedisTemplate.opsForValue()
                .set(prefix + phone, code, ttl, timeUnit);
        //5.发送验证码
        return Result.success(code);
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
        String codeORI = stringRedisTemplate.opsForValue().get(REGISTER_CODE_KEY + phone);
        if (!code.equals(codeORI)){
            return Result.fail("验证码不同，请重新输入");
        }

        //防止重复注册，按照手机号查一下是否存在
        User userOld = query().eq("phone", phone).one();
        if (userOld != null){
            return Result.fail("该手机号已注册");
        }

        //4.写入数据库
        //随机生成一个用户名
        String username = USER_PREFIX + RandomUtil.randomString(8);
        //雪花id
        Long id = userSnowFlake.nextId();
        User user = new User(id, username, passwordEncoder.encode(password),
                "aaaa", "hello", LocalDateTime.now(), LocalDateTime.now(), phone);

        save(user);
        //6.生成token
        String token = jwtUtil.createToken(id, username);
        //7.将用户和token放入redis
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        stringRedisTemplate.opsForValue()
                .set(
                        LOGIN_TOKEN_KEY + token,
                        JSONUtil.toJsonStr(userDTO),
                        LOGIN_TOKEN_KEY_TTL,
                        TimeUnit.MINUTES
                );
        //8.返回token
        return Result.success(token);
    }

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
        String token = jwtUtil.createToken(user.getId(), username);
        //5.将用户和token放入redis
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        stringRedisTemplate.opsForValue()
                .set(
                        LOGIN_TOKEN_KEY + token,
                        JSONUtil.toJsonStr(userDTO),
                        LOGIN_TOKEN_KEY_TTL,
                        TimeUnit.MINUTES
                );
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
        if (!delete){
            System.out.println("验证码删除失败");
        }
        //5.查询用户
        User user = query().eq("phone", phone).one();
        //6.生成token
        String token = jwtUtil.createToken(user.getId(), user.getUsername());
        //7.将用户和token放入redis
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        stringRedisTemplate.opsForValue()
                .set(
                        LOGIN_TOKEN_KEY + token,
                        JSONUtil.toJsonStr(userDTO),
                        LOGIN_TOKEN_KEY_TTL,
                        TimeUnit.MINUTES
                );

        return Result.success(token);
    }

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return 用户信息
     */
    @Override
    public Result queryUserById(Long id) {
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
        //4.转vo
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        //5.返回dto
        return Result.success(userVO);
    }
}
