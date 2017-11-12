package com.anji.allways.business.mapper;

import java.util.List;
import java.util.Map;

import com.anji.allways.business.entity.TempVehicleEntity;

public interface TempVehicleEntityMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_temp_vehicle
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_temp_vehicle
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    int insert(TempVehicleEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_temp_vehicle
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    int insertSelective(TempVehicleEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_temp_vehicle
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    TempVehicleEntity selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_temp_vehicle
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    int updateByPrimaryKeySelective(TempVehicleEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_temp_vehicle
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    int updateByPrimaryKey(TempVehicleEntity record);

    /**
     * 根据批次号（用户ID）删除入库计划单临时导入数据
     */
    int deleteByNo(String no);

    /**
     * 根据批次号（用户ID）查询入库计划单临时导入数据
     */
    List<TempVehicleEntity> selectByNo(String no);

    /**
     * 更新入库临时表错误信息
     */
    int updateConmmentByPrimaryKey(Map<String, Object> map);
}
