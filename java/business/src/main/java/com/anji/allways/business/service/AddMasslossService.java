/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import java.util.Map;

/**
 * @author Administrator
 * @version $Id: AddMasslossService.java, v 0.1 2017年9月15日 下午1:27:21 Administrator Exp $
 */
public interface AddMasslossService {

    Integer addMassloss(Map<String, Object> map);

    Map<String, Object> addMasslossWarehouse(Map<String, Object> map);

    Map<String, Object> toUpdateMassloss(Integer damageId);

    Integer updateMassloss(Map<String, Object> map);

    Map<String, Object> selectDamageByVehicleIdAndType(int vehicleId);

    int deleteMassloss(int damageId, int vehicleId, int type);
}
