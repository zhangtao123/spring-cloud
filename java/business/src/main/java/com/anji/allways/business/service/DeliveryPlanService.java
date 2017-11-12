/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import com.anji.allways.business.dto.DeliveryPlanDTO;
import com.anji.allways.business.vo.DeliveryPlanChildVO;
import com.anji.allways.business.vo.DeliveryPlanVO;

/**
 * <pre>
 * 出库计划单相关接口
 * </pre>
 * @author xuyuyang
 * @version $Id: DeliveryPlanService.java, v 0.1 2017年8月24日 上午10:41:29 xuyuyang Exp $
 */
public interface DeliveryPlanService {

    /**
     * <pre>
     * 根据主出库计划单ID查询出库计划单明细信息
     * </pre>
     * @param id
     * @return
     */
    DeliveryPlanVO selectInfoById(Integer id);

    /**
     * <pre>
     * 子出库计划单送达车辆信息
     * </pre>
     * @param id
     *            子出库计划单ID
     * @return
     */
    DeliveryPlanChildVO findChildDeliveryPlanInfo(Integer id);

    /**
     * <pre>
     * 确认送达更新操作
     * </pre>
     * @param id
     * @param fileNames
     * @param userId
     * @param acceptComment
     * @return
     */
    int updateForConfirmArrive(Integer id, String fileNames, String userId, String acceptComment);

    /**
     * 根据计划单ID查询计划单信息
     */
    DeliveryPlanDTO selectByPrimaryKey(Integer id);
}
