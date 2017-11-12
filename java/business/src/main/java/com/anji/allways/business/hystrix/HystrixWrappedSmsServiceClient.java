/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.hystrix;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.anji.allways.base.entity.BaseResponseModel;
import com.anji.allways.business.entity.SmsBaseRequestModel;
import com.anji.allways.business.feign.SmsServiceClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * <pre>
 * 实现信息
 * </pre>
 * @author Administrator
 * @version $Id: HystrixWrappedSmsServiceClient.java, v 0.1 2017年9月6日 下午3:12:16 Administrator Exp $
 */
@Service
public class HystrixWrappedSmsServiceClient implements SmsServiceClient {

    private Logger           logger = LogManager.getLogger(HystrixWrappedSmsServiceClient.class);

    @Autowired
    private SmsServiceClient smsServiceClient;

    /**
     * @param requestModel
     * @return
     * @see com.anji.allways.business.op.sign.feign.SmsServiceClient#SendBatchMessage(com.anji.allways.base.entity.SmsBaseRequestModel)
     */
    @Override
    @HystrixCommand(groupKey = "smsGroup", fallbackMethod = "sendBatchMessageFallBackCall")
    public BaseResponseModel<String> sendBatchMessage(SmsBaseRequestModel requestModel) {
        String str = (null == requestModel) ? "" : JSONObject.toJSONString(requestModel);
        logger.info("发送批量短信通知信息======》" + str);
        return smsServiceClient.sendBatchMessage(requestModel);
    }

    /**
     * @param requestModel
     * @return
     * @see com.anji.allways.business.feign.SmsServiceClient#sendMessageCode(com.anji.allways.business.entity.SmsBaseRequestModel)
     */
    @Override
    @HystrixCommand(groupKey = "smsGroup", fallbackMethod = "sendBatchMessageFallBackCall")
    public BaseResponseModel<String> sendMessageCode(SmsBaseRequestModel requestModel) {
        String str = (null == requestModel) ? "" : JSONObject.toJSONString(requestModel);
        logger.info("发送短信信息======》" + str);
        return smsServiceClient.sendMessageCode(requestModel);
    }

    /**
     * @param requestModel
     * @return
     * @see com.anji.allways.business.feign.SmsServiceClient#sendMessageVerify(com.anji.allways.business.entity.SmsBaseRequestModel)
     */
    @Override
    @HystrixCommand(groupKey = "smsGroup", fallbackMethod = "sendBatchMessageFallBackCall")
    public BaseResponseModel<String> sendMessageVerify(SmsBaseRequestModel requestModel) {
        String str = (null == requestModel) ? "" : JSONObject.toJSONString(requestModel);
        logger.info("发送短信信息======》" + str);
        return smsServiceClient.sendMessageVerify(requestModel);
    }

    /**
     * <pre>
     * 发送失败返回方法
     * </pre>
     * @param requestModel
     * @return
     */
    public Map<String, String> sendBatchMessageFallBackCall(SmsBaseRequestModel requestModel) {
        String str = (null == requestModel) ? "" : JSONObject.toJSONString(requestModel);
        logger.info("发送短信信息======》" + str);
        logger.info("发送短信信息失败!");
        return null;
    }

}
