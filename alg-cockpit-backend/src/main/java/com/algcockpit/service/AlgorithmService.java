package com.algcockpit.service;

import com.algcockpit.dto.request.AlgorithmExecuteRequest;
import com.algcockpit.dto.response.AlgorithmExecutionResponse;
import com.algcockpit.entity.Algorithm;
import com.algcockpit.entity.AlgorithmExecution;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 算法信息服务接口
 * 
 * @author Algorithm Cockpit Team
 */
public interface AlgorithmService extends IService<Algorithm> {

    /**
     * 根据分类ID获取算法列表
     */
    List<Algorithm> getAlgorithmsByCategoryId(Long categoryId);

    /**
     * 分页查询算法列表
     */
    IPage<Algorithm> getAlgorithmPage(Long pageNum, Long pageSize, Long categoryId, 
                                     Integer difficultyLevel, Integer status, String keyword);

    /**
     * 根据ID获取算法详情
     */
    Algorithm getAlgorithmDetail(Long id);

    /**
     * 创建算法
     */
    Algorithm createAlgorithm(Algorithm algorithm);

    /**
     * 更新算法
     */
    Algorithm updateAlgorithm(Algorithm algorithm);

    /**
     * 删除算法（逻辑删除）
     */
    boolean deleteAlgorithm(Long id);

    /**
     * 增加算法热度
     */
    void increasePopularity(Long id);

    /**
     * 执行算法
     */
    AlgorithmExecutionResponse executeAlgorithm(AlgorithmExecuteRequest request);

    /**
     * 获取执行记录
     */
    Page<AlgorithmExecution> getExecutionRecords(Long userId, Page<AlgorithmExecution> page);

    /**
     * 获取执行详情
     */
    AlgorithmExecution getExecutionDetail(Long executionId);

    /**
     * 停止算法执行
     */
    boolean stopAlgorithmExecution(Long executionId);

    /**
     * 获取热门算法
     */
    Page<Algorithm> getPopularAlgorithms(Page<Algorithm> page);

    /**
     * 根据类型获取算法
     */
    Page<Algorithm> getAlgorithmsByType(String type, Page<Algorithm> page);

    /**
     * 搜索算法
     */
    Page<Algorithm> searchAlgorithms(String keyword, Page<Algorithm> page);

    /**
     * 检查算法是否存在
     */
    Boolean existsById(Long id);
}