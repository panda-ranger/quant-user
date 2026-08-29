package com.quant.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author panda-y
 * @version 1.0
 * @date 2026/8/29 13:04
 */
@Data
public class UserUpdateDTO {

    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    private String avatar;

}
