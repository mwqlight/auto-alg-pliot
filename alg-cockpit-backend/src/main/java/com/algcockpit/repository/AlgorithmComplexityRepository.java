package com.algcockpit.repository;

import com.algcockpit.entity.AlgorithmComplexity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 算法复杂度数据访问层
 * 
 * @author Algorithm Cockpit Team
 */
@Mapper
public interface AlgorithmComplexityRepository extends BaseMapper<AlgorithmComplexity> {

    /**
     * 根据算法ID查询复杂度信息
     */
    @Select("SELECT * FROM algorithm_complexity WHERE algorithm_id = #{algorithmId} AND deleted = 0")
    AlgorithmComplexity selectByAlgorithmId(@Param("algorithmId") Long algorithmId);

    /**
     * 根据算法ID删除复杂度信息（逻辑删除）
     */
    @Select("UPDATE algorithm_complexity SET deleted = 1 WHERE algorithm_id = #{algorithmId}")
    void deleteByAlgorithmId(@Param("algorithmId") Long algorithmId);
}