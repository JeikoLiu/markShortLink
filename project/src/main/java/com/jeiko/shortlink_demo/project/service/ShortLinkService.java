package com.jeiko.shortlink_demo.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jeiko.shortlink_demo.project.dao.entity.ShortLinkDO;
import com.jeiko.shortlink_demo.project.dto.req.ShortLinkCreateReqDTO;
import com.jeiko.shortlink_demo.project.dto.req.ShortLinkPageReqDTO;
import com.jeiko.shortlink_demo.project.dto.resp.ShortLinkCreateRespDTO;
import com.jeiko.shortlink_demo.project.dto.resp.ShortLinkGroupCountRespDTO;
import com.jeiko.shortlink_demo.project.dto.resp.ShortLinkPageRespDTO;

import java.util.List;

/**
 * 短链接接口层
 */
public interface ShortLinkService extends IService<ShortLinkDO> {

    /**
     * 创建短链接
     * @param requestParam 创建短链接请求实体
     * @return 短链接的创建信息
     */
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam);

    /**
     * 分页查询短链接
     * @param requestParam 分页查询参数
     * @return 短链接分页返回结果
     */
    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam);

    /**
     * 查询短链接分组及组内数量
     * @param requestParam 查询短链接分组及组内数量请求参数
     * @return 查询短链接分组及组内数量返回参数
     */
    List<ShortLinkGroupCountRespDTO> countShortLinkListGroup(List<String> requestParam);
}
