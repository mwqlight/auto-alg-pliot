package com.algcockpit.repository;

import com.algcockpit.entity.AlgorithmCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 算法分类数据访问层
 * 
 * @author Algorithm Cockpit Team
 */
@Mapper
public interface AlgorithmCategoryRepository extends BaseMapper<AlgorithmCategory> {

    /**
     * 查询所有启用的算法分类（按排序序号升序）
     */
    @Select("SELECT * FROM algorithm_category WHERE status = 1 AND deleted = 0 ORDER BY sort_order ASC")
    List<AlgorithmCategory> selectAllEnabledCategories();

    /**
     * 根据分类名称查询分类
     */
    @Select("SELECT * FROM algorithm_category WHERE name = #{name} AND deleted = 0")
    AlgorithmCategory selectByName(String name);
}