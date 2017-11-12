package com.anji.allways.business.mapper;

import java.util.List;
import java.util.Map;

import com.anji.allways.business.entity.WarehouseLinkCustomerEntity;

public interface WarehouseLinkCustomerEntityMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_warehouse_link_customer
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_warehouse_link_customer
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insert(WarehouseLinkCustomerEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_warehouse_link_customer
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insertSelective(WarehouseLinkCustomerEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_warehouse_link_customer
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    WarehouseLinkCustomerEntity selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_warehouse_link_customer
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKeySelective(WarehouseLinkCustomerEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_warehouse_link_customer
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKey(WarehouseLinkCustomerEntity record);

    /**
     * 编辑库容信息时，更新客户关系关联
     * @param record
     * @return
     */
    int updateByIDForStorage(WarehouseLinkCustomerEntity record);

    /**
     * 查询某个仓库的所有客户
     * @param warehouseId
     * @return
     */
    List<WarehouseLinkCustomerEntity> selectByWarehouseId(Integer warehouseId);

    /**
     * 查询某个客户是否和某个仓库有合作关系
     * @param warehouseId
     * @return
     */
    int selectLinkByWarehouseAndCustomerId(Map<String, Object> map);

    /**
     * 更新客户所属车位数（入库或出库时使用）
     * @param warehouseId
     * @return
     */
    int updateSpaceAmountForInOrOut(Map<String, Object> map);

    /**
     * 更新客户所属车位数（出库时使用）
     * @param warehouseId
     * @return
     */
    int updateSpaceAmountForOut(Map<String, Object> map);

}
