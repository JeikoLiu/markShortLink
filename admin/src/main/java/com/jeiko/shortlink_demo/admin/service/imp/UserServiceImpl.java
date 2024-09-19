package com.jeiko.shortlink_demo.admin.service.imp;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jeiko.shortlink_demo.admin.common.convention.errorcode.BaseErrorCode;
import com.jeiko.shortlink_demo.admin.common.convention.exception.ClientException;
import com.jeiko.shortlink_demo.admin.common.convention.exception.ServiceException;
import com.jeiko.shortlink_demo.admin.dao.entity.UserDO;
import com.jeiko.shortlink_demo.admin.dao.mapper.UserMapper;
import com.jeiko.shortlink_demo.admin.dto.req.UserLoginReqDTO;
import com.jeiko.shortlink_demo.admin.dto.req.UserRegisterResDTO;
import com.jeiko.shortlink_demo.admin.dto.req.UserUpdateResDTO;
import com.jeiko.shortlink_demo.admin.dto.resp.UserLoginRespDTO;
import com.jeiko.shortlink_demo.admin.dto.resp.UserRespDTO;
import com.jeiko.shortlink_demo.admin.service.GroupService;
import com.jeiko.shortlink_demo.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.jeiko.shortlink_demo.admin.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;
import static com.jeiko.shortlink_demo.admin.common.constant.RedisCacheConstant.USER_LOGIN_KEY;

/**
 * 用户接口实现层
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;
    private final GroupService groupService;

    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ServiceException(BaseErrorCode.USER_NULL);
        }
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);
        return result;
    }

    @Override
    public Boolean hasUserName(String username) {
        return !userRegisterCachePenetrationBloomFilter.contains(username);
    }

    @Override
    public void register(UserRegisterResDTO requestParam) {
        if (!hasUserName(requestParam.getUsername())) {
            throw new ClientException(BaseErrorCode.USER_NAME_EXIST_ERROR);
        }
        // 1. 拿到当前请求的用户注册名的分布式锁
        RLock rLock = redissonClient.getLock(LOCK_USER_REGISTER_KEY + requestParam.getUsername());
        if (!rLock.tryLock()) {
            throw new ClientException(BaseErrorCode.USER_NAME_EXIST_ERROR);
        }
        try {
            int inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
            if (inserted < 1) {
                throw new ClientException(BaseErrorCode.USER_SAVE_ERROR);
            }
            // 2.将注册成功的用户刷添加至布隆过滤器内
            userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
            groupService.saveGroup(requestParam.getUsername(),"默认分组");
        } catch (DuplicateKeyException exception) {
            throw new ClientException(BaseErrorCode.USER_EXIST);
        } finally {
            rLock.unlock();
        }
    }

    @Override
    public void update(UserUpdateResDTO requestParam) {
        // TODO 验证当前用户是否为登录用户
        LambdaQueryWrapper<UserDO> updateWrapper = Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, requestParam.getUsername());
        baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), updateWrapper);
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                        .eq(UserDO::getPassword, requestParam.getPassword())
                                .eq(UserDO::getDelFlag, 0);
        UserDO result = baseMapper.selectOne(queryWrapper);
        if (result == null) {
            throw new ClientException("用户不存在");
        }
        Map<Object, Object> hasLoginMap = stringRedisTemplate.opsForHash().entries(USER_LOGIN_KEY + requestParam.getUsername());
        if (CollUtil.isNotEmpty(hasLoginMap)) {
            String userToken = hasLoginMap.keySet().stream()
                    .findFirst()
                    .map(Object::toString)
                    .orElseThrow(() -> new ClientException("用户重复登陆"));
            return new UserLoginRespDTO(userToken);
        }
        /**
         * Hash
         * Key：login_用户名
         * Value：
         *  Key：token标识
         *  Val：JSON 字符串（用户信息）
         */
        // 1. 为当前用户提供唯一token标识(UUID是一种 128 位的数字标识符)，防止重复登陆
        String uuid = UUID.randomUUID().toString();
        stringRedisTemplate.opsForHash().put(USER_LOGIN_KEY + requestParam.getUsername(), uuid, JSON.toJSONString(result));
        // 2. 为用户登录缓存设置过期时间
        stringRedisTemplate.expire(USER_LOGIN_KEY+requestParam.getUsername(), 30L, TimeUnit.DAYS);
        return new UserLoginRespDTO(uuid);
    }

    @Override
    public Boolean checkLogin(String username, String token) {
        return stringRedisTemplate.opsForHash().get(USER_LOGIN_KEY + username, token) != null;
    }

    @Override
    public void logout(String username, String token) {
        // 检查用户是否登录
        if (checkLogin(username, token)) {
            stringRedisTemplate.delete(USER_LOGIN_KEY + username);
            return;
        }
        throw new ClientException("用户Token不存在或用户未登录");
    }
}
