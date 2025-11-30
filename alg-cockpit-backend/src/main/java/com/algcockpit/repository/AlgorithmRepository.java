package com.algcockpit.repository;

import com.algcockpit.entity.Algorithm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 算法信息数据访问层
 * 
 * @author Algorithm Cockpit Team
 */
@Mapper
public interface AlgorithmRepository extends BaseMapper<Algorithm> {

    /**
     * 根据分类ID查询算法列表
     */
    @Select("SELECT * FROM algorithm WHERE category_id = #{categoryId} AND status = 1 AND deleted = 0 ORDER BY popularity DESC, created_time DESC")
    List<Algorithm> selectByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 分页查询算法列表
     */
    @Select("<script>" +
            "SELECT * FROM algorithm WHERE deleted = 0 " +
            "<if test='categoryId != null'>AND category_id = #{categoryId}</if>" +
            "<if test='difficultyLevel != null'>AND difficulty_level = #{difficultyLevel}</if>" +
            "<if test='status != null'>AND status = #{status}</if>" +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (name LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%'))" +
            "</if>" +
            "ORDER BY popularity DESC, created_time DESC" +
            "</script>")
    IPage<Algorithm> selectAlgorithmPage(Page<Algorithm> page, 
                                        @Param("categoryId") Long categoryId,
                                        @Param("difficultyLevel") Integer difficultyLevel,
                                        @Param("status") Integer status,
                                        @Param("keyword") String keyword);

    /**
     * 根据算法名称查询算法
     */
    @Select("SELECT * FROM algorithm WHERE name = #{name} AND deleted = 0")
    Algorithm selectByName(@Param("name") String name);

    /**
     * 增加算法热度
     */
    @Select("UPDATE algorithm SET popularity = popularity + 1 WHERE id = #{id} AND deleted = 0")
    void increasePopularity(@Param("id") Long id);
}