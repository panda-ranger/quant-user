package com.quant.user.exception;

import com.quant.user.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 全局异常处理器 —— 所有异常统一转为 {@link ApiResponse} 格式返回前端。
 * <p>
 * 统一返回 HTTP 200，通过 ApiResponse.code 区分成功/失败：
 * - code="000000" 表示成功
 * - 其它 code 表示业务异常
 * <p>
 * 只有 401/403（安全框架直接写响应）和真正的系统故障才用非 200 状态码。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常：BizException 携带机器可读的 code、出错的 field、限流的 retryAfter
     */
    @ExceptionHandler(BizException.class)
    public ResponseEntity<ApiResponse<Void>> handleBizException(BizException e) {
        log.warn("业务异常: code={}, field={}, msg={}", e.getCode(), e.getField(), e.getMessage());

        // 限流场景加 Retry-After header
        if ("TOO_MANY_REQUESTS".equals(e.getCode()) && e.getRetryAfter() != null) {
            return ResponseEntity.ok()
                    .header("Retry-After", String.valueOf(e.getRetryAfter()))
                    .body(ApiResponse.error(e.getCode(), e.getMessage()));
        }

        return ResponseEntity.ok()
                .body(ApiResponse.error(e.getCode(), e.getMessage()));
    }

    /**
     * @Valid/@Validated 校验失败（@RequestBody 参数）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String msg = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        String field = fieldError != null ? fieldError.getField() : null;
        log.warn("参数校验失败: field={}, msg={}", field, msg);

        return ResponseEntity.ok()
                .body(ApiResponse.error("PARAM_INVALID", msg));
    }

    /**
     * @Validated 校验失败（@RequestParam / @PathVariable 参数）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String msg = violations.isEmpty() ? "参数校验失败" : violations.iterator().next().getMessage();
        log.warn("约束校验失败: msg={}", msg);

        return ResponseEntity.ok()
                .body(ApiResponse.error("PARAM_INVALID", msg));
    }

    /**
     * 请求体不可读（JSON 格式错误、空 body 等）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotReadable(HttpMessageNotReadableException e) {
        log.warn("请求体解析失败: {}", e.getMessage());
        return ResponseEntity.ok()
                .body(ApiResponse.error("PARAM_INVALID", "请求体格式错误"));
    }

    /**
     * 参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.warn("参数类型不匹配: name={}, value={}", e.getName(), e.getValue());
        return ResponseEntity.ok()
                .body(ApiResponse.error("PARAM_INVALID", "参数类型错误"));
    }

    /**
     * 缺少必要请求参数
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParam(MissingServletRequestParameterException e) {
        log.warn("缺少请求参数: {}", e.getParameterName());
        return ResponseEntity.ok()
                .body(ApiResponse.error("PARAM_INVALID", "缺少必要参数: " + e.getParameterName()));
    }


    /**
     * 兜底：未预期的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return ResponseEntity.ok()
                .body(ApiResponse.error("SYSTEM_ERROR", "系统异常，请稍后重试"));
    }
}
