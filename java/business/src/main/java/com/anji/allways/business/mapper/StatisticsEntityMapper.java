package com.anji.allways.business.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.anji.allways.business.entity.StatisticsEntity;
import com.anji.allways.business.vo.StatisticsEntityResponseVo;
import com.anji.allways.business.vo.StatisticsEntityResquestVo;

public interface StatisticsEntityMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_statistics
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_statistics
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insert(StatisticsEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_statistics
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insertSelective(StatisticsEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_statistics
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    StatisticsEntity selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_statistics
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKeySelective(StatisticsEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_statistics
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKey(StatisticsEntity record);

    /**
     * 获取库位数
     * @param starquest
     * @return
     */
    List<StatisticsEntityResponseVo> queryStorgeCount(@Param("Starquest") StatisticsEntityResquestVo starquest);

    /**
     * 获取入库数
     * @param starquest
     * @return
     */
    List<StatisticsEntityResponseVo> queryStorgingCount(@Param("Starquest") StatisticsEntityResquestVo starquest);

    /**
     * 获取订单数
     * @param starquest
     * @return
     */
    List<StatisticsEntityResponseVo> queryOrderCount(@Param("Starquest") StatisticsEntityResquestVo starquest);

    /**
     * 获取出库单数
     * @param starquest
     * @return
     */
    List<StatisticsEntityResponseVo> queryOutStorgeCount(@Param("Starquest") StatisticsEntityResquestVo starquest);

    /**
     * 查询客户收车数
     * @param starquest
     * @return
     */
    List<StatisticsEntityResponseVo> queryCustomerGetCarCount(@Param("Starquest") StatisticsEntityResquestVo starquest);

    /**
     * 查询库存数
     * @param starquest
     * @return
     */
    List<StatisticsEntityResponseVo> queryStorageedCarCount(@Param("Starquest") StatisticsEntityResquestVo starquest);

    /**
     * 查询待收车数
     * @param starquest
     * @return
     */
    List<StatisticsEntityResponseVo> selectWaitingCarCount(@Param("Starquest") StatisticsEntityResquestVo starquest);

    /**
     * 查询在库信息
     * @param starquest
     * @return
     */
    List<StatisticsEntityResponseVo> selectStayTime(@Param("Starquest") StatisticsEntityResquestVo starquest);

    /**
     * 查询出库时间
     * @param starquest
     * @return
     */
    List<StatisticsEntityResponseVo> selectOutTime(@Param("Starquest") StatisticsEntityResquestVo starquest);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_statistics
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    StatisticsEntity selectByDateAndWarehouseIdAndCustomerIdOne(Map<String, Object> map);

    StatisticsEntity selectByDateAndWarehouseIdAndCustomerIdTwo(Map<String, Object> map);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_statistics
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    StatisticsEntity selectByDateAndAndCustomerId(Map<String, Object> map);

    StatisticsEntity selectByDateAndAndCustomerIdOne(Map<String, Object> map);

    StatisticsEntity selectByDateAndAndCustomerIdTwo(Map<String, Object> map);

    List<StatisticsEntity> selectAllWarehouse(Map<String, Object> map);

    List<StatisticsEntity> selectAllStockByWarehouse(Map<String, Object> map);
}