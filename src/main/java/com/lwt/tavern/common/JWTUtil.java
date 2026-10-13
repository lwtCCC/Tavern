package com.lwt.tavern.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JWTUtil {
    // 签名密钥，生产环境应放在配置文件里，长度至少 32 字节
    private static final String SECRET = "guan-yu-ci-xiang-mu-jin-yong-yu-shi-yan";
    private static final long EXPIRE = 3600_000L; // 1 小时

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * 生成 token
     */
    public String createToken(Long userId, String username) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(userId))       // 主题，通常放用户ID
                .claim("username", username)            // 自定义声明
                .issuedAt(now)                          // 签发时间
                .expiration(new Date(now.getTime() + EXPIRE)) // 过期时间
                .signWith(key)                          // 签名算法由 key 自动推断
                .compact();
    }

    /**
     * 解析 token，返回 Claims
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)   // 验签
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从 token 中取用户ID
     */
    public Long getUserId(String token) {
        return Long.valueOf(parseToken(token).getSubject());
    }

    /**
     * 判断 token 是否有效（未过期且签名正确）
     */
    public boolean isValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
