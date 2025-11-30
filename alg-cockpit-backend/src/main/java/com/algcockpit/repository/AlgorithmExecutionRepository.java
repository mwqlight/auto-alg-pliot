package com.algcockpit.repository;

import com.algcockpit.entity.AlgorithmExecution;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 算法执行记录仓库接口
 * 
 * @author 算法驾驶舱团队
 */
@Mapper
public interface AlgorithmExecutionRepository extends BaseMapper<AlgorithmExecution> {
    
}