/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import java.util.List;
import java.util.Map;

import com.anji.allways.business.entity.TransportCompanyEntity;
import com.anji.allways.business.vo.DictVO;
import com.anji.allways.business.vo.TransportCompanyVO;

/**
 * 运输公司相关接口
 * @author xuyuyang
 * @version $Id: BerthService.java, v 0.1 2017年8月24日 上午10:41:29 xuyuyang Exp $
 */
public interface TransportCompanyService {

    /**
     * 分页查询
     * @param bean
     * @param pageNum
     * @param pageRows
     * @return
     */
    Map<String, Object> queryTransportCompanyInfos(TransportCompanyEntity bean, Integer pageNum, Integer pageRows);

    /**
     * 批量激活或停用司机
     * @param ids
     *            选中记录的IDlist
     * @param userId
     *            操作用户
     * @param status
     *            要更新成的状态(false:停用 true:正常)
     */
    Integer batchUpdateCompany(String ids, Long userId, Integer status);

    /**
     * 新增运输公司信息
     * @param TransportCompanyVO
     */
    int addTransportCompanyEntity(TransportCompanyEntity bean);

    /**
     * 保存编辑后运输公司信息
     * @param TransportCompanyVO
     */
    int updateTransportCompanyEntity(TransportCompanyEntity bean);

    /**
     * 校验是否存在重复的值
     * @param value
     *            字段值
     * @param cloumnsName
     *            字段名
     * @param id
     * @return
     */
    Integer checkRepeatValue(String value, String cloumnsName, Integer id);

    /**
     * 根据ID获取运输公司信息
     * @param ID
     * @return
     */
    TransportCompanyVO selectByPrimaryKey(Integer id);

    /**
     * 获取运输公司信息下拉列表
     */
    List<DictVO> queryCompanyForDict(Map<String, Object> map);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_transport_company
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     * @return 返回插入记录后的ID
     */
    Integer insert(TransportCompanyEntity record);

    /**
     * check所选运输公司下司机是否已经全部停用
     * @param ID
     *            运输公司ID
     * @return 运输公司名称列表
     */
    List<String> checkCompanyAllDriver(String ids);

}
