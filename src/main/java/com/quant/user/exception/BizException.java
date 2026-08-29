package com.quant.user.exception;

import lombok.Getter;

/**
 * 业务异常：除了给用户看的 message，额外带上机器可读的 code 和出错字段名。
 * <p>
 * field 是给前端用的 —— 拿到 field 就能把错误文案显示到对应的输入框下方，
 * 而不是笼统地弹一个全局 toast。
 */
@Getter
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** 机器可读的错误码，如 USERNAME_TAKEN */
    private final String code;

    /** 出错的表单字段名，如 username；与具体字段无关时为 null */
    private final String field;

    /** 限流场景下的重试等待秒数，其余场景为 null */
    private final Long retryAfter;

    public BizException(String code, String field, String message) {
        this(code, field, message, null);
    }

    public BizException(String code, String field, String message, Long retryAfter) {
        super(message);
        this.code = code;
        this.field = field;
        this.retryAfter = retryAfter;
    }
}
