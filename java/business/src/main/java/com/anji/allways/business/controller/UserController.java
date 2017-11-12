/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anji.allways.base.aop.LoggerManage;
import com.anji.allways.base.entity.BaseRequestModel;
import com.anji.allways.base.entity.BaseResponseModel;
//import com.anji.allways.business.common.UserCenter;
import com.anji.allways.business.common.constant.SlnsConstants;
//import com.anji.allways.business.dto.UserDTO;
import com.anji.allways.business.entity.CustomerEntity;
import com.anji.allways.business.entity.UserEntity;
import com.anji.allways.business.service.CustomerService;
import com.anji.allways.business.service.SlnsSendMessageService;
import com.anji.allways.business.service.TokenService;
import com.anji.allways.business.service.TruckDriverService;
import com.anji.allways.business.service.UserService;
import com.anji.allways.business.vo.TruckDriverVO;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.DateUtil;
import com.anji.allways.common.util.HttpTool;
import com.anji.allways.common.util.MD5Util;
import com.anji.allways.common.util.StringUtil;

/**
 * @author wangyanjun
 * @version $Id: LoginController.java, v 0.1 2017年8月22日 下午3:55:23 wangyanjun Exp $
 */
@RestController
@RequestMapping("/")
public class UserController {
    private Logger                 logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService            loginService;

    @Autowired
    private TokenService           tokenService;

    @Autowired
    private TruckDriverService     truckDriverService;

    @Autowired
    private CustomerService        customerService;

    @Autowired
    private SlnsSendMessageService slnsSendMessageService;

    @RequestMapping(value = "/user/login/token/validate")
    public boolean validateToken(@RequestParam("token") String token) {
        logger.error("----------------------------valid received token is:" + token + "------");
        if (null != token && !StringUtil.isEmpty(token, true)) {
            String[] tokenInfo = token.split("_");
            if (1 == tokenInfo.length) {
                token = tokenInfo[0];
            } else if (2 == tokenInfo.length) {
                token = tokenInfo[1];
            } else {
                return false;
            }
        } else {
            return false;
        }
        logger.error("----------------------------valid real token is:" + token + "------");
        boolean result = true;
        String userID = tokenService.getUserId(token);
        logger.error("----------------------------valid keep userID is:" + userID + "------");
        if (null != userID) {
            result = true;
        }
        return result;
    }

    @RequestMapping("/business/user/login/name")
    @ResponseBody
    @LoggerManage(description = "仓库用户名登录")
    public BaseResponseModel<Object> loginForWarehouse(@RequestBody BaseRequestModel request) {
        return login(request, 2);
    }

    @RequestMapping("/driver/user/login/name")
    @ResponseBody
    @LoggerManage(description = "司机用户名登录")
    public BaseResponseModel<Object> loginForDriver(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        String resultMessage = null;
        String userName = request.getReqData().getString("accountName");
        String password = request.getReqData().getString("password");
        if (StringUtil.isEmpty(userName, true) || StringUtil.isEmpty(password, true)) {
            response.setRepCode(RespCode.LOGIN_FAILED);
            response.setRepMsg(RespMsg.LOGIN_NULL_USER_PASSWORD_MSG);
        } else {
            String url = SlnsConstants.PERMISSION_AUTH_URL;
            UserEntity userEntity = loginService.queryUserEntityByUserName(userName, 4);
            if (null == userEntity) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.ACCOUNT_IS_NOEXIST_MSG);
            } else {
                if (userEntity.getLocked() == 1) {
                    response.setRepCode(RespCode.LOGIN_FAILED);
                    response.setRepMsg(RespMsg.ACCOUNT_IS_LOCKED_MSG);
                } else {
                    long timeSec = System.currentTimeMillis() / 1000;
                    String originSign = "reqData={\"isMobile\":\"0\",\"operName\":\"" + userName + "\",\"operPassword\":\"" + password + "\"}" + "&time=" + timeSec + "&token=";
                    StringBuffer sb = new StringBuffer();
                    sb.append("{\"userId\":\"" + userEntity.getPermissionUserId())
                        .append("\",\"token\":\"\",\"sign\":\"" + MD5Util.getInstance().encrypt(originSign) + "\",\"time\":\"" + timeSec + "\",\"reqData\":{\"operName\":\"").append(userName)
                        .append("\",\"operPassword\":\"").append(password).append("\",\"isMobile\":\"").append(0).append("\"}}");
                    String result = HttpTool.getInstance().post(url, sb.toString(), "application/json", null, request.getUserId().toString());
                    JSONObject object = JSONObject.parseObject(result);
                    if (null != object) {
                        if (object.getString("repCode").equals("0000")) {
                            JSONObject repData = JSONObject.parseObject(object.getString("repData"));
                            if (null != repData) {
                                resultMessage = loginService.loginByUser(userName);
                                if (resultMessage.contains("成功")) {
                                    String token = tokenService.generateToken();
                                    tokenService.putToken(token, userEntity.getId().toString());

//                                    UserDTO userDTO = new UserDTO(repData);
//                                    userDTO.setAccountName(userEntity.getAccountName());
//                                    userDTO.setAccountType(userEntity.getAccountType());
//                                    userDTO.setAddress(userEntity.getAddress());
//                                    userDTO.setCreateName(userEntity.getCreateName());
//                                    userDTO.setCredentialsSalt(userEntity.getCredentialsSalt());
//                                    userDTO.setCustomer(userEntity.getCustomer());
//                                    userDTO.setCustomerId(userEntity.getCustomerId());
//                                    userDTO.setDescription(userEntity.getDescription());
//                                    userDTO.setId(userEntity.getId());
//                                    userDTO.setIsValid(userEntity.getIsValid());
//                                    userDTO.setLocked(userEntity.getLocked());
//                                    userDTO.setName(userEntity.getName());
//                                    userDTO.setPassword(userEntity.getPassword());
//                                    userDTO.setPermissionUserId(userEntity.getPermissionUserId());
//                                    userDTO.setTelephone(userEntity.getTelephone());
//                                    userDTO.setType(userEntity.getType());
//                                    userDTO.setUpdateTime(userEntity.getUpdateTime());
//                                    userDTO.setUpdateUser(userEntity.getUpdateUser());
//                                    UserCenter.getInstance().putUserDTO(token, userDTO);

                                    JSONObject retRepData = new JSONObject();
                                    retRepData.put("userId", userEntity.getId());
                                    retRepData.put("token", token);

                                    TruckDriverVO truckDriverVO = truckDriverService.selectByPrimaryKey(userEntity.getCustomerId());
                                    if (null != truckDriverVO) {
                                        retRepData.put("truckNumber", truckDriverVO.getTruckNumber());
                                        retRepData.put("driverName", truckDriverVO.getName());
                                        retRepData.put("driverMobile", truckDriverVO.getMobile());
                                        response.setRepCode(RespCode.SUCCESS);
                                        response.setRepData(retRepData);
                                        response.setRepMsg(RespMsg.LOGIN_SUCCEED_MSG);
                                    } else {
                                        response.setRepCode(RespCode.LOGIN_FAILED);
                                        response.setRepMsg(RespMsg.LOGIN_GET_DRIVER_FAILED_MSG);
                                        response.setRepMsg(resultMessage);
                                    }

                                }
                            }
                        } else {
                            response.setRepCode(object.getString("repCode"));
                            response.setRepMsg(object.getString("repMsg"));
                        }
                    }
                    if (!RespCode.SUCCESS.equals(response.getRepCode())) {
                        response.setRepCode(RespCode.LOGIN_FAILED);
                        if (null == response.getRepMsg()) {
                            response.setRepMsg(resultMessage);
                        }
                    }
                }
            }
        }

        return response;

    }

    @RequestMapping("/dealer/user/login/name")
    @ResponseBody
    @LoggerManage(description = "经销商用户名登录")
    public BaseResponseModel<Object> loginForDealer(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        String resultMessage = null;
        String userName = request.getReqData().getString("accountName");
        String password = request.getReqData().getString("password");
        if (StringUtil.isEmpty(userName, true) || StringUtil.isEmpty(password, true)) {
            response.setRepCode(RespCode.LOGIN_FAILED);
            response.setRepMsg(RespMsg.LOGIN_NULL_USER_PASSWORD_MSG);
        } else {
            String url = SlnsConstants.PERMISSION_AUTH_URL;
            UserEntity userEntity = loginService.queryUserEntityByUserName(userName, 1);
            if (null == userEntity) {
                userEntity = loginService.queryUserEntityByUserName(userName, 7);
                if (null == userEntity) {
                    response.setRepCode(RespCode.LOGIN_FAILED);
                    response.setRepMsg(RespMsg.ACCOUNT_IS_NOEXIST_MSG);
                }
            }
            if (null != userEntity) {
                if (userEntity.getLocked() == 1) {
                    response.setRepCode(RespCode.LOGIN_FAILED);
                    response.setRepMsg(RespMsg.ACCOUNT_IS_LOCKED_MSG);
                } else {
                    long timeSec = System.currentTimeMillis() / 1000;
                    String originSign = "reqData={\"isMobile\":\"0\",\"operName\":\"" + userName + "\",\"operPassword\":\"" + password + "\"}" + "&time=" + timeSec + "&token=";
                    StringBuffer sb = new StringBuffer();
                    sb.append("{\"userId\":\"" + userEntity.getPermissionUserId())
                        .append("\",\"token\":\"\",\"sign\":\"" + MD5Util.getInstance().encrypt(originSign) + "\",\"time\":\"" + timeSec + "\",\"reqData\":{\"operName\":\"").append(userName)
                        .append("\",\"operPassword\":\"").append(password).append("\",\"isMobile\":\"").append(0).append("\"}}");
                    String result = HttpTool.getInstance().post(url, sb.toString(), "application/json", null, request.getUserId().toString());
                    JSONObject object = JSONObject.parseObject(result);
                    if (null != object) {
                        if (object.getString("repCode").equals("0000")) {
                            JSONObject repData = JSONObject.parseObject(object.getString("repData"));
                            if (null != repData) {
                                resultMessage = loginService.loginByUser(userName);
                                if (resultMessage.contains("成功")) {
                                    String token = tokenService.generateToken();
                                    tokenService.putToken(token, userEntity.getId().toString());

//                                    UserDTO userDTO = new UserDTO(repData);
//                                    userDTO.setAccountName(userEntity.getAccountName());
//                                    userDTO.setAccountType(userEntity.getAccountType());
//                                    userDTO.setAddress(userEntity.getAddress());
//                                    userDTO.setCreateName(userEntity.getCreateName());
//                                    userDTO.setCredentialsSalt(userEntity.getCredentialsSalt());
//                                    userDTO.setCustomer(userEntity.getCustomer());
//                                    userDTO.setCustomerId(userEntity.getCustomerId());
//                                    userDTO.setDescription(userEntity.getDescription());
//                                    userDTO.setId(userEntity.getId());
//                                    userDTO.setIsValid(userEntity.getIsValid());
//                                    userDTO.setLocked(userEntity.getLocked());
//                                    userDTO.setName(userEntity.getName());
//                                    userDTO.setPassword(userEntity.getPassword());
//                                    userDTO.setPermissionUserId(userEntity.getPermissionUserId());
//                                    userDTO.setTelephone(userEntity.getTelephone());
//                                    userDTO.setType(userEntity.getType());
//                                    userDTO.setUpdateTime(userEntity.getUpdateTime());
//                                    userDTO.setUpdateUser(userEntity.getUpdateUser());
//                                    UserCenter.getInstance().putUserDTO(token, userDTO);

                                    JSONObject retRepData = new JSONObject();
                                    retRepData.put("userId", userEntity.getId());
                                    retRepData.put("token", token);
                                    if (7 == userEntity.getType()) {
                                        retRepData.put("isGroup", 2);
                                    } else {
                                        retRepData.put("isGroup", 1);
                                    }

                                    CustomerEntity customerEntity = customerService.queryCustomerById(userEntity.getCustomerId());
                                    if (null != customerEntity) {
                                        retRepData.put("group", customerEntity.getGroup());
                                        retRepData.put("company", customerEntity.getName());
                                        retRepData.put("name", userEntity.getName());
                                        retRepData.put("mobile", userEntity.getTelephone());

                                        JSONArray permissions = repData.getJSONArray("permissions");
                                        JSONObject permissionObj = null;
                                        Iterator<Object> it = permissions.iterator();
                                        while (it.hasNext()) {
                                            JSONObject ob = (JSONObject) it.next();
                                            if (SlnsConstants.SYSTEM_NAME.equals(ob.getString("name"))) {
                                                permissionObj = ob;
                                                break;
                                            }
                                        }
                                        boolean getPermissionSucc = false;
                                        if (null != permissionObj) {
                                            getPermissionSucc = true;
                                            JSONArray permissions1st = permissionObj.getJSONArray("child");
                                            if (null != permissions1st) {
                                                JSONObject dataPermissionObj = null;
                                                Iterator<Object> it1st = permissions1st.iterator();
                                                while (it1st.hasNext()) {
                                                    JSONObject ob = (JSONObject) it1st.next();
                                                    if (SlnsConstants.PERMISSION_DATA.equals(ob.getString("name"))) {
                                                        dataPermissionObj = ob;
                                                        break;
                                                    }
                                                }
                                                // 数据权限解析
                                                retRepData = this.dataProcess(dataPermissionObj, retRepData);
                                            }
                                        }
                                        if (!getPermissionSucc) {
                                            resultMessage = "查询经销商权限信息失败。";
                                            response.setRepCode(RespCode.LOGIN_FAILED);
                                            response.setRepMsg(resultMessage);
                                        }
                                        response.setRepCode(RespCode.SUCCESS);
                                        response.setRepData(retRepData);
                                        response.setRepMsg(RespMsg.LOGIN_SUCCEED_MSG);
                                    } else {
                                        resultMessage = "查询经销商信息失败。";
                                        response.setRepCode(RespCode.LOGIN_FAILED);
                                        response.setRepMsg(resultMessage);
                                    }

                                }
                            }
                        } else {
                            response.setRepCode(object.getString("repCode"));
                            response.setRepMsg(object.getString("repMsg"));
                        }
                    }
                    if (!RespCode.SUCCESS.equals(response.getRepCode())) {
                        response.setRepCode(RespCode.LOGIN_FAILED);
                        if (null == response.getRepMsg()) {
                            response.setRepMsg(resultMessage);
                        }
                    }
                }
            }
        }

        return response;
    }

    @RequestMapping("/driver/user/resetpassword")
    @ResponseBody
    @LoggerManage(description = "司机重设密码")
    public BaseResponseModel<Object> resetPasswordForDriver(@RequestBody BaseRequestModel request) {
        return resetPassword(request, 4);
    }

    @RequestMapping("/dealer/user/resetpassword")
    @ResponseBody
    @LoggerManage(description = "经销商用户重设密码")
    public BaseResponseModel<Object> resetPasswordForDealer(@RequestBody BaseRequestModel request) {
        return resetPassword(request, 1);
    }

    @RequestMapping("/business/user/resetpassword")
    @ResponseBody
    @LoggerManage(description = "仓库用户重设密码")
    public BaseResponseModel<Object> resetPasswordForWarehouse(@RequestBody BaseRequestModel request) {
        return resetPassword(request, 2);
    }

    private BaseResponseModel<Object> resetPassword(@RequestBody BaseRequestModel request, int type) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        boolean isReturn = false;
        String userId = request.getUserId();
        String permissionUserId = "";
        if (!isReturn && StringUtil.isEmpty(userId, true)) {
            response.setRepCode(RespCode.RESET_PASSWORD_FAILED);
            response.setRepMsg(RespMsg.RESET_USERID_NULL_MSG);
            isReturn = true;
        }
        try {
            if (!isReturn) {
                Integer userIdInteger = Integer.valueOf(userId);
                UserEntity userEntity = loginService.queryUserEntityByUserId(userIdInteger);
                if (null == userEntity) {
                    response.setRepCode(RespCode.RESET_PASSWORD_FAILED);
                    response.setRepMsg(RespMsg.ACCOUNT_IS_NOEXIST_MSG);
                    isReturn = true;
                } else {
                    if (userEntity.getLocked() == 1) {
                        response.setRepCode(RespCode.RESET_PASSWORD_FAILED);
                        response.setRepMsg(RespMsg.ACCOUNT_IS_LOCKED_MSG);
                        isReturn = true;
                    }

                    if (!isReturn && userEntity.getType() != type) {
                        response.setRepCode(RespCode.RESET_PASSWORD_FAILED);
                        response.setRepMsg(RespMsg.ACCOUNT_IS_NOEXIST_MSG);
                        isReturn = true;
                    }

                    if (!isReturn) {
                        if (null != userEntity.getPermissionUserId()) {
                            permissionUserId = userEntity.getPermissionUserId().toString();
                        } else {
                            response.setRepCode(RespCode.RESET_PASSWORD_FAILED);
                            response.setRepMsg(RespMsg.PERMISSION_ID_IS_NOEXIST_MSG);
                            isReturn = true;
                        }
                    }
                }

            }
        } catch (NumberFormatException e) {
            response.setRepCode(RespCode.RESET_PASSWORD_FAILED);
            response.setRepMsg(RespMsg.RESET_ERROR_USERID_MSG);
            isReturn = true;
        }

        if (!isReturn) {
            String oldPassword = request.getReqData().getString("oldPassword");
            String newPassword = request.getReqData().getString("newPassword");
            if (StringUtil.isEmpty(oldPassword, true) || StringUtil.isEmpty(newPassword, true)) {
                response.setRepCode(RespCode.RESET_PASSWORD_FAILED);
                response.setRepMsg(RespMsg.RESET_NULL_PASSWORD_MSG);
                isReturn = true;
            }
            if (!isReturn && oldPassword.equals(newPassword)) {
                response.setRepCode(RespCode.RESET_PASSWORD_FAILED);
                response.setRepMsg(RespMsg.RESET_SAME_PASSWORD_MSG);
                isReturn = true;
            }
            if (!isReturn) {
                String url = SlnsConstants.RESET_PASSWORD_URL;
                StringBuffer sb = new StringBuffer();
                sb.append("{\"userId\":\"" + permissionUserId).append("\",\"token\":\"\",\"sign\":\"\",\"time\":\"\",\"reqData\":{\"oldPassword\":\"").append(oldPassword)
                    .append("\",\"newPassword\":\"").append(newPassword).append("\",\"isReset\":\"").append(0).append("\"}}");
                String result = HttpTool.getInstance().post(url, sb.toString(), "application/json", null, request.getUserId().toString());
                JSONObject object = JSONObject.parseObject(result);
                if (null != object) {
                    if ("0000".equals(object.getString("repCode"))) {
                        response.setRepCode(RespCode.SUCCESS);
                        response.setRepMsg(RespMsg.RESET_PASSWORD_SUCCEED_MSG);
                    } else {
                        // 修改密码失败
                        response.setRepCode(RespCode.RESET_PASSWORD_FAILED);
                        response.setRepMsg(object.getString("repMsg"));
                    }
                }
                isReturn = true;
            }
        }

        return response;
    }

    @RequestMapping("/driver/user/updatePassword")
    @ResponseBody
    @LoggerManage(description = "司机重置密码")
    public BaseResponseModel<Object> updatePasswordForDriver(@RequestBody BaseRequestModel request) {
        return updatePassword(request, 4);
    }

    @RequestMapping("/dealer/user/updatePassword")
    @ResponseBody
    @LoggerManage(description = "经销商用户重置密码")
    public BaseResponseModel<Object> updatePasswordForDealer(@RequestBody BaseRequestModel request) {
        return updatePassword(request, 1);
    }

    @RequestMapping("/business/user/updatePassword")
    @ResponseBody
    @LoggerManage(description = "仓库用户重置密码")
    public BaseResponseModel<Object> updatePasswordForWarehouse(@RequestBody BaseRequestModel request) {
        return updatePassword(request, 2);
    }

    @SuppressWarnings("static-access")
    private BaseResponseModel<Object> updatePassword(@RequestBody BaseRequestModel request, int type) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        boolean isReturn = false;
        String requestTime = request.getReqData().getString("requestTime");
        String mobile = request.getReqData().getString("mobile");
        try {
            // 验证是否过期
            if (StringUtils.isEmpty(requestTime) || StringUtils.isEmpty(mobile)) {
                response.setRepCode(RespCode.RESET_PASSWORD_FAILED);
                response.setRepMsg(RespMsg.RESET_USERID_NULL_MSG);
                isReturn = true;
            }
            DateUtil util = DateUtil.getInstance();
            int len = util.getPeriodSeconds(util.getDateByString(requestTime, DateUtil.TIMENOWPATTERN), new Date());
            if (len > (60 * 15)) {
                response.setRepCode(RespCode.AUTH_CODE_OVERDUE);
                response.setRepMsg(RespMsg.AUTH_CODE_OVERDUE_MSG);
                isReturn = true;
            }
        } catch (Exception e1) {
            response.setRepCode(RespCode.RESET_PASSWORD_FAILED);
            response.setRepMsg(RespMsg.RESET_USERID_NULL_MSG);
            isReturn = true;
        }
        String permissionUserId = "";
        try {
            if (!isReturn) {
                UserEntity userEntity = loginService.queryUserEntityByUserMobile(mobile, type);
                if (null == userEntity) {
                    response.setRepCode(RespCode.RESET_PASSWORD_FAILED);
                    response.setRepMsg(RespMsg.ACCOUNT_IS_NOEXIST_MSG);
                    isReturn = true;
                } else {
                    if (userEntity.getLocked() == 1) {
                        response.setRepCode(RespCode.RESET_PASSWORD_FAILED);
                        response.setRepMsg(RespMsg.ACCOUNT_IS_LOCKED_MSG);
                        isReturn = true;
                    }
                    if (!isReturn) {
                        if (null != userEntity.getPermissionUserId()) {
                            permissionUserId = userEntity.getPermissionUserId().toString();
                        } else {
                            response.setRepCode(RespCode.RESET_PASSWORD_FAILED);
                            response.setRepMsg(RespMsg.PERMISSION_ID_IS_NOEXIST_MSG);
                            isReturn = true;
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            response.setRepCode(RespCode.RESET_PASSWORD_FAILED);
            response.setRepMsg(RespMsg.RESET_ERROR_USERID_MSG);
            isReturn = true;
        }
        if (!isReturn) {
            String newPassword = request.getReqData().getString("newPassword");
            if (StringUtil.isEmpty(newPassword, true)) {
                response.setRepCode(RespCode.RESET_PASSWORD_FAILED);
                response.setRepMsg(RespMsg.RESET_NULL_PASSWORD_MSG);
                isReturn = true;
            }
            if (!isReturn) {
                String url = SlnsConstants.RESET_PASSWORD_URL;
                StringBuffer sb = new StringBuffer();
                sb.append("{\"userId\":\"" + permissionUserId).append("\",\"token\":\"\",\"sign\":\"\",\"time\":\"\",\"reqData\":{\"oldPassword\":\"").append("").append("\",\"newPassword\":\"")
                    .append(newPassword).append("\",\"isReset\":\"").append(1).append("\"}}");
                String result = HttpTool.getInstance().post(url, sb.toString(), "application/json", null, request.getUserId().toString());
                JSONObject object = JSONObject.parseObject(result);
                if (null != object) {
                    if ("0000".equals(object.getString("repCode"))) {
                        response.setRepCode(RespCode.SUCCESS);
                        response.setRepMsg(RespMsg.RESET_PASSWORD_SUCCEED_MSG);
                    } else {
                        // 修改密码失败
                        response.setRepCode(RespCode.RESET_PASSWORD_FAILED);
                        response.setRepMsg(object.getString("repMsg"));
                    }
                }
                isReturn = true;
            }
        }
        return response;
    }

    private BaseResponseModel<Object> login(@RequestBody BaseRequestModel request, int type) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        String resultMessage = null;
        String userName = request.getReqData().getString("accountName");
        String password = request.getReqData().getString("password");
        if (StringUtil.isEmpty(userName, true) || StringUtil.isEmpty(password, true)) {
            response.setRepCode(RespCode.LOGIN_FAILED);
            response.setRepMsg(RespMsg.LOGIN_NULL_USER_PASSWORD_MSG);
        } else {
            String url = SlnsConstants.PERMISSION_AUTH_URL;
            UserEntity userEntity = loginService.queryUserEntityByUserName(userName, type);
            if (null == userEntity) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.ACCOUNT_IS_NOEXIST_MSG);
            } else {
                if (userEntity.getLocked() == 1) {
                    response.setRepCode(RespCode.LOGIN_FAILED);
                    response.setRepMsg(RespMsg.ACCOUNT_IS_LOCKED_MSG);
                } else {
                    long timeSec = System.currentTimeMillis() / 1000;
                    String originSign = "reqData={\"isMobile\":\"0\",\"operName\":\"" + userName + "\",\"operPassword\":\"" + password + "\"}" + "&time=" + timeSec + "&token=";
                    StringBuffer sb = new StringBuffer();
                    sb.append("{\"userId\":\"" + userEntity.getPermissionUserId())
                        .append("\",\"token\":\"\",\"sign\":\"" + MD5Util.getInstance().encrypt(originSign) + "\",\"time\":\"" + timeSec + "\",\"reqData\":{\"operName\":\"").append(userName)
                        .append("\",\"operPassword\":\"").append(password).append("\",\"isMobile\":\"").append(0).append("\"}}");
                    String result = HttpTool.getInstance().post(url, sb.toString(), "application/json", null, request.getUserId().toString());
                    JSONObject object = JSONObject.parseObject(result);
                    if (object != null) {
                        if ("0000".equals(object.getString("repCode"))) {

                            JSONObject repData = JSONObject.parseObject(object.getString("repData"));
                            if (null != repData) {
                                resultMessage = loginService.loginByUser(userName);
                                if (resultMessage.contains("成功")) {
                                    String token = tokenService.generateToken();
                                    tokenService.putToken(token, userEntity.getId().toString());
                                    repData.put("userId", userEntity.getId());
                                    repData.put("permissionUserId", userEntity.getPermissionUserId());
                                    repData.put("token", userEntity.getId() + "_" + token);

//                                    UserDTO userDTO = new UserDTO(repData);
//                                    userDTO.setAccountName(userEntity.getAccountName());
//                                    userDTO.setAccountType(userEntity.getAccountType());
//                                    userDTO.setAddress(userEntity.getAddress());
//                                    userDTO.setCreateName(userEntity.getCreateName());
//                                    userDTO.setCredentialsSalt(userEntity.getCredentialsSalt());
//                                    userDTO.setCustomer(userEntity.getCustomer());
//                                    userDTO.setCustomerId(userEntity.getCustomerId());
//                                    userDTO.setDescription(userEntity.getDescription());
//                                    userDTO.setId(userEntity.getId());
//                                    userDTO.setIsValid(userEntity.getIsValid());
//                                    userDTO.setLocked(userEntity.getLocked());
//                                    userDTO.setName(userEntity.getName());
//                                    userDTO.setPassword(userEntity.getPassword());
//                                    userDTO.setPermissionUserId(userEntity.getPermissionUserId());
//                                    userDTO.setTelephone(userEntity.getTelephone());
//                                    userDTO.setType(userEntity.getType());
//                                    userDTO.setUpdateTime(userEntity.getUpdateTime());
//                                    userDTO.setUpdateUser(userEntity.getUpdateUser());
//                                    UserCenter.getInstance().putUserDTO(token, userDTO);

                                    response.setRepCode(RespCode.SUCCESS);
                                    response.setRepData(repData);
                                    response.setRepMsg(RespMsg.LOGIN_SUCCEED_MSG);
                                }
                            }
                        } else {
                            response.setRepCode(object.getString("repCode"));
                            response.setRepMsg(object.getString("repMsg"));
                        }
                    }
                    if (!RespCode.SUCCESS.equals(response.getRepCode())) {
                        response.setRepCode(RespCode.LOGIN_FAILED);
                        if (null == response.getRepMsg()) {
                            response.setRepMsg(resultMessage);
                        }
                    }
                }
            }
        }

        return response;

    }

    /**
     * 仓库用户手机号登录
     * @param request
     * @return
     */
    @RequestMapping("/business/user/login/mobile")
    @ResponseBody
    @LoggerManage(description = "仓库用户手机号登录")
    public BaseResponseModel<Object> loginForWarehouseByMobile(@RequestBody BaseRequestModel request) {
        return loginByMobile(request);
    }

    /**
     * 通过手机登陆
     * @param request
     * @param type
     * @return
     */
    private BaseResponseModel<Object> loginByMobile(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        String resultMessage = null;
        String mobile = request.getReqData().getString("mobile");
        String code = request.getReqData().getString("code");
        String msgId = request.getReqData().getString("msgId");
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(code) || StringUtils.isEmpty(msgId)) {
            response.setRepCode(RespCode.LOGIN_FAILED);
            response.setRepMsg(RespMsg.LOGIN_NULL_USER_PASSWORD_MSG);
        } else {
            // 验证验证码
            Boolean verifyType = slnsSendMessageService.verifyCode(msgId, code);
            if (!verifyType) {
                response.setRepCode(RespCode.AUTH_CODE_FAILED);
                response.setRepMsg(RespMsg.AUTH_CODE_FAILED_MSG);
                return response;
            }
            String url = SlnsConstants.PERMISSION_AUTH_URL;
            UserEntity userEntity = loginService.queryUserEntityByUserMobile(mobile, 2);
            if (null == userEntity) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.ACCOUNT_IS_NOEXIST_MSG);
            } else {
                if (userEntity.getLocked() == 1) {
                    response.setRepCode(RespCode.LOGIN_FAILED);
                    response.setRepMsg(RespMsg.ACCOUNT_IS_LOCKED_MSG);
                } else {
                    StringBuffer sb = new StringBuffer();
                    sb.append("{\"userId\":\"" + userEntity.getPermissionUserId()).append("\",\"token\":\"\",\"sign\":\"\",\"time\":\"\",\"reqData\":{\"operName\":\"")
                        .append(userEntity.getAccountName()).append("\",\"operPassword\":\"").append("").append("\",\"isMobile\":\"").append(1).append("\"}}");
                    String result = HttpTool.getInstance().post(url, sb.toString(), "application/json", null, request.getUserId().toString());
                    JSONObject object = JSONObject.parseObject(result);
                    if (null != object) {
                        if (object.getString("repCode").equals("0000")) {

                            JSONObject repData = JSONObject.parseObject(object.getString("repData"));
                            if (null != repData) {
                                resultMessage = loginService.loginByUser(userEntity.getAccountName());
                                if (resultMessage.contains("成功")) {
                                    String token = tokenService.generateToken();
                                    tokenService.putToken(token, userEntity.getId().toString());

//                                    UserDTO userDTO = new UserDTO(repData);
//                                    userDTO.setAccountName(userEntity.getAccountName());
//                                    userDTO.setAccountType(userEntity.getAccountType());
//                                    userDTO.setAddress(userEntity.getAddress());
//                                    userDTO.setCreateName(userEntity.getCreateName());
//                                    userDTO.setCredentialsSalt(userEntity.getCredentialsSalt());
//                                    userDTO.setCustomer(userEntity.getCustomer());
//                                    userDTO.setCustomerId(userEntity.getCustomerId());
//                                    userDTO.setDescription(userEntity.getDescription());
//                                    userDTO.setId(userEntity.getId());
//                                    userDTO.setIsValid(userEntity.getIsValid());
//                                    userDTO.setLocked(userEntity.getLocked());
//                                    userDTO.setName(userEntity.getName());
//                                    userDTO.setPassword(userEntity.getPassword());
//                                    userDTO.setPermissionUserId(userEntity.getPermissionUserId());
//                                    userDTO.setTelephone(userEntity.getTelephone());
//                                    userDTO.setType(userEntity.getType());
//                                    userDTO.setUpdateTime(userEntity.getUpdateTime());
//                                    userDTO.setUpdateUser(userEntity.getUpdateUser());
//                                    UserCenter.getInstance().putUserDTO(token, userDTO);
                                    response.setRepCode(RespCode.LOGIN_SUCCEED);
                                    response.setRepData(repData);
                                    response.setRepMsg(resultMessage);
                                }
                            }
                        } else {
                            response.setRepCode(object.getString("repCode"));
                            response.setRepMsg(object.getString("repMsg"));
                        }
                    }
                    if (!RespCode.LOGIN_SUCCEED.equals(response.getRepCode())) {
                        response.setRepCode(RespCode.LOGIN_FAILED);
                        if (null == response.getRepMsg()) {
                            response.setRepMsg(resultMessage);
                        }
                    }
                }
            }
        }
        return response;
    }

    /**
     * 司机通过手机号登陆
     * @param request
     * @return
     */
    @RequestMapping("/driver/user/login/mobile")
    @ResponseBody
    @LoggerManage(description = "司机用户通过手机号登录")
    public BaseResponseModel<Object> loginForDriverByMobile(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        String resultMessage = null;
        String mobile = request.getReqData().getString("mobile");
        String code = request.getReqData().getString("code");
        String msgId = request.getReqData().getString("msgId");
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(code) || StringUtils.isEmpty(msgId)) {
            response.setRepCode(RespCode.LOGIN_FAILED);
            response.setRepMsg(RespMsg.LOGIN_NULL_USER_PASSWORD_MSG);
        } else {
            // 验证验证码
            Boolean verifyType = slnsSendMessageService.verifyCode(msgId, code);
            if (!verifyType) {
                response.setRepCode(RespCode.AUTH_CODE_FAILED);
                response.setRepMsg(RespMsg.AUTH_CODE_FAILED_MSG);
                return response;
            }

            String url = SlnsConstants.PERMISSION_AUTH_URL;
            UserEntity userEntity = loginService.queryUserEntityByUserMobile(mobile, 4);
            if (null == userEntity) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.ACCOUNT_IS_NOEXIST_MSG);
            } else {
                if (userEntity.getLocked() == 1) {
                    response.setRepCode(RespCode.LOGIN_FAILED);
                    response.setRepMsg(RespMsg.ACCOUNT_IS_LOCKED_MSG);
                } else {
                    StringBuffer sb = new StringBuffer();
                    sb.append("{\"userId\":\"" + userEntity.getPermissionUserId()).append("\",\"token\":\"\",\"sign\":\"\",\"time\":\"\",\"reqData\":{\"operName\":\"")
                        .append(userEntity.getAccountName()).append("\",\"operPassword\":\"").append("").append("\",\"isMobile\":\"").append(1).append("\"}}");
                    String result = HttpTool.getInstance().post(url, sb.toString(), "application/json", null, request.getUserId().toString());
                    JSONObject object = JSONObject.parseObject(result);
                    if (null != object) {
                        if (object.getString("repCode").equals("0000")) {
                            JSONObject repData = JSONObject.parseObject(object.getString("repData"));
                            if (null != repData) {
                                resultMessage = loginService.loginByUser(userEntity.getAccountName());
                                if (resultMessage.contains("成功")) {
                                    String token = tokenService.generateToken();
                                    tokenService.putToken(token, userEntity.getId().toString());

//                                    UserDTO userDTO = new UserDTO(repData);
//                                    userDTO.setAccountName(userEntity.getAccountName());
//                                    userDTO.setAccountType(userEntity.getAccountType());
//                                    userDTO.setAddress(userEntity.getAddress());
//                                    userDTO.setCreateName(userEntity.getCreateName());
//                                    userDTO.setCredentialsSalt(userEntity.getCredentialsSalt());
//                                    userDTO.setCustomer(userEntity.getCustomer());
//                                    userDTO.setCustomerId(userEntity.getCustomerId());
//                                    userDTO.setDescription(userEntity.getDescription());
//                                    userDTO.setId(userEntity.getId());
//                                    userDTO.setIsValid(userEntity.getIsValid());
//                                    userDTO.setLocked(userEntity.getLocked());
//                                    userDTO.setName(userEntity.getName());
//                                    userDTO.setPassword(userEntity.getPassword());
//                                    userDTO.setPermissionUserId(userEntity.getPermissionUserId());
//                                    userDTO.setTelephone(userEntity.getTelephone());
//                                    userDTO.setType(userEntity.getType());
//                                    userDTO.setUpdateTime(userEntity.getUpdateTime());
//                                    userDTO.setUpdateUser(userEntity.getUpdateUser());
//                                    UserCenter.getInstance().putUserDTO(token, userDTO);

                                    JSONObject retRepData = new JSONObject();
                                    retRepData.put("userId", userEntity.getId());
                                    retRepData.put("token", token);

                                    TruckDriverVO truckDriverVO = truckDriverService.selectByPrimaryKey(userEntity.getCustomerId());
                                    if (null != truckDriverVO) {
                                        retRepData.put("truckNumber", truckDriverVO.getTruckNumber());
                                        retRepData.put("driverName", truckDriverVO.getName());
                                        retRepData.put("driverMobile", truckDriverVO.getMobile());
                                        response.setRepCode(RespCode.SUCCESS);
                                        response.setRepData(retRepData);
                                        response.setRepMsg(RespMsg.LOGIN_SUCCEED_MSG);
                                    } else {
                                        response.setRepCode(RespCode.LOGIN_FAILED);
                                        response.setRepMsg(RespMsg.LOGIN_GET_DRIVER_FAILED_MSG);
                                        response.setRepMsg(resultMessage);
                                    }

                                }
                            }
                        } else {
                            response.setRepCode(object.getString("repCode"));
                            response.setRepMsg(object.getString("repMsg"));
                        }
                    }
                    if (!RespCode.SUCCESS.equals(response.getRepCode())) {
                        response.setRepCode(RespCode.LOGIN_FAILED);
                        if (null == response.getRepMsg()) {
                            response.setRepMsg(resultMessage);
                        }
                    }
                }
            }
        }
        return response;
    }

    /**
     * 经销商通过手机号登录
     * @param request
     * @return
     */
    @RequestMapping("/dealer/user/login/mobile")
    @ResponseBody
    @LoggerManage(description = "经销商通过手机号登录")
    public BaseResponseModel<Object> loginForDealerByMobile(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        String resultMessage = null;
        String mobile = request.getReqData().getString("mobile");
        String code = request.getReqData().getString("code");
        String msgId = request.getReqData().getString("msgId");
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(code) || StringUtils.isEmpty(msgId)) {
            response.setRepCode(RespCode.LOGIN_FAILED);
            response.setRepMsg(RespMsg.LOGIN_NULL_USER_PASSWORD_MSG);
        } else {
            // 验证验证码
            Boolean verifyType = slnsSendMessageService.verifyCode(msgId, code);
            if (!verifyType) {
                response.setRepCode(RespCode.AUTH_CODE_FAILED);
                response.setRepMsg(RespMsg.AUTH_CODE_FAILED_MSG);
                return response;
            }
            String url = SlnsConstants.PERMISSION_AUTH_URL;
            UserEntity userEntity = loginService.queryUserEntityByUserMobile(mobile, 1);
            if (null == userEntity) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.ACCOUNT_IS_NOEXIST_MSG);
            } else {
                if (userEntity.getLocked() == 1) {
                    response.setRepCode(RespCode.LOGIN_FAILED);
                    response.setRepMsg(RespMsg.ACCOUNT_IS_LOCKED_MSG);
                } else {
                    StringBuffer sb = new StringBuffer();
                    sb.append("{\"userId\":\"" + userEntity.getPermissionUserId()).append("\",\"token\":\"\",\"sign\":\"\",\"time\":\"\",\"reqData\":{\"operName\":\"")
                        .append(userEntity.getAccountName()).append("\",\"operPassword\":\"").append("").append("\",\"isMobile\":\"").append(1).append("\"}}");
                    String result = HttpTool.getInstance().post(url, sb.toString(), "application/json", null, request.getUserId().toString());
                    JSONObject object = JSONObject.parseObject(result);
                    if (null != object) {
                        if (object.getString("repCode").equals("0000")) {
                            JSONObject repData = JSONObject.parseObject(object.getString("repData"));
                            if (null != repData) {
                                resultMessage = loginService.loginByUser(userEntity.getAccountName());
                                if (resultMessage.contains("成功")) {
                                    String token = tokenService.generateToken();
                                    tokenService.putToken(token, userEntity.getId().toString());
//                                    UserDTO userDTO = new UserDTO(repData);
//                                    userDTO.setAccountName(userEntity.getAccountName());
//                                    userDTO.setAccountType(userEntity.getAccountType());
//                                    userDTO.setAddress(userEntity.getAddress());
//                                    userDTO.setCreateName(userEntity.getCreateName());
//                                    userDTO.setCredentialsSalt(userEntity.getCredentialsSalt());
//                                    userDTO.setCustomer(userEntity.getCustomer());
//                                    userDTO.setCustomerId(userEntity.getCustomerId());
//                                    userDTO.setDescription(userEntity.getDescription());
//                                    userDTO.setId(userEntity.getId());
//                                    userDTO.setIsValid(userEntity.getIsValid());
//                                    userDTO.setLocked(userEntity.getLocked());
//                                    userDTO.setName(userEntity.getName());
//                                    userDTO.setPassword(userEntity.getPassword());
//                                    userDTO.setPermissionUserId(userEntity.getPermissionUserId());
//                                    userDTO.setTelephone(userEntity.getTelephone());
//                                    userDTO.setType(userEntity.getType());
//                                    userDTO.setUpdateTime(userEntity.getUpdateTime());
//                                    userDTO.setUpdateUser(userEntity.getUpdateUser());
//                                    UserCenter.getInstance().putUserDTO(token, userDTO);

                                    JSONObject retRepData = new JSONObject();
                                    retRepData.put("userId", userEntity.getId());
                                    retRepData.put("token", token);

                                    CustomerEntity customerEntity = customerService.queryCustomerById(userEntity.getCustomerId());
                                    if (null != customerEntity) {
                                        retRepData.put("group", customerEntity.getGroup());
                                        retRepData.put("company", customerEntity.getName());
                                        retRepData.put("name", userEntity.getName());
                                        retRepData.put("mobile", userEntity.getTelephone());

                                        if (repData.toJSONString().contains("收车权限")) {
                                            retRepData.put("receiveCarPermission", "1");
                                        } else {
                                            retRepData.put("receiveCarPermission", "0");
                                        }
                                        response.setRepCode(RespCode.SUCCESS);
                                        response.setRepData(retRepData);
                                        response.setRepMsg(RespMsg.LOGIN_SUCCEED_MSG);
                                    } else {
                                        resultMessage = "查询司机信息失败。";
                                        response.setRepCode(RespCode.LOGIN_FAILED);
                                        response.setRepMsg(resultMessage);
                                    }

                                }
                            }
                        } else {
                            response.setRepCode(object.getString("repCode"));
                            response.setRepMsg(object.getString("repMsg"));
                        }
                    }
                    if (!RespCode.SUCCESS.equals(response.getRepCode())) {
                        response.setRepCode(RespCode.LOGIN_FAILED);
                        if (null == response.getRepMsg()) {
                            response.setRepMsg(resultMessage);
                        }
                    }
                }
            }
        }

        return response;
    }

    @RequestMapping("/business/user/logout")
    @ResponseBody
    @LoggerManage(description = "用户注销")
    public BaseResponseModel<Object> logout(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        String userToken = request.getToken();
        String token = "";

        String[] tokenInfo = userToken.split("_");
        if (tokenInfo.length != 2) {
            token = userToken;
        } else {
            token = tokenInfo[1];
        }
        tokenService.removeToken(token);
        response.setRepCode(RespCode.SUCCESS);

        return response;
    }

    @RequestMapping("/business/user/userRegister")
    @ResponseBody
    @LoggerManage(description = "用户注册")
    public BaseResponseModel<Object> userRegister(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        Map<String, Object> mapVue = new HashMap<String, Object>();
        try {
            mapVue.put("userId", request.getToken().substring(0, request.getToken().indexOf("_")));
            mapVue.put("name", request.getReqData().get("name"));
            mapVue.put("accountName", request.getReqData().get("accountName"));
            mapVue.put("password", request.getReqData().get("password"));
            mapVue.put("token", request.getToken());
            mapVue.put("description", StringUtil.convertNullToEmpty(request.getReqData().get("description")));
            mapVue.put("credentialsSalt", StringUtil.convertNullToEmpty(request.getReqData().get("credentialsSalt")));
            mapVue.put("email", StringUtil.convertNullToEmpty(request.getReqData().get("email")));
            mapVue.put("idCardFrontPicture", StringUtil.convertNullToEmpty(request.getReqData().get("idCardFrontPicture")));
            mapVue.put("idCardBackPicture", StringUtil.convertNullToEmpty(request.getReqData().get("idCardBackPicture")));
            mapVue.put("birthday", StringUtil.convertNullToEmpty(request.getReqData().get("birthday")));
            mapVue.put("customerId", StringUtil.convertNullToEmpty(request.getReqData().get("customerId")));
            mapVue.put("type", request.getReqData().get("type"));
            mapVue.put("accountType", request.getReqData().get("accountType"));
            mapVue.put("sex", StringUtil.convertNullToEmpty(request.getReqData().get("sex")));
            mapVue.put("telephone", request.getReqData().get("telephone"));
            mapVue.put("address", StringUtil.convertNullToEmpty(request.getReqData().get("address")));
            mapVue.put("idCardNo", StringUtil.convertNullToEmpty(request.getReqData().get("idCardNo")));
            mapVue.put("transportCompanyName", StringUtil.convertNullToEmpty(request.getReqData().get("transportCompanyName")));
            mapVue.put("transportCompanyId", StringUtil.convertNullToEmpty(request.getReqData().get("transportCompanyId")));
            mapVue.put("truckNumber", StringUtil.convertNullToEmpty(request.getReqData().get("truckNumber")));
            int count = loginService.userRegister(mapVue);
            if (count == 1) {
                response.setRepCode(RespCode.USER_REGISTER_REPEAT);
                response.setRepMsg(RespMsg.USER_REGISTER_REPEAT);
                response.setRepData(false);
            } else if (count == 2) {
                response.setRepCode(RespCode.USER_REGISTER);
                response.setRepMsg(RespMsg.USER_REGISTER);
                response.setRepData(false);
            } else {
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
                response.setRepData(true);
            }
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.USER_REGISTER);
            response.setRepMsg(RespMsg.USER_REGISTER);
            response.setRepData(false);
        }
        return response;
    }

    // @RequestMapping(value = "/business/user/validateByUserId")
    // @ResponseBody
    // @LoggerManage(description = "用户权限验证")
    // public BaseResponseModel<Object> validateByUserId(@RequestBody BaseRequestModel request) {
    // BaseResponseModel<Object> response = new BaseResponseModel<>();
    // Integer userId = Integer.valueOf(request.getUserId());
    // Long permissionId = Long.valueOf(request.getReqData().getLongValue("authId"));
    // boolean hasPermission = loginService.validatePermissionByUserId(userId, permissionId);
    // response.setRepCode(RespCode.SUCCESS);
    // JSONObject object = new JSONObject();
    // object.put("hasPermission", hasPermission);
    // response.setRepData(object);
    // return response;
    // }
    //
    // @RequestMapping("/sms/code/send")
    // @ResponseBody
    // @LoggerManage(description = "验证码短信发送")
    // public BaseResponseModel<Object> sendSmsCode(@RequestBody BaseRequestModel request) {
    // BaseResponseModel<Object> response = new BaseResponseModel<>();
    // String userId = request.getUserId();
    // // 封装短信请求信息
    // SmsBaseRequestModel smsBaseRequestModel = new SmsBaseRequestModel();
    // smsBaseRequestModel.setSysCode("fs");
    // smsBaseRequestModel.setTime(String.valueOf(System.currentTimeMillis()));
    // // 请求内容
    // JSONObject reqData = new JSONObject();
    // JSONObject data = new JSONObject();
    // data.put("mobile", "13482208338");
    // reqData.put("data", data);
    // reqData.put("send_type", 1);
    // reqData.put("message_type", "MESSAGE_TEMP");
    // reqData.put("is_retry", 1);
    // smsBaseRequestModel.setReqData(reqData);
    // String baseSignMsg = "reqData" + smsBaseRequestModel.getReqData().toJSONString() + "time" + smsBaseRequestModel.getTime() + "token" + "ee21152d83d64272b8d46d7ce496cbb4";
    // smsBaseRequestModel.setSign(MD5Util.getInstance().encrypt(baseSignMsg).toString());
    // logger.info("发送自提通知短信实体======》" + JSON.toJSONString(smsBaseRequestModel));
    // JSONObject result = (JSONObject) JSONObject.toJSON(smsServiceClient.sendBatchMessage(smsBaseRequestModel));
    // logger.info("接收自提通知短信实体======》" + result.toJSONString());
    // response.setRepCode(RespCode.LOGOUT_SUCCEED);
    //
    // return response;
    // }

    /**
     * 数据权限解析
     * @param dataPermissionObj
     * @param retRepData
     */
    private JSONObject dataProcess(JSONObject dataPermissionObj, JSONObject retRepData) {
        // 数据权限解析
        if (null != dataPermissionObj) {
            JSONArray permissions2st = dataPermissionObj.getJSONArray("child");
            if (null != permissions2st) {
                JSONObject receiveCarPermissionObj = null;
                JSONObject transportOrderPermissionObj = null;
                Iterator<Object> it2st = permissions2st.iterator();
                while (it2st.hasNext()) {
                    JSONObject ob = (JSONObject) it2st.next();
                    if (SlnsConstants.PERMISSION_RECEIVE_CAR.equals(ob.getString("name"))) {
                        receiveCarPermissionObj = ob;
                    } else if (SlnsConstants.PERMISSION_TRANSPORT_ORDER.equals(ob.getString("name"))) {
                        transportOrderPermissionObj = ob;
                    }
                    if (null != receiveCarPermissionObj && null != transportOrderPermissionObj) {
                        break;
                    }
                }
                // 收车权限
                if (null != receiveCarPermissionObj) {
                    retRepData.put("receiveCarPermission", "1");
                } else {
                    retRepData.put("receiveCarPermission", "0");
                }
                // 取消订单权限
                if (null != transportOrderPermissionObj) {
                    JSONArray permissions3st = transportOrderPermissionObj.getJSONArray("child");
                    if (null != permissions3st) {
                        Iterator<Object> it3st = permissions3st.iterator();
                        while (it3st.hasNext()) {
                            JSONObject ob = (JSONObject) it3st.next();
                            if (SlnsConstants.PERMISSION_CANCEL.equals(ob.getString("name"))) {
                                retRepData.put("cancelTransportOrderPermission", "1");
                            } else {
                                retRepData.put("cancelTransportOrderPermission", "0");
                            }
                        }
                    }
                }
                if (null == retRepData.get("cancelTransportOrderPermission")) {
                    retRepData.put("cancelTransportOrderPermission", "0");
                }
            }
        }
        return retRepData;
    }

    @RequestMapping("/test")
    @ResponseBody
    @LoggerManage(description = "测试")
    public BaseResponseModel<Object> test() {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        String resultMessage = "This is a test case.";
        response.setRepCode(RespCode.LOGIN_FAILED);
        response.setRepMsg(resultMessage);

        return response;
    }
}
