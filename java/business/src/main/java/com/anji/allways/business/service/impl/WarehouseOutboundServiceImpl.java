package com.anji.allways.business.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.anji.allways.business.common.constant.SlnsConstants;
import com.anji.allways.business.common.constant.SlnsSendMessageConstants;
import com.anji.allways.business.dto.DeliveryPlanDTO;
import com.anji.allways.business.entity.DamageEntity;
import com.anji.allways.business.entity.DeliveryPlanEntity;
import com.anji.allways.business.entity.OrderEntity;
import com.anji.allways.business.entity.ShipReceiptEntity;
import com.anji.allways.business.entity.TruckDriverEntity;
import com.anji.allways.business.entity.UserEntity;
import com.anji.allways.business.entity.VehicleEntity;
import com.anji.allways.business.mapper.DamageEntityMapper;
import com.anji.allways.business.mapper.DeliveryPlanEntityMapper;
import com.anji.allways.business.mapper.FileEntityMapper;
import com.anji.allways.business.mapper.OrderEntityMapper;
import com.anji.allways.business.mapper.ShipReceiptEntityMapper;
import com.anji.allways.business.mapper.VehicleEntityMapper;
import com.anji.allways.business.mapper.WarehouseLinkCustomerEntityMapper;
import com.anji.allways.business.service.AddMasslossService;
import com.anji.allways.business.service.SerialNumberBuildService;
import com.anji.allways.business.service.SlnsSendMessageService;
import com.anji.allways.business.service.UserService;
import com.anji.allways.business.service.WarehouseOutboundService;
import com.anji.allways.business.vo.DeliveryPlanVO;
import com.anji.allways.business.vo.DestinationVO;
import com.anji.allways.business.vo.HomePageVO;
import com.anji.allways.business.vo.OrderVO;
import com.anji.allways.business.vo.ShipReceiptVO;
import com.anji.allways.business.vo.VehicleVO;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.DateUtil;
import com.anji.allways.common.util.PageUtil;
import com.anji.allways.common.util.StringUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class WarehouseOutboundServiceImpl implements WarehouseOutboundService {

    @Autowired
    private DeliveryPlanEntityMapper          deliveryPlanMapper;

    @Autowired
    private OrderEntityMapper                 orderEntityMapper;

    @Autowired
    private DamageEntityMapper                damageEntityMapper;

    @Autowired
    private SerialNumberBuildService          serialNumberBuildService;

    @Autowired
    private VehicleEntityMapper               vehicleEntityMapper;

    @Autowired
    private ShipReceiptEntityMapper           shipReceiptEntityMapper;

    @Autowired
    private FileEntityMapper                  fileEntityMapper;

    @Autowired
    private AddMasslossService                addMasslossService;

    @Autowired
    private UserService                       userService;

    @Autowired
    private SlnsSendMessageConstants          slnsSendConstants;

    @Autowired
    private SlnsSendMessageService            slnsSendMessageService;

    @Autowired
    private WarehouseLinkCustomerEntityMapper warehouseLinkCustomerEntityMapper;

    /**
     * 分页查询出库计划
     * @param DeliveryPlanEntity
     *            bean
     * @param Integer
     *            pageNum
     * @param Integer
     *            pageRows
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> queryDeliveryPlan(DeliveryPlanDTO bean, Integer pageNum, Integer pageRows) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 获取总件数
        Integer num = deliveryPlanMapper.selectDeliveryPlanCount(bean);
        // 获取仓库列表信息
        List<DeliveryPlanDTO> list = null;
        if (num > 0) {
            // 每页显示数量
            bean.setPageNumber(pageRows);
            // 总数量
            bean.setTotalNumber(num);
            // 当前页数
            bean.setCurrentPage(pageNum);
            // 查询分页信息
            list = deliveryPlanMapper.selectDeliveryPlan(bean);
        }
        map.put("total", num);
        map.put("rows", list);
        return map;
    }

    /**
     * 根据电话号码查询司机信息
     * @param String
     *            mobile
     * @return TruckDriverEntity
     */
    @Override
    public Map<String, Object> selectDeliveryInfoByDriverMobile(String mobile) {
        TruckDriverEntity truckDriverEntity = deliveryPlanMapper.selectDriverByMobile(mobile);
        if (truckDriverEntity != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", truckDriverEntity.getId());
            map.put("transportCompanyName", truckDriverEntity.getTransportCompanyName());
            map.put("truckNumber", truckDriverEntity.getTruckNumber());
            map.put("name", truckDriverEntity.getName());
            map.put("mobile", truckDriverEntity.getMobile());
            return map;
        }
        return null;
    }

    /**
     * 根据登录人获取仓库信息完善调度信息
     * @param String
     *            userId
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> selectWareHouseNameByUserId(String userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        DeliveryPlanEntity deliveryPlan = deliveryPlanMapper.selectWareHouseNameByUserId(userId);
        if (deliveryPlan != null) {
            map.put("WarehouseName", deliveryPlan.getWarehouseName());
            map.put("warehouseId", deliveryPlan.getWarehouseId());
        }
        return map;
    }

    /**
     * 根据查询条件获取运输单列表
     * @param Map<String,Object>
     *            map
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> selectOrderListByCondition(Map<String, Object> map) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
        Integer num = null;
        // 查询数据库获取订单总件数
        if ("fy".equals(map.get("flag"))) {
            num = orderEntityMapper.selectOrderFYByConditionCount(map);
        } else {
            num = orderEntityMapper.selectOrderZTByConditionCount(map);
        }
        // 获取分页信息
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNumber((int) map.get("pageNumber"));
        pageUtil.setTotalNumber(num);
        pageUtil.setCurrentPage((int) map.get("currentPage"));
        map.put("dbIndex", pageUtil.getDbIndex());
        map.put("dbNumber", pageUtil.getDbNumber());
        DateUtil dateUtil = DateUtil.getInstance();
        if ("fy".equals(map.get("flag"))) {
            // 获取订单列表信息
            List<OrderEntity> retOrderListFy = orderEntityMapper.selectOrderListFYByCondition(map);

            if (retOrderListFy != null) {
                for (int i = 0; i < retOrderListFy.size(); i++) {
                    Map<String, Object> mapVal = new HashMap<String, Object>();
                    OrderEntity order = retOrderListFy.get(i);
                    mapVal.put("id", order.getId());
                    mapVal.put("no", order.getNo());
                    mapVal.put("createTime", dateUtil.formatAll(order.getCreateTime(), DateUtil.TIMENOWPATTERN));
                    mapVal.put("vin", order.getVin());
                    mapVal.put("customer", order.getCustomer());
                    mapVal.put("destination", order.getDestination());
                    list.add(mapVal);
                }
            }

        } else {
            // 分组后的列表
            List<OrderVO> retOrderList = orderEntityMapper.selectOrderListZTByCondition(map);
            if (retOrderList != null) {
                for (int i = 0; i < retOrderList.size(); i++) {
                    List<Map<String, Object>> slist = new LinkedList<Map<String, Object>>();
                    OrderVO orderVO = retOrderList.get(i);

                    // 查询每个组里面的明细
                    // 如果idList为空，表示是普通用户查询，根据创建人查询，如果idList不为空则查询该仓库下所有
                    if (map.get("idList") != null) {
                        orderVO.setCreateUser(null);
                    }
                    List<OrderVO> retOrderSub = orderEntityMapper.selectOrderSubZTByCondition(orderVO);
                    for (int j = 0; j < retOrderSub.size(); j++) {
                        Map<String, Object> mapSub = new HashMap<String, Object>();
                        mapSub.put("id", retOrderSub.get(j).getId());
                        mapSub.put("no", retOrderSub.get(j).getNo());
                        mapSub.put("createTime", dateUtil.formatAll(retOrderSub.get(j).getCreateTime(), DateUtil.TIMENOWPATTERN));
                        mapSub.put("vin", retOrderSub.get(j).getVin());
                        mapSub.put("customer", retOrderSub.get(j).getCustomer());
                        mapSub.put("PickupSelfTime", dateUtil.formatAll(retOrderSub.get(j).getPickupSelfTime(), "yyyy-MM-dd HH:mm:ss"));
                        slist.add(mapSub);
                    }

                    Map<String, Object> mapVal = new HashMap<String, Object>();
                    mapVal.put("PickupSelfTime", dateUtil.formatAll(orderVO.getPickupSelfTime(), "yyyy-MM-dd HH:mm:ss"));
                    mapVal.put("customer", orderVO.getCustomer());
                    mapVal.put("warehouseName", orderVO.getWarehouseName());
                    mapVal.put("orderNum", orderVO.getOrderNum());
                    mapVal.put("consigneeMobile", orderVO.getConsigneeMobile());
                    mapVal.put("consigneeName", orderVO.getConsigneeName());
                    mapVal.put("consigneeId", orderVO.getConsigneeId());
                    mapVal.put("warehouseId", orderVO.getWarehouseId());
                    mapVal.put("data", slist);
                    list.add(mapVal);
                }
            }
        }
        reMap.put("rows", list);
        reMap.put("total", num);
        return reMap;
    }

    /**
     * 新增出库计划单，包括（发运：需要拆分）和（自提：无需拆分）
     * @param int
     *            userId
     * @param DeliveryPlanDTO
     *            deliveryPlanEntity
     * @param String
     *            orderIds
     * @param String
     *            type
     * @return Integer
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Integer addDeliveryPlan(int userId, DeliveryPlanDTO deliveryPlanEntity, String orderIds, int type) {
        int rs = -1;
        try {
            // 更新所包含的运单状态
            String[] lists = orderIds.split(",");
            List<Long> list = new ArrayList<Long>();
            for (int i = 0; i < lists.length; i++) {
                list.add(Long.valueOf(lists[i]));
            }
            Map<String, Object> chackMap = new HashMap<String, Object>();
            chackMap.put("idList", list);
            int count = orderEntityMapper.checkOrderIsCancel(chackMap);
            if (count > 0) {
                return 5015;
            }
            // 获取到仓库信息
            DeliveryPlanEntity deliveryPlan = deliveryPlanMapper.selectWareHouseNameByUserId(userId + "");
            if (deliveryPlan == null) {
                throw new RuntimeException();
            } else {
                deliveryPlanEntity.setWarehouseId(deliveryPlan.getWarehouseId());
                deliveryPlanEntity.setWarehouseName(deliveryPlan.getWarehouseName());
            }
            // 获取到操作人名称
            UserEntity userEntity = userService.queryUserEntityByUserId(userId);
            if (userEntity == null) {
                throw new RuntimeException();
            } else {
                deliveryPlanEntity.setOperator(userEntity.getName());
                deliveryPlanEntity.setWarehouseId(userEntity.getCustomerId());
            }
            // 获取出库计划单号
            String serialNo = serialNumberBuildService.getSerialNumberByType(SlnsConstants.SERIAL_NUMBER_TYPE_DELIVERY);
            deliveryPlanEntity.setNo(serialNo);
            // 默认是父计划
            deliveryPlanEntity.setParentId(0);
            // 计划创建时间
            deliveryPlanEntity.setCreateTime(new Date());
            // 计划创建人
            deliveryPlanEntity.setCreateUser(userId);
            // 计划单默认的是父类
            deliveryPlanEntity.setType(0);
            // 发运（待出库）
            deliveryPlanEntity.setStatus(3);
            if (type == 2) {
                // 自提（待自提）
                // 自提计划单默认的是子类
                deliveryPlanEntity.setType(1);
                deliveryPlanEntity.setStatus(2);
            }
            // 返回插入的计划单编号
            rs = deliveryPlanMapper.insertSelective(deliveryPlanEntity);
            if (rs < 0) {
                throw new RuntimeException();
            }
            int deliveryPlanId = deliveryPlanEntity.getId();

            if (type == 2) {
                // 新增自提计划单
                rs = addDeliveryPlanZt(deliveryPlanId, list, deliveryPlanEntity, serialNo);
                if (rs > 0) {
                    return 1;
                } else {
                    throw new RuntimeException();
                }
            }
            // 根据所选的运单进行拆分生成多个子出库计划单
            List<OrderEntity> orderEntity = orderEntityMapper.selectOrderGroup(list);
            if (orderEntity == null) {
                return null;
            } else {
                int i;
                // 需要进行拆分
                for (i = 0; i < orderEntity.size(); i++) {
                    // 更新子计划单下的运输单
                    // 【新增子出库计划单】
                    OrderEntity order = orderEntity.get(i);
                    // 先增加一个子出库计划
                    serialNo = serialNumberBuildService.getSerialNumberByType(SlnsConstants.SERIAL_NUMBER_TYPE_DELIVERY);
                    DeliveryPlanEntity deliveryPlanEntitysub = new DeliveryPlanEntity();
                    try {
                        BeanUtils.copyProperties(deliveryPlanEntitysub, deliveryPlanEntity);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    // 该计划单是子类
                    deliveryPlanEntitysub.setType(1);
                    // 设置副计划单id
                    deliveryPlanEntitysub.setParentId(deliveryPlanId);
                    deliveryPlanEntitysub.setNo(serialNo);
                    deliveryPlanEntitysub.setCustomer(order.getCustomer());
                    // 设置出库计划收货人id
                    deliveryPlanEntitysub.setConsigneeId(String.valueOf(order.getConsigneeId()));
                    // 插入出库计划单
                    rs = deliveryPlanMapper.insertSelective(deliveryPlanEntitysub);
                    if (rs < 0) {
                        throw new RuntimeException();
                    }
                    // 出库计划单编号【更新运单状态】
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("customer", order.getCustomer());
                    map2.put("destination", order.getDestination());
                    map2.put("consigneeId", order.getConsigneeId());
                    map2.put("deliveryPlanId", deliveryPlanEntitysub.getId());
                    map2.put("status", 3);
                    map2.put("list", list);
                    // 待出库状态【待出库：3】
                    rs = orderEntityMapper.updateOrderDeliveryPlanIdSub(map2);
                    if (rs < 0) {
                        throw new RuntimeException();
                    }
                }
                // 查询车辆信息
                Map<String, Object> vehicleMapFy = new HashMap<String, Object>();
                vehicleMapFy.put("list", list);
                List<OrderEntity> orderFy = orderEntityMapper.selectVehicleId(vehicleMapFy);
                if (orderFy != null) {
                    List<Long> vehicleList = new ArrayList<Long>();
                    for (int j = 0; j < orderFy.size(); j++) {
                        vehicleList.add(Long.valueOf(orderFy.get(j).getVehicleId()));
                    }
                    if (vehicleList.size() == 0) {
                        throw new RuntimeException();
                    }
                    // 设置汽车的状态为待出库【待出库：3】(更新汽车状态)
                    Map<String, Object> vehicleMap = new HashMap<String, Object>();
                    vehicleMap.put("vehicleStatus", 3);
                    vehicleMap.put("list", vehicleList);
                    // 更新车辆信息
                    rs = vehicleEntityMapper.updateVehicleStatus(vehicleMap);
                    if (rs < 0) {
                        throw new RuntimeException();
                    }
                }
                return i;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private int addDeliveryPlanZt(int deliveryPlanId, List<Long> list, DeliveryPlanDTO deliveryPlanEntity, String serialNo) {
        int rs = -1;
        // （自提）
        // 自提只会生成一个出库计划单，直接更新订单的状态即可
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("deliveryPlanId", deliveryPlanId);
        // 自提时，运输单新增后状态是【待自提：2】(更新运输单状态)
        map.put("status", 2);
        map.put("list", list);
        rs = orderEntityMapper.updateOrderDeliveryPlanId(map);
        if (rs < 0) {
            throw new RuntimeException();
        }
        // 遍历查询所有车辆的id集合
        List<OrderEntity> orderZt = orderEntityMapper.selectVehicleId(map);
        if (orderZt != null) {
            List<Long> vehicleList = new ArrayList<Long>();
            for (int i = 0; i < orderZt.size(); i++) {
                vehicleList.add(Long.valueOf(orderZt.get(i).getVehicleId()));
            }
            // 设置汽车的状态为待自提【待自提：2】(更新汽车状态)
            Map<String, Object> vehicleMap = new HashMap<String, Object>();
            vehicleMap.put("vehicleStatus", 2);
            vehicleMap.put("list", vehicleList);
            rs = vehicleEntityMapper.updateVehicleStatus(vehicleMap);
            if (rs < 0) {
                throw new RuntimeException();
            } else {
                DateUtil dateUtil = DateUtil.getInstance();
                String mobile = deliveryPlanEntity.getMobile();
                JSONObject tempPara = new JSONObject();
                // 自提出库计划编号
                tempPara.put("selfNo", serialNo);
                tempPara.put("selfAdress", deliveryPlanEntity.getWarehouseName());
                tempPara.put("selfTime", dateUtil.formatAll(deliveryPlanEntity.getPickupPlanTime(), DateUtil.TIMENOWPATTERN));
                slnsSendMessageService.sendSingleMessage(Integer.parseInt(slnsSendConstants.getSmsOutStorageId()), Integer.parseInt(slnsSendConstants.getSendTypeSync()),
                    Integer.parseInt(slnsSendConstants.getIsRetryNo()), mobile, tempPara);
            }
        }
        return rs;
    }

    /**
     * 查询出库计划单详情（自提）
     * @param int
     *            Id
     * @return DeliveryPlanVO
     */
    @Override
    public DeliveryPlanVO selectDeliveryDetailZT(int id) throws Exception {

        DeliveryPlanVO deliveryPlanVO = deliveryPlanMapper.selectDeliveryPlanInfoByIdZT(id);
        if (deliveryPlanVO != null) {
            String name = deliveryPlanVO.getIdentityCardPicturePath();
            List<Map<String, Object>> list = fileEntityMapper.getVehiclePic(name);
            if (list != null && list.size() > 0) {
                String rePath;
                StringBuffer path = new StringBuffer();
                for (Map<String, Object> map : list) {
                    path.append(map.get("path")).append(",");
                }
                rePath = path.toString();
                deliveryPlanVO.setIdentityCardPicturePath(rePath);
            }
        }
        return deliveryPlanVO;
    }

    /**
     * 查询出库计划单详情（发运）
     * @param int
     *            Id
     * @return Map<String,Object>
     */
    @Override
    public Map<String, Object> selectDeliveryDetailFY(int id) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        // DeliveryPlanEntity deliveryPlanVO =
        // deliveryPlanMapper.selectByPrimaryKey(id);

        // 根据父订单id去找父订单
        DeliveryPlanDTO deliveryPlanPar = deliveryPlanMapper.selectDeliveryPlanPar(id);
        map.put("parNo", deliveryPlanPar.getId());
        map.put("deliveryPar", deliveryPlanPar);
        // 存在父计划单,查询父计划单下所有的子计划
        List<DeliveryPlanDTO> deliveryPlanVOList = deliveryPlanMapper.selectDeliveryPlanSub(id);
        DestinationVO destinationVO = null;
        List<Map<String, Object>> subList = new LinkedList<Map<String, Object>>();
        // 遍历每个子计划单
        for (int i = 0; i < deliveryPlanVOList.size(); i++) {
            Map<String, Object> mapValue = new HashMap<String, Object>();
            destinationVO = deliveryPlanMapper.selectDeliveryPlanInfoByIdFY(deliveryPlanVOList.get(i).getId());

            String pathName = destinationVO.getDespatchConfirmPicturePath();
            List<Map<String, Object>> list = fileEntityMapper.getVehiclePic(pathName);
            if (list != null && list.size() > 0) {
                List<String> listPath = new ArrayList<String>();
                for (Map<String, Object> map1 : list) {
                    listPath.add((String) map1.get("path"));
                }
                destinationVO.setDespatchConfirmPicturePathList(listPath);
            }

            mapValue.put("id", deliveryPlanVOList.get(i).getId());
            mapValue.put("key", deliveryPlanVOList.get(i).getNo());
            mapValue.put("shipNo", deliveryPlanVOList.get(i).getShipNo());
            mapValue.put("data", destinationVO);
            subList.add(mapValue);
        }
        map.put("deliverySub", subList);
        return map;
    }

    /**
     * 出库计划管理（提取钥匙）（审核）(交接单的生成)（自提）
     * @param int
     *            Id
     * @return int
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public DeliveryPlanDTO updateDeliveryPlanZt(DeliveryPlanDTO record, String type) {
        // 证件信息状态0：待审核，1：审核通过，2：审核不通过
        // 取消：status：6
        int rs = -1;
        int deliveryPlanId = record.getId();
        DeliveryPlanDTO deliveryPlan = null;
        try {

            if ("SH".equals(type) || "YS".equals(type)) {
                // 审核计划单操作，更新identityCardStatus为审核已通过
                rs = deliveryPlanMapper.updateIdentityCardStatus(record);
                if (rs < 0) {
                    throw new RuntimeException();
                }
                // 得到计划单的id
                deliveryPlan = deliveryPlanMapper.selectByPrimaryKey(deliveryPlanId);
                // 是否领取钥匙，领取钥匙后更新计划单status为4：待收货
                if (deliveryPlan != null && deliveryPlan.getKeyIsTaken() == 1) {

                    String shipNo = "";
                    Map<String, Object> shipMap = saveReceiptEntity(record);
                    if (RespCode.SUCCESS.equals(shipMap.get("code"))) {
                        shipNo = (String) shipMap.get("shipNo");
                        // 如果是自提计划单，设置计划单状态,运单,车辆的状态为待收货【发运类领取钥匙时不改变状态】
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", deliveryPlanId);
                        map.put("status", 4);
                        map.put("shipNo", shipNo);
                        rs = deliveryPlanMapper.updateDeliveryPlanStruts(map);
                        if (rs < 0) {
                            throw new RuntimeException();
                        }

                        // 更新车辆状态：待收货：【4】
                        List<VehicleVO> vehicleList = vehicleEntityMapper.selectVehicleIdByDeliveryPlanId(deliveryPlanId);
                        if (vehicleList.size() == 0) {
                            throw new RuntimeException();
                        }
                        List<Long> list = new ArrayList<Long>();
                        for (VehicleVO vehicleVO : vehicleList) {
                            list.add(Long.valueOf(vehicleVO.getId()));
                        }
                        Map<String, Object> mapVehicle = new HashMap<String, Object>();
                        mapVehicle.put("list", list);
                        mapVehicle.put("deliveryPlanId", deliveryPlanId);
                        mapVehicle.put("vehicleStatus", 4);
                        rs = vehicleEntityMapper.updateVehicleStatus(mapVehicle);
                        if (rs < 0) {
                            throw new RuntimeException();
                        }

                        // 更新运单状态：待收货：【4】
                        map.put("deliveryPlanId", deliveryPlanId);
                        rs = orderEntityMapper.updateOrderStrutsByDeliveryPlanId(map);
                        if (rs < 0) {
                            throw new RuntimeException();
                        }

                        // 表示新增自提交接单完成
                        deliveryPlan = deliveryPlanMapper.selectByPrimaryKey(deliveryPlanId);
                    }
                }
            }
            if ("ZP".equals(type)) {
                rs = deliveryPlanMapper.updateIdentityCardStatus(record);
                if (rs > 0) {
                    return deliveryPlanMapper.selectByPrimaryKey(deliveryPlanId);
                } else {
                    throw new RuntimeException();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return deliveryPlan;
    }

    /**
     * 出库计划管理（提取钥匙）(交接单的生成)(发运)
     * @param int
     *            Id 父出库计划id
     * @return int
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public DeliveryPlanDTO updateDeliveryPlanFy(DeliveryPlanDTO record) {
        int rs = -1;
        DeliveryPlanDTO deliveryPlan = null;
        // 父出库计划单的id
        int deliveryPlanId = record.getId();
        try {
            // 领取钥匙,更新钥匙状态
            rs = deliveryPlanMapper.updatekeyIsTakenById(record);
            if (rs < 0) {
                throw new RuntimeException();
            }
            // 查询更新完的对象状态
            deliveryPlan = deliveryPlanMapper.selectByPrimaryKey(deliveryPlanId);
            // 是否领取钥匙，领取后新增交接单号
            if (deliveryPlan != null && deliveryPlan.getKeyIsTaken() == 1) {
                // 生成车辆交接单【每个子出库计划单生成一个对应的交接单】
                List<DeliveryPlanDTO> deliveryPlanDTO = deliveryPlanMapper.selectDeliveryPlanSub(deliveryPlanId);
                for (DeliveryPlanDTO deliveryPlan1 : deliveryPlanDTO) {
                    // 生成车辆交接单【自提只生成一个交接单】
                    deliveryPlan1.setUpdateUser(record.getUpdateUser());
                    Map<String, Object> shipMap = saveReceiptEntity(deliveryPlan1);
                    if (RespCode.SUCCESS.equals(shipMap.get("code"))) {
                        String shipNo = (String) shipMap.get("shipNo");
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", deliveryPlan1.getId());
                        map.put("shipNo", shipNo);
                        // 更新计划单的交接单号
                        rs = deliveryPlanMapper.updateDeliveryPlanStruts(map);
                        if (rs < 0) {
                            throw new RuntimeException();
                        }
                    } else {
                        throw new RuntimeException();
                    }
                }
            }
            return deliveryPlanMapper.selectByPrimaryKey(deliveryPlanId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * 取消出库计划
     * @param int
     *            Id 出库计划id
     * @return DeliveryPlanDTO
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public DeliveryPlanDTO cancelDeliveryPlan(DeliveryPlanDTO record) {
        int rs = -1;
        DeliveryPlanDTO deliveryPlan = null;
        // 出库计划单的id
        int deliveryPlanId = record.getId();
        try {

            List<VehicleVO> vehicleList = vehicleEntityMapper.selectVehicleIdByDeliveryPlanIdFy(deliveryPlanId);
            StringBuffer sb = new StringBuffer("");
            for (VehicleVO vehicleVO : vehicleList) {
                sb.append(vehicleVO.getId() + ",");
            }
            // 更新车辆状态待调度
            rs = updateStatusEditZt(sb.toString(), deliveryPlanId, 1, "vehicle");
            if (rs < 0) {
                throw new RuntimeException();
            }

            // 更新运单状态待调度,没有去掉所属计划单号
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 1);
            map.put("deliveryPlanId", deliveryPlanId);
            rs = orderEntityMapper.updateOrderStrutsByDeliveryPlanId(map);
            if (rs < 0) {
                throw new RuntimeException();
            }

            // 取消计划单的
            map.clear();
            map.put("status", 6);
            map.put("id", deliveryPlanId);
            rs = deliveryPlanMapper.updateDeliveryPlanStruts(map);
            if (rs < 0) {
                throw new RuntimeException();
            }
            deliveryPlan = deliveryPlanMapper.selectByPrimaryKey(deliveryPlanId);

            if ("FY".equals(record.getDoType())) {
                int deliveryPlanParId = deliveryPlan.getParentId();
                List<DeliveryPlanDTO> deliveryPlanDTO = deliveryPlanMapper.selectDeliveryPlanSub(deliveryPlanParId);
                if (deliveryPlanDTO != null && deliveryPlanDTO.size() == 0) {
                    Map<String, Object> map1 = new HashMap<String, Object>();
                    map1.put("id", deliveryPlanParId);
                    map1.put("status", 6);
                    rs = deliveryPlanMapper.updateDeliveryPlanStruts(map1);
                    if (rs < 0) {
                        throw new RuntimeException();
                    }
                }
            }
            if (rs > 0) {

                // ************发送取消短信通知**************
                JSONObject tempPara = new JSONObject();
                DeliveryPlanDTO delivery = deliveryPlanMapper.selectMessageInfo(deliveryPlanId);
                // 收货人电话
                String mobile = delivery.getMobile();
                // 仓库管理员电话
                tempPara.put("selfMobile", delivery.getContactorMobile());
                // 自提出库计划编号
                tempPara.put("selfNo", deliveryPlan.getNo());
                // 仓库名
                tempPara.put("selfAdress", delivery.getWarehouseName());
                slnsSendMessageService.sendSingleMessage(Integer.parseInt(slnsSendConstants.getSmsCancleStorageId()), Integer.parseInt(slnsSendConstants.getSendTypeSync()),
                    Integer.parseInt(slnsSendConstants.getIsRetryNo()), mobile, tempPara);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return deliveryPlanMapper.selectByPrimaryKey(deliveryPlanId);
    }

    /**
     * 计划单编辑【发运和自提】
     * @param String
     *            orderIds 新增的运单id
     * @param int
     *            deliveryPlanId出库计划id
     * @param int
     *            deliveryPlanParId父出库计划单id
     * @param int
     *            userId 登录用户id
     * @param String
     *            type：FY：发运
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int updateDeliveryPlanOrderForEditZt(String orderIds, int deliveryPlanId, int userId, String type) {
        int rs = -1;
        List<VehicleVO> vehicleList = new ArrayList<VehicleVO>();
        // 更新计划下所有的运单为待调度
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", 1);
        map.put("deliveryPlanId", deliveryPlanId);
        // 更新车辆状态：待调度，该计划单下所有的【待自提】或者【待出库】车辆
        vehicleList = vehicleEntityMapper.selectVehicleIdByDeliveryPlanId(deliveryPlanId);
        StringBuffer sb = new StringBuffer("");
        for (VehicleVO vehicleVO : vehicleList) {
            sb.append(vehicleVO.getId() + ",");
        }
        rs = updateStatusEditZt(sb.toString(), deliveryPlanId, 1, "vehicle");
        if (rs < 0) {
            throw new RuntimeException();
        }

        // 更新计划单下所有运单的状态为待调度
        rs = orderEntityMapper.updateOrderStrutsForEditInit(map);
        if (rs < 0) {
            throw new RuntimeException();
        }

        if (StringUtils.isNotEmpty(orderIds)) {

            if ("FY".equals(type)) {
                // 更新选中的运单状态为待自提
                rs = updateStatusEditZt(orderIds, deliveryPlanId, 3, "order");
                if (rs < 0) {
                    throw new RuntimeException();
                }

                // 更新选中运单下车辆状态为待自提
                // ===========================================================
                vehicleList = vehicleEntityMapper.selectVehicleIdByDeliveryPlanId(deliveryPlanId);
                StringBuffer sb1 = new StringBuffer("");
                for (VehicleVO vehicleVO : vehicleList) {
                    sb1.append(vehicleVO.getId() + ",");
                }
                rs = updateStatusEditZt(sb1.toString(), deliveryPlanId, 3, "vehicle");
                if (rs < 0) {
                    throw new RuntimeException();
                }
            } else {
                // 更新选中的运单状态为待自提
                rs = updateStatusEditZt(orderIds, deliveryPlanId, 2, "order");
                if (rs < 0) {
                    throw new RuntimeException();
                }

                // 更新选中运单下车辆状态为待自提
                // ===========================================================
                vehicleList = vehicleEntityMapper.selectVehicleIdByDeliveryPlanId(deliveryPlanId);
                StringBuffer sb1 = new StringBuffer("");
                for (VehicleVO vehicleVO : vehicleList) {
                    sb1.append(vehicleVO.getId() + ",");
                }
                rs = updateStatusEditZt(sb1.toString(), deliveryPlanId, 2, "vehicle");
                if (rs < 0) {
                    throw new RuntimeException();
                }
            }
        } else {
            // 是否存在已经交接的运单
            rs = orderEntityMapper.selectCountHandover(deliveryPlanId);
            if (rs == 0) {
                Map<String, Object> map1 = new HashMap<String, Object>();
                map1.put("status", 6);
                map1.put("id", deliveryPlanId);
                rs = deliveryPlanMapper.updateDeliveryPlanStruts(map1);
                if (rs < 0) {
                    throw new RuntimeException();
                }
                if ("FY".equals(type)) {
                    // 查询该出库计划所在的父计划下所包含的【子计划的个数】
                    int deliveryPlanParId = -1;
                    DeliveryPlanDTO deliveryPlanPar = deliveryPlanMapper.selectDeliveryPlanPar(deliveryPlanId);
                    if (deliveryPlanPar != null) {
                        deliveryPlanParId = deliveryPlanPar.getParentId();
                    } else {
                        throw new RuntimeException();
                    }

                    List<DeliveryPlanDTO> deliveryPlanDTO = deliveryPlanMapper.selectDeliveryPlanSub(deliveryPlanParId);

                    if (deliveryPlanDTO != null && deliveryPlanDTO.size() == 0) {
                        map1.put("id", deliveryPlanParId);
                        rs = deliveryPlanMapper.updateDeliveryPlanStruts(map1);
                        if (rs < 0) {
                            throw new RuntimeException();
                        }
                    }
                }
            }
        }
        return rs;
    }

    /**
     * 更新运单和车辆的状态【加入计划和取消计划】批量更新
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int updateStatusEditZt(String orderIds, int deliveryPlanId, int status, String type) {
        int re = -1;
        try {
            if (StringUtils.isNotEmpty(orderIds)) {
                // 【更新新增的运单状态】
                String[] lists = orderIds.split(",");
                // 所有要更新的运单集合
                List<Long> orderIdList = new ArrayList<Long>();
                for (int i = 0; i < lists.length; i++) {
                    orderIdList.add(Long.valueOf(lists[i]));
                }
                if (orderIdList.size() == 0) {
                    throw new RuntimeException();
                }
                Map<String, Object> mapOrder = new HashMap<String, Object>();
                mapOrder.put("list", orderIdList);
                mapOrder.put("status", status);
                mapOrder.put("deliveryPlanId", deliveryPlanId);
                mapOrder.put("vehicleStatus", status);
                // 新增运单到自提计划
                if ("order".equals(type)) {

                    re = orderEntityMapper.updateOrderDeliveryPlanId(mapOrder);
                    if (re < 0) {
                        throw new RuntimeException();
                    }
                }
                // 更新运单中的车辆
                if ("vehicle".equals(type)) {
                    re = vehicleEntityMapper.updateVehicleStatus(mapOrder);
                    if (re < 0) {
                        throw new RuntimeException();
                    }
                }
                return re;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return -1;
    }

    /**
     * 车辆交接时生成车辆交接记录(统一在领取钥匙时操作)
     * @param DamageEntity
     *            damageEntity
     * @param int
     *            id 子出库计划单id
     * @return Map<String,Object>
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Map<String, Object> saveReceiptEntity(DeliveryPlanDTO deliveryPlan) {
        int rs = -1;
        Map<String, Object> map = new HashMap<String, Object>();
        // 【新增交接单号】:当出库计划开始点击交接时，立即生成交接单号;
        // 获取交接单号
        String no = serialNumberBuildService.getSerialNumberByType(SlnsConstants.SERIAL_NUMBER_TYPE_SHIP);
        ShipReceiptVO shipReceiptVO = null;
        // 获取到创建者名称
        String register = "";
        // 1：表示操作的父出库计划交接单
        if (deliveryPlan != null) {
            if (deliveryPlan.getType() == 1) {
                shipReceiptVO = shipReceiptEntityMapper.selectByPrimaryPlanId(deliveryPlan.getId());
            } else {
                shipReceiptVO = shipReceiptEntityMapper.selectShipReceipt(deliveryPlan.getId());
            }

            UserEntity userEntity = userService.queryUserEntityByUserId(deliveryPlan.getUpdateUser());

            if (userEntity != null) {
                register = userEntity.getName();
            }
        }
        ShipReceiptEntity shipReceiptEntity = new ShipReceiptEntity();
        if (shipReceiptVO != null) {
            shipReceiptEntity.setTruckDriverId(shipReceiptVO.getTruckDriverId());
            shipReceiptEntity.setCustomer(shipReceiptVO.getCustomer());
            shipReceiptEntity.setDeliveryPlanNo(shipReceiptVO.getDeliveryPlanNo());
            shipReceiptEntity.setReceivingAddress(shipReceiptVO.getReceivingAddress());
            shipReceiptEntity.setCreateTime(new Date());
            shipReceiptEntity.setCreatorName(register);
            shipReceiptEntity.setNo(no);
            rs = shipReceiptEntityMapper.insertSelective(shipReceiptEntity);
            if (rs > 0) {
                map.put("code", RespCode.SUCCESS);
                map.put("msg", RespMsg.SUCCESS_MSG);
                map.put("shipNo", no);
            } else {
                map.put("code", RespCode.ADD_SHIP_RECEIPT_FAILED);
                map.put("msg", RespMsg.ADD_SHIP_RECEIPT_FAILED_MSG);
                return map;
            }
        }
        return map;
    }

    /**
     * 无质损交接
     * @param DamageEntity
     *            damageEntity
     * @param int
     *            id 运单的id
     * @return int
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int saveDamageEntity(DamageEntity damage, int id, String type, String userId) {
        int rs = -1;
        int vehicleId = -1;
        try {
            // 生成质损单号
            String damageNo = null;
            // 查询车辆相关信息
            OrderEntity orderEntity = orderEntityMapper.selectVehicleIdByOrderId(id);
            if (orderEntity != null) {
                vehicleId = orderEntity.getVehicleId();
                if ("发运".equals(orderEntity.getTransportType())) {
                    damage.setDamageType(1);
                } else {
                    damage.setDamageType(2);
                }
            } else {
                throw new RuntimeException();
            }
            // 获取到操作人名称
            UserEntity userEntity = userService.queryUserEntityByUserId(Integer.parseInt(userId));
            if (userEntity == null) {
                // 登记人
                damage.setRegister(null);
                // 创建者
                damage.setCreatorName(null);
            } else {
                damage.setRegister(userEntity.getName());
                damage.setCreatorName(userEntity.getName());
            }
            // 质损单编号获取
            damageNo = serialNumberBuildService.getSerialNumberByType(SlnsConstants.SERIAL_NUMBER_TYPE_DAMAGE);
            damage.setNo(damageNo);
            damage.setVehicleId(vehicleId);
            damage.setRegisterTime(new Date());
            damage.setCreateTime(new Date());
            rs = damageEntityMapper.insertSelective(damage);
            if (rs < 0) {
                throw new RuntimeException();
            }
            // 表示是自提操作
            // 更新运单状态为【确认交接】
            rs = updateStatusHandover(id, 7, "order");
            if (rs < 0) {
                throw new RuntimeException();
            }
            // 更新车辆状态为【确认交接】
            rs = updateStatusHandover(vehicleId, 7, "vehicle");
            if (rs < 0) {
                throw new RuntimeException();
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("warehouseId", orderEntity.getWarehouseId());
            map.put("customerId", orderEntity.getCustomerId());
            rs = warehouseLinkCustomerEntityMapper.updateSpaceAmountForOut(map);

            if (rs < 0) {
                throw new RuntimeException();
            } else {
                return damage.getId();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * 更新运单和车辆的状态【车辆交接】单个更新【自提】
     * @param int
     *            id 运单id
     * @param int
     *            status
     * @param String
     *            type
     * @return int
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int updateStatusHandover(int id, int status, String type) {
        int re = -1;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            // 【更新取消的运单的状态】
            map.put("status", status);
            map.put("vehicleStatus", status);
            map.put("id", id);
            if ("order".equals(type)) {
                map.put("deliveryTime", new Date());
                re = orderEntityMapper.updateOrderStrutsOrderId(map);
                if (re < 0) {
                    throw new RuntimeException();
                }
            }
            if ("vehicle".equals(type)) {
                re = vehicleEntityMapper.updateVehicleStatusById(map);
                if (re < 0) {
                    throw new RuntimeException();
                }
            }
            return re;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * 新增质损后的返回信息
     * @param DamageEntity
     *            damageEntity
     * @param int
     *            id 运单的id
     * @return int
     */
    @Override
    public Map<String, Object> carHandoverBack(int orderId, String type) {
        int vehicleId = -1;
        Map<String, Object> map = new HashMap<String, Object>();
        OrderEntity orderEntity = orderEntityMapper.selectVehicleIdByOrderId(orderId);
        if (orderEntity != null) {
            vehicleId = orderEntity.getVehicleId();

            // 查询到车辆信息
            VehicleEntity vehicleEntity = vehicleEntityMapper.selectByPrimaryKey(vehicleId);
            // 获取车辆的logo图片
            Map<String, Object> mapModelPicturMap = new HashMap<String, Object>();
            mapModelPicturMap.put("deliveryId", orderEntity.getDeliveryPlanId());
            mapModelPicturMap.put("brand", vehicleEntity.getBrand());
            mapModelPicturMap.put("series", vehicleEntity.getSeries());
            mapModelPicturMap.put("model", vehicleEntity.getModel());
            mapModelPicturMap.put("announceYear", vehicleEntity.getAnnounceYear());
            String modelPicturMap = vehicleEntityMapper.getmodelPicture(mapModelPicturMap);
            vehicleEntity.setLogoPicturePath(modelPicturMap);

            // 获取所有的质损信息
            List<DamageEntity> damageList = null;

            if ("自提".equals(orderEntity.getTransportType())) {
                // 自提
                damageList = damageEntityMapper.selectDamageByVehicleIdAndType(vehicleId);
            } else {
                // 发运
                damageList = damageEntityMapper.selectDamageByVehicleIdAndTypeFy(vehicleId);
            }
            if (null != damageList && damageList.size() > 0) {
                // 有质损信息
                if (damageList.get(0) != null && !StringUtil.isEmpty(damageList.get(0).getDamageDetail(), true)) {
                    // 获取所有的质损信息
                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    for (DamageEntity damageEntity : damageList) {
                        list.add(addMasslossService.toUpdateMassloss(damageEntity.getId()));
                    }
                    map.put("damageList", list);
                }

                map.put("damageEntity", damageList.get(0));
            }
            map.put("vehicleEntity", vehicleEntity);
        }
        return map;
    }

    /**
     * 根据子计划单获取到该计划单下的所有的运单集合，以及该子计划单下能放最大的运单数
     * @param int
     *            id
     * @return int
     */
    @Override
    public Map<String, Object> selectDeliveryPlanOrdersForEditFy(int id) {
        Map<String, Object> reMap = new HashMap<String, Object>();
        // 查询到当前计划单
        DeliveryPlanVO deliveryPlanVO = deliveryPlanMapper.selectDeliveryPlanOrdersForEditFy(id);
        // 获取所属父计划单下所有的子计划单集合
        List<DeliveryPlanDTO> deliveryPlanDTO = deliveryPlanMapper.selectDeliveryPlanSub(deliveryPlanVO.getParentId());
        List<Long> deliveryPlanIds = new ArrayList<Long>();
        int planId = 0;
        for (DeliveryPlanDTO deliveryPlanDTO2 : deliveryPlanDTO) {
            planId = deliveryPlanDTO2.getId();
            if (planId != id) {
                deliveryPlanIds.add(Long.valueOf(planId));
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("list", deliveryPlanIds);
            int orderNum = orderEntityMapper.selectOrderByDeliveryPlanId(map);

            reMap.put("capacity", 12 - orderNum);
            reMap.put("data", deliveryPlanVO);
        }
        return reMap;
    }

    // *********************************改版后新增页面***********************************************************************************
    /**
     * 查询首页数据
     * @param Map<String,
     *            Object> map
     * @return HomePageVO
     */
    public HomePageVO selectHomepageData(Map<String, Object> map) {
        return orderEntityMapper.selectHomepageData(map);
    }

    /**
     * 查询待调度列表数据
     * @param Map<String,
     *            Object> map
     * @return List<OrderEntity>
     */
    public List<OrderEntity> selectOrderListByConditionForDispatch(Map<String, Object> map) {
        return orderEntityMapper.selectOrderListByCondition(map);
    }

    /**
     * 查询出库计划 ：待自提;待出库
     * @param DeliveryPlanEntity
     *            bean
     * @return Map<String, Object>
     */
    @Override
    public List<DeliveryPlanDTO> queryDeliveryPlanPro(DeliveryPlanDTO bean) {
        return deliveryPlanMapper.selectDeliveryPlan(bean);
    }

    /**
     * 取消出库计划
     * @param int
     *            Id 出库计划id
     * @return DeliveryPlanDTO
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public DeliveryPlanDTO cancelDeliveryPlanForTwo(DeliveryPlanDTO record) {
        int rs = -1;
        // 出库计划单的id
        int deliveryPlanId = record.getId();
        Map<String, Object> map = new HashMap<String, Object>();
        List<DeliveryPlanDTO> deliveryPlanList = new ArrayList<DeliveryPlanDTO>();
        try {

            if ("FY".equals(record.getDoType())) {
                map.put("status", 6);
                map.put("id", deliveryPlanId);
                // 取消父出库计划单
                rs = deliveryPlanMapper.updateDeliveryPlanStruts(map);
                if (rs < 0) {
                    throw new RuntimeException();
                }
                // 查询所包含的所有的子出库计划单
                deliveryPlanList = deliveryPlanMapper.selectDeliveryPlanSub(deliveryPlanId);
            } else {
                deliveryPlanList.add(record);
            }

            for (DeliveryPlanDTO deliveryPlan1 : deliveryPlanList) {
                int deliveryPlanSubId = deliveryPlan1.getId();
                List<VehicleVO> vehicleList = vehicleEntityMapper.selectVehicleIdByDeliveryPlanIdFy(deliveryPlanSubId);
                StringBuffer sb = new StringBuffer("");
                for (VehicleVO vehicleVO : vehicleList) {
                    sb.append(vehicleVO.getId() + ",");
                }
                // 更新车辆状态待调度
                rs = updateStatusEditZt(sb.toString(), deliveryPlanSubId, 1, "vehicle");
                if (rs < 0) {
                    throw new RuntimeException();
                }

                // 更新运单状态待调度,没有去掉所属计划单号
                map.clear();
                map.put("status", 1);
                map.put("deliveryPlanId", deliveryPlanSubId);
                rs = orderEntityMapper.updateOrderStrutsByDeliveryPlanId(map);
                if (rs < 0) {
                    throw new RuntimeException();
                }

                // 取消计划单的
                map.clear();
                map.put("status", 6);
                map.put("id", deliveryPlanSubId);
                rs = deliveryPlanMapper.updateDeliveryPlanStruts(map);
                if (rs < 0) {
                    throw new RuntimeException();
                }
            }
            if (rs > 0 && !"FY".equals(record.getDoType())) {

                // ************发送取消短信通知**************
                JSONObject tempPara = new JSONObject();
                DeliveryPlanDTO delivery = deliveryPlanMapper.selectMessageInfo(deliveryPlanId);
                // 收货人电话
                String mobile = delivery.getMobile();
                // 仓库管理员电话
                tempPara.put("selfMobile", delivery.getContactorMobile());
                // 自提出库计划编号
                tempPara.put("selfNo", delivery.getNo());
                // 仓库名
                tempPara.put("selfAddress", delivery.getWarehouseName());
                slnsSendMessageService.sendSingleMessage(Integer.parseInt(slnsSendConstants.getSmsCancleStorageId()), Integer.parseInt(slnsSendConstants.getSendTypeSync()),
                    Integer.parseInt(slnsSendConstants.getIsRetryNo()), mobile, tempPara);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return deliveryPlanMapper.selectByPrimaryKey(deliveryPlanId);
    }

    /**
     * 出库计划管理审核
     * @param int
     *            Id
     * @return int
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public DeliveryPlanDTO updateDeliveryPlanZtForTwo(DeliveryPlanDTO record, String type) {
        // 证件信息状态0：待审核，1：审核通过，2：审核不通过
        // 取消：status：6
        int rs = -1;
        int deliveryPlanId = record.getId();
        DeliveryPlanDTO deliveryPlan = null;
        try {
            if ("SH".equals(type)) {
                // 审核计划单操作，更新identityCardStatus为审核已通过
                rs = deliveryPlanMapper.updateIdentityCardStatus(record);
                if (rs < 0) {
                    throw new RuntimeException();
                }
            }
            if ("ZP".equals(type)) {
                rs = deliveryPlanMapper.updateIdentityCardStatus(record);
                if (rs > 0) {
                    return deliveryPlanMapper.selectByPrimaryKey(deliveryPlanId);
                } else {
                    throw new RuntimeException();
                }
            }
            // 得到计划单的id
            deliveryPlan = deliveryPlanMapper.selectByPrimaryKey(deliveryPlanId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return deliveryPlan;
    }

    /**
     * 车辆交接
     * @param int
     *            Id 父出库计划id
     * @return int
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int checkKeyIsTaken(Map<String, Object> map) {
        int rs = 1;
        DeliveryPlanDTO deliveryPlan = null;
        // 出库计划单的id
        int deliveryPlanId = (int) map.get("id");
        /*      int orderId = (int) map.get("orderId");
        int vehicleId = (int) map.get("vehicleId");*/
        try {
            // 获取到该出库计划
            deliveryPlan = deliveryPlanMapper.selectByPrimaryKey(deliveryPlanId);
            if (deliveryPlan == null) {
                throw new RuntimeException();
            }
            if (deliveryPlan.getKeyIsTaken() == 0) {
                // 钥匙未领取
                Map<String, Object> ketMap = new HashMap<String, Object>();
                ketMap.put("keyIsTaken", 1);
                ketMap.put("id", deliveryPlanId);
                rs = deliveryPlanMapper.updateIdentityCardStatusForTwo(ketMap);
                if (rs < 0) {
                    throw new RuntimeException();
                }
            }
            /* // 更新运单状态为【确认交接】
             rs = updateStatusHandover(orderId, 7, "order");
             if (rs < 0) {
                 throw new RuntimeException();
             }
             // 更新车辆状态为【确认交接】
             rs = updateStatusHandover(vehicleId, 7, "vehicle");*/

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return rs;
    }

    /**
     * 出库管理页面查询
     * @param DeliveryPlanDTO
     *            bean 计划单对象
     * @param Integer
     *            pageNum 页数
     * @param Integer
     *            pageRows 每页记录数
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> selectDeliveryPlanManage(DeliveryPlanDTO bean, Integer pageNum, Integer pageRows) {
        Map<String, Object> map = new HashMap<String, Object>();
        DeliveryPlanDTO bean1 = new DeliveryPlanDTO();
        try {
            BeanUtils.copyProperties(bean1, bean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        // 获取总件数
        int num = 0;
        if (StringUtil.isEmpty(bean.getTransportType(), true)) {
            bean.setType(0);
            Integer num1 = deliveryPlanMapper.selectDeliveryPlanCountForTwo(bean);
            bean.setType(1);
            bean.setTransportType("自提");
            Integer num2 = deliveryPlanMapper.selectDeliveryPlanCountForTwo(bean);
            num = num1 + num2;
        } else if ("发运".equals(bean.getTransportType())) {
            bean.setType(0);
            num = deliveryPlanMapper.selectDeliveryPlanCountForTwo(bean);
        } else {
            bean.setType(1);
            num = deliveryPlanMapper.selectDeliveryPlanCountForTwo(bean);
        }

        // 获取仓库列表信息
        List<DeliveryPlanDTO> list = null;
        if (num > 0) {
            // 每页显示数量
            bean1.setPageNumber(pageRows);
            // 总数量
            bean1.setTotalNumber(num);
            // 当前页数
            bean1.setCurrentPage(pageNum);
            // 查询分页信息
            list = deliveryPlanMapper.selectDeliveryPlanManage(bean1);
        }
        map.put("total", num);
        map.put("rows", list);
        return map;
    }

    /**
     * 打印交接单
     * @param int
     *            deliveryPlanId
     * @return DeliveryPlanDTO
     */
    public DeliveryPlanVO printDeliveryPlan(int deliveryPlanId) {
        DeliveryPlanVO deliveryPlanVO = null;
        try {
            deliveryPlanVO = deliveryPlanMapper.printDeliveryPlanVOMapFy(deliveryPlanId);
            if (deliveryPlanVO != null) {
                ShipReceiptEntity shipReceipt = shipReceiptEntityMapper.selectShipReceiptByPlanId(deliveryPlanId);
                if (shipReceipt != null && shipReceipt.getIsPrinted() == 0) {
                    shipReceipt.setIsPrinted(1);
                    int rs = shipReceiptEntityMapper.updateByPrimaryKeyForPrint(shipReceipt);
                    if (rs < 0) {
                        throw new RuntimeException();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return deliveryPlanVO;
    }
}
