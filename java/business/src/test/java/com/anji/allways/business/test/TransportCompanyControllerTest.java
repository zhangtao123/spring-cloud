/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.test;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.anji.allways.base.aop.LoggerManage;
import com.anji.allways.business.BusinessServiceApplication;
import com.anji.allways.business.controller.TransportCompanyController;
import com.anji.allways.business.entity.CategoryDictEntity;
import com.anji.allways.business.entity.DictEntity;
import com.anji.allways.business.entity.TransportCompanyEntity;
import com.anji.allways.business.service.CategoryDictService;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.service.TransportCompanyService;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.jayway.jsonpath.JsonPath;

/**
 * <pre>
 * </pre>
 *
 * @author xuyuyang
 * @version $Id: TransportCompanyControllerTest.java, v 0.1 2017年8月24日 下午4:37:13 xuyuyang Exp $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BusinessServiceApplication.class)
@WebAppConfiguration
public class TransportCompanyControllerTest {

    @Autowired
    private TransportCompanyController transportCompanyController;

    @Autowired
    private TransportCompanyService    transportCompanyService;

    @Autowired
    private CategoryDictService        categoryDictService;

    // 获取数据字典Service
    @Autowired
    private DictService                dictService;

    /**
     * 测试查询运输公司信息列表
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试查询运输公司信息列表")
    public void testQueryTransportCompanyInfos() throws Exception {

        // 初始化司机状态数据字典
        Integer dictCategoryId = this.mockCategoryDictDate(null, 1, "COMSTA000001", "运输公司状态");

        // 初始化数据字典表
        this.mockDictDate(null, dictCategoryId, 1, "0", "停用");
        this.mockDictDate(null, dictCategoryId, 1, "1", "正常");

        // 初始化运输公司数据
        this.mockDate(null, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "测试查询运输公司信息列表");

        MockMvc mock = MockMvcBuilders.standaloneSetup(transportCompanyController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("pageRows", 10);
        map.put("name", "海汽");
        map.put("city", "上海1");
        map.put("status", "1");
        map.put("contactor", "李");
        map.put("contactorMobile", "567");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(
                MockMvcRequestBuilders.post("/business/transportCompany/queryTransportCompanyInfos").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试获取运输公司状态下拉列表
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试获取运输公司状态下拉列表")
    public void testQueryCompanyStatusForDict() throws Exception {

        // 初始化司机状态数据字典
        Integer dictCategoryId = this.mockCategoryDictDate(null, 1, "COMSTA000001", "运输公司状态");

        // 初始化数据字典表
        this.mockDictDate(null, dictCategoryId, 1, "0", "停用");
        this.mockDictDate(null, dictCategoryId, 1, "1", "正常");

        // 初始化运输公司数据
        this.mockDate(null, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "测试获取运输公司状态下拉列表");

        MockMvc mock = MockMvcBuilders.standaloneSetup(transportCompanyController).build();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(
                MockMvcRequestBuilders.post("/business/transportCompany/queryCompanyStatusForDict").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试批量激活运输公司信息（未选中记录）
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试批量激活运输公司信息（未选中记录）")
    public void testSetEnableCompanyInfos01() throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("ids", "");

        MockMvc mock = MockMvcBuilders.standaloneSetup(transportCompanyController).build();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/transportCompany/setEnableCompanyInfos").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.COMPANY_ENABLE_IDS_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.COMPANY_ENABLE_IDS_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试批量激活运输公司信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试批量激活运输公司信息")
    public void testSetEnableCompanyInfos02() throws Exception {

        // 初始化运输公司数据
        int id1 = this.mockDate(null, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "测试批量激活运输公司信息");

        // 初始化运输公司数据
        int id2 = this.mockDate(null, 0, "上海汽车运输1", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "王五", "12345678922", "测试批量激活运输公司信息");

        // 初始化运输公司数据
        int id3 = this.mockDate(null, 0, "上海汽车运输1", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "王五", "12345678922", "测试批量激活运输公司信息");

        // 选中的记录Id
        String ids = String.valueOf(id1) + "," + String.valueOf(id2) + "," + String.valueOf(id3);

        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);

        MockMvc mock = MockMvcBuilders.standaloneSetup(transportCompanyController).build();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/transportCompany/setEnableCompanyInfos").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试批量停用运输公司信息（未选中记录）
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试批量停用运输公司信息（未选中记录）")
    public void testSetDisableCompanyInfos01() throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("ids", "");

        MockMvc mock = MockMvcBuilders.standaloneSetup(transportCompanyController).build();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(
                MockMvcRequestBuilders.post("/business/transportCompany/setDisableCompanyInfos").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.COMPANY_DISABLE_IDS_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.COMPANY_DISABLE_IDS_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试批量停用运输公司信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试批量停用运输公司信息")
    public void testSetDisableCompanyInfos02() throws Exception {

        // 初始化运输公司数据
        int id1 = this.mockDate(null, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "测试批量停用运输公司信息");

        // 初始化运输公司数据
        int id2 = this.mockDate(null, 1, "上海汽车运输1", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "王五", "12345678922", "测试批量停用运输公司信息");

        // 初始化运输公司数据
        int id3 = this.mockDate(null, 0, "上海汽车运输1", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "王五", "12345678922", "测试批量停用运输公司信息");

        // 选中的记录Id
        String ids = String.valueOf(id1) + "," + String.valueOf(id2) + "," + String.valueOf(id3);

        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);

        MockMvc mock = MockMvcBuilders.standaloneSetup(transportCompanyController).build();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(
                MockMvcRequestBuilders.post("/business/transportCompany/setDisableCompanyInfos").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试新增运输公司信息(名称重复)
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试新增运输公司信息(名称重复)")
    public void testSaveTransportCompany01() throws Exception {

        this.mockDate(null, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "测试新增运输公司信息(名称重复)");
        MockMvc mock = MockMvcBuilders.standaloneSetup(transportCompanyController).build();
        // 创建测试数据
        TransportCompanyEntity bean = new TransportCompanyEntity();
        // 运输公司名
        bean.setName("上海汽车运输");
        // 所属省份编码
        bean.setProvinceCode("310000");
        // 详细地址
        bean.setAddress("上海市徐汇区");
        // 联系人
        bean.setContactor("张三");
        // 联系电话
        bean.setContactorMobile("12345678912");
        // 描述
        bean.setComment("测试新增运输公司信息");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", bean);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/transportCompany/saveTransportCompany").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.COMPANY_NAME_REPEAT_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.COMPANY_NAME_REPEAT_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试编辑运输公司信息(名称重复)
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试编辑运输公司信息(名称重复)")
    public void testSaveTransportCompany02() throws Exception {

        this.mockDate(1, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "测试编辑运输公司信息(名称重复)");
        MockMvc mock = MockMvcBuilders.standaloneSetup(transportCompanyController).build();
        // 创建测试数据
        TransportCompanyEntity bean = new TransportCompanyEntity();
        bean.setId(2);
        // 运输公司名
        bean.setName("上海汽车运输");
        // 所属省份编码
        bean.setProvinceCode("310000");
        // 详细地址
        bean.setAddress("上海市徐汇区");
        // 联系人
        bean.setContactor("张三");
        // 联系电话
        bean.setContactorMobile("12345678912");
        // 描述
        bean.setComment("测试新增运输公司信息");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", bean);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/transportCompany/saveTransportCompany").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.COMPANY_NAME_REPEAT_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.COMPANY_NAME_REPEAT_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试新增运输公司信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试新增运输公司信息")
    public void testSaveTransportCompany03() throws Exception {

        MockMvc mock = MockMvcBuilders.standaloneSetup(transportCompanyController).build();
        // 创建测试数据
        TransportCompanyEntity bean = new TransportCompanyEntity();
        // 运输公司名
        bean.setName("上海汽车运输");
        // 所属省份编码
        bean.setProvinceCode("310000");
        // 所属省份
        bean.setProvinceName("上海");
        // 所属城市
        bean.setCityName("上海城区");
        // 所属城市编码
        bean.setCityCode("310100");
        // 所属区县
        bean.setDistrictName("杨浦区");
        // 所属区县编码
        bean.setDistrictCode("310110");
        // 详细地址
        bean.setAddress("上海市杨浦区");
        // 联系人
        bean.setContactor("张三");
        // 联系电话
        bean.setContactorMobile("12345678912");
        // 描述
        bean.setComment("测试新增运输公司信息");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", bean);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/transportCompany/saveTransportCompany").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试编辑运输公司信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试编辑运输公司信息")
    public void testSaveTransportCompany04() throws Exception {

        Integer id = this.mockDate(1, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "测试编辑运输公司信息");
        MockMvc mock = MockMvcBuilders.standaloneSetup(transportCompanyController).build();
        // 创建测试数据
        TransportCompanyEntity bean = new TransportCompanyEntity();
        bean.setId(id);
        // 运输公司名
        bean.setName("上海汽车运输");
        // 所属省份编码
        bean.setProvinceCode("310000");
        // 所属省份
        bean.setProvinceName("上海");
        // 所属城市
        bean.setCityName("上海城区");
        // 所属城市编码
        bean.setCityCode("310100");
        // 所属区县
        bean.setDistrictName("杨浦区");
        // 所属区县编码
        bean.setDistrictCode("310110");
        // 详细地址
        bean.setAddress("上海市杨浦区1");
        // 联系人
        bean.setContactor("张三");
        // 联系电话
        bean.setContactorMobile("12345678912");
        // 描述
        bean.setComment("测试新增运输公司信息");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", bean);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/transportCompany/saveTransportCompany").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试根据ID获取运输公司信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试根据ID获取运输公司信息")
    public void testselectInfoById() throws Exception {

        Integer id = this.mockDate(null, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "试根据ID获取运输公司信息");
        MockMvc mock = MockMvcBuilders.standaloneSetup(transportCompanyController).build();
        // 创建测试数据
        TransportCompanyEntity bean = new TransportCompanyEntity();

        bean.setId(id);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", bean);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/transportCompany/selectInfoById").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 初始化运输公司数据
     * @param bean
     *            运输公司Entity
     * @param id
     * @param status
     *            运输公司状态
     * @param params
     * @throws ParseException
     */
    private Integer mockDate(Integer id, Integer status, String... params) throws ParseException {
        Assert.assertNotNull(params);
        Assert.assertTrue(params.length == 11);

        TransportCompanyEntity bean = new TransportCompanyEntity();
        int i = 0;
        // 运输公司名
        bean.setName(params[i++]);
        // 所属省份
        bean.setProvinceName(params[i++]);
        // 所属省份编码
        bean.setProvinceCode(params[i++]);
        // 所属城市
        bean.setCityName(params[i++]);
        // 所属城市编码
        bean.setCityCode(params[i++]);
        // 所属区县
        bean.setDistrictName(params[i++]);
        // 所属区县编码
        bean.setDistrictCode(params[i++]);
        // 详细地址
        bean.setAddress(params[i++]);
        // 联系人
        bean.setContactor(params[i++]);
        // 联系电话
        bean.setContactorMobile(params[i++]);
        // 司机数
        bean.setDriverAmount(2);
        // 状态
        bean.setStatus(status);
        // 好评率
        bean.setFavorableRate(Float.valueOf(1));
        // 及时率
        bean.setOntimeRate(Float.valueOf(1));
        // 描述
        bean.setComment(params[i++]);
        // 创建用户
        bean.setCreateUser(112233456);
        // 创建时间
        bean.setCreateTime(new Date());
        // 最后更新用户
        bean.setUpdateUser(2222334);
        // 更新时间
        bean.setUpdateTime(new Date());

        if (null != id && id > 0) {
            bean.setId(id);
        }

        return transportCompanyService.insert(bean);
    }

    /**
     * 初始化数据分类字典表
     * @param bean
     *            运输公司Entity
     * @param id
     * @param isValid
     *            是否有效
     * @param params
     * @throws ParseException
     */
    private Integer mockCategoryDictDate(Integer id, Integer isValid, String... params) throws ParseException {
        Assert.assertNotNull(params);
        Assert.assertTrue(params.length == 2);

        CategoryDictEntity categoryDictEntity = new CategoryDictEntity();

        int i = 0;
        // 分类码
        categoryDictEntity.setCategoryCode(params[i++]);

        // 类别名
        categoryDictEntity.setCategoryName(params[i++]);

        // 是否有效
        categoryDictEntity.setIsValid(isValid);

        if (null != id && id > 0) {
            categoryDictEntity.setId(id);
        }

        return categoryDictService.insert(categoryDictEntity);
    }

    /**
     * 初始化数据字典表
     * @param id
     * @param dictCategoryId
     * @param isValid
     *            是否有效
     * @param params
     * @throws ParseException
     */
    private void mockDictDate(Integer id, Integer dictCategoryId, Integer isValid, String... params) throws ParseException {
        Assert.assertNotNull(params);
        Assert.assertTrue(params.length == 2);

        DictEntity bean = new DictEntity();

        int i = 0;

        // 关键字
        bean.setDictKey(params[i++]);

        // 值
        bean.setDictValue(params[i++]);

        // 索引分类id
        bean.setDictCategoryId(dictCategoryId);

        // 是否有效
        bean.setIsValid(1);

        if (null != id && id > 0) {
            bean.setId(id);
        }

        dictService.insert(bean);
    }

}
