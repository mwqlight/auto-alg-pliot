package com.algcockpit.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 算法代码实体类
 * 
 * @author Algorithm Cockpit Team
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("algorithm_code")
public class AlgorithmCode implements Serializable {

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
     * 编程语言：java, python, javascript
     */
    @TableField("language")
    private String language;

    /**
     * 代码内容
     */
    @TableField("code_content")
    private String codeContent;

    /**
     * 代码注释
     */
    @TableField("code_comment")
    private String codeComment;

    /**
     * 执行示例
     */
    @TableField("execution_example")
    private String executionExample;

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