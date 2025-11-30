package com.algcockpit.service;

import com.algcockpit.entity.AlgorithmCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 算法分类服务接口
 * 
 * @author Algorithm Cockpit Team
 */
public interface AlgorithmCategoryService extends IService<AlgorithmCategory> {

    /**
     * 获取所有启用的算法分类
     */
    List<AlgorithmCategory> getAllEnabledCategories();

    /**
     * 根据ID获取算法分类详情
     */
    AlgorithmCategory getCategoryDetail(Long id);

    /**
     * 创建算法分类
     */
    AlgorithmCategory createCategory(AlgorithmCategory category);

    /**
     * 更新算法分类
     */
    AlgorithmCategory updateCategory(AlgorithmCategory category);

    /**
     * 删除算法分类（逻辑删除）
     */
    boolean deleteCategory(Long id);
}