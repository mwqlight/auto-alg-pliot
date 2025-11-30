package com.algcockpit.controller;

import com.algcockpit.dto.response.ApiResponse;
import com.algcockpit.entity.AlgorithmCategory;
import com.algcockpit.service.AlgorithmCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 算法分类控制器
 * 
 * @author Algorithm Cockpit Team
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "算法分类管理", description = "算法分类相关接口")
public class AlgorithmCategoryController {

    @Autowired
    private AlgorithmCategoryService algorithmCategoryService;

    /**
     * 获取所有启用的算法分类
     */
    @GetMapping("/")
    @Operation(summary = "获取所有算法分类", description = "获取所有启用的算法分类列表")
    public ApiResponse<List<AlgorithmCategory>> getAllCategories() {
        try {
            List<AlgorithmCategory> categories = algorithmCategoryService.getAllEnabledCategories();
            return ApiResponse.success(categories);
        } catch (Exception e) {
            log.error("获取算法分类列表失败", e);
            return ApiResponse.error(500, "获取算法分类列表失败");
        }
    }

    /**
     * 根据ID获取算法分类详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取算法分类详情", description = "根据ID获取算法分类详细信息")
    public ApiResponse<AlgorithmCategory> getCategoryDetail(
            @Parameter(description = "分类ID", required = true)
            @PathVariable Long id) {
        try {
            AlgorithmCategory category = algorithmCategoryService.getCategoryDetail(id);
            return ApiResponse.success(category);
        } catch (Exception e) {
            log.error("获取算法分类详情失败: {}", id, e);
            return ApiResponse.error(500, "获取算法分类详情失败");
        }
    }

    /**
     * 创建算法分类
     */
    @PostMapping("/")
    @Operation(summary = "创建算法分类", description = "创建新的算法分类")
    public ApiResponse<AlgorithmCategory> createCategory(
            @Parameter(description = "分类信息", required = true)
            @Valid @RequestBody AlgorithmCategory category) {
        try {
            AlgorithmCategory createdCategory = algorithmCategoryService.createCategory(category);
            return ApiResponse.success(createdCategory);
        } catch (Exception e) {
            log.error("创建算法分类失败", e);
            return ApiResponse.error(500, "创建算法分类失败: " + e.getMessage());
        }
    }

    /**
     * 更新算法分类
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新算法分类", description = "更新指定ID的算法分类")
    public ApiResponse<AlgorithmCategory> updateCategory(
            @Parameter(description = "分类ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "分类信息", required = true)
            @Valid @RequestBody AlgorithmCategory category) {
        try {
            category.setId(id);
            AlgorithmCategory updatedCategory = algorithmCategoryService.updateCategory(category);
            return ApiResponse.success(updatedCategory);
        } catch (Exception e) {
            log.error("更新算法分类失败: {}", id, e);
            return ApiResponse.error(500, "更新算法分类失败: " + e.getMessage());
        }
    }

    /**
     * 删除算法分类
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除算法分类", description = "删除指定ID的算法分类（逻辑删除）")
    public ApiResponse<Boolean> deleteCategory(
            @Parameter(description = "分类ID", required = true)
            @PathVariable Long id) {
        try {
            boolean success = algorithmCategoryService.deleteCategory(id);
            return ApiResponse.success(success);
        } catch (Exception e) {
            log.error("删除算法分类失败: {}", id, e);
            return ApiResponse.error(500, "删除算法分类失败: " + e.getMessage());
        }
    }
}