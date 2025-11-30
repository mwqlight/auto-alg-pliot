package com.algcockpit.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 统一API响应格式
 * 
 * @author Algorithm Cockpit Team
 */
@Data
@Accessors(chain = true)
public class ApiResponse<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 状态码
     */
    private Integer code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 时间戳
     */
    private Long timestamp;
    
    public ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public static <T> ApiResponse<T> success() {
        return success(null);
    }
    
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("操作成功");
        response.setData(data);
        return response;
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return error(500, message);
    }
    
    public static <T> ApiResponse<T> error(Integer code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
    
    public static <T> ApiResponse<T> error(Integer code, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}