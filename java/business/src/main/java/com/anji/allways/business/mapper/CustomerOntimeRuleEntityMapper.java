package com.anji.allways.business.mapper;

import java.util.List;

import com.anji.allways.business.entity.CustomerOntimeRuleEntity;

public interface CustomerOntimeRuleEntityMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_customer_ontime_rule
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_customer_ontime_rule
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insert(CustomerOntimeRuleEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_customer_ontime_rule
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int insertSelective(CustomerOntimeRuleEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_customer_ontime_rule
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    CustomerOntimeRuleEntity selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_customer_ontime_rule
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKeySelective(CustomerOntimeRuleEntity record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_customer_ontime_rule
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    int updateByPrimaryKey(CustomerOntimeRuleEntity record);

    /**
     * 根据客户ID获取相关及时率规则信息
     * @param customerId
     *            客户ID
     */
    List<CustomerOntimeRuleEntity> selectByCustomerId(Integer customerId);

    /**
     * 根据客户ID删除相关规则信息
     * @param record
     * @return
     */
    int deleteByCustomerId(Integer customerId);
}
