/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anji.allways.business.dto.DeliveryPlanDTO;
import com.anji.allways.business.entity.FileEntity;
import com.anji.allways.business.mapper.DeliveryPlanEntityMapper;
import com.anji.allways.business.mapper.FileEntityMapper;
import com.anji.allways.business.mapper.VehicleEntityMapper;
import com.anji.allways.business.service.DeliveryPlanService;
import com.anji.allways.business.vo.DeliveryPlanChildVO;
import com.anji.allways.business.vo.DeliveryPlanVO;
import com.anji.allways.business.vo.VehicleVO;

/**
 * <pre>
 * </pre>
 * @author xuyuyang
 * @version $Id: DeliveryPlanServiceImpl.java, v 0.1 2017年8月24日 上午10:52:29 xuyuyang Exp $
 */
@Service
@Transactional
public class DeliveryPlanServiceImpl implements DeliveryPlanService {

    @Autowired
    private DeliveryPlanEntityMapper deliveryPlanEntityMapper;

    @Autowired
    private VehicleEntityMapper      vehicleEntityMapper;

    @Autowired
    private FileEntityMapper         fileEntityMapper;

    /**
     * <pre>
     * 根据主出库计划单ID查询出库计划单明细信息
     * </pre>
     * @param id
     * @return
     */
    public DeliveryPlanVO selectInfoById(Integer id) {
        DeliveryPlanVO vo = deliveryPlanEntityMapper.selectInfoById(id);

        if (vo != null) {

            // 处理主出库计划单仓库地址(所属省份+所属城市+所属区县+详细地址)
            String address = vo.getProvinceName() + vo.getCityName() + vo.getDistrictName() + vo.getWarehouseAddress();
            vo.setWarehouseAddress(address);

            if (vo.getChildList() != null) {
                for (DeliveryPlanChildVO childVO : vo.getChildList()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    // 子出库计划单ID
                    map.put("deliveryPlanId", childVO.getId());
                    // 根据子出库计划单ID查询车辆信息
                    List<VehicleVO> carList = deliveryPlanEntityMapper.findCarByDeliveryPlanNo(map);
                    // 处理车辆的Logo图片
                    for (VehicleVO carVo : carList) {
                        Map<String, Object> mapModelPicturMap = new HashMap<String, Object>();
                        mapModelPicturMap.put("brand", carVo.getBrand());
                        mapModelPicturMap.put("series", carVo.getSeries());
                        mapModelPicturMap.put("model", carVo.getModel());
                        mapModelPicturMap.put("announceYear", carVo.getAnnounceYear());
                        String modelPicturMap = vehicleEntityMapper.getmodelPicture(mapModelPicturMap);
                        carVo.setLogoPicturePath(modelPicturMap);
                    }

                    // 车辆数
                    childVO.setCarNum(carList.size());
                    childVO.setCarList(carList);
                }
            }
        }
        return vo;
    }

    /**
     * <pre>
     * 子出库计划单送达车辆信息
     * </pre>
     * @param id
     *            子出库计划单ID
     * @return
     */
    public DeliveryPlanChildVO findChildDeliveryPlanInfo(Integer id) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);

        DeliveryPlanChildVO vo = deliveryPlanEntityMapper.findChildDeliveryPlanByChildId(paramMap);

        return vo;
    }

    /**
     * <pre>
     * 确认送达更新操作
     * </pre>
     * @param id
     * @param fileNames
     * @param userId
     * @param acceptComment
     * @return 影响数据行数
     */
    public int updateForConfirmArrive(Integer id, String fileNames, String userId, String acceptComment) {

        // 图片名称（固定值）
        String fileName = "childVehiclePath" + id;
        String[] split = fileNames.split(",");
        for (int i = 0; i < split.length; i++) {
            FileEntity fileEntity = new FileEntity();
            // 文件名
            fileEntity.setName("childVehiclePath" + id);
            // 文件路径
            fileEntity.setPath(split[i]);
            fileEntityMapper.insertSelective(fileEntity);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        // 用户ID
        map.put("userId", userId);
        // 行程状态更新为2（0:默认 1:出库交接 2:确认送达 3:身份确认 4:收车交接）
        map.put("routingStatus", 2);
        // 子出库计划单ID
        map.put("id", id);
        // 路径存图片名称，后面去File表中读取图片路径
        map.put("despatchConfirmPicturePath", fileName);
        // 发运交接现场照片张数
        map.put("despatchConfirmPictureNumber", split.length);
        // 送达备注
        map.put("acceptComment", acceptComment);
        return deliveryPlanEntityMapper.updateForConfirmArrive(map);
    }

    /**
     * 根据计划单ID查询计划单信息
     */
    public DeliveryPlanDTO selectByPrimaryKey(Integer id) {
        return deliveryPlanEntityMapper.selectByPrimaryKey(id);
    }
}
