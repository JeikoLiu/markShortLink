package com.jeiko.shortlink_demo.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jeiko.shortlink_demo.admin.dao.entity.UserDO;
import com.jeiko.shortlink_demo.admin.dto.req.UserLoginReqDTO;
import com.jeiko.shortlink_demo.admin.dto.req.UserRegisterResDTO;
import com.jeiko.shortlink_demo.admin.dto.req.UserUpdateResDTO;
import com.jeiko.shortlink_demo.admin.dto.resp.UserLoginRespDTO;
import com.jeiko.shortlink_demo.admin.dto.resp.UserRespDTO;

/**
 * 用户接口层
 */
public interface UserService extends IService<UserDO> {
    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 返回用户实体
     */
    UserRespDTO getUserByUsername(String username);

    /**
     * 查询用户名是否存在
     * @param username 用户名
     * @return 用户名存在返回 True，不存在返回 False
     */
    Boolean hasUserName(String username);

    /**
     * 用户的注册
     * @param requestParam 用户注册请求参数
     */
    void register(UserRegisterResDTO requestParam);

    /**
     * 修改用户信息
     * @param requestParam 用户修改信息请求参数
     */
    void update(UserUpdateResDTO requestParam);

    /**
     * 用户登录
     * @param requestParam 用户登录请求参数
     * @return 用户token
     */
    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    /**
     * 检查用户是否登录
     */
    Boolean checkLogin(String username, String token);

    /**
     * 用户注销退出登录
     */
    void logout(String username, String token);
}
