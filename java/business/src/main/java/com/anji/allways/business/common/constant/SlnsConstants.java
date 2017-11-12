/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.common.constant;

/**
 * @author wangyanjun
 * @version $Id: CustomerConstants.java, v 0.1 2017年8月31日 下午1:52:12 wangyanjun Exp $
 */
public final class SlnsConstants {

    public static final String SYSTEM_NAME                  = "社会化信息系统";

    public static final String PERMISSION_DATA              = "数据权限";

    public static final String PERMISSION_RECEIVE_CAR       = "收车权限";

    public static final String PERMISSION_TRANSPORT_ORDER   = "运输订单";

    public static final String PERMISSION_CANCEL            = "取消";

    public static final String CUSTOMER_PERFIX              = "ANJIJIAJIA_CUSTOMER_";

    public static final String CUSTOMER_KEY_SALT            = "ANJIJIAJIA_KEY_XXXXXX";
    // 达示数据appid
    public static final String DAAS_AUTO_APPID              = "1ej7nqcxg9sl8dst2h5ry6hnceajs4qs";
    // 达示数据appkey
    public static final String DAAS_AUTO_APPKEY             = "a060c4hlw3u9vocdu3b0kycz9wsenlxx";
    // 达示数据服务url
    public static final String DAAS_GET_DATA_URL            = "http://www.daas-auto.com/APIS/custReqm/yongnuo/v1/product-database/lists";
    // 出库订单：OP
    public static final String SERIAL_NUMBER_TYPE_DELIVERY  = "OP";
    // 运输订单：TO
    public static final String SERIAL_NUMBER_TYPE_TRANSPORT = "TO";
    // 入库计划单：WP
    public static final String SERIAL_NUMBER_TYPE_STORAGE   = "WP";
    // 质损单：ML
    public static final String SERIAL_NUMBER_TYPE_DAMAGE    = "ML";
    // 交接单号：T
    public static final String SERIAL_NUMBER_TYPE_SHIP      = "T";
    // 服务返回成功
    public static final String SERVICE_SUCCEED              = "SERVICE SUCCEED";
    // 服务返回失败
    public static final String SERVICE_FAILED               = "SERVICE FAILED";
    // 权限系统认证
    public static final String PERMISSION_AUTH_URL          = "http://10.108.10.30:38060/user/noauth/innlogin";
    // 权限系统重设密码
    public static final String RESET_PASSWORD_URL           = "http://10.108.10.30:38060/user/password/modify";
    // 权限系统验证权限
    public static final String VALID_PERMISSION_URL         = "http://10.108.10.30:38060/auth/validateByUserId";

}
