package com.anji.allways.business.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.anji.allways.business.entity.VehicleEntity;
import com.anji.allways.business.vo.VehicleVO;

public interface VehicleEntityMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insert(VehicleEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insertSelective(VehicleEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    VehicleEntity selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKeySelective(VehicleEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKey(VehicleEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    VehicleEntity selectAllVehicle(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    VehicleEntity getOrderTransport(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int getCount(Map<String, Object> map);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    List<VehicleEntity> getStockDetail(Map<String, Object> map);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateLockStruts(Map<String, Object> map);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    String getmodelPicture(Map<String, Object> map);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    List<VehicleEntity> getTransportOrder(Map<String, Object> map);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    VehicleEntity getByVinVehicle(@Param("vin") String vin, @Param("userId") int userId);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int getCountToTransport(Map<String, Object> map);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    List<VehicleEntity> getBydeliveryPlanId(int deliveryId);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    VehicleEntity getBydeliveryVehicleId(int vehicleId);

    // Integer insertBatch(List<VehicleEntity> vehicleList);

    // query vehicles count
    Integer queryStockVehicleCount(VehicleEntity entity);

    // query vehicles for page
    List<VehicleVO> queryStockVehicles(VehicleEntity entity);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_vehicle
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int queryOrderVehicleCount(int vehicleId);

    int updateVehicleStatus(Map<String, Object> map);

    int queryOrderIncomeVehicleStatus(int deliveryId);

    List<VehicleVO> selectVehicleIdByDeliveryPlanId(@Param("deliveryPlanId") int deliveryPlanId);

    int updateVehicleStatusById(Map<String, Object> map);

    int queryDriverIncomeVehicleStatus(int deliveryId);

    List<VehicleVO> selectVehicleIdByDeliveryPlanIdFy(@Param("deliveryPlanId") int deliveryPlanId);

    /**
     * 查询导出库存记录
     * @param entity
     * @param pageNum
     * @param pageRows
     * @return
     */
    List<Map<String, Object>> queryStorageRecordsForExportData(Map<String, Object> map);

    int updateQualityStatusById(Map<String, Object> map);

    /**
     * 查询入库时间
     * @param entity
     * @param pageNum
     * @param pageRows
     * @return
     */
    List<String> seleByWareHouseIdToStorageTime(Map<String, Object> map);

    int updateByPrimaryKeyById(VehicleEntity vehicleEntity);

    int updatePathById(Map<String, Object> map);

    /**
     * 经销商首页数据
     */
    int selectByIdToHomeCount(Map<String, Object> map);

    /**
     * 经销商入库数
     */
    int selectStorageVehicleCountByCustomer(Map<String, Object> map);

    /**
     * 经销商订单数
     */
    int selectOrderCountByCustomer(Map<String, Object> map);

    /**
     * 经销商库存数
     */
    List<Map<String, Object>> selectWarehouseVehicleCountByCustomer(Map<String, Object> map);

    /**
     * 仓库入库数
     */
    int selectStorageVehicleCountByWarehouse(Map<String, Object> map);

    /**
     * 仓库订单数
     */
    int selectOrderCountByWarehouse(Map<String, Object> map);

    /**
     * 仓库出库数
     */
    int selectOutStorageVehicleCountByWarehouse(Map<String, Object> map);

    /**
     * 仓库收车数
     */
    int selectIncomeVehicleCountByWarehouse(Map<String, Object> map);

    /**
     * 仓库客户分布
     */
    List<Map<String, Object>> selectStockByCustomer(Map<String, Object> map);

    /**
     * 经销商当前库位数
     */
    String selectSpaceAmountByCustomer(Map<String, Object> map);

    /**
     * 经销商当前库位数
     */
    int selectStorageAmountByCustomerAndWarehouse(Map<String, Object> map);

    /**
     * 经销商某个仓库当前库位数
     */
    String selectSpaceAmountByCustomerAndWarehouse(Map<String, Object> map);

    /**
     * 经销商集团获取库存
     */
    int selectStockByGroupId(Map<String, Object> map);

    /**
     * 经销商集团获取库位
     */
    String selectSpaceAmountByGroupId(Map<String, Object> map);

    /**
     * 经销商集团获取入库数
     */
    int selectStorageVehicleCountByGroup(Map<String, Object> map);

    /**
     * 经销商集团获取订单数
     */
    int selectOrderCountByGroup(Map<String, Object> map);

    /**
     * 经销商仓库库存
     */
    int selectVehicleCountByWarehouseAndCustomer(Map<String, Object> map);

    /**
     * 仓库查询待收车
     */
    int selectNotIncomeVehicleByWeb(Map<String, Object> map);

    /**
     * 仓库查询车辆
     */
    int selectByIdToHomeCountByWeb(Map<String, Object> map);

    /**
     * 仓库查询库容
     */
    String selectSpaceAmountByWarehouse(Map<String, Object> map);

    /**
     * 仓库
     */
    List<Map<String, Object>> selectCustomerByWarehouseWeb(Map<String, Object> map);

    /**
     * 仓库经销商库存
     */
    int selectStorageAmountByCustomerAndWarehouseWeb(Map<String, Object> map);
}
