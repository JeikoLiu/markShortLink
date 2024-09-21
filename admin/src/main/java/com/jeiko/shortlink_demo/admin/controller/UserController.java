package com.jeiko.shortlink_demo.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.jeiko.shortlink_demo.admin.common.convention.result.BaseResult;
import com.jeiko.shortlink_demo.admin.common.convention.result.ResultUtils;
import com.jeiko.shortlink_demo.admin.dto.req.UserLoginReqDTO;
import com.jeiko.shortlink_demo.admin.dto.req.UserRegisterResDTO;
import com.jeiko.shortlink_demo.admin.dto.req.UserUpdateResDTO;
import com.jeiko.shortlink_demo.admin.dto.resp.UserActualRespDTO;
import com.jeiko.shortlink_demo.admin.dto.resp.UserLoginRespDTO;
import com.jeiko.shortlink_demo.admin.dto.resp.UserRespDTO;
import com.jeiko.shortlink_demo.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户中心控制层
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 根据用户名查询寻用脱敏后的用户信息
     */
    @GetMapping("/api/short-link/admin/v1/user/{username}")
    public BaseResult<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        UserRespDTO result = userService.getUserByUsername(username);
        return ResultUtils.success(result);
    }

    /**
     * 根据用户名查询寻未脱敏的用户信息
     */
    @GetMapping("/api/short-link/admin/v1/actual/user/{username}")
    public BaseResult<UserActualRespDTO> getActualUserByUsername(@PathVariable("username") String username) {
        return ResultUtils.success(BeanUtil.toBean(userService.getUserByUsername(username), UserActualRespDTO.class));
    }

    /**
     * 查村用户名是否存在
     */
    @GetMapping("/api/short-link/admin/v1/user/has-username")
    public BaseResult<Boolean> hasUserName(@RequestParam("username") String username) {
        return ResultUtils.success(userService.hasUserName(username));
    }

    /**
     * 用户注册
     */
    @PostMapping("/api/short-link/admin/v1/user")
    public BaseResult<Void> register(@RequestBody UserRegisterResDTO requestParam) {
        userService.register(requestParam);
        return ResultUtils.success();
    }

    /**
     * 修改用户信息
     */
    @PutMapping("/api/short-link/admin/v1/user")
    public BaseResult<Void> update(@RequestBody UserUpdateResDTO requestParam) {
        userService.update(requestParam);
        return ResultUtils.success();
    }

    /**
     * 用户登录
     * @param requestParam 用户登录请求参数
     * @return 用户token
     */
    @PostMapping("/api/short-link/admin/v1/user/login")
    public BaseResult<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO requestParam) {
        return ResultUtils.success(userService.login(requestParam));
    }

    /**
     * 检查用户是否登录
     */
    @GetMapping("/api/short-link/admin/v1/user/check-login")
    public BaseResult<Boolean> checkLogin(@RequestParam("username") String username, @RequestParam("token") String token) {
        return ResultUtils.success(userService.checkLogin(username, token));
    }

    /**
     * 用户注销退出登录
     */
    @DeleteMapping("/api/short-link/admin/v1/user/logout")
    public BaseResult<Void> logout(@RequestParam("username") String username, @RequestParam("token") String token) {
        userService.logout(username, token);
        return ResultUtils.success();
    }

}
