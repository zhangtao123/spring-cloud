/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import java.util.Map;

import com.anji.allways.business.entity.UserEntity;

/**
 * @author wangyanjun
 * @version $Id: UserLoginService.java, v 0.1 2017年8月22日 下午2:40:39 wangyanjun Exp $
 */
public interface UserService {

    String loginByUser(String userName);

    String loginByMobile(String phoneNumber, String dynamicCode);

    // 通过用户id查询所属的实体id
    Integer queryOwnerIdByUserId(Integer userId);

    // 通过用户id查询账户表实体
    UserEntity queryUserEntityByUserId(Integer userId);

    /**
     * 根据用户ID和所属组织类型查询所属客户ID
     * @param userId
     *            用户ID
     * @param orgType
     *            用户所属组织 0:无效 1：经销商 2：仓库 3：运输公司 4：司机 5：系统管理
     * @return 0(超级管理员能查看所有数据) null(登录用户不存在) -1(普通用户查询自己创建的数据) <br>
     *         以上三种情况以外返回的是管理员用户对应的客户ID
     */
    Integer queryCustomerId(Integer userId, Integer orgType);

    // 通过用户名查用户
    UserEntity queryUserEntityByUserName(String userName, int customerId);

    // 通过手机号查询
    UserEntity queryUserEntityByUserMobile(String telephone, int type);

    // 验证用户权限
    boolean validatePermissionByUserId(Integer userId, Long permissionId);

    Integer userRegister(Map<String, Object> map);

    /**
     * 根据客户ID及所属组织查询用户ID
     */
    Integer selectUserIdByCustomerIdAndType(Map<String, Object> map);
}
