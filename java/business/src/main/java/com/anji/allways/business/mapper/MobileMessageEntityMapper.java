package com.anji.allways.business.mapper;

import com.anji.allways.business.entity.MobileMessageEntity;

public interface MobileMessageEntityMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_mobile_message
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_mobile_message
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insert(MobileMessageEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_mobile_message
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insertSelective(MobileMessageEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_mobile_message
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    MobileMessageEntity selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_mobile_message
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKeySelective(MobileMessageEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_mobile_message
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKey(MobileMessageEntity record);
}
