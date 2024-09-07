package com.jeiko.shortlink_demo.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jeiko.shortlink_demo.admin.dao.entity.GroupDO;
import com.jeiko.shortlink_demo.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.jeiko.shortlink_demo.admin.dto.resp.ShortLinkGroupListRespDTO;

import java.util.List;

/**
 * 短链接分组服务接口层
 */
public interface GroupService extends IService<GroupDO> {
    /**
     * 新增短链接分组
     * @param groupName 短链接分组名
     */
    void saveGroup(String groupName);

    /**
     * 查询用户端连接分组集合
     * @return 返回用户短链接分组集合
     */
    List<ShortLinkGroupListRespDTO> listGroup();

    /**
     * 修改短链接分组名称
     * @param requestParam 修改短链接分组名参数
     */
    void updateGroup(ShortLinkGroupUpdateReqDTO requestParam);
}
