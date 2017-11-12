package com.anji.allways.business.mapper;

import java.util.List;
import java.util.Map;

import com.anji.allways.business.entity.FileEntity;

public interface FileEntityMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_file
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_file
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insert(FileEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_file
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insertSelective(FileEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_file
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    FileEntity selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_file
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKeySelective(FileEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_file
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKey(FileEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_file
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    List<Map<String, Object>> getVehiclePic(String id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_file
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int deleteByPrimaryName(String name);
}
