/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.common.constant;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 发送短信配置信息
 * @author Administrator
 * @version $Id: SendAdviceUtils.java, v 0.1 2017年9月5日 下午3:19:02 Administrator Exp $
 */
@Component
public class SlnsSendMessageConstants implements Serializable {
    /**
     * 序列化
     */
    private static final long serialVersionUID = 6907521217158588526L;

    // 短信网关token
    @Value("${SMS_TOKEN}")
    private String            smsToken;

    // 网关短信系统编码
    @Value("${SYSTEM_CODE}")
    private String            systemCode;

    // 短信验证码模板ID
    @Value("${SMS_TEMPLATE_ID}")
    private String            smsTemplateId;

    // 短信文本验证码
    @Value("${SMS_MESSAGE_CODE}")
    private String            smsMessageCode;

    // 短信语音验证码
    @Value("${SMS_MESSAGE_VOICE}")
    private String            smsMessageVoice;

    // 单个通知短信
    @Value("${SMS_MESSAGE_TEMP}")
    private String            smsMessageTemp;

    // 批量短信通知
    @Value("${SMS_MESSAGE_BATCH}")
    private String            smsMessageBatch;

    // 同步方式
    @Value("${SEND_TYPE_SYNC}")
    private String            sendTypeSync;

    // 异步方式
    @Value("${SEND_TYPE_ASYN}")
    private String            sendTypeAsyn;

    // 重试
    @Value("${IS_RETRY_YES}")
    private String            isRetryYes;

    // 不重试
    @Value("${IS_RETRY_NO}")
    private String            isRetryNo;

    // 自提確認
    @Value("${SMS_OUT_STORAGE_ID}")
    private String            smsOutStorageId;

    // 自提取消
    @Value("${SMS_CANCLE_STORAGE_ID}")
    private String            smsCancleStorageId;

    /**
     * Getter method for property <tt>smsToken</tt>.
     * @return property value of smsToken
     */
    public String getSmsToken() {
        return smsToken;
    }

    /**
     * Setter method for property <tt>smsToken</tt>.
     * @param smsToken
     *            value to be assigned to property smsToken
     */
    public void setSmsToken(String smsToken) {
        this.smsToken = smsToken;
    }

    /**
     * Getter method for property <tt>systemCode</tt>.
     * @return property value of systemCode
     */
    public String getSystemCode() {
        return systemCode;
    }

    /**
     * Setter method for property <tt>systemCode</tt>.
     * @param systemCode
     *            value to be assigned to property systemCode
     */
    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    /**
     * Getter method for property <tt>smsMessageTemp</tt>.
     * @return property value of smsMessageTemp
     */
    public String getSmsMessageTemp() {
        return smsMessageTemp;
    }

    /**
     * Setter method for property <tt>smsMessageTemp</tt>.
     * @param smsMessageTemp
     *            value to be assigned to property smsMessageTemp
     */
    public void setSmsMessageTemp(String smsMessageTemp) {
        this.smsMessageTemp = smsMessageTemp;
    }

    /**
     * Getter method for property <tt>smsMessageBatch</tt>.
     * @return property value of smsMessageBatch
     */
    public String getSmsMessageBatch() {
        return smsMessageBatch;
    }

    /**
     * Setter method for property <tt>smsMessageBatch</tt>.
     * @param smsMessageBatch
     *            value to be assigned to property smsMessageBatch
     */
    public void setSmsMessageBatch(String smsMessageBatch) {
        this.smsMessageBatch = smsMessageBatch;
    }

    /**
     * Getter method for property <tt>sendTypeSync</tt>.
     * @return property value of sendTypeSync
     */
    public String getSendTypeSync() {
        return sendTypeSync;
    }

    /**
     * Setter method for property <tt>sendTypeSync</tt>.
     * @param sendTypeSync
     *            value to be assigned to property sendTypeSync
     */
    public void setSendTypeSync(String sendTypeSync) {
        this.sendTypeSync = sendTypeSync;
    }

    /**
     * Getter method for property <tt>sendTypeAsyn</tt>.
     * @return property value of sendTypeAsyn
     */
    public String getSendTypeAsyn() {
        return sendTypeAsyn;
    }

    /**
     * Setter method for property <tt>sendTypeAsyn</tt>.
     * @param sendTypeAsyn
     *            value to be assigned to property sendTypeAsyn
     */
    public void setSendTypeAsyn(String sendTypeAsyn) {
        this.sendTypeAsyn = sendTypeAsyn;
    }

    /**
     * Getter method for property <tt>isRetryYes</tt>.
     * @return property value of isRetryYes
     */
    public String getIsRetryYes() {
        return isRetryYes;
    }

    /**
     * Setter method for property <tt>isRetryYes</tt>.
     * @param isRetryYes
     *            value to be assigned to property isRetryYes
     */
    public void setIsRetryYes(String isRetryYes) {
        this.isRetryYes = isRetryYes;
    }

    /**
     * Getter method for property <tt>isRetryNo</tt>.
     * @return property value of isRetryNo
     */
    public String getIsRetryNo() {
        return isRetryNo;
    }

    /**
     * Setter method for property <tt>isRetryNo</tt>.
     * @param isRetryNo
     *            value to be assigned to property isRetryNo
     */
    public void setIsRetryNo(String isRetryNo) {
        this.isRetryNo = isRetryNo;
    }

    /**
     * Getter method for property <tt>smsTemplateId</tt>.
     * @return property value of smsTemplateId
     */
    public String getSmsTemplateId() {
        return smsTemplateId;
    }

    /**
     * Setter method for property <tt>smsTemplateId</tt>.
     * @param smsTemplateId
     *            value to be assigned to property smsTemplateId
     */
    public void setSmsTemplateId(String smsTemplateId) {
        this.smsTemplateId = smsTemplateId;
    }

    /**
     * Getter method for property <tt>smsMessageCode</tt>.
     * @return property value of smsMessageCode
     */
    public String getSmsMessageCode() {
        return smsMessageCode;
    }

    /**
     * Setter method for property <tt>smsMessageCode</tt>.
     * @param smsMessageCode
     *            value to be assigned to property smsMessageCode
     */
    public void setSmsMessageCode(String smsMessageCode) {
        this.smsMessageCode = smsMessageCode;
    }

    /**
     * Getter method for property <tt>smsMessageVoice</tt>.
     * @return property value of smsMessageVoice
     */
    public String getSmsMessageVoice() {
        return smsMessageVoice;
    }

    /**
     * Setter method for property <tt>smsMessageVoice</tt>.
     * @param smsMessageVoice
     *            value to be assigned to property smsMessageVoice
     */
    public void setSmsMessageVoice(String smsMessageVoice) {
        this.smsMessageVoice = smsMessageVoice;
    }

    /**
     * @return the smsOutStorageId
     */
    public String getSmsOutStorageId() {
        return smsOutStorageId;
    }

    /**
     * @param smsOutStorageId
     *            the smsOutStorageId to set
     */
    public void setSmsOutStorageId(String smsOutStorageId) {
        this.smsOutStorageId = smsOutStorageId;
    }

    /**
     * @return the smsCancleStorageId
     */
    public String getSmsCancleStorageId() {
        return smsCancleStorageId;
    }

    /**
     * @param smsCancleStorageId
     *            the smsCancleStorageId to set
     */
    public void setSmsCancleStorageId(String smsCancleStorageId) {
        this.smsCancleStorageId = smsCancleStorageId;
    }

}
