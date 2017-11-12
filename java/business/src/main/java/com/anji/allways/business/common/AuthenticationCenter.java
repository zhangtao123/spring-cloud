/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.common;

import com.anji.allways.base.entity.BaseRequestModel;
import com.anji.allways.base.entity.BaseResponseModel;
import com.anji.allways.business.service.TokenService;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.MD5Util;
import com.anji.allways.common.util.StringUtil;

/**
 * <pre>
 * </pre>
 * @author wangyanjun
 * @version $Id: AuthenticationCenter.java, v 0.1 2017年9月2日 上午9:52:46 wangyanjun Exp $
 */
public final class AuthenticationCenter {

    private static AuthenticationCenter instance = null;

    private AuthenticationCenter() {

    }

    public static synchronized AuthenticationCenter getInstance() {
        if (instance == null) {
            instance = new AuthenticationCenter();
        }
        return instance;
    }

    public BaseResponseModel<Object> isLogin(BaseRequestModel request, TokenService service) {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String userId = request.getUserId();
        String token = request.getToken();
        if (StringUtil.isEmpty(token, true) || StringUtil.isEmpty(userId, true)) {
            response.setRepCode(RespCode.AUTH_FAILED);
            response.setRepMsg(RespMsg.AUTH_NULL_TOKEN_USERID_MSG);
            return response;
        }
//        // 验证token
//        // start for test
//        service.putToken(userId, "43a15e19ebb3cce7a15e7f9f43ecfca0");
//        // end
        if (!userId.equals(service.getUserId(token))) {
            response.setRepCode(RespCode.AUTH_FAILED);
            response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
            return response;
        }
        String sign = request.getSign();
        // 验证sign
//        // start for test
//        sign = MD5Util.encrypt(request.getReqData().toString());
//        // end
        if (StringUtil.isEmpty(sign, true) || !sign.equals(MD5Util.encrypt(request.getReqData().toString()))) {
            response.setRepCode(RespCode.AUTH_FAILED);
            response.setRepMsg(RespMsg.AUTH_ERROR_SIGN_MSG);
            return response;
        }

        response.setRepCode(RespCode.AUTH_SUCCEED);
        response.setRepMsg(RespMsg.AUTH_SUCCEED_MSG);
        return response;
    }
}
