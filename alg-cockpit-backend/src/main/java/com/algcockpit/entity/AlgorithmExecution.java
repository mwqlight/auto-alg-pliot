package com.algcockpit.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 算法执行记录实体类
 * 
 * @author Algorithm Cockpit Team
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("alg_execution")
public class AlgorithmExecution {

    /**
     * 执行记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 算法ID
     */
    @TableField("algorithm_id")
    private Long algorithmId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 输入参数
     */
    @TableField("input_params")
    private String inputParams;

    /**
     * 输出结果
     */
    @TableField("output_result")
    private String outputResult;

    /**
     * 执行状态：0-执行中，1-成功，2-失败，3-超时
     */
    @TableField("status")
    private Integer status;

    /**
     * 执行时间（毫秒）
     */
    @TableField("execution_time")
    private Long executionTime;

    /**
     * 内存使用（MB）
     */
    @TableField("memory_usage")
    private Long memoryUsage;

    /**
     * 错误信息
     */
    @TableField("error_message")
    private String errorMessage;

    /**
     * 执行环境信息
     */
    @TableField("environment_info")
    private String environmentInfo;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}