package com.algcockpit.service.impl;

import com.algcockpit.dto.request.LoginRequest;
import com.algcockpit.dto.response.LoginResponse;
import com.algcockpit.entity.User;
import com.algcockpit.repository.UserRepository;
import com.algcockpit.service.UserService;
import com.algcockpit.util.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 * 
 * @author Algorithm Cockpit Team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserRepository, User> implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 认证用户
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成Token
        String token = jwtUtil.generateToken(loginRequest.getUsername());

        // 获取用户信息
        User user = getByUsername(loginRequest.getUsername());

        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setExpiresIn(jwtUtil.getExpirationDateFromToken(token).getTime());

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setRole(user.getRole());
        response.setUserInfo(userInfo);

        // 将Token存入Redis
        redisTemplate.opsForValue().set("token:" + user.getUsername(), token, 
                jwtUtil.getExpirationDateFromToken(token).getTime() - System.currentTimeMillis(), 
                TimeUnit.MILLISECONDS);

        log.info("用户登录成功: {}", loginRequest.getUsername());
        return response;
    }

    @Override
    public boolean register(User user) {
        // 检查用户名是否已存在
        if (userRepository.selectCount(new QueryWrapper<User>().eq("username", user.getUsername())) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setStatus(1);

        return userRepository.insert(user) > 0;
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.selectOne(new QueryWrapper<User>().eq("username", username));
    }

    @Override
    public String refreshToken(String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        
        if (username != null && !jwtUtil.isTokenExpired(token)) {
            String newToken = jwtUtil.generateToken(username);
            
            // 更新Redis中的Token
            redisTemplate.opsForValue().set("token:" + username, newToken, 
                    jwtUtil.getExpirationDateFromToken(newToken).getTime() - System.currentTimeMillis(), 
                    TimeUnit.MILLISECONDS);
            
            return newToken;
        }
        
        throw new RuntimeException("Token刷新失败");
    }

    @Override
    public void logout(String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        
        if (username != null) {
            // 从Redis中删除Token
            redisTemplate.delete("token:" + username);
            SecurityContextHolder.clearContext();
            log.info("用户登出: {}", username);
        }
    }
}