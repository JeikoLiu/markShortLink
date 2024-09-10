package com.jeiko.shortlink_demo.project.common.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ValidateTypeEnum {
    /**
     * 永久有效期
     */
    PERMANENT(0),

    /**
     * 自定义有效期
     */
    CUSTOM(1);

    private final int Type;

    public int getType() {
        return Type;
    }
}
