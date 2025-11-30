package com.algcockpit.exception;

import com.algcockpit.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 
 * @author Algorithm Cockpit Team
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<String> handleAuthenticationException(AuthenticationException ex) {
        log.warn("认证异常: {}", ex.getMessage());
        
        if (ex instanceof BadCredentialsException) {
            return ApiResponse.error(401, "用户名或密码错误");
        }
        
        return ApiResponse.error(401, "认证失败: " + ex.getMessage());
    }

    /**
     * 处理权限不足异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<String> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("权限不足: {}", ex.getMessage());
        return ApiResponse.error(403, "权限不足: " + ex.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, String>> handleValidationException(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException manvEx = (MethodArgumentNotValidException) ex;
            manvEx.getBindingResult().getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage())
            );
        } else if (ex instanceof BindException) {
            BindException bindEx = (BindException) ex;
            bindEx.getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage())
            );
        }
        
        log.warn("参数校验失败: {}", errors);
        return ApiResponse.error(400, "参数校验失败", errors);
    }

    /**
     * 处理约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<String> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn("约束违反异常: {}", ex.getMessage());
        return ApiResponse.error(400, ex.getMessage());
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<String> handleBusinessException(BusinessException ex) {
        log.warn("业务异常: {}", ex.getMessage());
        return ApiResponse.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<String> handleException(Exception ex) {
        log.error("系统异常: ", ex);
        return ApiResponse.error(500, "系统异常，请稍后重试");
    }
}