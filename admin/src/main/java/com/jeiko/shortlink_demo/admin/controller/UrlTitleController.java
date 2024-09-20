package com.jeiko.shortlink_demo.admin.controller;

import com.jeiko.shortlink_demo.admin.common.convention.result.BaseResult;
import com.jeiko.shortlink_demo.admin.remote.dto.ShortLinkActualRemoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * URL 标题控制层
 */
@RestController
@RequiredArgsConstructor
public class UrlTitleController {

    private final ShortLinkActualRemoteService shortLinkActualRemoteService;

    /**
     * 根据URL获取对应网站的标题
     */
    @GetMapping("/api/short-link/admin/v1/title")
    public BaseResult<String> getTitleByUrl(@RequestParam("url") String url) {
        return shortLinkActualRemoteService.getTitleByUrl(url);
    }
}
