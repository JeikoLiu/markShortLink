package com.jeiko.shortlink_demo.admin.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jeiko.shortlink_demo.admin.dao.entity.GroupDO;
import com.jeiko.shortlink_demo.admin.dao.mapper.GroupMapper;
import com.jeiko.shortlink_demo.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 短链接分组接口实现
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {
}
