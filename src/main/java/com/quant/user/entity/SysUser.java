package com.quant.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.quant.user.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseEntity {

    private String username;
    private String password;
    private String email;
    private String phone;
    private String realName;
    private String avatar;
    private String department;
    private Boolean enabled = true;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;


}