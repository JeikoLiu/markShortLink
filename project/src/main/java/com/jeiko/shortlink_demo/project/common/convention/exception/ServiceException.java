package com.jeiko.shortlink_demo.project.common.convention.exception;

import com.jeiko.shortlink_demo.project.common.convention.errorcode.BaseErrorCode;
import com.jeiko.shortlink_demo.project.common.convention.errorcode.IErrorCode;

/**
 * 服务端异常
 */
public class ServiceException extends AbstractException{

    public ServiceException(String message) {
        this(message, null, BaseErrorCode.SERVICE_ERROR);
    }

    public ServiceException(IErrorCode errorCode) {
        this(null, null, errorCode);
    }

    public ServiceException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public ServiceException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }
}
