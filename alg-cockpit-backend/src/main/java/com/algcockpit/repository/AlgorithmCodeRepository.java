package com.algcockpit.repository;

import com.algcockpit.entity.AlgorithmCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 算法代码数据访问层
 * 
 * @author Algorithm Cockpit Team
 */
@Mapper
public interface AlgorithmCodeRepository extends BaseMapper<AlgorithmCode> {

    /**
     * 根据算法ID查询代码列表
     */
    @Select("SELECT * FROM algorithm_code WHERE algorithm_id = #{algorithmId} AND deleted = 0 ORDER BY language")
    List<AlgorithmCode> selectByAlgorithmId(@Param("algorithmId") Long algorithmId);

    /**
     * 根据算法ID和编程语言查询代码
     */
    @Select("SELECT * FROM algorithm_code WHERE algorithm_id = #{algorithmId} AND language = #{language} AND deleted = 0")
    AlgorithmCode selectByAlgorithmIdAndLanguage(@Param("algorithmId") Long algorithmId, 
                                                @Param("language") String language);

    /**
     * 根据算法ID删除代码（逻辑删除）
     */
    @Select("UPDATE algorithm_code SET deleted = 1 WHERE algorithm_id = #{algorithmId}")
    void deleteByAlgorithmId(@Param("algorithmId") Long algorithmId);
}