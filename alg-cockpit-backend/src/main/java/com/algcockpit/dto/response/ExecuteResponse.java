package com.algcockpit.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 算法执行响应DTO
 * 
 * @author Algorithm Cockpit Team
 */
@Data
@Accessors(chain = true)
public class ExecuteResponse {

    /**
     * 执行是否成功
     */
    private Boolean success;

    /**
     * 执行结果
     */
    private String output;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;

    /**
     * 内存使用（KB）
     */
    private Long memoryUsed;

    /**
     * 退出码
     */
    private Integer exitCode;

    /**
     * 是否超时
     */
    private Boolean timeout;

    /**
     * 是否内存溢出
     */
    private Boolean memoryExceeded;

    /**
     * 执行状态
     */
    private String status;

    /**
     * 执行ID（用于异步执行）
     */
    private String executionId;

    /**
     * 获取执行是否成功
     */
    public Boolean isSuccess() {
        return success;
    }
}