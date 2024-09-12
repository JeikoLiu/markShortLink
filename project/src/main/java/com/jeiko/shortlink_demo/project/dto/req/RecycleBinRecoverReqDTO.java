package com.jeiko.shortlink_demo.project.dto.req;

import lombok.Data;

/**
 * 恢复短链接请求参数
 */
@Data
public class RecycleBinRecoverReqDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 完整短链接
     */
    private String fullShortUrl;
}
