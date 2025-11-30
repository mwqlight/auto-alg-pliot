package com.algcockpit.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 算法复杂度实体类
 * 
 * @author Algorithm Cockpit Team
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("algorithm_complexity")
public class AlgorithmComplexity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 算法ID
     */
    @TableField("algorithm_id")
    private Long algorithmId;

    /**
     * 最佳时间复杂度
     */
    @TableField("time_best")
    private String timeBest;

    /**
     * 平均时间复杂度
     */
    @TableField("time_average")
    private String timeAverage;

    /**
     * 最坏时间复杂度
     */
    @TableField("time_worst")
    private String timeWorst;

    /**
     * 空间复杂度
     */
    @TableField("space_complexity")
    private String spaceComplexity;

    /**
     * 稳定性：0-不稳定，1-稳定
     */
    @TableField("stability")
    private Integer stability;

    /**
     * 复杂度分析说明
     */
    @TableField("complexity_analysis")
    private String complexityAnalysis;

    /**
     * 性能测试数据
     */
    @TableField("performance_data")
    private String performanceData;

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