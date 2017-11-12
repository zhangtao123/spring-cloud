package com.anji.allways.business.service;

import java.util.Map;

/**
 * @author 李军
 * @version $Id: DealerIndexService.java, v 0.1 2017年8月24日 下午3:11:40 李军 Exp $
 */
public interface DealerIndexService {

    // 获取首页信息
    Map<String, Object> getCustomerAbbreviation(String userId);

    Map<String, Object> checkEditionInformation(int type);

    Map<String, Object> getVehicleCount(String userId);
}
