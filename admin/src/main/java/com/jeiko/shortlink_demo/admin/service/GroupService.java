package com.jeiko.shortlink_demo.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jeiko.shortlink_demo.admin.dao.entity.GroupDO;

/**
 * 短链接分组服务接口层
 */
public interface GroupService extends IService<GroupDO> {
    /**
     * 新增短链接分组
     * @param groupName 短链接分组名
     */
    void saveGroup(String groupName);
}
