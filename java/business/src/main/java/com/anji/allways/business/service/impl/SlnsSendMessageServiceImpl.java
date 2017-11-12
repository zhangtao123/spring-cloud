/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anji.allways.base.entity.BaseResponseModel;
import com.anji.allways.business.common.constant.SlnsSendMessageConstants;
import com.anji.allways.business.entity.SmsBaseRequestModel;
import com.anji.allways.business.feign.SmsServiceClient;
import com.anji.allways.business.service.SlnsSendMessageService;
import com.anji.allways.common.util.MD5Util;

/**
 * <pre>
 * 发送短信Service实现类
 * </pre>
 * @author Administrator
 * @version $Id: SlnsSendMessageServiceImpl.java, v 0.1 2017年9月27日 下午12:56:18 Administrator Exp $
 */
@Service
public class SlnsSendMessageServiceImpl implements SlnsSendMessageService {

    private static final Logger      LOGGER = LogManager.getLogger(SlnsSendMessageServiceImpl.class);

    @Autowired
    private SlnsSendMessageConstants slnsSendConstants;

    @Autowired
    private SmsServiceClient         smsServiceClient;

    /**
     * @param messageType
     * @param mobile
     * @param ttl
     * @return
     * @see com.anji.allways.business.service.SlnsSendMessageService#sendCode(java.lang.String, java.lang.String, java.lang.Integer)
     */
    @SuppressWarnings("static-access")
    @Override
    public String sendCode(String messageType, String mobile, int ttl) {
        LOGGER.info("发送验证码短信======》");
        // 封装短信请求信息
        SmsBaseRequestModel smsBaseRequestModel = new SmsBaseRequestModel();
        smsBaseRequestModel.setSysCode(slnsSendConstants.getSystemCode());
        smsBaseRequestModel.setTime(String.valueOf(System.currentTimeMillis()));
        JSONObject reqData = new JSONObject();
        reqData.put("message_type", messageType);
        JSONObject data = new JSONObject();
        // 文本验证码
        if (slnsSendConstants.getSmsMessageCode().equals(messageType)) {
            data.put("temp_id", Long.parseLong(slnsSendConstants.getSmsTemplateId()));
            data.put("mobile", mobile);
        }
        // 语音验证码
        if (slnsSendConstants.getSmsMessageVoice().equals(messageType)) {
            data.put("ttl", 0 == ttl ? 60 : ttl);
            data.put("mobile", mobile);
        }
        reqData.put("data", data);
        smsBaseRequestModel.setReqData(reqData);
        String baseSignMsg = "reqData" + smsBaseRequestModel.getReqData().toJSONString() + "time" + smsBaseRequestModel.getTime() + "token" + slnsSendConstants.getSmsToken();
        smsBaseRequestModel.setSign(MD5Util.getInstance().encrypt(baseSignMsg).toString());
        LOGGER.info("发送验证码短信实体======》" + JSON.toJSONString(smsBaseRequestModel));
        // 发送签收通知信息
        BaseResponseModel<String> resultMap = smsServiceClient.sendMessageCode(smsBaseRequestModel);
        if (null == resultMap || StringUtils.isEmpty(resultMap.getRepData())) {
            return "error";
        }
        LOGGER.info("返回发送验证码短信实体======》" + JSONObject.toJSONString(resultMap));
        if (!"50000".equals(resultMap.getRepCode())) {
            return "error";
        }
        JSONObject result = JSONObject.parseObject(resultMap.getRepData());
        if (result.containsKey("msg_id")) {
            return result.getString("msg_id");
        }
        return "error";
    }

    /**
     * @param msgId
     * @param code
     * @return
     * @see com.anji.allways.business.service.SlnsSendMessageService#verifyCode(java.lang.String, java.lang.String)
     */
    @SuppressWarnings("static-access")
    @Override
    public Boolean verifyCode(String msgId, String code) {
        Boolean resultBoolean = false;
        LOGGER.info("短信验证======》");
        // 封装短信请求信息
        SmsBaseRequestModel smsBaseRequestModel = new SmsBaseRequestModel();
        smsBaseRequestModel.setSysCode(slnsSendConstants.getSystemCode());
        smsBaseRequestModel.setTime(String.valueOf(System.currentTimeMillis()));
        JSONObject reqData = new JSONObject();
        reqData.put("msg_id", msgId);
        JSONObject data = new JSONObject();
        data.put("code", code);
        reqData.put("data", data);
        smsBaseRequestModel.setReqData(reqData);
        String baseSignMsg = "reqData" + smsBaseRequestModel.getReqData().toJSONString() + "time" + smsBaseRequestModel.getTime() + "token" + slnsSendConstants.getSmsToken();
        smsBaseRequestModel.setSign(MD5Util.getInstance().encrypt(baseSignMsg).toString());
        LOGGER.info("发送短信验证实体======》" + JSON.toJSONString(smsBaseRequestModel));
        // 发送签收通知信息
        BaseResponseModel<String> resultMap = smsServiceClient.sendMessageVerify(smsBaseRequestModel);
        if (null == resultMap || StringUtils.isEmpty(resultMap.getRepData())) {
            return resultBoolean;
        }
        LOGGER.info("返回短信验证实体======》" + JSONObject.toJSONString(resultMap));
        if (!"50000".equals(resultMap.getRepCode())) {
            return resultBoolean;
        }
        JSONObject result = JSONObject.parseObject(resultMap.getRepData());
        if (result.containsKey("is_valid")) {
            resultBoolean = result.getBoolean("is_valid");
        }
        return resultBoolean;
    }

    /**
     * @param tempId
     * @param sendType
     * @param isRetry
     * @param mobile
     * @param tempPara
     * @return
     * @see com.anji.allways.business.service.SlnsSendMessageService#sendSingleMessage(int, int, int, java.lang.String, net.minidev.json.JSONObject)
     */
    @SuppressWarnings("static-access")
    @Override
    public String sendSingleMessage(int tempId, int sendType, int isRetry, String mobile, JSONObject tempPara) {
        // 封装短信请求信息
        SmsBaseRequestModel smsBaseRequestModel = new SmsBaseRequestModel();
        smsBaseRequestModel.setSysCode(slnsSendConstants.getSystemCode());
        smsBaseRequestModel.setTime(String.valueOf(System.currentTimeMillis()));
        JSONObject reqData = new JSONObject();
        reqData.put("send_type", sendType);
        reqData.put("is_retry", isRetry);
        reqData.put("message_type", slnsSendConstants.getSmsMessageTemp());
        JSONObject data = new JSONObject();
        data.put("mobile", mobile);
        data.put("temp_id", tempId);
        data.put("temp_para", tempPara);
        reqData.put("data", data);
        smsBaseRequestModel.setReqData(reqData);
        String baseSignMsg = "reqData" + smsBaseRequestModel.getReqData().toJSONString() + "time" + smsBaseRequestModel.getTime() + "token" + slnsSendConstants.getSmsToken();
        smsBaseRequestModel.setSign(MD5Util.getInstance().encrypt(baseSignMsg).toString());
        LOGGER.info("发送单个通知短信实体======》" + JSON.toJSONString(smsBaseRequestModel));
        // 发送签收通知信息
        BaseResponseModel<String> resultMap = smsServiceClient.sendMessageCode(smsBaseRequestModel);
        if (null == resultMap) {
            return "error";
        }
        LOGGER.info("返回单个通知短信实体======》" + JSONObject.toJSONString(resultMap));
        if (!"50000".equals(resultMap.getRepCode())) {
            return resultMap.getRepMsg();
        }
        return "success";
    }

    /**
     * @param tempId
     * @param sendType
     * @param isRetry
     * @param tempPara
     * @return
     * @see com.anji.allways.business.service.SlnsSendMessageService#sendMoreMessage(int, int, int, com.alibaba.fastjson.JSONArray)
     */
    @SuppressWarnings("static-access")
    @Override
    public String sendMoreMessage(int tempId, int sendType, int isRetry, JSONArray tempPara) {
        // 封装短信请求信息
        SmsBaseRequestModel smsBaseRequestModel = new SmsBaseRequestModel();
        smsBaseRequestModel.setSysCode(slnsSendConstants.getSystemCode());
        smsBaseRequestModel.setTime(String.valueOf(System.currentTimeMillis()));
        JSONObject reqData = new JSONObject();
        reqData.put("send_type", sendType);
        reqData.put("is_retry", isRetry);
        reqData.put("message_type", slnsSendConstants.getSmsMessageBatch());
        JSONObject data = new JSONObject();
        data.put("temp_id", tempId);
        data.put("recipients", tempPara);
        reqData.put("data", data);
        smsBaseRequestModel.setReqData(reqData);
        String baseSignMsg = "reqData" + smsBaseRequestModel.getReqData().toJSONString() + "time" + smsBaseRequestModel.getTime() + "token" + slnsSendConstants.getSmsToken();
        smsBaseRequestModel.setSign(MD5Util.getInstance().encrypt(baseSignMsg).toString());
        LOGGER.info("发送批量通知短信实体======》" + JSON.toJSONString(smsBaseRequestModel));
        // 发送签收通知信息
        BaseResponseModel<String> resultMap = smsServiceClient.sendMessageCode(smsBaseRequestModel);
        if (null == resultMap) {
            return "error";
        }
        LOGGER.info("返回批量通知短信实体======》" + JSONObject.toJSONString(resultMap));
        if (!"50000".equals(resultMap.getRepCode())) {
            return resultMap.getRepMsg();
        }
        return "success";
    }

}
