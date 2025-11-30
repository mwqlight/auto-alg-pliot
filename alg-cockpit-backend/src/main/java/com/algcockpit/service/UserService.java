package com.algcockpit.service;

import com.algcockpit.dto.request.LoginRequest;
import com.algcockpit.dto.response.LoginResponse;
import com.algcockpit.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户服务接口
 * 
 * @author Algorithm Cockpit Team
 * @version 1.0.0
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * 用户注册
     */
    boolean register(User user);

    /**
     * 根据用户名获取用户信息
     */
    User getByUsername(String username);

    /**
     * 刷新Token
     */
    String refreshToken(String token);

    /**
     * 登出
     */
    void logout(String token);
}