/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <pre>
 *
 * </pre>
 * @author wangyanjun
 * @version $Id: SlnsBusinessException.java, v 0.1 2017年8月22日 下午1:50:15 wangyanjun Exp $
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SlnsBusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String            message;

    private int               errorCode;

    public SlnsBusinessException(String message) {
        super(message);
    }

    public SlnsBusinessException(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
