package com.algcockpit.exception;

import lombok.Getter;

/**
 * 业务异常类
 * 
 * @author Algorithm Cockpit Team
 */
@Getter
public class BusinessException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 错误码
     */
    private final Integer code;
    
    public BusinessException(String message) {
        this(10000, message);
    }
    
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    
    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}