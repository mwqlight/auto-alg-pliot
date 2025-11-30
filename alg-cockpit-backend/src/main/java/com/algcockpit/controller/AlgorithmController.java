package com.algcockpit.controller;

import com.algcockpit.dto.request.AlgorithmExecuteRequest;
import com.algcockpit.dto.response.AlgorithmExecutionResponse;
import com.algcockpit.dto.response.ApiResponse;
import com.algcockpit.entity.Algorithm;
import com.algcockpit.entity.AlgorithmExecution;
import com.algcockpit.service.AlgorithmService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 算法信息控制器
 * 
 * @author Algorithm Cockpit Team
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/algorithms")
@Tag(name = "算法信息管理", description = "算法信息相关接口")
public class AlgorithmController {

    @Autowired
    private AlgorithmService algorithmService;

    /**
     * 根据分类ID获取算法列表
     */
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "根据分类获取算法列表", description = "根据分类ID获取该分类下的算法列表")
    public ApiResponse<List<Algorithm>> getAlgorithmsByCategory(
            @Parameter(description = "分类ID", required = true)
            @PathVariable Long categoryId) {
        try {
            List<Algorithm> algorithms = algorithmService.getAlgorithmsByCategoryId(categoryId);
            return ApiResponse.success(algorithms);
        } catch (Exception e) {
            log.error("根据分类获取算法列表失败，分类ID: {}", categoryId, e);
            return ApiResponse.error(500, "获取算法列表失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询算法列表
     */
    @GetMapping("/")
    @Operation(summary = "分页查询算法列表", description = "分页查询算法列表，支持分类、难度、状态、关键词筛选")
    public ApiResponse<IPage<Algorithm>> getAlgorithmPage(
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "页大小", example = "10")
            @RequestParam(defaultValue = "10") Long pageSize,
            @Parameter(description = "分类ID")
            @RequestParam(required = false) Long categoryId,
            @Parameter(description = "难度级别")
            @RequestParam(required = false) Integer difficultyLevel,
            @Parameter(description = "状态")
            @RequestParam(required = false) Integer status,
            @Parameter(description = "关键词")
            @RequestParam(required = false) String keyword) {
        try {
            IPage<Algorithm> page = algorithmService.getAlgorithmPage(pageNum, pageSize, 
                    categoryId, difficultyLevel, status, keyword);
            return ApiResponse.success(page);
        } catch (Exception e) {
            log.error("分页查询算法列表失败，页码: {}, 页大小: {}", pageNum, pageSize, e);
            return ApiResponse.error(500, "分页查询算法列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取算法详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取算法详情", description = "根据ID获取算法详细信息")
    public ApiResponse<Algorithm> getAlgorithmDetail(
            @Parameter(description = "算法ID", required = true)
            @PathVariable Long id) {
        try {
            Algorithm algorithm = algorithmService.getAlgorithmDetail(id);
            return ApiResponse.success(algorithm);
        } catch (Exception e) {
            log.error("获取算法详情失败，算法ID: {}", id, e);
            return ApiResponse.error(500, "获取算法详情失败: " + e.getMessage());
        }
    }

    /**
     * 创建算法
     */
    @PostMapping("/")
    @Operation(summary = "创建算法", description = "创建新的算法")
    public ApiResponse<Algorithm> createAlgorithm(
            @Parameter(description = "算法信息", required = true)
            @Valid @RequestBody Algorithm algorithm) {
        try {
            Algorithm createdAlgorithm = algorithmService.createAlgorithm(algorithm);
            return ApiResponse.success(createdAlgorithm);
        } catch (Exception e) {
            log.error("创建算法失败", e);
            return ApiResponse.error(500, "创建算法失败: " + e.getMessage());
        }
    }

    /**
     * 更新算法
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新算法", description = "更新指定ID的算法")
    public ApiResponse<Algorithm> updateAlgorithm(
            @Parameter(description = "算法ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "算法信息", required = true)
            @Valid @RequestBody Algorithm algorithm) {
        try {
            algorithm.setId(id);
            Algorithm updatedAlgorithm = algorithmService.updateAlgorithm(algorithm);
            return ApiResponse.success(updatedAlgorithm);
        } catch (Exception e) {
            log.error("更新算法失败，算法ID: {}", id, e);
            return ApiResponse.error(500, "更新算法失败: " + e.getMessage());
        }
    }

    /**
     * 删除算法
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除算法", description = "删除指定ID的算法（逻辑删除）")
    public ApiResponse<Boolean> deleteAlgorithm(
            @Parameter(description = "算法ID", required = true)
            @PathVariable Long id) {
        try {
            boolean success = algorithmService.deleteAlgorithm(id);
            return ApiResponse.success(success);
        } catch (Exception e) {
            log.error("删除算法失败，算法ID: {}", id, e);
            return ApiResponse.error(500, "删除算法失败: " + e.getMessage());
        }
    }

    /**
     * 执行算法
     */
    @PostMapping("/execute")
    @Operation(summary = "执行算法", description = "执行指定算法并返回执行结果")
    public ApiResponse<AlgorithmExecutionResponse> executeAlgorithm(
            @Parameter(description = "算法执行请求", required = true)
            @Valid @RequestBody AlgorithmExecuteRequest request) {
        try {
            AlgorithmExecutionResponse response = algorithmService.executeAlgorithm(request);
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("执行算法失败，算法ID: {}", request.getAlgorithmId(), e);
            return ApiResponse.error(500, "执行算法失败: " + e.getMessage());
        }
    }

    /**
     * 获取执行记录列表
     */
    @GetMapping("/executions")
    @Operation(summary = "获取执行记录列表", description = "获取当前用户的算法执行记录列表")
    public ApiResponse<IPage<AlgorithmExecution>> getExecutionRecords(
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "页大小", example = "10")
            @RequestParam(defaultValue = "10") Long pageSize,
            @Parameter(description = "用户ID")
            @RequestHeader("X-User-Id") Long userId) {
        try {
            Page<AlgorithmExecution> page = new Page<>(pageNum, pageSize);
            IPage<AlgorithmExecution> executionPage = algorithmService.getExecutionRecords(userId, page);
            return ApiResponse.success(executionPage);
        } catch (Exception e) {
            log.error("获取执行记录列表失败，用户ID: {}", userId, e);
            return ApiResponse.error(500, "获取执行记录列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取执行详情
     */
    @GetMapping("/executions/{executionId}")
    @Operation(summary = "获取执行详情", description = "根据执行记录ID获取执行详情")
    public ApiResponse<AlgorithmExecution> getExecutionDetail(
            @Parameter(description = "执行记录ID", required = true)
            @PathVariable Long executionId) {
        try {
            AlgorithmExecution execution = algorithmService.getExecutionDetail(executionId);
            return ApiResponse.success(execution);
        } catch (Exception e) {
            log.error("获取执行详情失败，执行记录ID: {}", executionId, e);
            return ApiResponse.error(500, "获取执行详情失败: " + e.getMessage());
        }
    }

    /**
     * 停止算法执行
     */
    @PostMapping("/executions/{executionId}/stop")
    @Operation(summary = "停止算法执行", description = "停止指定执行记录中的算法执行")
    public ApiResponse<Boolean> stopAlgorithmExecution(
            @Parameter(description = "执行记录ID", required = true)
            @PathVariable Long executionId) {
        try {
            boolean success = algorithmService.stopAlgorithmExecution(executionId);
            return ApiResponse.success(success);
        } catch (Exception e) {
            log.error("停止算法执行失败，执行记录ID: {}", executionId, e);
            return ApiResponse.error(500, "停止算法执行失败: " + e.getMessage());
        }
    }

    /**
     * 获取热门算法
     */
    @GetMapping("/popular")
    @Operation(summary = "获取热门算法", description = "根据热度排序获取热门算法列表")
    public ApiResponse<IPage<Algorithm>> getPopularAlgorithms(
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "页大小", example = "10")
            @RequestParam(defaultValue = "10") Long pageSize) {
        try {
            Page<Algorithm> page = new Page<>(pageNum, pageSize);
            IPage<Algorithm> algorithmPage = algorithmService.getPopularAlgorithms(page);
            return ApiResponse.success(algorithmPage);
        } catch (Exception e) {
            log.error("获取热门算法失败", e);
            return ApiResponse.error(500, "获取热门算法失败: " + e.getMessage());
        }
    }

    /**
     * 根据类型获取算法
     */
    @GetMapping("/type/{type}")
    @Operation(summary = "根据类型获取算法", description = "根据算法类型获取算法列表")
    public ApiResponse<IPage<Algorithm>> getAlgorithmsByType(
            @Parameter(description = "算法类型", required = true)
            @PathVariable String type,
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "页大小", example = "10")
            @RequestParam(defaultValue = "10") Long pageSize) {
        try {
            Page<Algorithm> page = new Page<>(pageNum, pageSize);
            IPage<Algorithm> algorithmPage = algorithmService.getAlgorithmsByType(type, page);
            return ApiResponse.success(algorithmPage);
        } catch (Exception e) {
            log.error("根据类型获取算法失败，类型: {}", type, e);
            return ApiResponse.error(500, "根据类型获取算法失败: " + e.getMessage());
        }
    }

    /**
     * 搜索算法
     */
    @GetMapping("/search")
    @Operation(summary = "搜索算法", description = "根据关键词搜索算法")
    public ApiResponse<IPage<Algorithm>> searchAlgorithms(
            @Parameter(description = "搜索关键词", required = true)
            @RequestParam String keyword,
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "页大小", example = "10")
            @RequestParam(defaultValue = "10") Long pageSize) {
        try {
            Page<Algorithm> page = new Page<>(pageNum, pageSize);
            IPage<Algorithm> algorithmPage = algorithmService.searchAlgorithms(keyword, page);
            return ApiResponse.success(algorithmPage);
        } catch (Exception e) {
            log.error("搜索算法失败，关键词: {}", keyword, e);
            return ApiResponse.error(500, "搜索算法失败: " + e.getMessage());
        }
    }
}