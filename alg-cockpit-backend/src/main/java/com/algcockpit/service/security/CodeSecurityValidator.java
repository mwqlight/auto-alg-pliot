package com.algcockpit.service.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 代码安全验证器
 * 负责验证各种编程语言代码的安全性
 * 
 * @author Algorithm Cockpit Team
 */
@Slf4j
@Component
public class CodeSecurityValidator {

    // 通用危险操作的正则表达式模式
    private static final Pattern DANGEROUS_PATTERNS = Pattern.compile(
        "(?i)(system\\.|runtime\\.|processbuilder|exec\\s*\\(|eval\\s*\\(|scriptengine|reflection|unsafe|jni|native)"
    );

    /**
     * 验证代码安全性
     * 
     * @param code 代码内容
     * @param language 编程语言
     * @return 是否安全
     */
    public Boolean validateCodeSecurity(String code, String language) {
        if (code == null || code.trim().isEmpty()) {
            log.warn("代码为空或null");
            return false;
        }

        // 检查代码长度限制
        if (code.length() > 10000) {
            log.warn("代码长度超过限制: {} 字符", code.length());
            return false;
        }

        // 语言特定的安全检查
        switch (language.toLowerCase()) {
            case "java":
                return validateJavaCodeSecurity(code);
            case "python":
                return validatePythonCodeSecurity(code);
            case "javascript":
                return validateJavaScriptCodeSecurity(code);
            case "cpp":
            case "c":
                return validateCppCodeSecurity(code);
            default:
                return validateGenericCodeSecurity(code);
        }
    }

    /**
     * 验证Java代码安全性
     */
    private Boolean validateJavaCodeSecurity(String code) {
        // 检查危险的系统调用
        if (DANGEROUS_PATTERNS.matcher(code).find()) {
            log.warn("Java代码包含危险操作");
            return false;
        }

        // 检查文件操作
        if (Pattern.compile("(?i)(fileinputstream|fileoutputstream|filewriter|filereader)").matcher(code).find()) {
            log.warn("Java代码包含文件操作");
            return false;
        }

        // 检查网络操作
        if (Pattern.compile("(?i)(socket|urlconnection|httpclient)").matcher(code).find()) {
            log.warn("Java代码包含网络操作");
            return false;
        }

        return true;
    }

    /**
     * 验证Python代码安全性
     */
    private Boolean validatePythonCodeSecurity(String code) {
        // 检查危险模块导入
        if (Pattern.compile("(?i)(import\\s+(os|sys|subprocess|shutil|importlib|ctypes|socket|urllib|requests|pickle|marshal|eval|exec|compile|__import__))").matcher(code).find()) {
            log.warn("Python代码包含危险模块导入");
            return false;
        }

        // 检查危险函数调用
        if (Pattern.compile("(?i)(\\b(exec|eval|compile|input|open|file|__import__|getattr|setattr|delattr|globals|locals|vars|dir|type|isinstance|issubclass|super|__subclasses__|__bases__|__mro__|__class__|__dict__)\\s*\\(").matcher(code).find()) {
            log.warn("Python代码包含危险函数调用");
            return false;
        }

        return true;
    }

    /**
     * 验证JavaScript代码安全性
     */
    private Boolean validateJavaScriptCodeSecurity(String code) {
        // 检查危险函数调用
        if (Pattern.compile("(?i)(\\b(eval|Function|setTimeout|setInterval|exec|import|require)\\s*\\(").matcher(code).find()) {
            log.warn("JavaScript代码包含危险函数调用");
            return false;
        }

        // 检查全局对象访问
        if (Pattern.compile("(?i)(window\\.|document\\.|location\\.|history\\.|navigator\\.)").matcher(code).find()) {
            log.warn("JavaScript代码包含全局对象访问");
            return false;
        }

        return true;
    }

    /**
     * 验证C/C++代码安全性
     */
    private Boolean validateCppCodeSecurity(String code) {
        // 检查系统调用
        if (Pattern.compile("(?i)(system|exec|fork|popen|fopen|fwrite|socket|connect|bind)").matcher(code).find()) {
            log.warn("C/C++代码包含危险系统调用");
            return false;
        }

        // 检查指针操作
        if (Pattern.compile("(?i)(malloc|calloc|realloc|free|new|delete)").matcher(code).find()) {
            log.warn("C/C++代码包含动态内存操作");
            return false;
        }

        return true;
    }

    /**
     * 通用代码安全检查
     */
    private Boolean validateGenericCodeSecurity(String code) {
        // 检查常见的危险模式
        if (DANGEROUS_PATTERNS.matcher(code).find()) {
            log.warn("代码包含通用危险操作");
            return false;
        }

        return true;
    }
}