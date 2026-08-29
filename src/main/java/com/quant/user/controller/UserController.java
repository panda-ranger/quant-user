package com.quant.user.controller;

import com.quant.user.annotation.RequirePermission;
import com.quant.user.dto.ApiResponse;
import com.quant.user.dto.UserInfoDTO;
import com.quant.user.dto.UserUpdateDTO;
import com.quant.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author panda-y
 * @version 1.0
 * @date 2026/8/29 10:45
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserService userService;

    @RequirePermission("user:profile")
    @GetMapping("/info")
    public ApiResponse<UserInfoDTO> info(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-Username") String username){
        return ApiResponse.success(
                userService.getCurrentUserInfo(userId)
        );
    }

    @PutMapping("/info")
    public ApiResponse<Void> updateInfo(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody UserUpdateDTO dto) {

        userService.updateInfo(userId, dto);
        return ApiResponse.success();
    }

    @RequirePermission("user:delete")
    @DeleteMapping("/{id}")
    public ApiResponse<Long> delete(@PathVariable Long id) {
        return ApiResponse.success(id);
    }
}
