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
import com.anji.allways.business.controller.TruckDriverController;
import com.anji.allways.business.entity.CategoryDictEntity;
import com.anji.allways.business.entity.DictEntity;
import com.anji.allways.business.entity.TransportCompanyEntity;
import com.anji.allways.business.entity.TruckDriverEntity;
import com.anji.allways.business.service.CategoryDictService;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.service.TransportCompanyService;
import com.anji.allways.business.service.TruckDriverService;
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
public class TruckDriverControllerTest {

    @Autowired
    private TruckDriverController   truckDriverController;

    @Autowired
    private TransportCompanyService transportCompanyService;

    // 获取数据字典Service
    @Autowired
    private DictService             dictService;

    @Autowired
    private CategoryDictService     categoryDictService;

    // 运输司机Service
    @Autowired
    private TruckDriverService      truckDriverService;

    /**
     * 测试查询运输司机信息列表
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试查询运输司机信息列表")
    public void testQueryTruckDriverInfos() throws Exception {

        // 初始化司机状态数据字典
        Integer dictCategoryId = this.mockCategoryDictDate(null, 1, "DRIVERSTA000001", "运输司机状态");

        // 初始化数据字典表
        this.mockDictDate(null, dictCategoryId, 1, "0", "停用");
        this.mockDictDate(null, dictCategoryId, 1, "1", "正常");

        // 初始化运输公司数据
        int companyId = this.mockCompanyDate(null, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "测试新增运输公司信息");

        // 初始化运输司机数据
        this.mockDriverDate(null, 1, companyId, "12233445566", "张三", "420683199922221111", "/path/front.jpg", "/path/back.jpg", "zhangsan", "上海汽车运输", "沪S-A0001");

        MockMvc mock = MockMvcBuilders.standaloneSetup(truckDriverController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("pageRows", 10);
        map.put("name", "张");
        map.put("mobile", "5566");
        map.put("truckNumber", "-A0001");
        map.put("accountName", "san");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/truckDriver/queryTruckDriverInfos").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试获取运输公司名称下拉列表
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试获取运输公司名称下拉列表")
    public void testQueryCompanyForDict() throws Exception {

        // 初始化运输公司数据
        this.mockCompanyDate(null, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "测试新增运输公司信息(名称重复)");

        MockMvc mock = MockMvcBuilders.standaloneSetup(truckDriverController).build();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "李");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/truckDriver/queryCompanyForDict").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试获取运输司机状态下拉列表
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试获取运输司机状态下拉列表")
    public void testQueryDriverStatusForDict() throws Exception {

        // 初始化司机状态数据字典
        Integer dictCategoryId = this.mockCategoryDictDate(null, 1, "DRIVERSTA000001", "运输司机状态");

        // 初始化数据字典表
        this.mockDictDate(null, dictCategoryId, 1, "0", "停用");
        this.mockDictDate(null, dictCategoryId, 1, "1", "正常");

        // 初始化运输公司数据
        int companyId = this.mockCompanyDate(null, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "测试新增运输公司信息(名称重复)");

        // 初始化运输司机数据
        this.mockDriverDate(null, 1, companyId, "12233445566", "张三", "420683199922221111", "/path/front.jpg", "/path/back.jpg", "zhangsan", "上海汽车运输", "沪S-A0001");

        MockMvc mock = MockMvcBuilders.standaloneSetup(truckDriverController).build();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/truckDriver/queryDriverStatusForDict").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试批量激活司机信息（未选中记录）
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试批量激活司机信息（未选中记录）")
    public void testSetEnableDriverInfos01() throws Exception {

        MockMvc mock = MockMvcBuilders.standaloneSetup(truckDriverController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("ids", "");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/truckDriver/setEnableDriverInfos").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.DRIVER_ENABLE_IDS_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.DRIVER_ENABLE_IDS_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试批量激活司机信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试批量激活司机信息")
    public void testSetEnableDriverInfos02() throws Exception {

        // 初始化运输司机数据
        int id1 = this.mockDriverDate(null, 1, 1, "12233445566", "张三", "420683199922221111", "/path/front.jpg", "/path/back.jpg", "zhangsan", "上海汽车运输1", "沪S-A0001");

        int id2 = this.mockDriverDate(null, 0, 2, "12233445577", "王五", "420683199922221133", "/path/front.jpg", "/path/back.jpg", "wangwu", "上海汽车运输2", "沪S-A0002");

        int id3 = this.mockDriverDate(null, 0, 3, "12233445588", "马六", "420683199922221144", "/path/front.jpg", "/path/back.jpg", "maliu", "上海汽车运输3", "沪S-A0003");

        // 选中的记录Id
        String ids = String.valueOf(id1) + "," + String.valueOf(id2) + "," + String.valueOf(id3);

        MockMvc mock = MockMvcBuilders.standaloneSetup(truckDriverController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/truckDriver/setEnableDriverInfos").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试批量停用司机信息（未选中记录）
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试批量停用司机信息（未选中记录）")
    public void testSetDisableDriverInfos01() throws Exception {

        MockMvc mock = MockMvcBuilders.standaloneSetup(truckDriverController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("ids", "");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/truckDriver/setDisableDriverInfos").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.DRIVER_DISABLE_IDS_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.DRIVER_DISABLE_IDS_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试批量停用司机信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试批量停用司机信息")
    public void testSetDisableDriverInfos02() throws Exception {

        // 初始化运输司机数据
        int id1 = this.mockDriverDate(null, 1, 1, "12233445566", "张三", "420683199922221111", "/path/front.jpg", "/path/back.jpg", "zhangsan", "上海汽车运输1", "沪S-A0001");

        int id2 = this.mockDriverDate(null, 1, 2, "12233445577", "王五", "420683199922221133", "/path/front.jpg", "/path/back.jpg", "wangwu", "上海汽车运输2", "沪S-A0002");

        int id3 = this.mockDriverDate(null, 0, 3, "12233445588", "马六", "420683199922221144", "/path/front.jpg", "/path/back.jpg", "maliu", "上海汽车运输3", "沪S-A0003");

        // 选中的记录Id
        String ids = String.valueOf(id1) + "," + String.valueOf(id2) + "," + String.valueOf(id3);

        MockMvc mock = MockMvcBuilders.standaloneSetup(truckDriverController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/truckDriver/setDisableDriverInfos").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试根据ID获取运输司机信息(查询不到司机信息)
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试根据ID获取运输司机信息(查询不到司机信息)")
    public void testSelectInfoById01() throws Exception {

        // 初始化司机状态数据字典
        Integer dictCategoryId = this.mockCategoryDictDate(null, 1, "DRIVERSTA000001", "运输司机状态");

        // 初始化数据字典表
        this.mockDictDate(null, dictCategoryId, 1, "0", "停用");
        this.mockDictDate(null, dictCategoryId, 1, "1", "正常");

        // 初始化运输司机数据
        this.mockDriverDate(null, 1, 1, "12233445566", "张三", "420683199922221111", "/path/front.jpg", "/path/back.jpg", "zhangsan", "上海汽车运输1", "沪S-A0001");

        MockMvc mock = MockMvcBuilders.standaloneSetup(truckDriverController).build();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 0);

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/truckDriver/selectInfoById").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.DRIVER_ID_EMPTY_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.DRIVER_ID_EMPTY_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试根据ID获取运输司机信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试根据ID获取运输司机信息")
    public void testSelectInfoById02() throws Exception {

        // 初始化运输司机数据
        int id1 = this.mockDriverDate(null, 1, 1, "12233445566", "张三", "420683199922221111", "/path/front.jpg", "/path/back.jpg", "zhangsan", "上海汽车运输1", "沪S-A0001");

        MockMvc mock = MockMvcBuilders.standaloneSetup(truckDriverController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("id", id1);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/truckDriver/selectInfoById").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试新增运输司机信息（所属运输公司不存在）
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试新增运输司机信息（所属运输公司不存在）")
    public void testSaveTruckDriver01() throws Exception {

        // 初始化运输公司数据
        this.mockCompanyDate(null, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "测试新增运输公司信息(名称重复)");

        // 初始化运输司机数据
        this.mockDriverDate(null, 1, 1, "12233445566", "张三", "420683199922221111", "/path/front.jpg", "/path/back.jpg", "zhangsan", "上海汽车运输1", "沪S-A0001");

        MockMvc mock = MockMvcBuilders.standaloneSetup(truckDriverController).build();

        TruckDriverEntity bean = new TruckDriverEntity();
        // 运输公司名称
        bean.setTransportCompanyName("XXXXXXXX");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", bean);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/truckDriver/saveTruckDriver").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.DRIVER_COMPANY_NOTEXISTS_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.DRIVER_COMPANY_NOTEXISTS_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试新增运输司机信息（账号名已存在）
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试新增运输司机信息（账号名已存在）")
    public void testSaveTruckDriver03() throws Exception {

        // 初始化运输公司数据
        this.mockCompanyDate(null, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "测试新增运输公司信息(名称重复)");

        // 初始化运输司机数据
        this.mockDriverDate(null, 1, 1, "12233445566", "张三", "420683199922221111", "/path/front.jpg", "/path/back.jpg", "zhangsan", "上海汽车运输1", "沪S-A0001");

        MockMvc mock = MockMvcBuilders.standaloneSetup(truckDriverController).build();

        TruckDriverEntity bean = new TruckDriverEntity();
        // 运输公司名称
        bean.setTransportCompanyName("上海汽车运输");
        // 车牌号
        bean.setTruckNumber("沪S-A0002");
        // 账号名
        bean.setAccountName("zhangsan");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", bean);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/truckDriver/saveTruckDriver").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.ACCOUNT_NAME_EXISTS_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.ACCOUNT_NAME_EXISTS_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试新增运输司机信息（司机已存在）
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试新增运输司机信息（司机已存在）")
    public void testSaveTruckDriver04() throws Exception {

        // 初始化运输公司数据
        int id = this.mockCompanyDate(null, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "测试新增运输公司信息(名称重复)");

        // 初始化运输司机数据
        this.mockDriverDate(null, 1, id, "12233445566", "张三", "420683199922221111", "/path/front.jpg", "/path/back.jpg", "zhangsan", "上海汽车运输", "沪S-A0001");

        MockMvc mock = MockMvcBuilders.standaloneSetup(truckDriverController).build();

        TruckDriverEntity bean = new TruckDriverEntity();
        // 运输公司名称
        bean.setTransportCompanyId(id);
        // 运输公司名称
        bean.setTransportCompanyName("上海汽车运输");
        // 车牌号
        bean.setTruckNumber("沪S-A0002");
        // 账号名
        bean.setAccountName("lisi");
        // 联系电话
        bean.setMobile("12233445566");
        // 姓名
        bean.setName("李四");
        // 身份证号码
        bean.setIdCardNo("420683199922221133");
        // 身份证正面照
        bean.setIdCardFrontPicture("/path/front.jpg");
        // 身份证背面照
        bean.setIdCardBackPicture("/path/back.jpg");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", bean);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/truckDriver/saveTruckDriver").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.DRIVER_EXISTS_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.DRIVER_EXISTS_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试新增运输司机信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试新增运输司机信息")
    public void testSaveTruckDriver05() throws Exception {

        // 初始化运输公司数据
        this.mockCompanyDate(null, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "测试新增运输公司信息(名称重复)");

        // 初始化运输司机数据
        this.mockDriverDate(null, 1, 1, "12233445566", "张三", "420683199922221111", "/path/front.jpg", "/path/back.jpg", "zhangsan", "上海汽车运输", "沪S-A0001");

        MockMvc mock = MockMvcBuilders.standaloneSetup(truckDriverController).build();

        TruckDriverEntity bean = new TruckDriverEntity();
        // 运输公司名称
        bean.setTransportCompanyName("上海汽车运输");
        // 车牌号
        bean.setTruckNumber("沪S-A0002");
        // 账号名
        bean.setAccountName("lisi");
        // 联系电话
        bean.setMobile("12233445577");
        // 姓名
        bean.setName("李四");
        // 身份证号码
        bean.setIdCardNo("420683199922221133");
        // 身份证正面照
        bean.setIdCardFrontPicture("/path/front.jpg");
        // 身份证背面照
        bean.setIdCardBackPicture("/path/back.jpg");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", bean);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/truckDriver/saveTruckDriver").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试编辑运输司机信息(所属运输公司不存在)
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试编辑运输司机信息(所属运输公司不存在)")
    public void testSaveTruckDriver06() throws Exception {

        // 初始化运输公司数据
        this.mockCompanyDate(null, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "测试新增运输公司信息(名称重复)");

        // 初始化运输司机数据
        int id = this.mockDriverDate(null, 1, 1, "12233445566", "张三", "420683199922221111", "/path/front.jpg", "/path/back.jpg", "zhangsan", "上海汽车运输", "沪S-A0001");

        this.mockDriverDate(null, 0, 2, "12233445577", "王五", "420683199922221133", "/path/front.jpg", "/path/back.jpg", "wangwu", "上海汽车运输2", "沪S-A0002");

        MockMvc mock = MockMvcBuilders.standaloneSetup(truckDriverController).build();

        TruckDriverEntity bean = new TruckDriverEntity();
        bean.setId(id);
        // 运输公司名称
        bean.setTransportCompanyName("上海汽车运输XXX");
        // 车牌号
        bean.setTruckNumber("沪S-A0002");
        // 账号名
        bean.setAccountName("lisi");
        // 联系电话
        bean.setMobile("12233445577");
        // 姓名
        bean.setName("李四");
        // 身份证号码
        bean.setIdCardNo("420683199922221133");
        // 身份证正面照
        bean.setIdCardFrontPicture("/path/front.jpg");
        // 身份证背面照
        bean.setIdCardBackPicture("/path/back.jpg");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", bean);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/truckDriver/saveTruckDriver").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.DRIVER_COMPANY_NOTEXISTS_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.DRIVER_COMPANY_NOTEXISTS_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试编辑运输司机信息(账号名已存在)
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试编辑运输司机信息(账号名已存在)")
    public void testSaveTruckDriver08() throws Exception {

        // 初始化运输公司数据
        this.mockCompanyDate(null, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "测试新增运输公司信息(名称重复)");

        // 初始化运输司机数据
        int id = this.mockDriverDate(null, 1, 1, "12233445566", "张三", "420683199922221111", "/path/front.jpg", "/path/back.jpg", "zhangsan", "上海汽车运输", "沪S-A0001");

        this.mockDriverDate(null, 0, 2, "12233445577", "王五", "420683199922221133", "/path/front.jpg", "/path/back.jpg", "wangwu", "上海汽车运输2", "沪S-A0002");

        MockMvc mock = MockMvcBuilders.standaloneSetup(truckDriverController).build();

        TruckDriverEntity bean = new TruckDriverEntity();
        bean.setId(id);
        // 运输公司名称
        bean.setTransportCompanyName("上海汽车运输");
        // 车牌号
        bean.setTruckNumber("沪S-A0003");
        // 账号名
        bean.setAccountName("wangwu");
        // 联系电话
        bean.setMobile("12233445588");
        // 姓名
        bean.setName("李四");
        // 身份证号码
        bean.setIdCardNo("420683199922221133");
        // 身份证正面照
        bean.setIdCardFrontPicture("/path/front.jpg");
        // 身份证背面照
        bean.setIdCardBackPicture("/path/back.jpg");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", bean);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/truckDriver/saveTruckDriver").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.ACCOUNT_NAME_EXISTS_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.ACCOUNT_NAME_EXISTS_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试编辑运输司机信息(司机已在其他运输公司存在)
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试编辑运输司机信息(司机已在其他运输公司存在)")
    public void testSaveTruckDriver09() throws Exception {

        // 初始化运输公司数据
        int id1 = this.mockCompanyDate(null, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "测试新增运输公司信息(名称重复)");

        // 初始化运输司机数据
        int id = this.mockDriverDate(null, 1, 1, "12233445577", "张三", "420683199922221111", "/path/front.jpg", "/path/back.jpg", "zhangsan", "上海汽车运输", "沪S-A0001");

        this.mockDriverDate(null, 0, 2, "12233445577", "王五", "420683199922221133", "/path/front.jpg", "/path/back.jpg", "wangwu", "上海汽车运输2", "沪S-A0002");

        MockMvc mock = MockMvcBuilders.standaloneSetup(truckDriverController).build();

        TruckDriverEntity bean = new TruckDriverEntity();
        bean.setId(id);
        bean.setTransportCompanyId(id1);
        // 运输公司名称
        bean.setTransportCompanyName("上海汽车运输");
        // 车牌号
        bean.setTruckNumber("沪S-A0003");
        // 账号名
        bean.setAccountName("zhangsan");
        // 联系电话
        bean.setMobile("12233445577");
        // 姓名
        bean.setName("李四");
        // 身份证号码
        bean.setIdCardNo("420683199922221133");
        // 身份证正面照
        bean.setIdCardFrontPicture("/path/front.jpg");
        // 身份证背面照
        bean.setIdCardBackPicture("/path/back.jpg");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", bean);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/truckDriver/saveTruckDriver").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.DRIVER_IN_OTHERCOMPANY_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.DRIVER_IN_OTHERCOMPANY_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试编辑运输司机信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试编辑运输司机信息")
    public void testSaveTruckDriver10() throws Exception {

        // 初始化运输公司数据
        this.mockCompanyDate(null, 1, "上海汽车运输", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区1", "李四", "12345678912", "测试新增运输公司信息(名称重复)");

        // 初始化运输司机数据
        int id = this.mockDriverDate(null, 1, 1, "12233445566", "张三", "420683199922221111", "/path/front.jpg", "/path/back.jpg", "zhangsan", "上海汽车运输", "沪S-A0001");

        this.mockDriverDate(null, 0, 2, "12233445577", "王五", "420683199922221133", "/path/front.jpg", "/path/back.jpg", "wangwu", "上海汽车运输2", "沪S-A0002");

        MockMvc mock = MockMvcBuilders.standaloneSetup(truckDriverController).build();

        TruckDriverEntity bean = new TruckDriverEntity();
        bean.setId(id);
        // 运输公司名称
        bean.setTransportCompanyName("上海汽车运输");
        // 车牌号
        bean.setTruckNumber("沪S-A0003");
        // 账号名
        bean.setAccountName("lisi");
        // 联系电话
        bean.setMobile("12233445588");
        // 姓名
        bean.setName("李四");
        // 身份证号码
        bean.setIdCardNo("420683199922221133");
        // 身份证正面照
        bean.setIdCardFrontPicture("/path/front.jpg");
        // 身份证背面照
        bean.setIdCardBackPicture("/path/back.jpg");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", bean);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/truckDriver/saveTruckDriver").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
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
    private int mockCompanyDate(Integer id, Integer status, String... params) throws ParseException {
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
     * 初始化运输司机数据
     * @param id
     * @param status
     *            运输司机状态(0:停用 1:正常)
     * @param companyId
     *            运输公司ID
     * @param params
     * @throws ParseException
     */
    private int mockDriverDate(Integer id, Integer status, int companyId, String... params) throws ParseException {
        Assert.assertNotNull(params);
        Assert.assertTrue(params.length == 8);

        TruckDriverEntity bean = new TruckDriverEntity();
        int i = 0;
        // 司机手机号
        bean.setMobile(params[i++]);

        // 司机名字
        bean.setName(params[i++]);

        // 身份证号码
        bean.setIdCardNo(params[i++]);

        // 身份证正面照
        bean.setIdCardFrontPicture(params[i++]);

        // 身份证背面照
        bean.setIdCardBackPicture(params[i++]);

        // 账号名
        bean.setAccountName(params[i++]);

        // 所属运输公司名
        bean.setTransportCompanyName(params[i++]);

        // 所属运输公司id
        bean.setTransportCompanyId(companyId);

        // 车牌号
        bean.setTruckNumber(params[i++]);

        // 状态
        bean.setStatus(status);

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

        return truckDriverService.insert(bean);
    }

}
