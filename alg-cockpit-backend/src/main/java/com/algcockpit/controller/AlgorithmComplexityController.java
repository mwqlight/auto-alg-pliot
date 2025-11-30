package com.algcockpit.controller;

import com.algcockpit.dto.response.ApiResponse;
import com.algcockpit.entity.AlgorithmComplexity;
import com.algcockpit.service.AlgorithmComplexityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 算法复杂度控制器
 * 
 * @author Algorithm Cockpit Team
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/complexities")
@Tag(name = "算法复杂度管理", description = "算法复杂度相关接口")
public class AlgorithmComplexityController {

    @Autowired
    private AlgorithmComplexityService algorithmComplexityService;

    /**
     * 根据算法ID获取复杂度信息
     */
    @GetMapping("/algorithm/{algorithmId}")
    @Operation(summary = "获取算法复杂度", description = "根据算法ID获取复杂度信息")
    public ApiResponse<AlgorithmComplexity> getComplexityByAlgorithm(
            @Parameter(description = "算法ID", required = true)
            @PathVariable Long algorithmId) {
        try {
            AlgorithmComplexity complexity = algorithmComplexityService.getComplexityByAlgorithmId(algorithmId);
            return ApiResponse.success(complexity);
        } catch (Exception e) {
            log.error("获取算法复杂度失败，算法ID: {}", algorithmId, e);
            return ApiResponse.error(500, "获取复杂度信息失败: " + e.getMessage());
        }
    }

    /**
     * 创建算法复杂度
     */
    @PostMapping("/")
    @Operation(summary = "创建算法复杂度", description = "创建新的算法复杂度信息")
    public ApiResponse<AlgorithmComplexity> createAlgorithmComplexity(
            @Parameter(description = "复杂度信息", required = true)
            @Valid @RequestBody AlgorithmComplexity algorithmComplexity) {
        try {
            AlgorithmComplexity createdComplexity = algorithmComplexityService.createAlgorithmComplexity(algorithmComplexity);
            return ApiResponse.success(createdComplexity);
        } catch (Exception e) {
            log.error("创建算法复杂度失败", e);
            return ApiResponse.error(500, "创建算法复杂度失败: " + e.getMessage());
        }
    }

    /**
     * 更新算法复杂度
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新算法复杂度", description = "更新指定ID的算法复杂度信息")
    public ApiResponse<AlgorithmComplexity> updateAlgorithmComplexity(
            @Parameter(description = "复杂度ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "复杂度信息", required = true)
            @Valid @RequestBody AlgorithmComplexity algorithmComplexity) {
        try {
            algorithmComplexity.setId(id);
            AlgorithmComplexity updatedComplexity = algorithmComplexityService.updateAlgorithmComplexity(algorithmComplexity);
            return ApiResponse.success(updatedComplexity);
        } catch (Exception e) {
            log.error("更新算法复杂度失败，复杂度ID: {}", id, e);
            return ApiResponse.error(500, "更新算法复杂度失败: " + e.getMessage());
        }
    }

    /**
     * 删除算法复杂度
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除算法复杂度", description = "删除指定ID的算法复杂度信息（逻辑删除）")
    public ApiResponse<Boolean> deleteAlgorithmComplexity(
            @Parameter(description = "复杂度ID", required = true)
            @PathVariable Long id) {
        try {
            boolean success = algorithmComplexityService.deleteAlgorithmComplexity(id);
            return ApiResponse.success(success);
        } catch (Exception e) {
            log.error("删除算法复杂度失败，复杂度ID: {}", id, e);
            return ApiResponse.error(500, "删除算法复杂度失败: " + e.getMessage());
        }
    }
}