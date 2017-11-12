package com.anji.allways.business.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.anji.allways.business.dto.DeliveryPlanDTO;
import com.anji.allways.business.entity.DeliveryPlanEntity;
import com.anji.allways.business.entity.TruckDriverEntity;
import com.anji.allways.business.vo.DeliveryPlanChildVO;
import com.anji.allways.business.vo.DeliveryPlanVO;
import com.anji.allways.business.vo.DestinationVO;
import com.anji.allways.business.vo.VehicleVO;

public interface DeliveryPlanEntityMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_delivery_plan
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_delivery_plan
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insert(DeliveryPlanEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_delivery_plan
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insertSelective(DeliveryPlanEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_delivery_plan
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    DeliveryPlanDTO selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_delivery_plan
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKeySelective(DeliveryPlanEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_delivery_plan
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKey(DeliveryPlanEntity record);

    /**
     * 查询出库计划单记录数
     * @param record
     * @return
     */
    int selectDeliveryPlanCount(DeliveryPlanDTO record);

    /**
     * 查询出库计划单记录
     * @param record
     * @return
     */
    List<DeliveryPlanDTO> selectDeliveryPlan(DeliveryPlanDTO record);

    /**
     * 根据手机号查询司机信息
     * @param mobile
     * @return
     */
    TruckDriverEntity selectDriverByMobile(String mobile);

    /**
     * 根据登录人获取所在仓库信息
     * @param mobile
     * @return
     */
    DeliveryPlanEntity selectWareHouseNameByUserId(String userId);

    /**
     * 根据主出库计划单ID查询出库计划单明细信息
     * @param id
     * @return
     */
    DeliveryPlanVO selectInfoById(Integer id);

    /**
     * 查询出库计划单详情（自提），关联其中包含的运单列表（查看自提详情）
     * @param id
     * @return
     */
    DeliveryPlanVO selectDeliveryPlanInfoByIdZT(Integer id);

    /**
     * 查询出库计划单详情（发运）
     * @param id
     * @return
     */
    DeliveryPlanVO selectParentDeliveryPlanInfoByIdFY(Integer id);

    /**
     * 根据出库计划单查询计划单行和关联的订单列表信息
     * @param id
     * @return
     */
    DestinationVO selectDeliveryPlanInfoByIdFY(Integer id);

    /**
     * 根据id查询子出库计划单列表信息
     * @param id
     * @return
     */
    List<DeliveryPlanDTO> selectDeliveryPlanSub(Integer id);

    /**
     * 根据id查询父出库计划单信息
     * @param id
     * @return
     */
    DeliveryPlanDTO selectDeliveryPlanPar(Integer id);

    /**
     * 查询收车信息
     * @param id
     * @return
     */
    List<DeliveryPlanDTO> selectIncomeVehicle(Map<String, Object> map);

    /**
     * 查询收车总数
     * @param id
     * @return
     */
    int selectIncomeVehicleCount(Map<String, Object> map);

    /**
     * 经销商收车查询详情
     * @param id
     * @return
     */

    DeliveryPlanDTO incomeDetialForward(int deliveryId);

    /**
     * 取消出库计划
     * @param id
     * @return
     */
    Integer cancelDeliveryPlan(@Param("identityCardStatus") int identityCardStatus, @Param("id") int id);

    /**
     * 审核出库计划
     * @param id
     * @return
     */
    Integer checkDeliveryPlan(@Param("status") int status, @Param("id") int id);

    /**
     * 查询出库计划单下的运输单列表 （）
     * @param id
     * @return
     */
    DeliveryPlanVO selectDeliveryPlanOrdersForEditZt(int id);

    /**
     * 查询出库计划单下的运输单列表 （）
     * @param id
     * @return
     */
    DeliveryPlanVO selectDeliveryPlanOrdersForEditFy(int id);

    /**
     * 根据子出库计划单ID查询车辆信息
     * @param map
     * @return
     */
    List<VehicleVO> findCarByDeliveryPlanNo(Map<String, Object> map);

    /**
     * 取消父出库计划
     * @param map
     * @return
     */
    int checkDriver(int deliveryId);

    /**
     * 子出库计划单送达车辆信息
     * @param map
     * @return
     */
    DeliveryPlanChildVO findChildDeliveryPlanByChildId(Map<String, Object> map);

    /**
     * 更新子出库计划单行程状态
     * @param map
     * @return
     */
    int updateForConfirmArrive(Map<String, Object> map);

    /**
     * 司机查询出库计划
     * @param map
     * @return
     */
    List<DeliveryPlanDTO> driverDeliveryPlan(Map<String, Object> map);

    /**
     * 司机查询出库计划终点
     * @param map
     * @return
     */
    List<Map<String, Object>> driverDeliveryPlanPoint(Integer deliveryId);

    /**
     * 司机查询出库计划总数
     * @param map
     * @return
     */
    int driverDeliveryPlanCount(Map<String, Object> map);

    /**
     * 更新出库计划单为副出库计划单
     * @param map
     * @return
     */
    void updateDeliveryPlanType(Map<String, Object> map);

    /**
     * 经销商查询所有子出库计划单
     * @param map
     * @return
     */
    int selectAllIncomeVehicleCount(int userId);

    /**
     * 修改出库计划单
     * @param map
     * @return
     */
    int updateDeliveryPlanStruts(Map<String, Object> map);

    /**
     * 经销商查询待收货所有子出库计划单
     * @param map
     * @return
     */
    int selectAllDeliveryPlanCount(int deliveryId);

    /**
     * 经销商查询父类id
     * @param map
     * @return
     */
    int selectFatherDeliveryPlanId(int deliveryId);

    /**
     * 经销商修改行程状态
     * @param map
     * @return
     */
    int updateDeliveryPlanRoutingStatus(Map<String, Object> map);

    /**
     * 经销商修改身份状态
     * @param map
     * @return
     */
    int updateDeliveryPlanIdentityCardStatus(Map<String, Object> map);

    /**
     * 经销商修改身份路径
     * @param map
     * @return
     */
    int updateDeliveryPlanIdentityCardPath(Map<String, Object> map);

    /**
     * 审核出库计划(包括描述信息)
     * @param id
     * @return
     */
    Integer updateIdentityCardStatus(DeliveryPlanDTO record);

    /**
     * 领取钥匙(发运)
     * @param id
     * @return
     */
    Integer updatekeyIsTakenById(DeliveryPlanDTO record);

    /**
     * 打印(发运)
     * @param id
     * @return
     */
    DeliveryPlanVO printDeliveryPlanVOMapFy(int id);

    /**
     * 导出excel
     * @param id
     * @return
     */
    List<Map<String, Object>> queryDeliveryPlanForExport(Map<String, Object> map);

    /**
     * 查询收货联系人电话和仓库联系人电话
     * @param id
     * @return
     */
    DeliveryPlanDTO selectMessageInfo(int id);

    int selectAllPlanCount(int id);

    int updateDeliveryPlanStrutsTime(Map<String, Object> map);

    Date selectOrderReceiveTime(Integer id);

    int updateIdentityCardStatusForTwo(Map<String, Object> map);

    /**
     * 出库管理
     * @param DeliveryPlanDTO
     * @return
     */
    List<DeliveryPlanDTO> selectDeliveryPlanManage(DeliveryPlanDTO record);

    /**
     * 出库管理总数查询
     * @param DeliveryPlanDTO
     * @return Map<String, Object>
     */
    int selectDeliveryPlanCountForTwo(DeliveryPlanDTO record);

    /**
     * 打印新版
     * @param ap<String,
     *            Object> map
     * @return List<Map<String, Object>>
     */
    List<Map<String, Object>> queryDeliveryPlanForExportForTwo(Map<String, Object> map);
}