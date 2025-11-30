package com.algcockpit.service;

import com.algcockpit.entity.AlgorithmComplexity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 算法复杂度服务接口
 * 
 * @author Algorithm Cockpit Team
 */
public interface AlgorithmComplexityService extends IService<AlgorithmComplexity> {

    /**
     * 根据算法ID获取复杂度信息
     */
    AlgorithmComplexity getComplexityByAlgorithmId(Long algorithmId);

    /**
     * 创建算法复杂度
     */
    AlgorithmComplexity createAlgorithmComplexity(AlgorithmComplexity algorithmComplexity);

    /**
     * 更新算法复杂度
     */
    AlgorithmComplexity updateAlgorithmComplexity(AlgorithmComplexity algorithmComplexity);

    /**
     * 删除算法复杂度（逻辑删除）
     */
    boolean deleteAlgorithmComplexity(Long id);

    /**
     * 根据算法ID删除复杂度信息（逻辑删除）
     */
    boolean deleteComplexityByAlgorithmId(Long algorithmId);
}