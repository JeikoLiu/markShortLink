package com.jeiko.shortlink_demo.admin.common.convention.result;

import com.jeiko.shortlink_demo.admin.common.convention.errorcode.BaseErrorCode;
import com.jeiko.shortlink_demo.admin.common.convention.exception.AbstractException;

import java.util.Optional;

/**
 * 全局通用返回对象构造器
 */
public class ResultUtils {

    /**
     * 构造成功响应
     */
    public static BaseResult<Void> success() {
        return new BaseResult<Void>().setCode(BaseResult.SUCCESS_CODE);
    }

    /**
     * 构造带数据的成功响应
     */
    public static <T> BaseResult<T> success(T data) {
        return new BaseResult<T>().setData(data).setCode(BaseResult.SUCCESS_CODE);
    }

    /**
     * 构建服务端失败响应
     */
    public static BaseResult<Void> failure() {
        return new BaseResult<Void>()
                .setCode(BaseErrorCode.SERVICE_ERROR.code())
                .setMessage(BaseErrorCode.SERVICE_ERROR.message());
    }

    /**
     * 通过 errorCode、errorMessage 构建失败响应
     */
    public static BaseResult<Void> failure(String errorCode, String errorMessage) {
        return new BaseResult<Void>()
                .setCode(errorCode)
                .setMessage(errorMessage);
    }

    /**
     * 根据 {@link AbstractException} 构建失败响应
     */
    public static BaseResult<Void> failure(AbstractException abstractException) {
        String errorCode = Optional.ofNullable(abstractException.getErrorCode())
                .orElse(BaseErrorCode.SERVICE_ERROR.code());
        String errorMessage = Optional.ofNullable(abstractException.getErrorMessage())
                .orElse(BaseErrorCode.SERVICE_ERROR.message());
        return new BaseResult<Void>()
                .setCode(errorCode)
                .setMessage(errorMessage);
    }

}
