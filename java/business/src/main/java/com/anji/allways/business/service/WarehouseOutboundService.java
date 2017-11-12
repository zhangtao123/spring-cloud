package com.anji.allways.business.service;

import java.util.List;
import java.util.Map;

import com.anji.allways.business.dto.DeliveryPlanDTO;
import com.anji.allways.business.entity.DamageEntity;
import com.anji.allways.business.entity.OrderEntity;
import com.anji.allways.business.vo.DeliveryPlanVO;
import com.anji.allways.business.vo.HomePageVO;

public interface WarehouseOutboundService {

    /**
     * 获取出库计划单列表
     */
    Map<String, Object> queryDeliveryPlan(DeliveryPlanDTO bean, Integer pageNum, Integer pageRows);

    /**
     * 根据司机电话获取司机相关信息完善调度信息
     */
    Map<String, Object> selectDeliveryInfoByDriverMobile(String mobile);

    /**
     * 根据登录人获取仓库信息完善调度信息
     */
    Map<String, Object> selectWareHouseNameByUserId(String userId);

    /**
     * 根据过滤条件获取运输单信息列表
     */
    Map<String, Object> selectOrderListByCondition(Map<String, Object> map);

    /**
     * 新增出库计划单
     */
    Integer addDeliveryPlan(int userId, DeliveryPlanDTO plan, String orderIds, int type);

    /**
     * 查询出库计划单详情（自提）
     */
    DeliveryPlanVO selectDeliveryDetailZT(int id) throws Exception;

    /**
     * 查询出库计划单详情（发运）
     */
    Map<String, Object> selectDeliveryDetailFY(int id) throws Exception;

    /**
     * 审核出库计划操作（自提）
     */
    DeliveryPlanDTO updateDeliveryPlanZt(DeliveryPlanDTO record, String type);

    /**
     * 审核出库计划操作（发运）
     */
    DeliveryPlanDTO updateDeliveryPlanFy(DeliveryPlanDTO record);

    /**
     * 更新出库计划单（编辑）（自提）
     */
    int updateDeliveryPlanOrderForEditZt(String orderIds, int deliveryPlanId, int userId, String type);

    /**
     * 取消出库计划单
     */
    DeliveryPlanDTO cancelDeliveryPlan(DeliveryPlanDTO record);

    /**
     * 新增质损信息
     */
    int saveDamageEntity(DamageEntity damageList, int id, String type, String userId);

    Map<String, Object> selectDeliveryPlanOrdersForEditFy(int id);

    Map<String, Object> carHandoverBack(int orderId, String type);

    /**
     * 更新运单和车辆的状态【车辆交接】单个更新【自提】
     */
    int updateStatusHandover(int id, int status, String type);

    // ****************************************************************新版的改造****************************************************************

    /**
     * 查询首页数据
     * @param Map<String,
     *            Object> map
     * @return HomePageVO
     */
    HomePageVO selectHomepageData(Map<String, Object> map);

    /**
     * 查询待调度列表数据
     * @param Map<String,
     *            Object> map
     * @return List<OrderEntity>
     */
    List<OrderEntity> selectOrderListByConditionForDispatch(Map<String, Object> map);

    /**
     * 查询出库计划 ：待自提;待出库
     * @param DeliveryPlanEntity
     *            bean
     * @return Map<String, Object>
     */
    List<DeliveryPlanDTO> queryDeliveryPlanPro(DeliveryPlanDTO bean);

    /**
     * 取消出库计划
     * @param int
     *            Id 出库计划id
     * @return DeliveryPlanDTO
     */
    DeliveryPlanDTO cancelDeliveryPlanForTwo(DeliveryPlanDTO record);

    /**
     * 出库计划管理（提取钥匙）（审核）(交接单的生成)（自提）
     * @param int
     *            Id
     * @return int
     */
    DeliveryPlanDTO updateDeliveryPlanZtForTwo(DeliveryPlanDTO record, String type);

    /**
     * 车辆交接
     * @param int
     *            Id 父出库计划id
     * @return int
     */
    int checkKeyIsTaken(Map<String, Object> map);

    /**
     * 出库管理
     * @param DeliveryPlanDTO
     * @return Map<String, Object>
     */
    Map<String, Object> selectDeliveryPlanManage(DeliveryPlanDTO bean, Integer pageNum, Integer pageRows);

    /**
     * 打印交接单
     * @param int
     *            deliveryPlanId
     * @return DeliveryPlanDTO
     */
    DeliveryPlanVO printDeliveryPlan(int deliveryPlanId);

    Map<String, Object> saveReceiptEntity(DeliveryPlanDTO deliveryPlan);
}
