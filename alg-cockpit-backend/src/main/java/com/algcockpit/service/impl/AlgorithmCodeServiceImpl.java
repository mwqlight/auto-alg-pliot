package com.algcockpit.service.impl;

import com.algcockpit.entity.AlgorithmCode;
import com.algcockpit.exception.BusinessException;
import com.algcockpit.repository.AlgorithmCodeRepository;
import com.algcockpit.service.AlgorithmCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 算法代码服务实现类
 * 
 * @author Algorithm Cockpit Team
 */
@Slf4j
@Service
public class AlgorithmCodeServiceImpl extends ServiceImpl<AlgorithmCodeRepository, AlgorithmCode> implements AlgorithmCodeService {

    @Autowired
    private AlgorithmCodeRepository algorithmCodeRepository;

    @Override
    public List<AlgorithmCode> getCodesByAlgorithmId(Long algorithmId) {
        if (algorithmId == null || algorithmId <= 0) {
            throw new BusinessException(400, "算法ID不能为空");
        }
        
        try {
            List<AlgorithmCode> codes = algorithmCodeRepository.selectByAlgorithmId(algorithmId);
            log.info("根据算法ID查询代码列表成功，算法ID: {}, 数量: {}", algorithmId, codes.size());
            return codes;
        } catch (Exception e) {
            log.error("根据算法ID查询代码列表失败，算法ID: {}", algorithmId, e);
            throw new BusinessException(500, "查询代码列表失败");
        }
    }

    @Override
    public AlgorithmCode getCodeByAlgorithmIdAndLanguage(Long algorithmId, String language) {
        if (algorithmId == null || algorithmId <= 0) {
            throw new BusinessException(400, "算法ID不能为空");
        }
        if (language == null || language.trim().isEmpty()) {
            throw new BusinessException(400, "编程语言不能为空");
        }
        
        try {
            AlgorithmCode code = algorithmCodeRepository.selectByAlgorithmIdAndLanguage(algorithmId, language);
            if (code == null) {
                throw new BusinessException(404, "该编程语言的代码不存在");
            }
            log.info("根据算法ID和编程语言查询代码成功，算法ID: {}, 语言: {}", algorithmId, language);
            return code;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("根据算法ID和编程语言查询代码失败，算法ID: {}, 语言: {}", algorithmId, language, e);
            throw new BusinessException(500, "查询代码失败");
        }
    }

    @Override
    @Transactional
    public AlgorithmCode createAlgorithmCode(AlgorithmCode algorithmCode) {
        if (algorithmCode == null) {
            throw new BusinessException(400, "代码信息不能为空");
        }
        if (algorithmCode.getAlgorithmId() == null || algorithmCode.getAlgorithmId() <= 0) {
            throw new BusinessException(400, "算法ID不能为空");
        }
        if (algorithmCode.getLanguage() == null || algorithmCode.getLanguage().trim().isEmpty()) {
            throw new BusinessException(400, "编程语言不能为空");
        }
        if (algorithmCode.getCodeContent() == null || algorithmCode.getCodeContent().trim().isEmpty()) {
            throw new BusinessException(400, "代码内容不能为空");
        }
        
        // 检查是否已存在相同语言的代码
        AlgorithmCode existingCode = algorithmCodeRepository.selectByAlgorithmIdAndLanguage(
                algorithmCode.getAlgorithmId(), algorithmCode.getLanguage());
        if (existingCode != null) {
            throw new BusinessException(400, "该编程语言的代码已存在");
        }
        
        try {
            // 设置默认值
            algorithmCode.setId(null);
            algorithmCode.setDeleted(0);
            algorithmCode.setCreatedTime(LocalDateTime.now());
            algorithmCode.setUpdatedTime(LocalDateTime.now());
            
            int result = algorithmCodeRepository.insert(algorithmCode);
            if (result <= 0) {
                throw new BusinessException(500, "创建算法代码失败");
            }
            
            log.info("创建算法代码成功，算法ID: {}, 语言: {}", algorithmCode.getAlgorithmId(), algorithmCode.getLanguage());
            return algorithmCode;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("创建算法代码失败，算法ID: {}, 语言: {}", algorithmCode.getAlgorithmId(), algorithmCode.getLanguage(), e);
            throw new BusinessException(500, "创建算法代码失败");
        }
    }

    @Override
    @Transactional
    public AlgorithmCode updateAlgorithmCode(AlgorithmCode algorithmCode) {
        if (algorithmCode == null || algorithmCode.getId() == null || algorithmCode.getId() <= 0) {
            throw new BusinessException(400, "代码ID不能为空");
        }
        if (algorithmCode.getCodeContent() == null || algorithmCode.getCodeContent().trim().isEmpty()) {
            throw new BusinessException(400, "代码内容不能为空");
        }
        
        // 检查代码是否存在
        AlgorithmCode existingCode = algorithmCodeRepository.selectById(algorithmCode.getId());
        if (existingCode == null || existingCode.getDeleted() == 1) {
            throw new BusinessException(404, "代码不存在");
        }
        
        try {
            algorithmCode.setUpdatedTime(LocalDateTime.now());
            int result = algorithmCodeRepository.updateById(algorithmCode);
            if (result <= 0) {
                throw new BusinessException(500, "更新算法代码失败");
            }
            
            log.info("更新算法代码成功，代码ID: {}, 算法ID: {}", algorithmCode.getId(), algorithmCode.getAlgorithmId());
            return algorithmCode;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("更新算法代码失败，代码ID: {}", algorithmCode.getId(), e);
            throw new BusinessException(500, "更新算法代码失败");
        }
    }

    @Override
    @Transactional
    public boolean deleteAlgorithmCode(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(400, "代码ID不能为空");
        }
        
        // 检查代码是否存在
        AlgorithmCode code = algorithmCodeRepository.selectById(id);
        if (code == null || code.getDeleted() == 1) {
            throw new BusinessException(404, "代码不存在");
        }
        
        try {
            code.setDeleted(1);
            code.setUpdatedTime(LocalDateTime.now());
            int result = algorithmCodeRepository.updateById(code);
            
            if (result > 0) {
                log.info("删除算法代码成功，代码ID: {}", id);
                return true;
            } else {
                log.error("删除算法代码失败，代码ID: {}", id);
                return false;
            }
        } catch (Exception e) {
            log.error("删除算法代码失败，代码ID: {}", id, e);
            throw new BusinessException(500, "删除算法代码失败");
        }
    }

    @Override
    @Transactional
    public boolean deleteCodesByAlgorithmId(Long algorithmId) {
        if (algorithmId == null || algorithmId <= 0) {
            throw new BusinessException(400, "算法ID不能为空");
        }
        
        try {
            algorithmCodeRepository.deleteByAlgorithmId(algorithmId);
            log.info("根据算法ID删除所有代码成功，算法ID: {}", algorithmId);
            return true;
        } catch (Exception e) {
            log.error("根据算法ID删除所有代码失败，算法ID: {}", algorithmId, e);
            throw new BusinessException(500, "删除代码失败");
        }
    }
}