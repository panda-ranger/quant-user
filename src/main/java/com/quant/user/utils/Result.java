package com.quant.user.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author panda-y
 * @version 1.0
 * @date 2026/8/28 22:36
 */
@Data
@AllArgsConstructor
public class Result<T> {

    private Integer code;

    private String message;

    private T data;


    public static Result<Void> fail(
            Integer code,
            String message){

        return new Result<>(
                code,
                message,
                null
        );
    }
}
