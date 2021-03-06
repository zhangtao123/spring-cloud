package com.anji.allways.business.mapper;

import java.util.List;
import java.util.Map;

import com.anji.allways.business.entity.WarehouseEntity;
import com.anji.allways.business.vo.DictVO;
import com.anji.allways.business.vo.WarehouseLevelVO;
import com.anji.allways.business.vo.WarehouseVO;

public interface WarehouseEntityMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_warehouse
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_warehouse
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insert(WarehouseEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_warehouse
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insertSelective(WarehouseEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_warehouse
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    WarehouseEntity selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_warehouse
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKeySelective(WarehouseEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_warehouse
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKey(WarehouseEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_consignee
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    List<Map<String, Object>> selectByCustomerId(Integer userId);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_consignee
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    Integer getCount(Integer userId);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_consignee
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    List<WarehouseEntity> getMyWarehouseById(Map<String, Object> map);

    /**
     * 分页查询
     * @param bean
     * @return
     */
    List<WarehouseVO> queryWarehouseInfos(WarehouseEntity bean);

    /**
     * 分页查询记录数
     * @param bean
     * @return
     */
    Integer queryWarehouseInfosCount(WarehouseEntity bean);

    /**
     * 批量激活或停用仓库
     * @param map
     * @return
     */
    Integer updateByIdList(Map<String, Object> map);

    /**
     * 获取仓库等级信息
     * @param map
     *            包含上级仓库ID，仓库名称
     * @return
     */
    List<WarehouseLevelVO> selectWarehouseLevelInfor(Map<String, Object> map);

    /**
     * 校验字段值是否重复
     * @param record
     * @return
     */
    int checkRepeatValue(Map<String, Object> map);

    /**
     * 库容分页查询记录数
     * @param berth
     * @return
     */
    Integer queryStorageInfosCount(WarehouseEntity bean);

    /**
     * 分页查询
     * @param bean
     * @return
     */
    List<WarehouseVO> queryStorageInfos(WarehouseEntity bean);

    /**
     * 根据仓库ID获取库容信息
     * @param ID
     * @return
     */
    WarehouseVO selectStorageInfoById(Integer id);

    /**
     * 库容分页查询记录数
     * @param bean
     * @return
     */
    Integer queryWarehouseRelationInfosCount(WarehouseEntity bean);

    /**
     * 库容分页查询
     * @param bean
     * @return
     */
    List<WarehouseVO> queryWarehouseRelationInfos(WarehouseEntity bean);

    /**
     * 编辑库容时，更新仓库信息
     * @param map
     * @return
     */
    int updateByIdForStorage(Map<String, Object> map);

    /**
     * 保存编辑后的仓库信息(仓库管理)
     * @param bean
     * @return
     */
    int updateWarehouseForManageById(WarehouseEntity bean);

    /**
     * 获取客户名称下拉列表名称（仓库对应的客户信息）
     */
    List<DictVO> queryCustomerForDict(Map<String, Object> map);

    /**
     * 查询某父仓库ID的所有子库
     * @param parentId
     * @return
     */
    List<WarehouseEntity> getWarehouseByParentID(int parentId);

    /**
     * 查询某父仓库ID的所有子库
     * @param parentId
     * @return
     */
    List<WarehouseEntity> getAllWarehouse();

    /**
     * 校验仓库是否存在已入库的入库计划单或未完成的出库计划单
     */
    List<String> checkWarehouseStatus(Map<String, Object> map);

    /**
     * 查询所有最下级仓库
     */
    List<WarehouseEntity> getAllChildrenWarehouse();

    /**
     * 查询所有上级仓库
     */
    List<WarehouseEntity> getAllParentWarehouse();
}
