package com.algcockpit.controller;

import com.algcockpit.dto.response.ApiResponse;
import com.algcockpit.entity.AlgorithmCode;
import com.algcockpit.service.AlgorithmCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 算法代码控制器
 * 
 * @author Algorithm Cockpit Team
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/codes")
@Tag(name = "算法代码管理", description = "算法代码相关接口")
public class AlgorithmCodeController {

    @Autowired
    private AlgorithmCodeService algorithmCodeService;

    /**
     * 根据算法ID获取代码列表
     */
    @GetMapping("/algorithm/{algorithmId}")
    @Operation(summary = "根据算法获取代码列表", description = "根据算法ID获取该算法的代码列表")
    public ApiResponse<List<AlgorithmCode>> getCodesByAlgorithm(
            @Parameter(description = "算法ID", required = true)
            @PathVariable Long algorithmId) {
        try {
            List<AlgorithmCode> codes = algorithmCodeService.getCodesByAlgorithmId(algorithmId);
            return ApiResponse.success(codes);
        } catch (Exception e) {
            log.error("根据算法获取代码列表失败，算法ID: {}", algorithmId, e);
            return ApiResponse.error(500, "获取代码列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据算法ID和编程语言获取代码
     */
    @GetMapping("/algorithm/{algorithmId}/language/{language}")
    @Operation(summary = "获取特定语言的代码", description = "根据算法ID和编程语言获取特定代码")
    public ApiResponse<AlgorithmCode> getCodeByAlgorithmAndLanguage(
            @Parameter(description = "算法ID", required = true)
            @PathVariable Long algorithmId,
            @Parameter(description = "编程语言", required = true)
            @PathVariable String language) {
        try {
            AlgorithmCode code = algorithmCodeService.getCodeByAlgorithmIdAndLanguage(algorithmId, language);
            return ApiResponse.success(code);
        } catch (Exception e) {
            log.error("获取特定语言代码失败，算法ID: {}, 语言: {}", algorithmId, language, e);
            return ApiResponse.error(500, "获取代码失败: " + e.getMessage());
        }
    }

    /**
     * 创建算法代码
     */
    @PostMapping("/")
    @Operation(summary = "创建算法代码", description = "创建新的算法代码")
    public ApiResponse<AlgorithmCode> createAlgorithmCode(
            @Parameter(description = "代码信息", required = true)
            @Valid @RequestBody AlgorithmCode algorithmCode) {
        try {
            AlgorithmCode createdCode = algorithmCodeService.createAlgorithmCode(algorithmCode);
            return ApiResponse.success(createdCode);
        } catch (Exception e) {
            log.error("创建算法代码失败", e);
            return ApiResponse.error(500, "创建算法代码失败: " + e.getMessage());
        }
    }

    /**
     * 更新算法代码
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新算法代码", description = "更新指定ID的算法代码")
    public ApiResponse<AlgorithmCode> updateAlgorithmCode(
            @Parameter(description = "代码ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "代码信息", required = true)
            @Valid @RequestBody AlgorithmCode algorithmCode) {
        try {
            algorithmCode.setId(id);
            AlgorithmCode updatedCode = algorithmCodeService.updateAlgorithmCode(algorithmCode);
            return ApiResponse.success(updatedCode);
        } catch (Exception e) {
            log.error("更新算法代码失败，代码ID: {}", id, e);
            return ApiResponse.error(500, "更新算法代码失败: " + e.getMessage());
        }
    }

    /**
     * 删除算法代码
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除算法代码", description = "删除指定ID的算法代码（逻辑删除）")
    public ApiResponse<Boolean> deleteAlgorithmCode(
            @Parameter(description = "代码ID", required = true)
            @PathVariable Long id) {
        try {
            boolean success = algorithmCodeService.deleteAlgorithmCode(id);
            return ApiResponse.success(success);
        } catch (Exception e) {
            log.error("删除算法代码失败，代码ID: {}", id, e);
            return ApiResponse.error(500, "删除算法代码失败: " + e.getMessage());
        }
    }
}