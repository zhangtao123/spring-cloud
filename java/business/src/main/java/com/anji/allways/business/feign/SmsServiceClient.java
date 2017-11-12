/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.anji.allways.base.entity.BaseResponseModel;
import com.anji.allways.business.entity.SmsBaseRequestModel;

/**
 * 短信发送Client
 * @author Administrator
 * @version $Id: SmsServiceClient.java, v 0.1 2017年9月6日 下午2:56:02 Administrator Exp $
 */
@FeignClient(value = "SMS", url = "http://prod-elb-shh-1275010331.cn-north-1.elb.amazonaws.com.cn")
public interface SmsServiceClient {
    /**
     * 发送短信通知信息
     */
    @RequestMapping(value = "/sms/message/send", method = RequestMethod.POST, headers = { "Accept=application/json;charset=utf-8;", "Content-Type=application/json;charset=utf-8;" })
    BaseResponseModel<String> sendBatchMessage(@RequestBody SmsBaseRequestModel requestModel);

    /**
     * 发送验证码短信
     */
    @RequestMapping(value = "/sms/code/send", method = RequestMethod.POST, headers = { "Accept=application/json;charset=utf-8;", "Content-Type=application/json;charset=utf-8;" })
    BaseResponseModel<String> sendMessageCode(@RequestBody SmsBaseRequestModel requestModel);

    /**
     * 验证短信
     */
    @RequestMapping(value = "/sms/code/verify", method = RequestMethod.POST, headers = { "Accept=application/json;charset=utf-8;", "Content-Type=application/json;charset=utf-8;" })
    BaseResponseModel<String> sendMessageVerify(@RequestBody SmsBaseRequestModel requestModel);

}
