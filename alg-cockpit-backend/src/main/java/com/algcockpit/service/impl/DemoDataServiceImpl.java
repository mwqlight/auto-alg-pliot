package com.algcockpit.service.impl;

import com.algcockpit.entity.DemoData;
import com.algcockpit.exception.BusinessException;
import com.algcockpit.repository.DemoDataRepository;
import com.algcockpit.service.DemoDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 演示数据服务实现类
 * 
 * @author Algorithm Cockpit Team
 */
@Slf4j
@Service
public class DemoDataServiceImpl extends ServiceImpl<DemoDataRepository, DemoData> implements DemoDataService {

    @Autowired
    private DemoDataRepository demoDataRepository;

    @Override
    public List<DemoData> getDemoDataByAlgorithmId(Long algorithmId) {
        if (algorithmId == null || algorithmId <= 0) {
            throw new BusinessException(400, "算法ID不能为空");
        }
        
        try {
            List<DemoData> demoDataList = demoDataRepository.selectByAlgorithmId(algorithmId);
            log.info("根据算法ID查询演示数据成功，算法ID: {}, 数量: {}", algorithmId, demoDataList.size());
            return demoDataList;
        } catch (Exception e) {
            log.error("根据算法ID查询演示数据失败，算法ID: {}", algorithmId, e);
            throw new BusinessException(500, "查询演示数据失败");
        }
    }

    @Override
    public List<DemoData> getDemoDataByAlgorithmIdAndDataType(Long algorithmId, String dataType) {
        if (algorithmId == null || algorithmId <= 0) {
            throw new BusinessException(400, "算法ID不能为空");
        }
        if (dataType == null || dataType.trim().isEmpty()) {
            throw new BusinessException(400, "数据类型不能为空");
        }
        
        try {
            List<DemoData> demoDataList = demoDataRepository.selectByAlgorithmIdAndDataType(algorithmId, dataType);
            log.info("根据算法ID和数据类型查询演示数据成功，算法ID: {}, 数据类型: {}, 数量: {}", 
                    algorithmId, dataType, demoDataList.size());
            return demoDataList;
        } catch (Exception e) {
            log.error("根据算法ID和数据类型查询演示数据失败，算法ID: {}, 数据类型: {}", algorithmId, dataType, e);
            throw new BusinessException(500, "查询演示数据失败");
        }
    }

    @Override
    @Transactional
    public DemoData createDemoData(DemoData demoData) {
        if (demoData == null) {
            throw new BusinessException(400, "演示数据不能为空");
        }
        if (demoData.getAlgorithmId() == null || demoData.getAlgorithmId() <= 0) {
            throw new BusinessException(400, "算法ID不能为空");
        }
        if (demoData.getDataType() == null || demoData.getDataType().trim().isEmpty()) {
            throw new BusinessException(400, "数据类型不能为空");
        }
        if (demoData.getInputData() == null || demoData.getInputData().trim().isEmpty()) {
            throw new BusinessException(400, "输入数据不能为空");
        }
        
        try {
            // 设置默认值
            demoData.setId(null);
            demoData.setDeleted(0);
            demoData.setCreatedTime(LocalDateTime.now());
            demoData.setUpdatedTime(LocalDateTime.now());
            
            int result = demoDataRepository.insert(demoData);
            if (result <= 0) {
                throw new BusinessException(500, "创建演示数据失败");
            }
            
            log.info("创建演示数据成功，算法ID: {}, 数据类型: {}", demoData.getAlgorithmId(), demoData.getDataType());
            return demoData;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("创建演示数据失败，算法ID: {}, 数据类型: {}", demoData.getAlgorithmId(), demoData.getDataType(), e);
            throw new BusinessException(500, "创建演示数据失败");
        }
    }

    @Override
    @Transactional
    public DemoData updateDemoData(DemoData demoData) {
        if (demoData == null || demoData.getId() == null || demoData.getId() <= 0) {
            throw new BusinessException(400, "演示数据ID不能为空");
        }
        if (demoData.getInputData() == null || demoData.getInputData().trim().isEmpty()) {
            throw new BusinessException(400, "输入数据不能为空");
        }
        
        // 检查演示数据是否存在
        DemoData existingDemoData = demoDataRepository.selectById(demoData.getId());
        if (existingDemoData == null || existingDemoData.getDeleted() == 1) {
            throw new BusinessException(404, "演示数据不存在");
        }
        
        try {
            demoData.setUpdatedTime(LocalDateTime.now());
            int result = demoDataRepository.updateById(demoData);
            if (result <= 0) {
                throw new BusinessException(500, "更新演示数据失败");
            }
            
            log.info("更新演示数据成功，演示数据ID: {}, 算法ID: {}", demoData.getId(), demoData.getAlgorithmId());
            return demoData;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("更新演示数据失败，演示数据ID: {}", demoData.getId(), e);
            throw new BusinessException(500, "更新演示数据失败");
        }
    }

    @Override
    @Transactional
    public boolean deleteDemoData(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(400, "演示数据ID不能为空");
        }
        
        // 检查演示数据是否存在
        DemoData demoData = demoDataRepository.selectById(id);
        if (demoData == null || demoData.getDeleted() == 1) {
            throw new BusinessException(404, "演示数据不存在");
        }
        
        try {
            demoData.setDeleted(1);
            demoData.setUpdatedTime(LocalDateTime.now());
            int result = demoDataRepository.updateById(demoData);
            
            if (result > 0) {
                log.info("删除演示数据成功，演示数据ID: {}", id);
                return true;
            } else {
                log.error("删除演示数据失败，演示数据ID: {}", id);
                return false;
            }
        } catch (Exception e) {
            log.error("删除演示数据失败，演示数据ID: {}", id, e);
            throw new BusinessException(500, "删除演示数据失败");
        }
    }

    @Override
    @Transactional
    public boolean deleteDemoDataByAlgorithmId(Long algorithmId) {
        if (algorithmId == null || algorithmId <= 0) {
            throw new BusinessException(400, "算法ID不能为空");
        }
        
        try {
            demoDataRepository.deleteByAlgorithmId(algorithmId);
            log.info("根据算法ID删除所有演示数据成功，算法ID: {}", algorithmId);
            return true;
        } catch (Exception e) {
            log.error("根据算法ID删除所有演示数据失败，算法ID: {}", algorithmId, e);
            throw new BusinessException(500, "删除演示数据失败");
        }
    }
}