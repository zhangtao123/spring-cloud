/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.anji.allways.business.common.constant.SlnsConstants;
import com.anji.allways.business.entity.ConsigneeEntity;
import com.anji.allways.business.entity.TruckDriverEntity;
import com.anji.allways.business.entity.UserEntity;
import com.anji.allways.business.entity.UserInfoEntity;
import com.anji.allways.business.mapper.ConsigneeEntityMapper;
import com.anji.allways.business.mapper.TruckDriverEntityMapper;
import com.anji.allways.business.mapper.UserEntityMapper;
import com.anji.allways.business.mapper.UserInfoEntityMapper;
import com.anji.allways.business.service.UserService;
import com.anji.allways.common.util.DateUtil;
import com.anji.allways.common.util.FileUtil;
import com.anji.allways.common.util.HttpTool;

/**
 * @author wangyanjun
 * @version $Id: LoginServiceImpl.java, v 0.1 2017年8月22日 下午2:49:00 wangyanjun Exp $
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserEntityMapper        userMapper;
    @Autowired
    private UserService             loginService;

    @Autowired
    private UserInfoEntityMapper    userInfoEntityMapper;

    @Autowired
    private ConsigneeEntityMapper   consigneeEntityMapper;

    @Autowired
    private TruckDriverEntityMapper truckDriverEntityMapper;

    /**
     * @param userName
     * @param password
     * @return
     * @see com.anji.allways.business.service.UserService#loginByUser(java.lang.String, java.lang.String)
     */
    @Override
    public String loginByUser(String userName) {
        String resultMessage = "";

        UserEntity user = userMapper.selectByUserName(userName);
        if (null != user) {
            // 检查账号是否被锁定
            if (user.getLocked() == 1) {
                resultMessage = "账号被锁定，请找管理员解锁后再试。";
            }
            resultMessage = "查询用户信息成功。";
        } else {
            resultMessage = "用户名'" + userName + "'不存在，请检查后再试。";
        }

        return resultMessage;
    }

    /**
     * @param phoneNumber
     * @param dynamicCode
     * @return
     * @see com.anji.allways.business.service.UserService#loginByMobile(java.lang.String, java.lang.String)
     */
    @Override
    public String loginByMobile(String phoneNumber, String dynamicCode) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param userId
     * @return
     * @see com.anji.allways.business.service.UserService#queryOwnerIdByUserId(java.lang.String)
     */
    @Override
    public Integer queryOwnerIdByUserId(Integer userId) {
        UserEntity userEntity = userMapper.selectByPrimaryKey(userId);
        return null == userEntity ? null : userEntity.getCustomerId();
    }

    /**
     * 根据账号id获取账号实体
     * @param userId
     * @return
     * @see com.anji.allways.business.service.UserService#queryUserEntityByUserId(java.lang.Integer)
     */
    @Override
    public UserEntity queryUserEntityByUserId(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    /**
     * 根据用户ID和所属组织类型查询所属客户ID
     * @param userId
     *            用户ID
     * @param orgType
     *            用户所属组织 0:无效 1：经销商 2：仓库 3：运输公司 4：司机 5：系统管理
     * @return 0(超级管理员能查看所有数据) null(登录用户不存在) -1(普通用户查询自己创建的数据) <br>
     *         以上三种情况以外返回的是管理员用户对应的客户ID
     */
    @Override
    public Integer queryCustomerId(Integer userId, Integer orgType) {
        Integer retValue = -1;
        UserEntity userEntity = userMapper.selectByPrimaryKey(userId);

        if (userEntity != null) {
            // 如果所属组织不为传入的所属组织
            if (orgType != userEntity.getType()) {
                retValue = null;
            } else {
                // 账户类型(0:普通 1：管理员)不为普通时,返回客户ID
                if (userEntity.getAccountType() != 0) {
                    retValue = userEntity.getCustomerId();
                }
            }
        } else {
            retValue = null;
        }
        return retValue;
    }

    /**
     * @param userName
     * @return
     * @see com.anji.allways.business.service.UserService#queryUserEntityByUserName(java.lang.String)
     */
    @Override
    public UserEntity queryUserEntityByUserName(String userName, int customerId) {
        UserEntity userEntity = userMapper.selectByUserName(userName);
        if (null != userEntity && userEntity.getType() == customerId) {
            return userEntity;
        }
        return null;
    }

    /**
     * @param userId
     * @param permissionId
     * @return
     * @see com.anji.allways.business.service.UserService#validatePermissionByUserId(java.lang.Long, java.lang.Long)
     */
    @Override
    public boolean validatePermissionByUserId(Integer userId, Long permissionId) {
        String url = SlnsConstants.VALID_PERMISSION_URL;
        UserEntity userEntity = loginService.queryUserEntityByUserId(userId);
        if (null == userEntity) {
            return false;
        } else {
            if (userEntity.getLocked() == 1) {
                return false;
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append("{\"userId\":\"" + userEntity.getPermissionUserId()).append("\",\"token\":\"\",\"sign\":\"\",\"time\":\"\",\"reqData\":{\"authId\":\"").append(permissionId).append("\"}}");
                String result = HttpTool.getInstance().post(url, sb.toString(), "application/json", null, userEntity.getId().toString());
                JSONObject object = JSONObject.parseObject(result);
                if (null != object && object.getString("repCode").equals("0000")) {
                    JSONObject repData = JSONObject.parseObject(object.getString("repData"));
                    if (null != repData) {
                        return repData.getBooleanValue("hasPermission");
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param telephone
     * @return
     * @see com.anji.allways.business.service.UserService#queryUserEntityByUserMobile(java.lang.String)
     */
    @Override
    public UserEntity queryUserEntityByUserMobile(String telephone, int type) {
        List<UserEntity> userEntitys = userMapper.selectByUserMobile(telephone);
        UserEntity userEntity = null;
        if (null == userEntitys || userEntitys.size() <= 0) {
            return null;
        } else {
            for (UserEntity user : userEntitys) {
                if (user.getType() == type) {
                    userEntity = user;
                }
            }
        }
        return userEntity;
    }

    /**
     * @param map
     * @return
     * @see com.anji.allways.business.service.UserService#queryCustomerId(java.util.Map)
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer userRegister(Map<String, Object> map) {
        synchronized (this) {
            int count = userMapper.checkAccountNameByOne(map);
            if (count > 0) {
                return 1;
            }
            String permissionUserId = FileUtil.getInstance().userRegister(map.get("accountName").toString(), map.get("password").toString(), map.get("name").toString(),
                map.get("token").toString().substring(0, map.get("token").toString().indexOf("_")));
            if (!permissionUserId.equals("false")) {
                UserEntity userEntity = new UserEntity();
                userEntity.setName(map.get("name").toString());
                userEntity.setAccountName(map.get("accountName").toString());
                userEntity.setPassword(map.get("password").toString());
                userEntity.setPermissionUserId(Integer.valueOf(permissionUserId));
                userEntity.setCustomerId(Integer.valueOf(map.get("customerId").toString()));
                userEntity.setType(Integer.parseInt(map.get("type").toString()));
                userEntity.setAccountType(Integer.parseInt(map.get("accountType").toString()));
                userEntity.setIsValid(0);
                userEntity.setLocked(0);
                userEntity.setDescription(map.get("description").toString());
                userEntity.setCredentialsSalt(map.get("credentialsSalt").toString());
                userEntity.setCreateName(map.get("userId").toString());
                userEntity.setCreateTime(new Date());
                userEntity.setUpdateUser(map.get("userId").toString());
                userEntity.setUpdateTime(new Date());
                userMapper.insertSelective(userEntity);

                UserInfoEntity infoEntity = new UserInfoEntity();
                infoEntity.setId(userEntity.getId());
                infoEntity.setUserId(userEntity.getId());
                infoEntity.setSex(Integer.parseInt(map.get("sex").toString()));
                infoEntity.setBirthday((DateUtil.formateStringToDate(map.get("birthday").toString(), "yyyy-MM-dd")));
                infoEntity.setTelephone(map.get("telephone").toString());
                infoEntity.setEmail(map.get("email").toString());
                infoEntity.setCreateTime(new Date());
                infoEntity.setAddress(map.get("address").toString());
                infoEntity.setCreateName(map.get("userId").toString());
                userInfoEntityMapper.insertSelective(infoEntity);
                if (map.get("type").equals("1")) {
                    ConsigneeEntity consigneeEntity = new ConsigneeEntity();
                    consigneeEntity.setMobile(map.get("telephone").toString());
                    consigneeEntity.setUserId(userEntity.getId());
                    consigneeEntity.setName(map.get("name").toString());
                    consigneeEntity.setCreateUser(Integer.valueOf(map.get("userId").toString()));
                    consigneeEntity.setCreateTime(new Date());
                    consigneeEntity.setUpdateUser(Integer.valueOf(map.get("userId").toString()));
                    consigneeEntity.setUpdateTime(new Date());
                    consigneeEntityMapper.insertSelective(consigneeEntity);
                } else if (map.get("type").equals("4")) {
                    TruckDriverEntity driverEntity = new TruckDriverEntity();
                    driverEntity.setMobile(map.get("telephone").toString());
                    driverEntity.setName(map.get("name").toString());
                    driverEntity.setIdCardNo(map.get("idCardNo").toString());
                    driverEntity.setTransportCompanyId(Integer.valueOf(map.get("transportCompanyId").toString()));
                    driverEntity.setTransportCompanyName(map.get("transportCompanyName").toString());
                    driverEntity.setTruckNumber(map.get("truckNumber").toString());
                    driverEntity.setIdCardFrontPicture(map.get("idCardFrontPicture").toString());
                    driverEntity.setIdCardBackPicture(map.get("idCardBackPicture").toString());
                    driverEntity.setAccountName(map.get("accountName").toString());
                    driverEntity.setStatus(1);
                    driverEntity.setCreateUser(userEntity.getId());
                    driverEntity.setCreateTime(new Date());
                    driverEntity.setUpdateUser(Integer.valueOf(map.get("userId").toString()));
                    driverEntity.setUpdateTime(new Date());
                    truckDriverEntityMapper.insertSelective(driverEntity);
                }
            } else {
                return 2;
            }
            return 0;
        }

    }

    /**
     * 根据客户ID及所属组织查询用户ID
     */
    public Integer selectUserIdByCustomerIdAndType(Map<String, Object> map) {
        return userMapper.selectUserIdByCustomerIdAndType(map);
    }
}
