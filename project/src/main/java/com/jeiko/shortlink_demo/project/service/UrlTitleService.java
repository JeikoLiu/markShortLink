package com.jeiko.shortlink_demo.project.service;

/**
 * 根据URL获取标题接口层
 */
public interface UrlTitleService {
    /**
     * 根据url过去目标网站的标题
     * @param url url
     * @return 目标网站标题
     */
    String getTitleByUrl(String url);
}
