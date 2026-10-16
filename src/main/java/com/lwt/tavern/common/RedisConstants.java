package com.lwt.tavern.common;

public class RedisConstants {
    //验证码
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_KEY_TTL = 3600L;

    //注册验证码
    public static final String REGISTER_CODE_KEY = "register:code:";
    public static final Long REGISTER_CODE_KEY_TTL = 3600L;

    //重置密码的验证码
    public static final String RESET_CODE_KEY = "reset:code:";
    public static final Long RESET_CODE_KEY_TTL = 3600L;

    //登录token
    public static final String LOGIN_TOKEN_KEY = "login:token:";
    public static final Long LOGIN_TOKEN_KEY_TTL = 3600L;


    public static final String CACHE_BOOK_KEY = "cache:book:";
    public static final Long CACHE_BOOK_TTL = 3600L;
}
