package com.algcockpit.controller;

import com.algcockpit.dto.response.ApiResponse;
import com.algcockpit.entity.DemoData;
import com.algcockpit.service.DemoDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 演示数据控制器
 * 
 * @author Algorithm Cockpit Team
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/demo-data")
@Tag(name = "演示数据管理", description = "演示数据相关接口")
public class DemoDataController {

    @Autowired
    private DemoDataService demoDataService;

    /**
     * 根据算法ID获取演示数据列表
     */
    @GetMapping("/algorithm/{algorithmId}")
    @Operation(summary = "获取算法演示数据", description = "根据算法ID获取演示数据列表")
    public ApiResponse<List<DemoData>> getDemoDataByAlgorithm(
            @Parameter(description = "算法ID", required = true)
            @PathVariable Long algorithmId) {
        try {
            List<DemoData> demoDataList = demoDataService.getDemoDataByAlgorithmId(algorithmId);
            return ApiResponse.success(demoDataList);
        } catch (Exception e) {
            log.error("获取算法演示数据失败，算法ID: {}", algorithmId, e);
            return ApiResponse.error(500, "获取演示数据失败: " + e.getMessage());
        }
    }

    /**
     * 根据算法ID和数据类型获取演示数据
     */
    @GetMapping("/algorithm/{algorithmId}/type/{dataType}")
    @Operation(summary = "获取特定类型的演示数据", description = "根据算法ID和数据类型获取演示数据")
    public ApiResponse<List<DemoData>> getDemoDataByAlgorithmAndType(
            @Parameter(description = "算法ID", required = true)
            @PathVariable Long algorithmId,
            @Parameter(description = "数据类型", required = true)
            @PathVariable String dataType) {
        try {
            List<DemoData> demoDataList = demoDataService.getDemoDataByAlgorithmIdAndDataType(algorithmId, dataType);
            return ApiResponse.success(demoDataList);
        } catch (Exception e) {
            log.error("获取特定类型演示数据失败，算法ID: {}, 数据类型: {}", algorithmId, dataType, e);
            return ApiResponse.error(500, "获取演示数据失败: " + e.getMessage());
        }
    }

    /**
     * 创建演示数据
     */
    @PostMapping("/")
    @Operation(summary = "创建演示数据", description = "创建新的演示数据")
    public ApiResponse<DemoData> createDemoData(
            @Parameter(description = "演示数据信息", required = true)
            @Valid @RequestBody DemoData demoData) {
        try {
            DemoData createdDemoData = demoDataService.createDemoData(demoData);
            return ApiResponse.success(createdDemoData);
        } catch (Exception e) {
            log.error("创建演示数据失败", e);
            return ApiResponse.error(500, "创建演示数据失败: " + e.getMessage());
        }
    }

    /**
     * 更新演示数据
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新演示数据", description = "更新指定ID的演示数据")
    public ApiResponse<DemoData> updateDemoData(
            @Parameter(description = "演示数据ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "演示数据信息", required = true)
            @Valid @RequestBody DemoData demoData) {
        try {
            demoData.setId(id);
            DemoData updatedDemoData = demoDataService.updateDemoData(demoData);
            return ApiResponse.success(updatedDemoData);
        } catch (Exception e) {
            log.error("更新演示数据失败，演示数据ID: {}", id, e);
            return ApiResponse.error(500, "更新演示数据失败: " + e.getMessage());
        }
    }

    /**
     * 删除演示数据
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除演示数据", description = "删除指定ID的演示数据（逻辑删除）")
    public ApiResponse<Boolean> deleteDemoData(
            @Parameter(description = "演示数据ID", required = true)
            @PathVariable Long id) {
        try {
            boolean success = demoDataService.deleteDemoData(id);
            return ApiResponse.success(success);
        } catch (Exception e) {
            log.error("删除演示数据失败，演示数据ID: {}", id, e);
            return ApiResponse.error(500, "删除演示数据失败: " + e.getMessage());
        }
    }
}