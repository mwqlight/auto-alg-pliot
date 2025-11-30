package com.algcockpit.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 演示数据实体类
 * 
 * @author Algorithm Cockpit Team
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("demo_data")
public class DemoData implements Serializable {

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
     * 数据类型：array, graph, tree, string
     */
    @TableField("data_type")
    private String dataType;

    /**
     * 输入数据
     */
    @TableField("input_data")
    private String inputData;

    /**
     * 输出数据
     */
    @TableField("output_data")
    private String outputData;

    /**
     * 可视化配置
     */
    @TableField("visualization_config")
    private String visualizationConfig;

    /**
     * 数据描述
     */
    @TableField("description")
    private String description;

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