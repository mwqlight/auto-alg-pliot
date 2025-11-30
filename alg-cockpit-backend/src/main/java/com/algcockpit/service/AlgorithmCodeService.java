package com.algcockpit.service;

import com.algcockpit.entity.AlgorithmCode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 算法代码服务接口
 * 
 * @author Algorithm Cockpit Team
 */
public interface AlgorithmCodeService extends IService<AlgorithmCode> {

    /**
     * 根据算法ID获取代码列表
     */
    List<AlgorithmCode> getCodesByAlgorithmId(Long algorithmId);

    /**
     * 根据算法ID和编程语言获取代码
     */
    AlgorithmCode getCodeByAlgorithmIdAndLanguage(Long algorithmId, String language);

    /**
     * 创建算法代码
     */
    AlgorithmCode createAlgorithmCode(AlgorithmCode algorithmCode);

    /**
     * 更新算法代码
     */
    AlgorithmCode updateAlgorithmCode(AlgorithmCode algorithmCode);

    /**
     * 删除算法代码（逻辑删除）
     */
    boolean deleteAlgorithmCode(Long id);

    /**
     * 根据算法ID删除所有代码（逻辑删除）
     */
    boolean deleteCodesByAlgorithmId(Long algorithmId);
}