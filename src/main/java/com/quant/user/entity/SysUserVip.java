package com.quant.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.quant.user.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author panda-y
 * @version 1.0
 * @date 2026/8/29 13:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user_vip")
public class SysUserVip extends BaseEntity {
    private Long userId;
    private Integer vipLevel;
    private Date startTime;
    private Date expireTime;
    private Boolean enabled;
}
