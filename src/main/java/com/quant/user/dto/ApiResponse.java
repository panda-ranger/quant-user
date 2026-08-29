package com.quant.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应格式
 * <p>
 * code = "000000" 表示业务请求成功，其它表示业务异常
 *
 * @param <T> 成功时返回的数据类型
 */
@Data
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = -5663315430445422894L;

    /** 自定义业务异常码，000000 表示业务请求成功，其它表示业务异常 */
    private String code;

    /** 业务异常的提示信息 */
    private String msg;

    /** 请求成功后返回的信息 */
    private T result;

    private ApiResponse() {
    }

    private ApiResponse(String code, String msg, T result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    // ==================== 成功 ====================

    public static <T> ApiResponse<T> success(T result) {
        return new ApiResponse<>("000000", "success", result);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>("000000", "success", null);
    }

    public static <T> ApiResponse<T> success(String msg, T result) {
        return new ApiResponse<>("000000", msg, result);
    }

    // ==================== 失败 ====================

    public static <T> ApiResponse<T> error(String code, String msg) {
        return new ApiResponse<>(code, msg, null);
    }

    /**
     * 未知业务异常，code 默认 "999999"
     */
    public static <T> ApiResponse<T> error(String msg) {
        return new ApiResponse<>("999999", msg, null);
    }
}
