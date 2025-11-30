package com.algcockpit.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 算法执行请求DTO
 * 
 * @author Algorithm Cockpit Team
 */
@Data
@Accessors(chain = true)
public class ExecuteRequest {

    /**
     * 算法ID
     */
    @NotNull(message = "算法ID不能为空")
    private Long algorithmId;

    /**
     * 编程语言
     */
    @NotBlank(message = "编程语言不能为空")
    private String language;

    /**
     * 代码内容
     */
    @NotBlank(message = "代码内容不能为空")
    private String code;

    /**
     * 输入数据
     */
    private String inputData;

    /**
     * 执行超时时间（秒）
     */
    private Integer timeout = 10;

    /**
     * 内存限制（MB）
     */
    private Integer memoryLimit = 128;
}