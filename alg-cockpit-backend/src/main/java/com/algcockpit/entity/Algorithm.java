package com.algcockpit.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 算法信息实体类
 * 
 * @author Algorithm Cockpit Team
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("algorithm")
public class Algorithm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分类ID
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 算法名称
     */
    @TableField("name")
    private String name;

    /**
     * 算法描述
     */
    @TableField("description")
    private String description;

    /**
     * 算法思想
     */
    @TableField("algorithm_thought")
    private String algorithmThought;

    /**
     * 应用场景
     */
    @TableField("application_scenarios")
    private String applicationScenarios;

    /**
     * 变种与优化
     */
    @TableField("variants_optimizations")
    private String variantsOptimizations;

    /**
     * 思考题
     */
    @TableField("thinking_questions")
    private String thinkingQuestions;

    /**
     * 算法代码（Python/Java等）
     */
    @TableField("code")
    private String code;

    /**
     * 算法语言：python, java, cpp等
     */
    @TableField("language")
    private String language;

    /**
     * 输入参数说明
     */
    @TableField("input_params")
    private String inputParams;

    /**
     * 输出结果说明
     */
    @TableField("output_params")
    private String outputParams;

    /**
     * 时间复杂度
     */
    @TableField("time_complexity")
    private String timeComplexity;

    /**
     * 空间复杂度
     */
    @TableField("space_complexity")
    private String spaceComplexity;

    /**
     * 创建用户ID
     */
    @TableField("create_user_id")
    private Long createUserId;

    /**
     * 难度等级：1-简单，2-中等，3-困难
     */
    @TableField("difficulty_level")
    private Integer difficultyLevel;

    /**
     * 热度
     */
    @TableField("popularity")
    private Integer popularity;

    /**
     * 状态：0-禁用，1-启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}