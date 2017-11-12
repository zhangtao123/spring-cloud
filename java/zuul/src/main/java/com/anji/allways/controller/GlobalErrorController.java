/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.model.ZuulResponseModel;

/**
 * <pre>
 * 全局错误异常处理
 * </pre>
 * @author WangGuangYuan
 * @version $Id: GlobalErrorController.java, v 0.1 2017年5月31日 上午9:24:28 Administrator Exp $
 */
@RestController
@RequestMapping("/error")
public class GlobalErrorController extends AbstractErrorController {

    private static Logger log = LoggerFactory.getLogger(GlobalErrorController.class);

    @Autowired
    public GlobalErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    /**
     * @return
     * @see org.springframework.boot.autoconfigure.web.ErrorController#getErrorPath()
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(produces = MediaType.ALL_VALUE)
    public ZuulResponseModel<String> error(HttpServletRequest request) {
        ZuulResponseModel<String> response = new ZuulResponseModel<String>();
        HttpStatus status = getStatus(request);
        if (status.is5xxServerError()) {
            log.error("500错误");
        }
        response.setRepCode(RespCode.ZUUL_COMMON_ERROR);
        response.setRepMsg(RespMsg.ZUUL_COMMON_ERROR_MSG);
        return response;
    }
}
