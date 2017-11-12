/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.controller;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.anji.allways.base.aop.LoggerManage;
import com.anji.allways.base.entity.BaseRequestModel;
import com.anji.allways.base.entity.BaseResponseModel;
import com.anji.allways.business.common.constant.SlnsSendMessageConstants;
import com.anji.allways.business.entity.UserEntity;
import com.anji.allways.business.service.SlnsSendMessageService;
import com.anji.allways.business.service.UserService;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.DateUtil;

/**
 * 短信发送Controller
 * @author wangyanjun
 * @version $Id: CustomerController.java, v 0.1 2017年8月30日 上午12:01:48 wangyanjun Exp $
 */
@Controller
@RequestMapping("/")
public class SmsSendController {

    private Logger                   logger = LogManager.getLogger(SmsSendController.class);

    @Autowired
    private SlnsSendMessageService   slnsSendMessageService;

    @Autowired
    private SlnsSendMessageConstants slnsSendConstants;

    @Autowired
    private UserService              loginService;

    /**
     * 司机发送短信验证码
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/driver/sms/sendCode")
    @ResponseBody
    @LoggerManage(description = "司机发送短信验证码")
    public BaseResponseModel<Object> driverSendCode(@RequestBody BaseRequestModel request) throws Exception {
        return sendCode(request, 4);
    }

    /**
     * 经销商发送短信验证码
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/dealer/sms/sendCode")
    @ResponseBody
    @LoggerManage(description = "经销商发送短信验证码")
    public BaseResponseModel<Object> dealerSendCode(@RequestBody BaseRequestModel request) throws Exception {
        return sendCode(request, 1);
    }

    /**
     * PC端发送短信验证码
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/business/sms/sendCode")
    @ResponseBody
    @LoggerManage(description = "PC端发送短信验证码")
    public BaseResponseModel<Object> pcSendCode(@RequestBody BaseRequestModel request) throws Exception {
        return sendCode(request, 2);
    }

    /**
     * 发送短信验证码
     * @param request
     * @return
     * @throws Exception
     */
    public BaseResponseModel<Object> sendCode(BaseRequestModel request, int type) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        try {
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            JSONObject requestObj = request.getReqData();
            // 手机号
            String mobile = requestObj.containsKey("mobile") ? requestObj.getString("mobile") : "";
            if (StringUtils.isEmpty(mobile)) {
                response.setRepCode(RespCode.SMS_MISS_REQDATA_FAILED);
                response.setRepMsg(RespMsg.SMS_MISS_REQDATA_FAILED_MSG);
                return response;
            }
            // 验证是否有该客户
            UserEntity userEntity = loginService.queryUserEntityByUserMobile(mobile, type);
            if (null == userEntity) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.ACCOUNT_IS_NOEXIST_MSG);
                return response;
            }
            String result = slnsSendMessageService.sendCode(slnsSendConstants.getSmsMessageCode(), mobile, 0);
            if ("error".equals(result)) {
                response.setRepCode(RespCode.SMS_MESSAGE_FAILED);
                response.setRepMsg(RespMsg.SMS_MESSAGE_FAILED_MSG);
            } else {
                JSONObject obj = new JSONObject();
                obj.put("msg_id", result);
                response.setRepData(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.SMS_MESSAGE_FAILED);
            response.setRepMsg(RespMsg.SMS_MESSAGE_FAILED_MSG);
        }
        return response;
    }

    /**
     * 司机发送语音短信验证码
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/driver/sms/sendVoiceCode")
    @ResponseBody
    @LoggerManage(description = "司机语音发送短信验证码")
    public BaseResponseModel<Object> driverSendVoiceCode(@RequestBody BaseRequestModel request) throws Exception {
        return sendVoiceCode(request, 4);
    }

    /**
     * 经销商发送语音短信验证码
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/dealer/sms/sendVoiceCode")
    @ResponseBody
    @LoggerManage(description = "经销商发送语音短信验证码")
    public BaseResponseModel<Object> dealerSendVoiceCode(@RequestBody BaseRequestModel request) throws Exception {
        return sendVoiceCode(request, 1);
    }

    /**
     * PC端发送短信验证码
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/business/sms/sendVoiceCode")
    @ResponseBody
    @LoggerManage(description = "PC端发送短信验证码")
    public BaseResponseModel<Object> pcSendVoiceCode(@RequestBody BaseRequestModel request) throws Exception {
        return sendVoiceCode(request, 2);
    }

    /**
     * 发送语音短信验证码
     * @param request
     * @return
     * @throws Exception
     */
    public BaseResponseModel<Object> sendVoiceCode(BaseRequestModel request, int type) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        try {
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            JSONObject requestObj = request.getReqData();
            // 手机号
            String mobile = requestObj.containsKey("mobile") ? requestObj.getString("mobile") : "";
            // 有效期
            Integer ttl = requestObj.containsKey("ttl") ? requestObj.getInteger("ttl") : 0;
            if (StringUtils.isEmpty(mobile)) {
                response.setRepCode(RespCode.SMS_MISS_REQDATA_FAILED);
                response.setRepMsg(RespMsg.SMS_MISS_REQDATA_FAILED_MSG);
                return response;
            }
            // 验证是否有该客户
            UserEntity userEntity = loginService.queryUserEntityByUserMobile(mobile, type);
            if (null == userEntity) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.ACCOUNT_IS_NOEXIST_MSG);
                return response;
            }
            int tt = (null == ttl) ? 0 : ttl;
            String result = slnsSendMessageService.sendCode(slnsSendConstants.getSmsMessageVoice(), mobile, tt);
            if ("error".equals(result)) {
                response.setRepCode(RespCode.SMS_MESSAGE_FAILED);
                response.setRepMsg(RespMsg.SMS_MESSAGE_FAILED_MSG);
            } else {
                JSONObject obj = new JSONObject();
                obj.put("msg_id", result);
                response.setRepData(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.SMS_MESSAGE_FAILED);
            response.setRepMsg(RespMsg.SMS_MESSAGE_FAILED_MSG);
        }
        return response;
    }

    /**
     * 司机短信验证
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/driver/sms/verifyCode")
    @ResponseBody
    @LoggerManage(description = "司机短信验证")
    public BaseResponseModel<Object> driverVerifyCode(@RequestBody BaseRequestModel request) throws Exception {
        return verifyCode(request);
    }

    /**
     * 经销商短信验证
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/dealer/sms/verifyCode")
    @ResponseBody
    @LoggerManage(description = "经销商短信验证")
    public BaseResponseModel<Object> dealerVerifyCode(@RequestBody BaseRequestModel request) throws Exception {
        return verifyCode(request);
    }

    /**
     * PC端短信验证
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/business/sms/verifyCode")
    @ResponseBody
    @LoggerManage(description = "PC端短信验证")
    public BaseResponseModel<Object> pcVerifyCode(@RequestBody BaseRequestModel request) throws Exception {
        return verifyCode(request);
    }

    /**
     * 短信验证
     * @param request
     * @return
     * @throws Exception
     */
    public BaseResponseModel<Object> verifyCode(BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        try {
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            JSONObject requestObj = request.getReqData();
            // 消息ID
            String msgId = requestObj.containsKey("msgId") ? requestObj.getString("msgId") : "";
            // 验证码
            String code = requestObj.containsKey("code") ? requestObj.getString("code") : "";
            if (StringUtils.isEmpty(msgId) || StringUtils.isEmpty(code)) {
                response.setRepCode(RespCode.SMS_MISS_REQDATA_FAILED);
                response.setRepMsg(RespMsg.SMS_MISS_REQDATA_FAILED_MSG);
                return response;
            }
            Boolean result = slnsSendMessageService.verifyCode(msgId, code);
            if (result == false) {
                response.setRepCode(RespCode.SMS_MESSAGE_FAILED);
                response.setRepMsg(RespMsg.SMS_MESSAGE_FAILED_MSG);
            } else {
                JSONObject obj = new JSONObject();
                obj.put("type", result);
                DateUtil util = DateUtil.getInstance();
                obj.put("requestTime", util.formatAll(new Date(), DateUtil.TIMENOWPATTERN));
                response.setRepData(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.SMS_MESSAGE_FAILED);
            response.setRepMsg(RespMsg.SMS_MESSAGE_FAILED_MSG);
        }
        return response;
    }

}
