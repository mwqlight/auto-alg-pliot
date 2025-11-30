package com.algcockpit.service.impl;

import com.algcockpit.dto.request.ExecuteRequest;
import com.algcockpit.dto.response.ExecuteResponse;
import com.algcockpit.service.AlgorithmExecutionService;
import com.algcockpit.service.SandboxExecutor;
import com.algcockpit.service.execution.ExecutionLogger;
import com.algcockpit.service.security.CodeSecurityValidator;
import com.algcockpit.service.statistics.ExecutionStatisticsService;
import com.algcockpit.service.validation.AlgorithmValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 算法执行服务实现
 * 重构后的版本，职责单一，代码简洁
 * 
 * @author Algorithm Cockpit Team
 */
@Slf4j
@Service
public class AlgorithmExecutionServiceImpl implements AlgorithmExecutionService {

    @Autowired
    private SandboxExecutor sandboxExecutor;

    @Autowired
    private CodeSecurityValidator codeSecurityValidator;

    @Autowired
    private AlgorithmValidator algorithmValidator;

    @Autowired
    private ExecutionLogger executionLogger;

    @Autowired
    private ExecutionStatisticsService executionStatisticsService;

    @Override
    public ExecuteResponse executeAlgorithm(ExecuteRequest request) {
        log.info("开始执行算法，算法ID: {}, 语言: {}", request.getAlgorithmId(), request.getLanguage());
        
        // 验证算法存在性
        if (!algorithmValidator.validateAlgorithmExists(request.getAlgorithmId())) {
            return createErrorResponse("算法不存在，ID: " + request.getAlgorithmId(), "ALGORITHM_NOT_FOUND");
        }

        // 验证代码安全性
        if (!codeSecurityValidator.validateCodeSecurity(request.getCode(), request.getLanguage())) {
            return createErrorResponse("代码包含危险操作，拒绝执行", "UNSAFE_CODE");
        }

        // 执行代码
        ExecuteResponse response = sandboxExecutor.execute(request);
        
        // 记录执行日志
        executionLogger.logExecutionResult(request, response);
        
        return response;
    }

    @Override
    public String executeAlgorithmAsync(ExecuteRequest request) {
        log.info("开始异步执行算法，算法ID: {}", request.getAlgorithmId());
        
        // 验证算法存在性
        if (!algorithmValidator.validateAlgorithmExists(request.getAlgorithmId())) {
            throw new IllegalArgumentException("算法不存在，ID: " + request.getAlgorithmId());
        }

        // 验证代码安全性
        if (!codeSecurityValidator.validateCodeSecurity(request.getCode(), request.getLanguage())) {
            throw new SecurityException("代码包含危险操作，拒绝执行");
        }

        String executionId = sandboxExecutor.executeAsync(request);
        executionLogger.logAsyncExecutionStart(executionId, request.getAlgorithmId());
        
        return executionId;
    }

    @Override
    public ExecuteResponse getAsyncExecutionResult(String executionId) {
        ExecuteResponse response = sandboxExecutor.getAsyncResult(executionId);
        
        if (response != null) {
            executionLogger.logAsyncExecutionComplete(executionId, response.isSuccess());
        }
        
        return response;
    }

    @Override
    public Boolean cancelAsyncExecution(String executionId) {
        log.info("取消异步执行，执行ID: {}", executionId);
        
        Boolean result = sandboxExecutor.cancelAsyncExecution(executionId);
        
        if (result) {
            executionLogger.logExecutionCancelled(executionId);
        }
        
        return result;
    }

    @Override
    public Boolean validateCodeSecurity(String code, String language) {
        return codeSecurityValidator.validateCodeSecurity(code, language);
    }

    @Override
    public String getExecutionStatistics(Long algorithmId) {
        return executionStatisticsService.getExecutionStatistics(algorithmId);
    }

    /**
     * 创建错误响应
     */
    private ExecuteResponse createErrorResponse(String error, String status) {
        return new ExecuteResponse()
                .setSuccess(false)
                .setError(error)
                .setStatus(status);
    }
}