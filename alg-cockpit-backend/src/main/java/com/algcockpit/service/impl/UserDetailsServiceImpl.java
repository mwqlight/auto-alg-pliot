package com.algcockpit.service.impl;

import com.algcockpit.entity.User;
import com.algcockpit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 用户详情服务实现类
 * 
 * @author Algorithm Cockpit Team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.selectOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>()
                .eq("username", username)
                .eq("status", 1)
                .eq("deleted", 0));

        if (user == null) {
            log.warn("用户不存在或已被禁用: {}", username);
            throw new UsernameNotFoundException("用户不存在或已被禁用");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}