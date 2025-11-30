package com.algcockpit.dto.response;

import lombok.Data;

/**
 * 登录响应DTO
 * 
 * @author Algorithm Cockpit Team
 * @version 1.0.0
 */
@Data
public class LoginResponse {

    /**
     * 访问令牌
     */
    private String token;

    /**
     * 用户信息
     */
    private UserInfo userInfo;

    /**
     * 过期时间
     */
    private Long expiresIn;

    /**
     * 用户信息DTO
     */
    @Data
    public static class UserInfo {
        private Long id;
        private String username;
        private String email;
        private String avatar;
        private String role;
    }
}