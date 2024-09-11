package com.jeiko.shortlink_demo.project.dto.req;

import lombok.Data;

/**
 * 保存回收站请求参数
 */
@Data
public class RecycleBinSaveReqDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 全部短链接
     */
    private String fullShortUrl;
}
