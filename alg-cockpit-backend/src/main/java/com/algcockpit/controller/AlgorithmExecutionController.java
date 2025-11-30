package com.algcockpit.controller;

import com.algcockpit.dto.request.ExecuteRequest;
import com.algcockpit.dto.response.ExecuteResponse;
import com.algcockpit.service.AlgorithmExecutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 算法执行控制器
 * 提供算法代码执行、异步执行、安全验证等接口
 * 
 * @author Algorithm Cockpit Team
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/execution")
@Tag(name = "算法执行管理", description = "算法代码执行相关接口")
@Validated
public class AlgorithmExecutionController {

    @Autowired
    private AlgorithmExecutionService algorithmExecutionService;

    @PostMapping("/execute")
    @Operation(summary = "执行算法代码", description = "同步执行算法代码并返回结果")
    public ResponseEntity<ExecuteResponse> executeAlgorithm(
            @Valid @RequestBody ExecuteRequest request) {
        log.info("收到算法执行请求，算法ID: {}, 语言: {}", request.getAlgorithmId(), request.getLanguage());
        
        ExecuteResponse response = algorithmExecutionService.executeAlgorithm(request);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/execute-async")
    @Operation(summary = "异步执行算法代码", description = "异步执行算法代码，返回执行ID")
    public ResponseEntity<String> executeAlgorithmAsync(
            @Valid @RequestBody ExecuteRequest request) {
        log.info("收到异步算法执行请求，算法ID: {}", request.getAlgorithmId());
        
        String executionId = algorithmExecutionService.executeAlgorithmAsync(request);
        
        return ResponseEntity.ok(executionId);
    }

    @GetMapping("/async-result/{executionId}")
    @Operation(summary = "获取异步执行结果", description = "根据执行ID获取异步执行结果")
    public ResponseEntity<ExecuteResponse> getAsyncExecutionResult(
            @Parameter(description = "执行ID") @PathVariable String executionId) {
        log.info("获取异步执行结果，执行ID: {}", executionId);
        
        ExecuteResponse response = algorithmExecutionService.getAsyncExecutionResult(executionId);
        
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/async-cancel/{executionId}")
    @Operation(summary = "取消异步执行", description = "取消指定的异步执行任务")
    public ResponseEntity<Boolean> cancelAsyncExecution(
            @Parameter(description = "执行ID") @PathVariable String executionId) {
        log.info("取消异步执行，执行ID: {}", executionId);
        
        Boolean result = algorithmExecutionService.cancelAsyncExecution(executionId);
        
        return ResponseEntity.ok(result);
    }

    @PostMapping("/validate-security")
    @Operation(summary = "验证代码安全性", description = "验证代码是否包含危险操作")
    public ResponseEntity<Boolean> validateCodeSecurity(
            @RequestParam String code,
            @RequestParam String language) {
        log.info("验证代码安全性，语言: {}, 代码长度: {}", language, code.length());
        
        Boolean isValid = algorithmExecutionService.validateCodeSecurity(code, language);
        
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/statistics/{algorithmId}")
    @Operation(summary = "获取执行统计", description = "获取指定算法的执行统计信息")
    public ResponseEntity<String> getExecutionStatistics(
            @Parameter(description = "算法ID") @PathVariable Long algorithmId) {
        log.info("获取算法执行统计，算法ID: {}", algorithmId);
        
        String statistics = algorithmExecutionService.getExecutionStatistics(algorithmId);
        
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查算法执行服务是否正常")
    public ResponseEntity<String> healthCheck() {
        log.info("执行健康检查");
        
        return ResponseEntity.ok("算法执行服务运行正常");
    }
}