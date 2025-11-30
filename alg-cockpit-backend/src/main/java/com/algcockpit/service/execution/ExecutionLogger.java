package com.algcockpit.service.execution;

import com.algcockpit.dto.request.ExecuteRequest;
import com.algcockpit.dto.response.ExecuteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 执行日志记录器
 * 负责记录算法执行的详细日志
 * 
 * @author Algorithm Cockpit Team
 */
@Slf4j
@Component
public class ExecutionLogger {

    /**
     * 记录执行结果
     * 
     * @param request 执行请求
     * @param response 执行响应
     */
    public void logExecutionResult(ExecuteRequest request, ExecuteResponse response) {
        if (response.isSuccess()) {
            log.info("算法执行成功 - 算法ID: {}, 语言: {}, 执行时间: {}ms", 
                    request.getAlgorithmId(), 
                    request.getLanguage(), 
                    response.getExecutionTime());
        } else {
            log.warn("算法执行失败 - 算法ID: {}, 语言: {}, 错误: {}", 
                    request.getAlgorithmId(), 
                    request.getLanguage(), 
                    response.getError());
        }
    }

    /**
     * 记录异步执行开始
     * 
     * @param executionId 执行ID
     * @param algorithmId 算法ID
     */
    public void logAsyncExecutionStart(String executionId, Long algorithmId) {
        log.info("异步执行开始 - 执行ID: {}, 算法ID: {}", executionId, algorithmId);
    }

    /**
     * 记录异步执行完成
     * 
     * @param executionId 执行ID
     * @param success 是否成功
     */
    public void logAsyncExecutionComplete(String executionId, Boolean success) {
        if (success) {
            log.info("异步执行完成 - 执行ID: {}", executionId);
        } else {
            log.warn("异步执行失败 - 执行ID: {}", executionId);
        }
    }

    /**
     * 记录执行取消
     * 
     * @param executionId 执行ID
     */
    public void logExecutionCancelled(String executionId) {
        log.info("执行取消 - 执行ID: {}", executionId);
    }
}