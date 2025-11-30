package com.algcockpit.dto.response;

import lombok.Data;

/**
 * 算法执行响应DTO
 * 
 * @author Algorithm Cockpit Team
 * @version 1.0.0
 */
@Data
public class AlgorithmExecutionResponse {

    /**
     * 执行记录ID
     */
    private Long executionId;

    /**
     * 执行状态：0-执行中，1-成功，2-失败，3-超时
     */
    private Integer status;

    /**
     * 输出结果
     */
    private String outputResult;

    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;

    /**
     * 内存使用（MB）
     */
    private Long memoryUsage;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 执行环境信息
     */
    private String environmentInfo;
}