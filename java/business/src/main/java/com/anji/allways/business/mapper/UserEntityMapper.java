package com.anji.allways.business.mapper;

import java.util.List;
import java.util.Map;

import com.anji.allways.business.entity.UserEntity;

public interface UserEntityMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_user
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_user
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insert(UserEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_user
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insertSelective(UserEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_user
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    UserEntity selectByPrimaryKey(Integer id);

    UserEntity selectByUserName(String accountName);

    // 通过手机号查询
    List<UserEntity> selectByUserMobile(String telephone);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_user
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKeySelective(UserEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_user
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKey(UserEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_user
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    UserEntity selectUserAndInfo(int id);

    /**
     * 账号管理分页查询记录数
     * @param bean
     * @return
     */
    Integer queryCustomerAccountInfosCount(UserEntity bean);

    /**
     * 账号管理分页查询
     * @param bean
     * @return
     */
    List<UserEntity> queryCustomerAccountInfos(UserEntity bean);

    /**
     * 检查账号和手机号唯一
     * @param bean
     * @return
     */
    int checkAccountNameByOne(Map<String, Object> map);

    /**
     * 根据客户ID及所属组织查询用户ID
     */
    Integer selectUserIdByCustomerIdAndType(Map<String, Object> map);
}
