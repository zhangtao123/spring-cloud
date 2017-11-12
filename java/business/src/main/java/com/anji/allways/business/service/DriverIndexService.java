/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import java.util.Map;

/**
 * <pre>
 * </pre>
 * @author Administrator
 * @version $Id: DriverIndexService.java, v 0.1 2017年9月7日 下午8:25:03 Administrator Exp $
 */
public interface DriverIndexService {
    // 司机首页
    Map<String, Object> home(Map<String, Object> map);

    Map<String, Object> checkEditionInformation(int type);
}
