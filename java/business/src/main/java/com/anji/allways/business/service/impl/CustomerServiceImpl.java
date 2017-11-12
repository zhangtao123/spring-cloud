/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anji.allways.business.entity.BrandEntity;
import com.anji.allways.business.entity.CustomerEntity;
import com.anji.allways.business.entity.CustomerLinkBrandEntity;
import com.anji.allways.business.entity.CustomerOntimeRuleEntity;
import com.anji.allways.business.entity.UserEntity;
import com.anji.allways.business.entity.WarehouseLinkCustomerEntity;
import com.anji.allways.business.enums.RowDictCodeE;
import com.anji.allways.business.mapper.BrandEntityMapper;
import com.anji.allways.business.mapper.CustomerEntityMapper;
import com.anji.allways.business.mapper.CustomerLinkBrandEntityMapper;
import com.anji.allways.business.mapper.CustomerOntimeRuleEntityMapper;
import com.anji.allways.business.mapper.UserEntityMapper;
import com.anji.allways.business.mapper.WarehouseLinkCustomerEntityMapper;
import com.anji.allways.business.service.CustomerService;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.vo.BrandVO;
import com.anji.allways.business.vo.CustomerLevelVO;
import com.anji.allways.business.vo.CustomerVO;
import com.anji.allways.business.vo.DictVO;
import com.anji.allways.business.vo.StateVO;
import com.anji.allways.common.util.PageUtil;
import com.anji.allways.common.util.StringUtil;

/**
 * @author wangyanjun
 * @version $Id: CustomerServiceImpl.java, v 0.1 2017年8月29日 下午11:48:35 wangyanjun Exp $
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerEntityMapper              customerEntityMapper;

    // 获取数据字典Service
    @Autowired
    private DictService                       dictService;

    @Autowired
    private BrandEntityMapper                 brandEntityMapper;

    @Autowired
    private CustomerOntimeRuleEntityMapper    customerOntimeRuleEntityMapper;

    @Autowired
    private CustomerLinkBrandEntityMapper     customerLinkBrandEntityMapper;

    @Autowired
    private UserEntityMapper                  userEntityMapper;

    @Autowired
    private WarehouseLinkCustomerEntityMapper warehouseLinkCustomerEntityMapper;

    /**
     * 分页查询
     * @param bean
     * @param pageNum
     *            当前页数
     * @param pageRows
     *            每页显示数量
     * @return
     */
    @Override
    public Map<String, Object> queryCustomerInfos(CustomerEntity bean, Integer pageNum, Integer pageRows) {
        Map<String, Object> map = new HashMap<String, Object>();

        // 获取总件数
        Integer num = customerEntityMapper.queryCustomerInfosCount(bean);
        // 获取客户列表信息
        List<CustomerVO> retValueList = this.fingPageInfo(bean, pageNum, pageRows, num);

        // 获取客户状态数据字典
        Map<String, String> retValue = dictService.queryAllByCode(RowDictCodeE.CUSTOMER_STATUS.getValue());

        if (retValueList.size() > 0) {

            for (int i = 0; i < retValueList.size(); i++) {
                CustomerVO vo = retValueList.get(i);

                // 设置客户状态名称
                String statusName = retValue.get(String.valueOf(vo.getStatus()));
                vo.setStatusName(statusName);

                // 详细地址（省+市+区+地址）
                vo.setAddress(vo.getProvinceName() + vo.getCityName() + vo.getDistrictName() + vo.getAddress());
            }
        }
        map.put("total", num);
        map.put("rows", retValueList);
        return map;
    }

    /**
     * @param id
     * @return
     * @see com.anji.allways.business.service.CustomerService#queryCustomerById(java.lang.Integer)
     */
    @Override
    public CustomerEntity queryCustomerById(Integer id) {
        return customerEntityMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询(编辑库容页面使用)
     * @param berth
     * @return
     */
    @Override
    public Map<String, Object> queryCustomerInfosForStorage(Map<String, Object> map, Integer pageNum, Integer pageRows) {
        Map<String, Object> retMap = new HashMap<String, Object>();

        // 获取总件数
        Integer num = customerEntityMapper.queryCustomerInfosCountForStorage(map);
        // 获取客户列表信息
        List<CustomerVO> returnList = this.fingPageInfoForStorage(map, pageNum, pageRows, num);

        retMap.put("total", num);
        retMap.put("rows", returnList);
        return retMap;
    }

    /**
     * 分页查询功能(编辑库容页面使用)
     * @param pageNum
     *            当前页数
     * @param pageRows
     *            每页显示数量
     * @param num
     *            查询总件数
     * @return
     */
    private List<CustomerVO> fingPageInfoForStorage(Map<String, Object> map, Integer pageNum, Integer pageRows, Integer num) {

        if (num > 0) {

            PageUtil pageUtil = new PageUtil();
            pageUtil.setPageNumber(pageRows);
            pageUtil.setCurrentPage(pageNum);
            map.put("dbIndex", pageUtil.getDbIndex());
            map.put("dbNumber", pageUtil.getDbNumber());

            // 查询分页信息
            List<CustomerVO> list = customerEntityMapper.queryCustomerInfosForStorage(map);
            return list;
        }
        return new ArrayList<CustomerVO>();
    }

    /**
     * @Title: insert @Description: 插入对象
     * @param t
     *            插入的对象
     * @return 返回插入记录后的ID
     */
    @Override
    public Integer insert(CustomerEntity record) {
        customerEntityMapper.insert(record);
        return record.getId();
    }

    /**
     * 获取某个客户ID及其所有的下属客户ID
     * @param warehouseEntity
     */
    public List<Integer> selectAllLevelIdByParentId(Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();

        // 上级客户ID
        map.put("parentId", id);
        List<CustomerLevelVO> list = customerEntityMapper.selectCustomerLevelInfor(map);

        List<Integer> idList = new ArrayList<Integer>();
        // 递归处理所有层级ID
        processLevelId(list, idList);

        return idList;
    }

    /**
     * 获取所属集团下拉列表
     * @param map
     */
    public List<DictVO> queryGroupForDict(Map<String, Object> map) {
        return customerEntityMapper.queryGroupForDict(map);
    }

    /**
     * 批量激活或停用客户
     * @param list
     *            选中记录的IDlist
     * @param userId
     *            操作用户
     * @param status
     *            要更新成的状态(false:停用 true:正常)
     */
    @Override
    public Integer batchUpdateCustomer(List<Integer> list, Long userId, Integer status) {

        Map<String, Object> map = new HashMap<String, Object>();

        // 选中记录的IDlist
        map.put("idList", list);
        // 操作用户
        map.put("userId", userId);
        // 要更新的状态
        map.put("status", status);
        return customerEntityMapper.updateByIdList(map);
    }

    /**
     * 校验客户是否存在已入库的入库计划单且未完成的运输订单
     */
    public List<String> checkCustomerStatus(List<Integer> list) {
        Map<String, Object> map = new HashMap<String, Object>();

        // 选中记录的IDlist
        map.put("idList", list);
        return customerEntityMapper.checkCustomerStatus(map);
    }

    /**
     * 根据ID获取客户信息
     * @param id
     *            客户ID
     */
    public CustomerVO selectInfoById(Integer id) {

        // 获取客户信息
        CustomerVO customerVO = customerEntityMapper.selectInfoById(id);

        // 获取客户相关及时率规则信息
        List<CustomerOntimeRuleEntity> ruleList = customerOntimeRuleEntityMapper.selectByCustomerId(customerVO.getId());

        // 如果当前客户没有及时率规则信息，则显示上级客户规则信息
        if (ruleList == null || ruleList.size() == 0) {
            ruleList = customerOntimeRuleEntityMapper.selectByCustomerId(customerVO.getParentId());
        }
        customerVO.setRuleList(ruleList);

        // 如果当前客户logo图片为空时，则显示上级客户logo图片
        if (StringUtil.isEmpty(customerVO.getLogoPicturePath(), true)) {

            // 获取上级客户logo照片
            CustomerEntity entity = customerEntityMapper.selectByPrimaryKey(customerVO.getParentId());
            customerVO.setLogoPicturePath(entity.getLogoPicturePath());
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("customerId", id.toString());
        // 获取经营品牌范围
        List<BrandEntity> brandList = brandEntityMapper.selectBrandByCustomerId(map);

        // 如果当前客户无经营范围，则显示上级客户经营范围
        if (brandList == null || brandList.size() == 0) {
            map.put("customerId", customerVO.getParentId().toString());
            brandList = brandEntityMapper.selectBrandByCustomerId(map);
        }

        customerVO.setBrandList(brandList);

        return customerVO;
    }

    /**
     * 获取客户等级信息
     * @param parentId
     *            上级客户ID
     * @return
     */
    @Override
    public List<CustomerLevelVO> selectCustomerLevelInfor(Map<String, Object> map) {

        List<CustomerLevelVO> levelList = customerEntityMapper.selectCustomerLevelInfor(map);

        processLevelStatus(levelList);
        return levelList;
    }

    /**
     * 根据客户ID获取品牌信息
     * @param customerId
     *            客户ID
     * @return
     */
    public List<BrandEntity> selectBrandByCustomerId(String customerId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("customerId", customerId);
        return brandEntityMapper.selectBrandByCustomerId(map);
    }

    /**
     * 根据客户ID查询客户logo照片
     * @param customerId
     *            客户ID
     * @return
     */
    public String selectLogoPictureById(Integer customerId) {
        CustomerEntity entity = customerEntityMapper.selectByPrimaryKey(customerId);
        return entity.getLogoPicturePath();
    }

    /**
     * 根据客户ID查询及时率规则信息
     * @param customerId
     *            客户ID
     * @return
     */
    public List<CustomerOntimeRuleEntity> selectRuleInfoByCustomerId(Integer customerId) {
        // 获取客户相关及时率规则信息
        List<CustomerOntimeRuleEntity> ruleList = customerOntimeRuleEntityMapper.selectByCustomerId(customerId);
        return ruleList;
    }

    /**
     * 校验字段值是否重复
     * @param value
     *            字段值
     * @param cloumnsName
     *            参数名
     * @return
     */
    @Override
    public Integer checkRepeatValue(String value, String cloumnsName, Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 要查询的字段
        map.put(cloumnsName, value);
        // ID(排除自身)
        map.put("id", id);
        int selectCountByExample = customerEntityMapper.checkRepeatValue(map);
        return selectCountByExample;
    }

    /**
     * 新增客户信息
     * @param warehouseId
     *            仓库ID
     * @param entity
     * @param brandList
     *            经营品牌范围
     * @param ruleList
     *            及时率规则信息
     * @return
     */
    public Integer addCustomerEntity(Integer warehouseId, CustomerEntity entity, JSONArray brandList, JSONArray ruleList) {

        // 系统时间
        Date sysDate = new Date();

        // 状态默认为启用（0：停用 1：正常）
        entity.setStatus(1);

        // 创建时间
        entity.setCreateTime(sysDate);
        int num = customerEntityMapper.insertSelective(entity);

        // 添加经营品牌范围及及时率规则信息
        this.addBrandAndRuleInfo(sysDate, entity, brandList, ruleList);

        // 创建仓库与客户的关联关系
        WarehouseLinkCustomerEntity warehouseLinkCustomerEntity = new WarehouseLinkCustomerEntity();
        // 客户ID
        warehouseLinkCustomerEntity.setCustomerId(entity.getId());
        // 仓库ID
        warehouseLinkCustomerEntity.setWarehouseId(warehouseId);
        // 创建时间
        warehouseLinkCustomerEntity.setCreateTime(sysDate);
        // 创建者
        warehouseLinkCustomerEntity.setCreateUser(entity.getCreateUser());
        // 车位数 (默认为0)
        warehouseLinkCustomerEntity.setSpaceAmount(0);
        warehouseLinkCustomerEntityMapper.insert(warehouseLinkCustomerEntity);
        return num;
    }

    /**
     * 更新客户信息
     * @param entity
     * @param brandList
     *            经营品牌范围
     * @param ruleList
     *            及时率规则信息
     * @return
     */
    public Integer updateCustomerEntity(CustomerEntity entity, JSONArray brandList, JSONArray ruleList) {

        // 系统时间
        Date sysDate = new Date();

        // 更新时间
        entity.setUpdateTime(sysDate);

        int num = customerEntityMapper.updateInfoByID(entity);

        // 先删除已有的客户经营范围，然后再添加
        customerLinkBrandEntityMapper.deleteByCustomerId(entity.getId());

        // 删除已有规则信息
        customerOntimeRuleEntityMapper.deleteByCustomerId(entity.getId());

        // 添加经营品牌范围及及时率规则信息
        this.addBrandAndRuleInfo(sysDate, entity, brandList, ruleList);
        return num;
    }

    /**
     * 客户账号分页查询
     * @param bean
     * @param pageNum
     *            当前页数
     * @param pageRows
     *            每页显示数量
     * @return
     */
    public Map<String, Object> queryCustomerAccountInfos(UserEntity bean, Integer pageNum, Integer pageRows) {
        Map<String, Object> map = new HashMap<String, Object>();

        // 获取总件数
        Integer num = userEntityMapper.queryCustomerAccountInfosCount(bean);
        // 获取客户列表信息
        List<UserEntity> retValueList = this.fingAccountPageInfo(bean, pageNum, pageRows, num);

        // 获取客户账号锁定状态数据字典
        Map<String, String> retValue = dictService.queryAllByCode(RowDictCodeE.LOCK_STATUS.getValue());

        if (retValueList.size() > 0) {

            for (int i = 0; i < retValueList.size(); i++) {
                UserEntity vo = retValueList.get(i);

                // 设置客户账号锁定状态名称
                String lockedName = retValue.get(String.valueOf(vo.getLocked()));
                vo.setLockedName(lockedName);
            }
        }
        map.put("total", num);
        map.put("rows", retValueList);
        return map;
    }

    /**
     * 获取所属客户下拉列表
     * @param map
     */
    public List<DictVO> queryCustomerForDict(Map<String, Object> map) {
        return customerEntityMapper.queryCustomerForDict(map);
    }

    /**
     * 添加经营品牌范围及及时率规则信息
     * @param entity
     * @param brandList
     *            经营品牌范围
     * @param ruleList
     *            及时率规则信息
     */
    private void addBrandAndRuleInfo(Date sysDate, CustomerEntity entity, JSONArray brandList, JSONArray ruleList) {

        // 添加经营品牌范围
        if (brandList != null) {
            Iterator<Object> it = brandList.iterator();

            while (it.hasNext()) {
                JSONObject brand = (JSONObject) it.next();

                CustomerLinkBrandEntity brandEntity = new CustomerLinkBrandEntity();

                // 所属客户id
                brandEntity.setCustomerId(entity.getId());

                // 创建用户
                brandEntity.setCreateUser(entity.getCreateUser());

                // 创建时间
                brandEntity.setCreateTime(sysDate);

                // 根据品牌查询所有车系
                List<BrandVO> voList = brandEntityMapper.selectAllSeries(brand.getString("brand"));

                // 添加当前品牌中的所有车系
                for (BrandVO vo : voList) {
                    // 品牌id
                    brandEntity.setBrandId(Integer.valueOf(vo.getId()));
                    customerLinkBrandEntityMapper.insertSelective(brandEntity);
                }
            }
        }

        // 添加及时率规则信息
        if (ruleList != null) {
            Iterator<Object> it = ruleList.iterator();

            while (it.hasNext()) {
                JSONObject rule = (JSONObject) it.next();

                CustomerOntimeRuleEntity ruleEntity = new CustomerOntimeRuleEntity();

                // 所属客户id
                ruleEntity.setCustomerId(entity.getId());

                // 路程区间低值
                ruleEntity.setDistanceScopeMin(rule.getInteger("distanceScopeMin"));

                // 路程区间高值
                ruleEntity.setDistanceScopeMax(rule.getInteger("distanceScopeMax"));

                // 耗费时间（单位：小时）
                ruleEntity.setSpendTime(rule.getFloat("spendTime"));

                // 创建用户
                ruleEntity.setCreateUser(entity.getCreateUser());

                // 创建时间
                ruleEntity.setCreateTime(sysDate);

                customerOntimeRuleEntityMapper.insertSelective(ruleEntity);
            }
        }
    }

    /**
     * 处理客户层级状态
     * @param levelList
     */
    private void processLevelStatus(List<CustomerLevelVO> levelList) {
        for (CustomerLevelVO vo : levelList) {

            Map<String, String> attributes = new HashMap<>();
            attributes.put("id", vo.getId());
            vo.setAttributes(attributes);
            if (vo.getChildren() != null && vo.getChildren().size() > 0) {
                StateVO state = new StateVO();
                // 是否展开
                state.setOpened(true);
                // 是否选中
                state.setSelected(false);
                vo.setState(state);
                processLevelStatus(vo.getChildren());
            } else {
                StateVO state = new StateVO();
                // 是否展开
                state.setOpened(false);
                // 是否选中
                state.setSelected(false);
                vo.setState(state);

                vo.setType("leaf");
            }
        }
    }

    /**
     * 递归处理所有层级ID
     * @param list
     *            list集合
     * @param idList
     */
    private void processLevelId(List<CustomerLevelVO> list, List<Integer> idList) {

        for (CustomerLevelVO vo : list) {
            // 添加下属客户ID
            idList.add(Integer.valueOf(vo.getId()));
            if (vo.getChildren() != null && vo.getChildren().size() > 0) {
                // 递归添加所有下属客户ID
                processLevelId(vo.getChildren(), idList);
            }
        }
    }

    /**
     * 分页查询功能
     * @param pageNum
     *            当前页数
     * @param pageRows
     *            每页显示数量
     * @param num
     *            查询总件数
     * @return
     */
    private List<CustomerVO> fingPageInfo(CustomerEntity bean, Integer pageNum, Integer pageRows, Integer num) {

        if (num > 0) {
            // 每页显示数量
            bean.setPageNumber(pageRows);
            // 总数量
            bean.setTotalNumber(num);
            // 当前页数
            bean.setCurrentPage(pageNum);

            // 查询分页信息
            List<CustomerVO> list = customerEntityMapper.queryCustomerInfos(bean);
            return list;
        }
        return new ArrayList<CustomerVO>();
    }

    /**
     * 账号管理分页查询功能
     * @param pageNum
     *            当前页数
     * @param pageRows
     *            每页显示数量
     * @param num
     *            查询总件数
     * @return
     */
    private List<UserEntity> fingAccountPageInfo(UserEntity bean, Integer pageNum, Integer pageRows, Integer num) {

        if (num > 0) {
            // 每页显示数量
            bean.setPageNumber(pageRows);
            // 总数量
            bean.setTotalNumber(num);
            // 当前页数
            bean.setCurrentPage(pageNum);

            // 查询分页信息
            List<UserEntity> list = userEntityMapper.queryCustomerAccountInfos(bean);
            return list;
        }
        return new ArrayList<UserEntity>();
    }
}
