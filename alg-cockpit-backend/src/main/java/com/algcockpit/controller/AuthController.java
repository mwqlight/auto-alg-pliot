package com.algcockpit.controller;

import com.algcockpit.dto.request.LoginRequest;
import com.algcockpit.dto.response.LoginResponse;
import com.algcockpit.entity.User;
import com.algcockpit.service.UserService;
import com.algcockpit.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 认证控制器
 * 
 * @author Algorithm Cockpit Team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户认证相关接口")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口")
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "用户注册接口")
    public ResponseEntity<String> register(@Validated @RequestBody User user) {
        boolean result = userService.register(user);
        if (result) {
            return ResponseEntity.ok("注册成功");
        } else {
            return ResponseEntity.badRequest().body("注册失败");
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新Token", description = "刷新访问令牌")
    public ResponseEntity<String> refreshToken(HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);
        String newToken = userService.refreshToken(token);
        return ResponseEntity.ok(newToken);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出接口")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);
        userService.logout(token);
        return ResponseEntity.ok("登出成功");
    }

    @GetMapping("/info")
    @Operation(summary = "获取用户信息", description = "获取当前登录用户信息")
    public ResponseEntity<User> getUserInfo(HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequest(request);
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getByUsername(username);
        return ResponseEntity.ok(user);
    }
}