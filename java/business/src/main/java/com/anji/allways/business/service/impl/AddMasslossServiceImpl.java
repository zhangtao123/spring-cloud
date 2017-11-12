/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.anji.allways.business.common.constant.SlnsConstants;
import com.anji.allways.business.entity.DamageEntity;
import com.anji.allways.business.entity.FileEntity;
import com.anji.allways.business.entity.OrderEntity;
import com.anji.allways.business.entity.UserEntity;
import com.anji.allways.business.entity.VehicleEntity;
import com.anji.allways.business.mapper.DamageEntityMapper;
import com.anji.allways.business.mapper.FileEntityMapper;
import com.anji.allways.business.mapper.OrderEntityMapper;
import com.anji.allways.business.mapper.UserEntityMapper;
import com.anji.allways.business.mapper.VehicleEntityMapper;
import com.anji.allways.business.mapper.WarehouseLinkCustomerEntityMapper;
import com.anji.allways.business.service.AddMasslossService;
import com.anji.allways.business.service.IncomeVehicleService;
import com.anji.allways.business.service.SerialNumberBuildService;
import com.anji.allways.business.service.WarehouseOutboundService;
import com.anji.allways.common.util.FileUtil;
import com.anji.allways.common.util.StringUtil;

/**
 * @author Administrator
 * @version $Id: AddMasslossServiceImpl.java, v 0.1 2017年9月15日 下午1:27:41 Administrator Exp $
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AddMasslossServiceImpl implements AddMasslossService {

    @Autowired
    private UserEntityMapper                  userEntityMapper;

    @Autowired
    private SerialNumberBuildService          serialNumberBuildService;

    @Autowired
    private DamageEntityMapper                damageEntityMapper;

    @Autowired
    private FileEntityMapper                  fileEntityMapper;

    @Autowired
    private VehicleEntityMapper               vehicleEntityMapper;

    @Autowired
    private OrderEntityMapper                 orderEntityMapper;

    @Autowired
    private WarehouseOutboundService          warehouseOutboundService;

    @Autowired
    private IncomeVehicleService              incomeVehicleService;

    @Autowired
    private WarehouseLinkCustomerEntityMapper warehouseLinkCustomerEntityMapper;

    /**
     * @return
     * @see com.anji.allways.business.service.AddMasslossService#addMassloss()
     */
    @Override
    public Integer addMassloss(Map<String, Object> map) {
        synchronized (this) {
            int falg = 0;
            VehicleEntity vehicleEntity = vehicleEntityMapper.selectByPrimaryKey(Integer.valueOf(map.get("vehicleId").toString()));
            if (vehicleEntity.getVehicleStatus() != 4 && vehicleEntity.getVehicleStatus() != 9) {
                return 0;
            }

            List<DamageEntity> damageEntityList = damageEntityMapper.selectDamageByVehicleId(Integer.parseInt(map.get("vehicleId").toString()));

            List<DamageEntity> incomeDamage = new ArrayList<DamageEntity>();
            for (DamageEntity damageEntity : damageEntityList) {
                if (damageEntity.getDamageType() == 2) {
                    incomeDamage.add(damageEntity);
                }
            }
            if (incomeDamage.size() == 1) {
                DamageEntity damage = incomeDamage.get(0);
                if (damage.getDamageDetail() == null || damage.getDamageDetail().equals("")) {
                    damageEntityMapper.deleteByPrimaryKey(damage.getId());
                }
            }

            UserEntity userEntity = userEntityMapper.selectByPrimaryKey(Integer.valueOf(map.get("userId").toString()));
            DamageEntity damageEntity = new DamageEntity();
            damageEntity.setNo(serialNumberBuildService.getSerialNumberByType(SlnsConstants.SERIAL_NUMBER_TYPE_DAMAGE));
            damageEntity.setVehicleId(Integer.valueOf(map.get("vehicleId").toString()));
            damageEntity.setDamageType(2);
            damageEntity.setRegister(userEntity.getName());
            damageEntity.setRegisterTime(new Date());
            damageEntity.setConfirmedStatus(0);
            damageEntity.setConfirmer("");
            damageEntity.setDutyOfficer(map.get("dutyOfficer").toString());
            damageEntity.setDamageDetail(map.get("damageDetail").toString());
            damageEntity.setCreateTime(new Date());
            damageEntity.setCreatorName(userEntity.getId().toString());
            damageEntityMapper.insert(damageEntity);
            int id = damageEntity.getId();
            damageEntity = new DamageEntity();
            damageEntity.setId(id);
            damageEntity.setDamagePicturePath("damage" + id);
            damageEntityMapper.updateByPrimaryKeyToPath(damageEntity);
            String[] split = null;
            if (map.get("path") != null && !"".equals(map.get("path"))) {
                split = map.get("path").toString().split(",");
            }
            if (split != null) {
                for (String str : split) {
                    FileEntity fileEntity = new FileEntity();
                    fileEntity.setName("damage" + id);
                    fileEntity.setPath(str);
                    fileEntityMapper.insertSelective(fileEntity);
                }
            }
            if (map.get("orderId") != null && !map.get("orderId").equals("")) {
                falg = incomeVehicleService.sureDriverIncomeVehicle(Integer.valueOf(map.get("vehicleId").toString()), Integer.valueOf(map.get("orderId").toString()),
                    Integer.valueOf(map.get("deliveryId").toString()), Integer.valueOf(map.get("userId").toString()));
            } else {
                falg = 1;
            }
            return falg;
        }
    }

    /**
     * @return
     * @see com.anji.allways.business.service.AddMasslossService#addMassloss()
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Map<String, Object> addMasslossWarehouse(Map<String, Object> map) {
        int rs = -1;
        try {
            Map<String, Object> reMap = new HashMap<String, Object>();
            UserEntity userEntity = userEntityMapper.selectByPrimaryKey(Integer.valueOf(map.get("userId").toString()));

            OrderEntity orderEntity = orderEntityMapper.selectVehicleIdByOrderId(Integer.parseInt(map.get("orderId").toString()));
            if (orderEntity == null) {
                throw new RuntimeException();
            }
            if (!"first".equals(map.get("type")) && orderEntity.getStatus() != 7) {
                reMap.put("reCode", "7");
                return reMap;
            }
            DamageEntity damageEntity = new DamageEntity();
            damageEntity.setNo(serialNumberBuildService.getSerialNumberByType(SlnsConstants.SERIAL_NUMBER_TYPE_DAMAGE));
            damageEntity.setVehicleId(orderEntity.getVehicleId());
            damageEntity.setDamageType(2);
            if ("发运".equals(orderEntity.getTransportType())) {
                damageEntity.setDamageType(1);
            }
            damageEntity.setRegister(userEntity.getName());
            damageEntity.setRegisterTime(new Date());
            damageEntity.setConfirmedStatus(0);
            damageEntity.setConfirmer("");
            damageEntity.setDutyOfficer(map.get("dutyOfficer").toString());
            damageEntity.setDamageDetail(map.get("damageDetail").toString());
            damageEntity.setCreateTime(new Date());
            damageEntity.setCreatorName(userEntity.getName().toString());
            rs = damageEntityMapper.insert(damageEntity);
            if (rs < 0) {
                throw new RuntimeException();
            }
            int id = damageEntity.getId();
            damageEntity = new DamageEntity();
            damageEntity.setId(id);
            damageEntity.setDamagePicturePath("damage" + id);
            rs = damageEntityMapper.updateByPrimaryKeyToPath(damageEntity);

            if (rs < 0) {
                throw new RuntimeException();
            }

            String[] split = null;
            if (map.get("path") != null && !"".equals(map.get("path"))) {
                split = map.get("path").toString().split(",");
            }
            if (split != null) {
                for (String str : split) {
                    FileEntity fileEntity = new FileEntity();
                    fileEntity.setName("damage" + id);
                    fileEntity.setPath(str);
                    rs = fileEntityMapper.insertSelective(fileEntity);
                    if (rs < 0) {
                        throw new RuntimeException();
                    }
                }
            }
            if ("first".equals(map.get("type"))) {
                // 更新运单状态为【确认交接】
                rs = warehouseOutboundService.updateStatusHandover(Integer.parseInt(map.get("orderId").toString()), 7, "order");
                if (rs < 0) {
                    throw new RuntimeException();
                }
                // 更新车辆状态为【确认交接】
                rs = warehouseOutboundService.updateStatusHandover(orderEntity.getVehicleId(), 7, "vehicle");
                if (rs < 0) {
                    throw new RuntimeException();
                }
                Map<String, Object> mm = new HashMap<String, Object>();
                // 更新车辆的id
                mm.put("id", orderEntity.getVehicleId());
                // 更新车辆的质损状态
                mm.put("qualityStatus", 1);
                rs = vehicleEntityMapper.updateQualityStatusById(mm);
                mm.clear();
                mm.put("warehouseId", orderEntity.getWarehouseId());
                mm.put("customerId", orderEntity.getCustomerId());
                rs = warehouseLinkCustomerEntityMapper.updateSpaceAmountForOut(mm);
                if (rs < 0) {
                    throw new RuntimeException();
                }
            }

            List<DamageEntity> damageList = damageEntityMapper.selectDamageByVehicleId(orderEntity.getVehicleId());

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            if (damageList != null && damageList.size() > 0) {

                for (DamageEntity damageEntity2 : damageList) {
                    list.add(toUpdateMassloss(damageEntity2.getId()));
                }
            }
            reMap.put("damageList", list);
            return reMap;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * @param damageId
     * @return
     * @see com.anji.allways.business.service.AddMasslossService#updateMassloss(int)
     */
    @Override
    public Map<String, Object> toUpdateMassloss(Integer damageId) {
        Map<String, Object> mapVue = new HashMap<String, Object>();
        DamageEntity damageEntity = damageEntityMapper.selectByPrimaryKey(damageId);
        mapVue.put("dutyOfficer", damageEntity.getDutyOfficer());
        mapVue.put("id", damageEntity.getId());
        mapVue.put("damageDetail", damageEntity.getDamageDetail());
        mapVue.put("vehicleId", damageEntity.getVehicleId());
        List<Map<String, Object>> list = fileEntityMapper.getVehiclePic(damageEntity.getDamagePicturePath());
        mapVue.put("path", list);
        return mapVue;
    }

    /**
     * @param map
     * @return
     * @see com.anji.allways.business.service.AddMasslossService#updateMassloss(java.util.Map)
     */
    @Override
    public Integer updateMassloss(Map<String, Object> map) {
        DamageEntity damageEntity = damageEntityMapper.selectByPrimaryKey(Integer.valueOf(map.get("damageId").toString()));
        damageEntity.setDutyOfficer(map.get("dutyOfficer").toString());
        damageEntity.setDamageDetail(map.get("damageDetail").toString());

        VehicleEntity vehicleEntity = vehicleEntityMapper.selectByPrimaryKey(Integer.valueOf(map.get("vehicleId").toString()));
        if (vehicleEntity.getVehicleStatus() != 9 && map.get("type").equals(0)) {
            return -1;
        }
        String oldPaths = map.get("oldPath").toString();
        if (!StringUtil.isEmpty(oldPaths, true)) {
            String[] oldPath = oldPaths.split(",");
            for (String old : oldPath) {
                FileUtil.getInstance().delFilePathFromFileName(old);
            }
        }

        String ids = map.get("pathId").toString();
        if (!StringUtil.isEmpty(ids, true)) {
            String[] id = ids.split(",");
            for (String strId : id) {
                fileEntityMapper.deleteByPrimaryKey(Integer.valueOf(strId));
            }
        }

        String newPaths = map.get("newPath").toString();
        if (!StringUtil.isEmpty(newPaths, true)) {
            String[] newPath = newPaths.split(",");
            for (String news : newPath) {
                FileEntity fileEntity = new FileEntity();
                fileEntity.setName("damage" + Integer.valueOf(map.get("damageId").toString()));
                fileEntity.setPath(news);
                fileEntityMapper.insertSelective(fileEntity);
            }
        }
        int count = damageEntityMapper.updateByPrimaryKeySelective(damageEntity);
        return count;
    }

    /**
     * @param vehicleId
     * @return
     * @see com.anji.allways.business.service.AddMasslossService#selectDamageByVehicleIdAndType(int)
     */
    @Override
    public Map<String, Object> selectDamageByVehicleIdAndType(int vehicleId) {
        Map<String, Object> map = new HashMap<String, Object>();
        VehicleEntity vehicleEntity = vehicleEntityMapper.getBydeliveryVehicleId(vehicleId);
        map.put("vin", vehicleEntity.getVin());
        map.put("brand", vehicleEntity.getBrand());
        map.put("series", vehicleEntity.getSeries());
        map.put("model", vehicleEntity.getModel());
        map.put("manufacturerColor", vehicleEntity.getManufacturerColor());
        map.put("standardColor", vehicleEntity.getStandardColor());
        Map<String, Object> mapModelPicturMap = new HashMap<String, Object>();
        mapModelPicturMap.put("brand", vehicleEntity.getBrand());
        mapModelPicturMap.put("series", vehicleEntity.getSeries());
        mapModelPicturMap.put("model", vehicleEntity.getModel());
        mapModelPicturMap.put("announceYear", vehicleEntity.getAnnounceYear());
        String modelPicturMap = vehicleEntityMapper.getmodelPicture(mapModelPicturMap);
        if (modelPicturMap != null) {
            map.put("path", modelPicturMap);
        } else {
            map.put("path", "");
        }

        List<DamageEntity> list = damageEntityMapper.selectDamageByVehicleIdAndType(vehicleId);
        List<DamageEntity> incomeDamage = new ArrayList<DamageEntity>();
        for (DamageEntity damageEntity : list) {
            if (damageEntity.getDamageType() == 2) {
                incomeDamage.add(damageEntity);
            }
        }

        List<Map<String, Object>> listVue = new ArrayList<Map<String, Object>>();
        for (DamageEntity damageEntity : list) {
            Map<String, Object> mapVue = new HashMap<String, Object>();
            mapVue.put("damageId", damageEntity.getId());
            mapVue.put("dutyOfficer", damageEntity.getDutyOfficer());
            mapVue.put("damageDetail", damageEntity.getDamageDetail());
            List<Map<String, Object>> listPic = fileEntityMapper.getVehiclePic(damageEntity.getDamagePicturePath());
            mapVue.put("path", listPic);
            mapVue.put("vehicleId", vehicleId);
            listVue.add(mapVue);
        }
        if (incomeDamage.size() == 1) {
            DamageEntity damage = incomeDamage.get(0);
            if (damage.getDamageDetail() == null || damage.getDamageDetail().equals("")) {
                listVue = new ArrayList<Map<String, Object>>();
            }
        }
        map.put("damageList", listVue);
        return map;
    }

    /**
     * @param damageId
     * @return
     * @see com.anji.allways.business.service.AddMasslossService#deleteMassloss(int)
     */
    @Override
    public int deleteMassloss(int damageId, int vehicleId, int type) {
        VehicleEntity vehicleEntity = vehicleEntityMapper.selectByPrimaryKey(vehicleId);
        if (vehicleEntity.getVehicleStatus() != 9 && type == 0) {
            return -1;
        } else if (vehicleEntity.getVehicleStatus() != 7 && type == 1) {
            return -2;
        }
        int count = 0;
        List<DamageEntity> listDamage = damageEntityMapper.selectDamageByVehicleId(vehicleId);
        List<DamageEntity> inner = new ArrayList<DamageEntity>();
        List<DamageEntity> out = new ArrayList<DamageEntity>();
        List<DamageEntity> income = new ArrayList<DamageEntity>();
        if (listDamage.size() > 0) {
            for (DamageEntity damageEntity : listDamage) {
                if (damageEntity.getDamageType() == 0) {
                    inner.add(damageEntity);
                } else if (damageEntity.getDamageType() == 1) {
                    out.add(damageEntity);
                } else {
                    income.add(damageEntity);
                }
            }
        }

        // 库内质损
        if (type == 2) {
            if (inner.size() == 1) {
                List<Map<String, Object>> list = fileEntityMapper.getVehiclePic("damage" + damageId);
                for (Map<String, Object> map : list) {
                    FileUtil.getInstance().delFilePathFromFileName(map.get("path").toString());
                }
                fileEntityMapper.deleteByPrimaryName("damage" + damageId);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", damageId);
                map.put("damageDetail", "");
                count = damageEntityMapper.updateByPrimaryKeyToDetail(map);
            } else {
                List<Map<String, Object>> list = fileEntityMapper.getVehiclePic("damage" + damageId);
                for (Map<String, Object> map : list) {
                    FileUtil.getInstance().delFilePathFromFileName(map.get("path").toString());
                }
                fileEntityMapper.deleteByPrimaryName("damage" + damageId);
                count = damageEntityMapper.deleteByPrimaryKey(damageId);
            }
        } else if (type == 1) {
            // 出库质损
            if (out.size() == 1) {
                List<Map<String, Object>> list = fileEntityMapper.getVehiclePic("damage" + damageId);
                for (Map<String, Object> map : list) {
                    FileUtil.getInstance().delFilePathFromFileName(map.get("path").toString());
                }
                fileEntityMapper.deleteByPrimaryName("damage" + damageId);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", damageId);
                map.put("damageDetail", "");
                count = damageEntityMapper.updateByPrimaryKeyToDetail(map);
            } else {
                List<Map<String, Object>> list = fileEntityMapper.getVehiclePic("damage" + damageId);
                for (Map<String, Object> map : list) {
                    FileUtil.getInstance().delFilePathFromFileName(map.get("path").toString());
                }
                fileEntityMapper.deleteByPrimaryName("damage" + damageId);
                count = damageEntityMapper.deleteByPrimaryKey(damageId);
            }
        } else if (type == 0) {
            // 收车质损
            if (income.size() == 1) {
                List<Map<String, Object>> list = fileEntityMapper.getVehiclePic("damage" + damageId);
                for (Map<String, Object> map : list) {
                    FileUtil.getInstance().delFilePathFromFileName(map.get("path").toString());
                }
                fileEntityMapper.deleteByPrimaryName("damage" + damageId);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", damageId);
                map.put("damageDetail", "");
                count = damageEntityMapper.updateByPrimaryKeyToDetail(map);
            } else {
                List<Map<String, Object>> list = fileEntityMapper.getVehiclePic("damage" + damageId);
                for (Map<String, Object> map : list) {
                    FileUtil.getInstance().delFilePathFromFileName(map.get("path").toString());
                }
                fileEntityMapper.deleteByPrimaryName("damage" + damageId);
                count = damageEntityMapper.deleteByPrimaryKey(damageId);
            }
        }
        return count;
    }

}
