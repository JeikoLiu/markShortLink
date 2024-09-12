package com.jeiko.shortlink_demo.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeiko.shortlink_demo.admin.common.convention.result.BaseResult;
import com.jeiko.shortlink_demo.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.resp.ShortLinkPageRespDTO;

/**
 * 回收站接口层
 */
public interface RecycleBinService {
    /**
     * 回收站短链接分页查询
     * @param requestParam 分页查询请求参数
     * @return 分页查询结果返回
     */
    BaseResult<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam);
}
