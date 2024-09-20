package com.jeiko.shortlink_demo.admin.service.imp;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeiko.shortlink_demo.admin.common.biz.user.UserContext;
import com.jeiko.shortlink_demo.admin.common.convention.exception.ServiceException;
import com.jeiko.shortlink_demo.admin.common.convention.result.BaseResult;
import com.jeiko.shortlink_demo.admin.dao.entity.GroupDO;
import com.jeiko.shortlink_demo.admin.dao.mapper.GroupMapper;
import com.jeiko.shortlink_demo.admin.remote.dto.ShortLinkActualRemoteService;
import com.jeiko.shortlink_demo.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.jeiko.shortlink_demo.admin.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 短链接回收站接口实现层
 */
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl implements RecycleBinService {

    private final GroupMapper groupMapper;
    private final ShortLinkActualRemoteService shortLinkActualRemoteService;

    /**
     * 后管分页查询回收站中的短链接时是通过当前用户查询分组gid，再作为参数传给中台的接口
     */
    @Override
    public BaseResult<Page<ShortLinkPageRespDTO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0);
        List<GroupDO> groupDOList = groupMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(groupDOList)) {
            throw new ServiceException("当前用户无分组信息");
        }
        requestParam.setGidList(groupDOList.stream().map(GroupDO::getGid).collect(Collectors.toList()));
        return shortLinkActualRemoteService.pageRecycleBinShortLink(requestParam);
    }
}
