package com.algcockpit.repository;

import com.algcockpit.entity.DemoData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 演示数据数据访问层
 * 
 * @author Algorithm Cockpit Team
 */
@Mapper
public interface DemoDataRepository extends BaseMapper<DemoData> {

    /**
     * 根据算法ID查询演示数据列表
     */
    @Select("SELECT * FROM demo_data WHERE algorithm_id = #{algorithmId} AND deleted = 0 ORDER BY data_type, created_time")
    List<DemoData> selectByAlgorithmId(@Param("algorithmId") Long algorithmId);

    /**
     * 根据算法ID和数据类型查询演示数据
     */
    @Select("SELECT * FROM demo_data WHERE algorithm_id = #{algorithmId} AND data_type = #{dataType} AND deleted = 0 ORDER BY created_time")
    List<DemoData> selectByAlgorithmIdAndDataType(@Param("algorithmId") Long algorithmId, 
                                                  @Param("dataType") String dataType);

    /**
     * 根据算法ID删除演示数据（逻辑删除）
     */
    @Select("UPDATE demo_data SET deleted = 1 WHERE algorithm_id = #{algorithmId}")
    void deleteByAlgorithmId(@Param("algorithmId") Long algorithmId);
}