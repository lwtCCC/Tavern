package com.lwt.tavern.common;

public class RedisConstants {

    public static final String LOGIN_CODE_KEY = "login:code:";//验证码
    public static final String REGISTER_CODE_KEY = "register:code:";//注册验证码
    public static final String RESET_CODE_KEY = "reset:code:";//重置密码的验证码
    public static final Long CODE_KEY_TTL = 3600L;//超时时间

    //登录token
    public static final String LOGIN_TOKEN_KEY = "login:token:";
    public static final Long LOGIN_TOKEN_KEY_TTL = 3600L;

    //书的详情
    public static final String BOOK_DETAIL_KEY = "book:detail:";
    public static final Long BOOK_DETAIL_TTL = 3600L;

    //分页:书
    public static final String PAGE_BOOK_KEY = "page:book:";
    public static final Long PAGE_BOOK_TTL = 5L;

    //点赞用户列表
    public static final String LIKE_BOOK = "like:0";
}
