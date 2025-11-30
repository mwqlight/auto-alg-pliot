package com.algcockpit.service;

import com.algcockpit.dto.request.ExecuteRequest;
import com.algcockpit.dto.response.ExecuteResponse;

/**
 * 安全沙箱执行器接口
 * 
 * @author Algorithm Cockpit Team
 */
public interface SandboxExecutor {

    /**
     * 执行算法代码
     * 
     * @param request 执行请求
     * @return 执行响应
     */
    ExecuteResponse execute(ExecuteRequest request);

    /**
     * 异步执行算法代码
     * 
     * @param request 执行请求
     * @return 执行ID
     */
    String executeAsync(ExecuteRequest request);

    /**
     * 获取异步执行结果
     * 
     * @param executionId 执行ID
     * @return 执行响应
     */
    ExecuteResponse getAsyncResult(String executionId);

    /**
     * 取消异步执行
     * 
     * @param executionId 执行ID
     * @return 是否取消成功
     */
    Boolean cancelAsyncExecution(String executionId);

    /**
     * 检查支持的编程语言
     * 
     * @param language 编程语言
     * @return 是否支持
     */
    Boolean isLanguageSupported(String language);

    /**
     * 获取沙箱状态
     * 
     * @return 沙箱状态信息
     */
    String getSandboxStatus();
}