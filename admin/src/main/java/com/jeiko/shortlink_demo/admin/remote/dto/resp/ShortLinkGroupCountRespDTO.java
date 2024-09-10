package com.jeiko.shortlink_demo.admin.remote.dto.resp;

import lombok.Data;

/**
 * 短链接分组及组内数量查询返回实体
 */
@Data
public class ShortLinkGroupCountRespDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 短链接数量
     */
    private Integer shortLinkCount;
}
