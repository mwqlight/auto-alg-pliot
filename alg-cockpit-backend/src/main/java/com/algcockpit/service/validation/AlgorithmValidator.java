package com.algcockpit.service.validation;

import com.algcockpit.service.AlgorithmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 算法验证器
 * 负责验证算法相关参数的有效性
 * 
 * @author Algorithm Cockpit Team
 */
@Slf4j
@Component
public class AlgorithmValidator {

    @Autowired
    private AlgorithmService algorithmService;

    /**
     * 验证算法存在性
     * 
     * @param algorithmId 算法ID
     * @return 是否存在
     */
    public Boolean validateAlgorithmExists(Long algorithmId) {
        if (algorithmId == null) {
            log.warn("算法ID不能为空");
            return false;
        }

        Boolean exists = algorithmService.existsById(algorithmId);
        if (!exists) {
            log.warn("算法不存在，ID: {}", algorithmId);
        }

        return exists;
    }

    /**
     * 验证语言支持性
     * 
     * @param language 编程语言
     * @return 是否支持
     */
    public Boolean validateLanguageSupport(String language) {
        if (language == null || language.trim().isEmpty()) {
            log.warn("编程语言不能为空");
            return false;
        }

        // 支持的编程语言列表
        String[] supportedLanguages = {"java", "python", "javascript", "cpp", "c"};
        
        for (String supported : supportedLanguages) {
            if (supported.equalsIgnoreCase(language)) {
                return true;
            }
        }

        log.warn("不支持的编程语言: {}", language);
        return false;
    }

    /**
     * 验证执行请求参数
     * 
     * @param algorithmId 算法ID
     * @param language 编程语言
     * @param code 代码内容
     * @return 是否有效
     */
    public Boolean validateExecutionParameters(Long algorithmId, String language, String code) {
        if (!validateAlgorithmExists(algorithmId)) {
            return false;
        }

        if (!validateLanguageSupport(language)) {
            return false;
        }

        if (code == null || code.trim().isEmpty()) {
            log.warn("代码内容不能为空");
            return false;
        }

        return true;
    }
}