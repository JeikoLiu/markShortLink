package com.jeiko.shortlink_demo.admin.remote.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeiko.shortlink_demo.admin.common.convention.result.BaseResult;
import com.jeiko.shortlink_demo.admin.remote.dto.req.*;
import com.jeiko.shortlink_demo.admin.remote.dto.resp.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 短链接中台远程调用服务
 */
@FeignClient("short-link-project")
public interface ShortLinkActualRemoteService {

    /**
     * 创建短链接
     * @param requestParam 创建短链接请求参数
     * @return 短链接创建响应
     */
    @PostMapping("/api/short-link/v1/create")
    BaseResult<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam);

    /**
     * 批量创建短链接
     *
     * @param requestParam 批量创建短链接请求参数
     * @return 短链接批量创建响应
     */
    @PostMapping("/api/short-link/v1/create/batch")
    BaseResult<ShortLinkBatchCreateRespDTO> batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam);

    /**
     * 修改短链接
     *
     * @param requestParam 修改短链接请求参数
     */
    @PostMapping("/api/short-link/v1/update")
    void updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam);

    /**
     * 分页查询短链接
     *
     * @param requestParam 修改短链接分页请求参数
     * @return 查询短链接响应
     */
    @GetMapping("/api/short-link/v1/page")
    BaseResult<Page<ShortLinkPageRespDTO>> pageShortLink(@SpringQueryMap ShortLinkPageReqDTO requestParam);

    /**
     * 保存回收站
     *
     * @param requestParam 请求参数
     */
    @PostMapping("/api/short-link/v1/recycle-bin/save")
    void saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam);

    /**
     * 回收站分页查询短链接
     * @param requestParam 分页查询请求参数
     * @return 分页查询短链接结果
     */
    @GetMapping("/api/short-link/v1/recycle-bin/page")
    BaseResult<Page<ShortLinkPageRespDTO>> pageRecycleBinShortLink(@SpringQueryMap ShortLinkRecycleBinPageReqDTO requestParam);

    /**
     * 恢复短链接
     * @param requestParam 恢复短链接请求参数
     */
    @PostMapping("/api/short-link/v1/recycle-bin/recover")
    void recoverShortLink(@RequestBody RecycleBinRecoverReqDTO requestParam);

    /**
     * 移除短链接
     * @param requestParam 移除短链接请求参数
     */
    @PostMapping("/api/short-link/v1/recycle-bin/remove")
    void removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO requestParam);

    /**
     * 访问单个短链接指定时间内监控数据
     *
     * @param requestParam 访问短链接监控请求参数
     * @return 短链接监控信息
     */
    @GetMapping("/api/short-link/v1/stats")
    BaseResult<ShortLinkStatsRespDTO> oneShortLinkStats(@SpringQueryMap ShortLinkStatsReqDTO requestParam);

    /**
     * 访问分组短链接指定时间内监控数据
     *
     * @param requestParam 访分组问短链接监控请求参数
     * @return 分组短链接监控信息
     */
    @GetMapping("/api/short-link/v1/stats/group")
    BaseResult<ShortLinkStatsRespDTO> groupShortLinkStats(@SpringQueryMap ShortLinkGroupStatsReqDTO requestParam);

    /**
     * 访问单个短链接指定时间内访问记录监控数据
     * @param requestParam 访问记录请求参数
     * @return 短链接访问记录监控信息
     */
    @GetMapping("/api/short-link/v1/stats/access-record")
    BaseResult<Page<ShortLinkStatsAccessRecordRespDTO>> shortLinkStatsAccessRecord(@SpringQueryMap ShortLinkStatsAccessRecordReqDTO requestParam);

    /**
     * 访问分组短链接指定时间内访问记录监控数据
     * @param requestParam 访问记录请求参数
     * @return 分组短链接访问记录监控信息
     */
    @GetMapping("/api/short-link/v1/stats/access-record/group")
    BaseResult<Page<ShortLinkStatsAccessRecordRespDTO>> groupShortLinkStatsAccessRecord(@SpringQueryMap ShortLinkGroupStatsAccessRecordReqDTO requestParam);

    /**
     * 根据 URL 获取标题
     *
     * @param url 目标网站地址
     * @return 网站标题
     */
    @GetMapping("/api/short-link/v1/title")
    BaseResult<String> getTitleByUrl(@RequestParam("url") String url);

    /**
     * 查询短链接分组及组内数量
     *
     * @param requestParam 查询短链接分组及组内数量请求参数
     * @return 查询短链接分组及组内数量响应
     */
    @GetMapping("/api/short-link/v1/count")
    BaseResult<List<ShortLinkGroupCountRespDTO>> countShortLinkListGroup(@RequestParam("requestParam") List<String> requestParam);
}
