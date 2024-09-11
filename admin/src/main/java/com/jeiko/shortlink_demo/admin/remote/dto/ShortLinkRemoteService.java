package com.jeiko.shortlink_demo.admin.remote.dto;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeiko.shortlink_demo.admin.common.convention.result.BaseResult;
import com.jeiko.shortlink_demo.admin.remote.dto.req.RecycleBinSaveReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.req.ShortLinkCreateReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.req.ShortLinkUpdateReqDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.resp.ShortLinkGroupCountRespDTO;
import com.jeiko.shortlink_demo.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 短链接中台远程调用服务
 */
public interface ShortLinkRemoteService {
    /**
     * 创建短链接
     * @param requestParam 创建短链接请求参数
     * @return 短链接创建响应
     */
    default BaseResult<ShortLinkCreateRespDTO> createShortLink(ShortLinkCreateReqDTO requestParam) {
        String resultBodyStr = HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/create", JSON.toJSONString(requestParam));
        return JSON.parseObject(resultBodyStr, new TypeReference<>() {
        });
    }

    /**
     * 分页查询短链接
     *
     * @param requestParam 分页短链接请求参数
     * @return 查询短链接响应
     */
    default BaseResult<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("gid", requestParam.getGid());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());
        String resultPageStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/page", requestMap);

//        String url = "http://127.0.0.1:8001/api/short-link/v1/page";
//        requestMap.put("gid", requestParam.getGid());
//        requestMap.put("current", requestParam.getCurrent());
//        requestMap.put("size", requestParam.getSize());
//        String resultPageStr = HttpRequest.post(url)
//                .contentType("application/json")
//                .body(JSON.toJSONString(requestMap))
//                .execute()
//                .body(); // 发送请求并获取响应体
        return JSON.parseObject(resultPageStr, new TypeReference<>() {});
    }

    /**
     * 查询短链接分组及组内数量
     *
     * @param requestParam 查询短链接分组及组内数量请求参数
     * @return 查询短链接分组及组内数量响应
     */
    default BaseResult<List<ShortLinkGroupCountRespDTO>> countShortLinkListGroup(List<String> requestParam) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("requestParam", requestParam);
        String resultPageStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/count", requestMap);
        return JSON.parseObject(resultPageStr, new TypeReference<>() {});
    }

    /**
     * 修改短链接
     * @param requestParam 修改短链接请求参数
     */
    default void updateShortLink(ShortLinkUpdateReqDTO requestParam) {
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/update", JSON.toJSONString(requestParam));
    }

    /**
     * 根据 URL 获取标题
     *
     * @param url 目标网站地址
     * @return 网站标题
     */
    default BaseResult<String> getTitleByUrl(@RequestParam("url") String url) {
        String resultStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/title?url=" + url);
        return JSON.parseObject(resultStr, new TypeReference<>() {
        });
    }

    /**
     * 保存回收站
     *
     * @param requestParam 请求参数
     */
    default void saveRecycleBin(RecycleBinSaveReqDTO requestParam) {
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/recycle-bin/save", JSON.toJSONString(requestParam));
    }
}
