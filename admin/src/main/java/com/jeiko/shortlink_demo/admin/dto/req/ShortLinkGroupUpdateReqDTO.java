package com.jeiko.shortlink_demo.admin.dto.req;

import lombok.Data;

@Data
public class ShortLinkGroupUpdateReqDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组名
     */
    private String name;
}
