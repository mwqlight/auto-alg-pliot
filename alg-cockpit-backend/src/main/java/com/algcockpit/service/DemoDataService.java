package com.algcockpit.service;

import com.algcockpit.entity.DemoData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 演示数据服务接口
 * 
 * @author Algorithm Cockpit Team
 */
public interface DemoDataService extends IService<DemoData> {

    /**
     * 根据算法ID获取演示数据列表
     */
    List<DemoData> getDemoDataByAlgorithmId(Long algorithmId);

    /**
     * 根据算法ID和数据类型获取演示数据
     */
    List<DemoData> getDemoDataByAlgorithmIdAndDataType(Long algorithmId, String dataType);

    /**
     * 创建演示数据
     */
    DemoData createDemoData(DemoData demoData);

    /**
     * 更新演示数据
     */
    DemoData updateDemoData(DemoData demoData);

    /**
     * 删除演示数据（逻辑删除）
     */
    boolean deleteDemoData(Long id);

    /**
     * 根据算法ID删除所有演示数据（逻辑删除）
     */
    boolean deleteDemoDataByAlgorithmId(Long algorithmId);
}