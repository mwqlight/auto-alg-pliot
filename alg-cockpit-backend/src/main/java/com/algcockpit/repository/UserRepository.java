package com.algcockpit.repository;

import com.algcockpit.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层
 * 
 * @author Algorithm Cockpit Team
 * @version 1.0.0
 */
@Mapper
public interface UserRepository extends BaseMapper<User> {
}