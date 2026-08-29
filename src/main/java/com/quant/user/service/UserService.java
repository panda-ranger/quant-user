package com.quant.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.quant.user.dto.UserInfoDTO;
import com.quant.user.dto.UserUpdateDTO;
import com.quant.user.entity.SysUser;
import com.quant.user.entity.SysUserVip;
import com.quant.user.exception.BizException;
import com.quant.user.mapper.SysUserMapper;
import com.quant.user.mapper.SysUserVipMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author panda-y
 * @version 1.0
 * @date 2026/8/29 12:45
 */
@Service
public class UserService {

    @Resource
    private SysUserMapper userMapper;
    @Resource
    private SysUserVipMapper userVipMapper;

    public UserInfoDTO getCurrentUserInfo(Long userId) {

        SysUser user=userMapper.selectById(userId);

        if(user==null){
            throw new BizException(
                    "USER_NOT_FOUND",
                    "userId",
                    "用户不存在"
            );
        }

        SysUserVip vip = userVipMapper.selectOne(
                new LambdaQueryWrapper<SysUserVip>()
                        .eq(SysUserVip::getUserId, userId)
        );

        return UserInfoDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .department(user.getDepartment())
                .vipLevel(vip == null ? 0 : vip.getVipLevel())
                .vipExpireTime(vip == null ? null : vip.getExpireTime())
                .build();
    }

    public void updateInfo(Long userId, UserUpdateDTO dto) {

        SysUser user = userMapper.selectById(userId);

        if (user == null) {
            throw new BizException(
                    "USER_NOT_FOUND",
                    "userId",
                    "用户不存在"
            );
        }

        user.setRealName(dto.getRealName());
        user.setAvatar(dto.getAvatar());

        userMapper.updateById(user);
    }
}
