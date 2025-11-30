package com.algcockpit.service.impl;

import com.algcockpit.entity.AlgorithmComplexity;
import com.algcockpit.exception.BusinessException;
import com.algcockpit.repository.AlgorithmComplexityRepository;
import com.algcockpit.service.AlgorithmComplexityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 算法复杂度服务实现类
 * 
 * @author Algorithm Cockpit Team
 */
@Slf4j
@Service
public class AlgorithmComplexityServiceImpl extends ServiceImpl<AlgorithmComplexityRepository, AlgorithmComplexity> implements AlgorithmComplexityService {

    @Autowired
    private AlgorithmComplexityRepository algorithmComplexityRepository;

    @Override
    public AlgorithmComplexity getComplexityByAlgorithmId(Long algorithmId) {
        if (algorithmId == null || algorithmId <= 0) {
            throw new BusinessException(400, "算法ID不能为空");
        }
        
        try {
            AlgorithmComplexity complexity = algorithmComplexityRepository.selectByAlgorithmId(algorithmId);
            if (complexity == null) {
                throw new BusinessException(404, "算法复杂度信息不存在");
            }
            log.info("根据算法ID查询复杂度信息成功，算法ID: {}", algorithmId);
            return complexity;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("根据算法ID查询复杂度信息失败，算法ID: {}", algorithmId, e);
            throw new BusinessException(500, "查询复杂度信息失败");
        }
    }

    @Override
    @Transactional
    public AlgorithmComplexity createAlgorithmComplexity(AlgorithmComplexity algorithmComplexity) {
        if (algorithmComplexity == null) {
            throw new BusinessException(400, "复杂度信息不能为空");
        }
        if (algorithmComplexity.getAlgorithmId() == null || algorithmComplexity.getAlgorithmId() <= 0) {
            throw new BusinessException(400, "算法ID不能为空");
        }
        
        // 检查是否已存在复杂度信息
        AlgorithmComplexity existingComplexity = algorithmComplexityRepository.selectByAlgorithmId(algorithmComplexity.getAlgorithmId());
        if (existingComplexity != null) {
            throw new BusinessException(400, "该算法的复杂度信息已存在");
        }
        
        try {
            // 设置默认值
            algorithmComplexity.setId(null);
            algorithmComplexity.setDeleted(0);
            algorithmComplexity.setCreatedTime(LocalDateTime.now());
            algorithmComplexity.setUpdatedTime(LocalDateTime.now());
            
            int result = algorithmComplexityRepository.insert(algorithmComplexity);
            if (result <= 0) {
                throw new BusinessException(500, "创建算法复杂度失败");
            }
            
            log.info("创建算法复杂度成功，算法ID: {}", algorithmComplexity.getAlgorithmId());
            return algorithmComplexity;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("创建算法复杂度失败，算法ID: {}", algorithmComplexity.getAlgorithmId(), e);
            throw new BusinessException(500, "创建算法复杂度失败");
        }
    }

    @Override
    @Transactional
    public AlgorithmComplexity updateAlgorithmComplexity(AlgorithmComplexity algorithmComplexity) {
        if (algorithmComplexity == null || algorithmComplexity.getId() == null || algorithmComplexity.getId() <= 0) {
            throw new BusinessException(400, "复杂度ID不能为空");
        }
        
        // 检查复杂度信息是否存在
        AlgorithmComplexity existingComplexity = algorithmComplexityRepository.selectById(algorithmComplexity.getId());
        if (existingComplexity == null || existingComplexity.getDeleted() == 1) {
            throw new BusinessException(404, "复杂度信息不存在");
        }
        
        try {
            algorithmComplexity.setUpdatedTime(LocalDateTime.now());
            int result = algorithmComplexityRepository.updateById(algorithmComplexity);
            if (result <= 0) {
                throw new BusinessException(500, "更新算法复杂度失败");
            }
            
            log.info("更新算法复杂度成功，复杂度ID: {}, 算法ID: {}", algorithmComplexity.getId(), algorithmComplexity.getAlgorithmId());
            return algorithmComplexity;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("更新算法复杂度失败，复杂度ID: {}", algorithmComplexity.getId(), e);
            throw new BusinessException(500, "更新算法复杂度失败");
        }
    }

    @Override
    @Transactional
    public boolean deleteAlgorithmComplexity(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(400, "复杂度ID不能为空");
        }
        
        // 检查复杂度信息是否存在
        AlgorithmComplexity complexity = algorithmComplexityRepository.selectById(id);
        if (complexity == null || complexity.getDeleted() == 1) {
            throw new BusinessException(404, "复杂度信息不存在");
        }
        
        try {
            complexity.setDeleted(1);
            complexity.setUpdatedTime(LocalDateTime.now());
            int result = algorithmComplexityRepository.updateById(complexity);
            
            if (result > 0) {
                log.info("删除算法复杂度成功，复杂度ID: {}", id);
                return true;
            } else {
                log.error("删除算法复杂度失败，复杂度ID: {}", id);
                return false;
            }
        } catch (Exception e) {
            log.error("删除算法复杂度失败，复杂度ID: {}", id, e);
            throw new BusinessException(500, "删除算法复杂度失败");
        }
    }

    @Override
    @Transactional
    public boolean deleteComplexityByAlgorithmId(Long algorithmId) {
        if (algorithmId == null || algorithmId <= 0) {
            throw new BusinessException(400, "算法ID不能为空");
        }
        
        try {
            algorithmComplexityRepository.deleteByAlgorithmId(algorithmId);
            log.info("根据算法ID删除复杂度信息成功，算法ID: {}", algorithmId);
            return true;
        } catch (Exception e) {
            log.error("根据算法ID删除复杂度信息失败，算法ID: {}", algorithmId, e);
            throw new BusinessException(500, "删除复杂度信息失败");
        }
    }
}