/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.filter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.regex.Pattern;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.MD5Util;
import com.anji.allways.hystrix.HystrixWrappedUserAuthValidateServiceClient;
import com.anji.allways.hystrix.HystrixWrappedUserTokenValidateServiceClient;
import com.anji.allways.model.ZuulRequestModel;
import com.anji.allways.model.ZuulResponseModel;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * <pre>
 * 访问处理过滤器
 * </pre>
 * @author WangGuangYuan
 * @version $Id: AccessFilter.java, v 0.1 2017年5月5日 上午8:16:21 Administrator Exp $
 */
@RefreshScope
public class AccessFilter extends ZuulFilter {
    private static Logger                                log = LoggerFactory.getLogger(AccessFilter.class);

    @Autowired
    private HystrixWrappedUserTokenValidateServiceClient userTokenValidateServiceClient;

    @Autowired
    private HystrixWrappedUserAuthValidateServiceClient  userAuthValidateServiceClient;

    @Value("${timestamp.validate.limit:30}")
    private String                                       validateLimit;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("收到来自IP为 %s 的请求%s request to %s", getIpAddr(request), request.getMethod(), request.getRequestURL().toString()));

        // 是否跳过验证
        log.info("url is :" + request.getRequestURL().toString());
        if (skipAllFilters(request.getRequestURL().toString())) {
            log.info("跳过所有验证");
            return null;
        }
        log.info("开始验证");

        String jsonStr = getData(request);
        JSONObject jsonData = JSONObject.parseObject(jsonStr);
        ZuulRequestModel requestModel = JSON.parseObject(jsonData.toString(), ZuulRequestModel.class);
        requestModel.setJsonStr(jsonStr);
        requestModel.setUrl(request.getRequestURL().toString());
        requestModel.setAuthUrl(request.getServletPath());

        // 校验基本参数
        if (!requestModel.isVaildateRequest()) {
            ZuulResponseModel<String> responseModel = new ZuulResponseModel<String>();
            log.warn("请求参数格式不正确");
            responseModel.setRepCode(RespCode.ZUUL_PARM_ERROR);
            responseModel.setRepMsg(RespMsg.ZUUL_PARM_ERROR_MSG);
            ctx = getContextForError(ctx, responseModel);
            return null;
        }

        // 验证时间戳
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        Timestamp requestTime = new Timestamp(Long.parseLong(requestModel.getTime()));
        log.info("时间戳校验间隔:" + Integer.parseInt(validateLimit));
        if (Math.abs(nowTime.getTime() / 1000 - requestTime.getTime()) > Integer.parseInt(validateLimit)) {
            ZuulResponseModel<String> responseModel = new ZuulResponseModel<String>();
            log.warn("时间戳校验不正确");
            responseModel.setRepCode(RespCode.ZUUL_TIMESTAMP_ERROR);
            responseModel.setRepMsg(RespMsg.ZUUL_TIMESTAMP_ERROR_MSG);
            ctx = getContextForError(ctx, responseModel);
            return null;
        }
        log.debug("验证时间戳通过");

        // 验证签名
        // if (!validateSign(requestModel)) {
        // ZuulResponseModel<String> responseModel = new ZuulResponseModel<String>();
        // log.warn("签名不正确");
        // responseModel.setRepCode(RespCode.ZUUL_SIGN_ERROR);
        // responseModel.setRepMsg(RespMsg.ZUUL_SIGN_ERROR_MSG);
        // ctx = getContextForError(ctx, responseModel);
        // return null;
        // }
        // log.debug("验签通过");

        // 校验token
        if (!validateToken(requestModel)) {
            ZuulResponseModel<String> responseModel = new ZuulResponseModel<String>();
            log.warn("token校验失败");
            responseModel.setRepCode(RespCode.ZUUL_TOKEN_ERROR);
            responseModel.setRepMsg("");
            ctx = getContextForError(ctx, responseModel);
            return null;
        }
        log.debug("验证token通过");

        // 需要根据URL做权限校验,调用相关服务
        // if (!validateAuth(requestModel)) {
        // ZuulResponseModel<String> responseModel = new ZuulResponseModel<String>();
        // log.warn("权限校验失败");
        // responseModel.setRepCode(RespCode.ZUUL_AUTH_ERROR);
        // responseModel.setRepMsg(RespMsg.ZUUL_AUTH_ERROR_MSG);
        // ctx = getContextForError(ctx, responseModel);
        // return null;
        // }
        // log.debug("验证权限通过");
        return null;
    }

    private String getData(HttpServletRequest req) {
        String result = null;
        try {
            // 包装request的输入流
            BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) req.getInputStream(), "utf-8"));
            // 缓冲字符
            StringBuffer sb = new StringBuffer("");
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close(); // 关闭缓冲流
            result = sb.toString(); // 转换成字符
            log.info("获取data参数 " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private boolean validateSign(ZuulRequestModel requestModel) {
        boolean isValid = false;
        String reqDatazStr = "";
        if (null != requestModel.getReqData() || !"".equals(requestModel.getReqData().toJSONString())) {
            reqDatazStr = requestModel.getReqData().toJSONString();
        } else {
            reqDatazStr = "{}";
        }
        String baseSignMsg = "reqData=" + reqDatazStr + "&time=" + requestModel.getTime() + "&token=" + requestModel.getToken();
        if (MD5Util.getInstance().encrypt(baseSignMsg).equalsIgnoreCase(requestModel.getSign())) {
            isValid = true;
        }
        return isValid;
    }

    private boolean validateToken(ZuulRequestModel requestModel) {
        boolean isValid = false;
        if (Pattern.matches(".*/noauth/.*|.*/login/.*|.*storagePlan/extstorage", requestModel.getUrl())) { // 无需token校验
            return true;
        }

        if (null == requestModel.getToken() || "".equals(requestModel.getToken().trim())) {
            log.warn("token is null");
            isValid = false;
        }

        // 需要做token校验,判断token,调用SYS_USER相关服务
        isValid = userTokenValidateServiceClient.validate(requestModel.getToken());
        log.warn("token:" + requestModel.getToken() + "----- isValid :" + isValid);
        return isValid;
    }

    private boolean validateAuth(ZuulRequestModel requestModel) {
        boolean isValid = false;
        if (Pattern.matches(".*/noauth/.*", requestModel.getUrl())) { // 无需权限校验
            return true;
        }
        // 根据AUTH URL查询用户权限,调用SYS_AUTH相关服务
        String userId = requestModel.getUserId();
        String authUrl = requestModel.getAuthUrl();
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(authUrl)) {
            return isValid;
        }

        isValid = userAuthValidateServiceClient.validateByUrl(userId, authUrl);
        return isValid;
    }

    private String getReqDataStr(String reqStr) {
        return reqStr.substring(reqStr.indexOf("\"reqData\":") + 10, reqStr.indexOf(",\"sign\""));
    }

    private RequestContext getContextForError(RequestContext ctx, ZuulResponseModel<String> responseModel) {
        RequestContext requestContext = ctx;
        ctx.setResponseBody(responseModel.toJsonString());
        ctx.setSendZuulResponse(false);
        ctx.getResponse().setContentType("text/html;charset=UTF-8");
        ctx.getResponse().setContentType(String.valueOf(MediaType.APPLICATION_JSON));
        return requestContext;
    }

    private boolean skipAllFilters(String requestUrl) {
        boolean isSkip = true;
        if (Pattern.matches(".*/statistics/.*|.*/exportInfoByExcel|.*/importTemplateDownload|.*/exportInfoByExcel"
                            + "|.*/addInnstorageByExcel|.*/exportInfoByExcel|.*/sms/.*|.*/user/.*", requestUrl)) {
            isSkip = true;
        }
        return isSkip;
    }
    // public static void main(String[] args) {
    // String str =
    // "{\"token\":\"142_62955a3d345f4c7f950bc053474ff08f\",\"time\":1499393719455,\"reqData\":{},\"sign\":\"3996C430FCE22A3AE2574F807A1A022E\"}";
    // System.out.println(str.substring(str.indexOf("\"reqData\":") + 10,
    // str.indexOf(",\"sign\"")));
    // }
}
