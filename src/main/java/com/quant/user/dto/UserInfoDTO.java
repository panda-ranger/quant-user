package com.quant.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author panda-y
 * @version 1.0
 * @date 2026/8/29 10:46
 */
@Data
@Builder
public class UserInfoDTO {
    private Long userId;
    private String username;
    private String realName;
    private String email;
    private String phone;
    private String avatar;
    private String department;
    private Integer vipLevel;
    private Date vipExpireTime;
}
