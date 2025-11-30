package com.algcockpit.service;

import com.algcockpit.dto.request.ExecuteRequest;
import com.algcockpit.dto.response.ExecuteResponse;

/**
 * 算法执行服务接口
 * 
 * @author Algorithm Cockpit Team
 */
public interface AlgorithmExecutionService {

    /**
     * 执行算法代码
     * 
     * @param request 执行请求
     * @return 执行响应
     */
    ExecuteResponse executeAlgorithm(ExecuteRequest request);

    /**
     * 异步执行算法代码
     * 
     * @param request 执行请求
     * @return 执行ID
     */
    String executeAlgorithmAsync(ExecuteRequest request);

    /**
     * 获取异步执行结果
     * 
     * @param executionId 执行ID
     * @return 执行响应
     */
    ExecuteResponse getAsyncExecutionResult(String executionId);

    /**
     * 取消异步执行
     * 
     * @param executionId 执行ID
     * @return 是否取消成功
     */
    Boolean cancelAsyncExecution(String executionId);

    /**
     * 验证代码安全性
     * 
     * @param code 代码内容
     * @param language 编程语言
     * @return 是否安全
     */
    Boolean validateCodeSecurity(String code, String language);

    /**
     * 获取执行统计信息
     * 
     * @param algorithmId 算法ID
     * @return 统计信息
     */
    String getExecutionStatistics(Long algorithmId);
}