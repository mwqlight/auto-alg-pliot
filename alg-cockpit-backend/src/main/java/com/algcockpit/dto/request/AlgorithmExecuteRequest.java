package com.algcockpit.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 算法执行请求DTO
 * 
 * @author Algorithm Cockpit Team
 * @version 1.0.0
 */
@Data
public class AlgorithmExecuteRequest {

    /**
     * 算法ID
     */
    @NotNull(message = "算法ID不能为空")
    private Long algorithmId;

    /**
     * 输入参数（JSON格式）
     */
    private String inputParams;

    /**
     * 执行环境配置
     */
    private ExecutionConfig executionConfig;

    /**
     * 执行环境配置
     */
    @Data
    public static class ExecutionConfig {
        
        /**
         * 超时时间（秒）
         */
        private Integer timeout = 30;

        /**
         * 内存限制（MB）
         */
        private Integer memoryLimit = 512;

        /**
         * 是否启用沙箱模式
         */
        private Boolean sandboxMode = true;
    }
}