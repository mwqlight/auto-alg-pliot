package com.algcockpit.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 
 * @author Algorithm Cockpit Team
 * @version 1.0.0
 */
@Slf4j
@Component
public class JwtUtil {

    @Value("${alg.cockpit.jwt.secret}")
    private String secret;

    @Value("${alg.cockpit.jwt.expiration}")
    private Long expiration;

    @Value("${alg.cockpit.jwt.header}")
    private String header;

    /**
     * 生成JWT Token
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从Token中获取用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * 验证Token是否有效
     */
    public boolean validateToken(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * 检查Token是否过期
     */
    public boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 获取Token过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    /**
     * 从Token中获取Claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取签名密钥
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 获取Token Header名称
     */
    public String getHeader() {
        return header;
    }

    /**
     * 从HttpServletRequest中获取Token
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        // 从Header中获取
        String token = request.getHeader(header);
        
        // 如果Header中没有，从参数中获取
        if (!StringUtils.hasText(token)) {
            token = request.getParameter("token");
        }
        
        // 如果参数中没有，从Cookie中获取
        if (!StringUtils.hasText(token) && request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        
        return token;
    }
}