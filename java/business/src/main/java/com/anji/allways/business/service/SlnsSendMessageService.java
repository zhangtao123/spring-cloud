/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * <pre>
 * 发送短信Service
 * </pre>
 * @author Administrator
 * @version $Id: SlnsSendMessageService.java, v 0.1 2017年9月27日 下午12:54:59 Administrator Exp $
 */
public interface SlnsSendMessageService {
    /**
     * <pre>
     * 发送短信验证码
     * </pre>
     * @param messageType
     * @param mobile
     * @param ttl
     * @return
     */
    String sendCode(String messageType, String mobile, int ttl);

    /**
     * <pre>
     * 短信验证
     * </pre>
     * @param msgId
     * @param code
     * @return
     */
    Boolean verifyCode(String msgId, String code);

    /**
     * <pre>
     * 发送单个模板短信
     * </pre>
     * @return
     */
    String sendSingleMessage(int tempId, int sendType, int isRetry, String mobile, JSONObject tempPara);

    /**
     * <pre>
     * 发送批量模板短信
     * </pre>
     * @param tempId
     * @param sendType
     * @param isRetry
     * @param tempPara
     * @return
     */
    String sendMoreMessage(int tempId, int sendType, int isRetry, JSONArray tempPara);
}
