/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.anji.allways.business.controller.WarehouseController;
import com.anji.allways.business.entity.CategoryDictEntity;
import com.anji.allways.business.entity.CustomerEntity;
import com.anji.allways.business.entity.DictEntity;
import com.anji.allways.business.entity.UserEntity;
import com.anji.allways.business.entity.WarehouseEntity;
import com.anji.allways.business.entity.WarehouseLinkCustomerEntity;
import com.anji.allways.business.mapper.UserEntityMapper;
import com.anji.allways.business.service.CategoryDictService;
import com.anji.allways.business.service.CustomerService;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.service.WarehouseLinkCustomerService;
import com.anji.allways.business.service.WarehouseService;
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
public class WarehouseControllerTest {

    @Autowired
    private WarehouseController          warehouseController;

    // 获取数据字典Service
    @Autowired
    private DictService                  dictService;

    @Autowired
    private CategoryDictService          categoryDictService;

    @Autowired
    private WarehouseService             warehouseService;

    @Autowired
    private WarehouseLinkCustomerService warehouseLinkCustomerService;

    @Autowired
    private CustomerService              customerService;

    @Autowired
    private UserEntityMapper             userEntityMapper;

    /**
     * 测试查询仓库信息列表
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试查询仓库信息列表")
    public void testQueryWarehouseInfos() throws Exception {

        // 初始化仓库状态数据字典
        Integer dictCategoryId1 = this.mockCategoryDictDate(null, 1, "TESTWAREHOUSESTA000001", "仓库状态");

        // 初始化营业时间类型数据字典
        Integer dictCategoryId2 = this.mockCategoryDictDate(null, 1, "TESTBUSSIONTIME000001", "营业时间类型");

        // 初始化数据字典表（仓库状态）
        this.mockDictDate(null, dictCategoryId1, 1, "0", "停用");
        this.mockDictDate(null, dictCategoryId1, 1, "1", "启用");

        // 初始化数据字典表（营业时间类型）
        this.mockDictDate(null, dictCategoryId2, 1, "0", "全天");
        this.mockDictDate(null, dictCategoryId2, 1, "1", "时间段");

        // 创建仓库信息数据
        int id = this.mockWarehouseDate(null, 1, 0, "测试仓库", "测试仓库", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00", "18:00",
            "1", "10", "96", "测试仓库查询");
        this.mockWarehouseDate(null, 1, id, "测试仓库1", "测试仓库1", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "李四", "12233445577", "10:00", "17:00", "2",
            "11", "90", "测试仓库查询1");

        this.mockWarehouseDate(null, 1, id, "测试仓库2", "测试仓库2", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三11", "12233445588", "00:00", "23:59",
            "2", "11", "90", "测试仓库查询1");
        
        // 初始化账户数据
        int userId=this.mockUserDate(null, 2, "测试人", "test", "1111", "1", "2", "1", "1", "1", "测试账号", "dfsdfs", "测试", "测试");

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("pageRows", 10);
        map.put("name", "试仓");
        map.put("contactor", "张三");
        map.put("contactorMobile", "3445");
        map.put("status", "1");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "2_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", userId);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/queryWarehouseInfos").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试获取仓库状态下拉列表
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试获取仓库状态下拉列表")
    public void testQueryWarehouseStatusForDict() throws Exception {

        // 初始化仓库状态数据字典
        Integer dictCategoryId1 = this.mockCategoryDictDate(null, 1, "TESTWAREHOUSESTA000001", "仓库状态");

        // 初始化数据字典表（仓库状态）
        this.mockDictDate(null, dictCategoryId1, 1, "0", "停用");
        this.mockDictDate(null, dictCategoryId1, 1, "1", "启用");

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        Map<String, Object> map = new HashMap<>();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/queryWarehouseStatusForDict").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试获取仓库营业时间类型下拉列表
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试获取仓库营业时间类型下拉列表")
    public void testQueryBusinessTimeForDict() throws Exception {

        // 初始化营业时间类型数据字典
        Integer dictCategoryId2 = this.mockCategoryDictDate(null, 1, "TESTBUSSIONTIME000001", "营业时间类型");

        // 初始化数据字典表（营业时间类型）
        this.mockDictDate(null, dictCategoryId2, 1, "0", "全天");
        this.mockDictDate(null, dictCategoryId2, 1, "1", "时间段");

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/queryBusinessTimeForDict").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试批量激活仓库信息(未选择记录)
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试批量激活仓库信息(未选择记录)")
    public void testSetEnableWarehouseInfos01() throws Exception {

        // 创建仓库信息数据
        int id1 = this.mockWarehouseDate(null, 0, 0, "测试仓库", "测试仓库", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00",
            "18:00", "1", "10", "96", "测试批量激活仓库信息");
        this.mockWarehouseDate(null, 0, id1, "测试仓库1", "测试仓库1", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "李四", "12233445577", "10:00", "17:00", "2",
            "11", "90", "测试批量激活仓库信息");

        this.mockWarehouseDate(null, 1, id1, "测试仓库2", "测试仓库2", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三11", "12233445588", "00:00", "23:59",
            "2", "11", "90", "测试批量激活仓库信息");

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("ids", "");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/setEnableWarehouseInfos").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.WAREHOUSE_ENABLE_IDS_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.WAREHOUSE_ENABLE_IDS_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试批量激活仓库信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试批量激活仓库信息")
    public void testSetEnableWarehouseInfos02() throws Exception {

        // 创建仓库信息数据
        int id1 = this.mockWarehouseDate(null, 0, 0, "测试仓库", "测试仓库", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00",
            "18:00", "1", "10", "96", "测试批量激活仓库信息");
        int id2 = this.mockWarehouseDate(null, 0, id1, "测试仓库1", "测试仓库1", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "李四", "12233445577", "10:00",
            "17:00", "2", "11", "90", "测试批量激活仓库信息");

        int id3 = this.mockWarehouseDate(null, 1, id1, "测试仓库2", "测试仓库2", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三11", "12233445588", "00:00",
            "23:59", "2", "11", "90", "测试批量激活仓库信息");

        // 选中的记录Id
        String ids = String.valueOf(id1) + "," + String.valueOf(id2) + "," + String.valueOf(id3);

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/setEnableWarehouseInfos").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试批量停用仓库信息(未选择记录)
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试批量停用仓库信息(未选择记录)")
    public void testSetDisableWarehouseInfos01() throws Exception {

        // 创建仓库信息数据
        int id1 = this.mockWarehouseDate(null, 1, 0, "测试仓库", "测试仓库", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00",
            "18:00", "1", "10", "96", "测试批量停用仓库信息");
        this.mockWarehouseDate(null, 1, id1, "测试仓库1", "测试仓库1", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "李四", "12233445577", "10:00", "17:00", "2",
            "11", "90", "测试批量停用仓库信息");

        this.mockWarehouseDate(null, 0, id1, "测试仓库2", "测试仓库2", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三11", "12233445588", "00:00", "23:59",
            "2", "11", "90", "测试批量停用仓库信息");

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("ids", "");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/setDisableWarehouseInfos").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.WAREHOUSE_DISABLE_IDS_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.WAREHOUSE_DISABLE_IDS_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试批量停用仓库信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试批量停用仓库信息")
    public void testSetDisableWarehouseInfos02() throws Exception {

        // 创建仓库信息数据
        int id1 = this.mockWarehouseDate(null, 1, 0, "测试仓库", "测试仓库", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00",
            "18:00", "1", "10", "96", "测试批量停用仓库信息");
        int id2 = this.mockWarehouseDate(null, 1, id1, "测试仓库1", "测试仓库1", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "李四", "12233445577", "10:00",
            "17:00", "2", "11", "90", "测试批量停用仓库信息");

        int id3 = this.mockWarehouseDate(null, 0, id1, "测试仓库2", "测试仓库2", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三11", "12233445588", "00:00",
            "23:59", "2", "11", "90", "测试批量停用仓库信息");

        // 选中的记录Id
        String ids = String.valueOf(id1) + "," + String.valueOf(id2) + "," + String.valueOf(id3);

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/setDisableWarehouseInfos").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试根据ID获取仓库信息(未查询到仓库信息)
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试根据ID获取仓库信息(未查询到仓库信息)")
    public void testSelectInfoById01() throws Exception {

        // 创建仓库信息数据
        this.mockWarehouseDate(null, 1, 0, "测试仓库", "测试仓库", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00", "18:00", "1",
            "10", "96", "测试根据ID获取仓库信息");

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("id", 0);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/selectInfoById").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.WAREHOUSE_ID_EMPTY_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.WAREHOUSE_ID_EMPTY_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试根据ID获取仓库信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试根据ID获取仓库信息")
    public void testSelectInfoById02() throws Exception {

        // 创建仓库信息数据
        int id = this.mockWarehouseDate(null, 1, 0, "测试仓库", "测试仓库", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00", "18:00",
            "1", "10", "96", "测试根据ID获取仓库信息");

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/selectInfoById").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试获取仓库等级信息（层级展开）
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试获取仓库等级信息（层级展开）")
    public void testSelectWarehouseLevelInfor() throws Exception {

        // 创建仓库信息数据
        int id1 = this.mockWarehouseDate(null, 1, 0, "测试仓库", "测试仓库", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00",
            "18:00", "1", "10", "96", "测试获取仓库等级信息（层级展开）");

        int id2 = this.mockWarehouseDate(null, 1, id1, "测试仓库1", "测试仓库1", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "李四", "12233445577", "10:00",
            "17:00", "2", "11", "90", "测试获取仓库等级信息（层级展开）");

        int id3 = this.mockWarehouseDate(null, 0, id2, "测试仓库2", "测试仓库2", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三11", "12233445588", "00:00",
            "23:59", "2", "11", "90", "测试获取仓库等级信息（层级展开）");

        this.mockWarehouseDate(null, 0, id3, "测试仓库3", "测试仓库3", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三22", "12233445588", "00:00", "23:59",
            "2", "11", "90", "测试获取仓库等级信息（层级展开）");

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("parentId", "");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "2_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 2);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business-xyy/warehouse/selectWarehouseLevelInfor").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试新增仓库信息(仓库名称重复)
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试新增仓库信息(仓库名称重复)")
    public void testSaveWarehouseInfo01() throws Exception {

        // 创建仓库信息数据
        this.mockWarehouseDate(null, 1, 0, "测试仓库", "测试仓库", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00", "18:00", "1",
            "10", "96", "测试新增仓库信息");

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        WarehouseEntity entity = new WarehouseEntity();

        // 仓库名称
        entity.setName("测试仓库");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", entity);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/saveWarehouseInfo").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.WAREHOUSE_NAME_REPEAT_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.WAREHOUSE_NAME_REPEAT_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试新增仓库信息(仓库名简称重复)
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试新增仓库信息(仓库名简称重复)")
    public void testSaveWarehouseInfo02() throws Exception {

        // 创建仓库信息数据
        this.mockWarehouseDate(null, 1, 0, "测试仓库", "测试仓库简称", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00", "18:00", "1",
            "10", "96", "测试新增仓库信息");

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        WarehouseEntity entity = new WarehouseEntity();

        // 仓库名简称
        entity.setShortName("测试仓库简称");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", entity);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/saveWarehouseInfo").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.WAREHOUSE_SHORT_NAME_REPEAT_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.WAREHOUSE_SHORT_NAME_REPEAT_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试新增仓库信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试新增仓库信息")
    public void testSaveWarehouseInfo03() throws Exception {

        // 创建仓库信息数据
        int id = this.mockWarehouseDate(null, 1, 0, "测试仓库", "测试仓库简称", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00",
            "18:00", "1", "10", "96", "测试新增仓库信息");

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        WarehouseEntity entity = new WarehouseEntity();

        // 仓库名称
        entity.setName("测试仓库1");

        // 仓库名简称
        entity.setShortName("测试仓库简称1");

        // 上级仓库ID
        entity.setParentId(id);
        // 所属省份编码
        entity.setProvinceCode("310000");
        // 所属省份
        entity.setProvinceName("上海");
        // 所属城市
        entity.setCityName("上海城区");
        // 所属城市编码
        entity.setCityCode("310100");
        // 所属区县
        entity.setDistrictName("杨浦区");
        // 所属区县编码
        entity.setDistrictCode("310110");
        // 详细地址
        entity.setAddress("上海市杨浦区");
        // 营业开始时间
        entity.setBusinessStartTime("09:00");
        // 营业结束时间
        entity.setBusinessEndTime("17:00");
        // 联系人
        entity.setContactor("张三1");
        // 联系电话
        entity.setContactorMobile("12345678912");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", entity);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/saveWarehouseInfo").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试编辑仓库信息(仓库名称重复)
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试编辑仓库信息(仓库名称重复)")
    public void testSaveWarehouseInfo04() throws Exception {

        // 创建仓库信息数据
        int id = this.mockWarehouseDate(null, 1, 0, "测试仓库", "测试仓库", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00", "18:00",
            "1", "10", "96", "测试编辑仓库信息");

        this.mockWarehouseDate(null, 1, 0, "测试仓库1", "测试仓库1", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "李四", "12233445577", "10:00", "17:00", "2",
            "11", "90", "测试编辑仓库信息");

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        WarehouseEntity entity = new WarehouseEntity();

        entity.setId(id);
        // 仓库名称
        entity.setName("测试仓库1");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", entity);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/saveWarehouseInfo").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.WAREHOUSE_NAME_REPEAT_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.WAREHOUSE_NAME_REPEAT_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试编辑仓库信息(仓库名简称重复)
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试编辑仓库信息(仓库名简称重复)")
    public void testSaveWarehouseInfo05() throws Exception {

        // 创建仓库信息数据
        int id = this.mockWarehouseDate(null, 1, 0, "测试仓库", "测试仓库简称", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00",
            "18:00", "1", "10", "96", "测试新增仓库信息");

        this.mockWarehouseDate(null, 1, 0, "测试仓库1", "测试仓库简称1", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "李四", "12233445577", "10:00", "17:00", "2",
            "11", "90", "测试编辑仓库信息");

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        WarehouseEntity entity = new WarehouseEntity();

        entity.setId(id);
        // 仓库名简称
        entity.setShortName("测试仓库简称1");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", entity);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/saveWarehouseInfo").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.WAREHOUSE_SHORT_NAME_REPEAT_ERROR_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.WAREHOUSE_SHORT_NAME_REPEAT_ERROR));
        System.out.println(responseString);
    }

    /**
     * 测试编辑仓库信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试编辑仓库信息")
    public void testSaveWarehouseInfo06() throws Exception {

        // 创建仓库信息数据
        int id = this.mockWarehouseDate(null, 1, 0, "测试仓库", "测试仓库简称", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00",
            "18:00", "1", "10", "96", "测试新增仓库信息");

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        WarehouseEntity entity = new WarehouseEntity();

        entity.setId(id);
        // 仓库名称
        entity.setName("测试仓库1");
        // 仓库名简称
        entity.setShortName("测试仓库简称1");
        // 所属省份编码
        entity.setProvinceCode("310000");
        // 所属省份
        entity.setProvinceName("上海");
        // 所属城市
        entity.setCityName("上海城区");
        // 所属城市编码
        entity.setCityCode("310100");
        // 所属区县
        entity.setDistrictName("杨浦区");
        // 所属区县编码
        entity.setDistrictCode("310110");
        // 详细地址
        entity.setAddress("上海市杨浦区");
        // 营业开始时间
        entity.setBusinessStartTime("09:00");
        // 营业结束时间
        entity.setBusinessEndTime("17:00");
        // 联系人
        entity.setContactor("张三1");
        // 联系电话
        entity.setContactorMobile("12345678912");
        // 状态
        entity.setStatus(0);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", entity);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/saveWarehouseInfo").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试查询库容信息列表
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试查询库容信息列表")
    public void testQueryStorageInfos() throws Exception {

        // 初始化仓库状态数据字典
        Integer dictCategoryId1 = this.mockCategoryDictDate(null, 1, "TESTWAREHOUSESTA000001", "仓库状态");

        // 初始化数据字典表（仓库状态）
        this.mockDictDate(null, dictCategoryId1, 1, "0", "停用");
        this.mockDictDate(null, dictCategoryId1, 1, "1", "启用");

        // 创建仓库信息数据
        int id = this.mockWarehouseDate(null, 1, 0, "测试仓库", "测试仓库", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00", "18:00",
            "1", "10", "96", "测试查询库容信息列表");

        this.mockWarehouseDate(null, 1, id, "测试仓库1", "测试仓库1", "2000", "3333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "李四", "12233445577", "10:00", "17:00", "2",
            "11", "90", "测试查询库容信息列表");

        // 初始化客户数据
        int customerId = mockCustomerDate(null, 1, 0, "测试客户", "测试客户简称", "测试集团", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "李四", "12233445577", "96", "测试查询库容信息列表");

        // 初始化仓库客户关联库数据
        this.mockWarehouseLinkCustomerDate(null, customerId, id, 3);
        this.mockWarehouseLinkCustomerDate(null, customerId + 1, id, 4);

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("pageRows", 10);
        map.put("name", "测试仓库");
        map.put("status", "1");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/queryStorageInfos").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试根据仓库ID获取库容信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试根据仓库ID获取库容信息")
    public void testSelectStorageInfoById() throws Exception {

        // 初始化仓库状态数据字典
        Integer dictCategoryId1 = this.mockCategoryDictDate(null, 1, "TESTWAREHOUSESTA000001", "仓库状态");

        // 初始化数据字典表（仓库状态）
        this.mockDictDate(null, dictCategoryId1, 1, "0", "停用");
        this.mockDictDate(null, dictCategoryId1, 1, "1", "启用");

        // 创建仓库信息数据
        int id = this.mockWarehouseDate(null, 1, 0, "测试仓库", "测试仓库", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00", "18:00",
            "1", "10", "96", "测试根据仓库ID获取库容信息");

        // 初始化客户数据
        int customerId = mockCustomerDate(null, 1, 0, "测试客户", "测试客户简称", "测试集团", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "李四", "12233445577", "96",
            "测试根据仓库ID获取库容信息");
        int customerId1 = mockCustomerDate(null, 1, 0, "测试客户1", "测试客户简称1", "测试集团1", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "李四1", "12233445588", "96",
            "测试根据仓库ID获取库容信息");

        // 初始化仓库客户关联库数据
        this.mockWarehouseLinkCustomerDate(null, customerId, id, 3);
        this.mockWarehouseLinkCustomerDate(null, customerId1, id, 4);

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/selectStorageInfoById").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试获取使用告警类型下拉列表
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试获取使用告警类型下拉列表")
    public void testQueryWarningTypeForDict() throws Exception {

        // 初始化营业时间类型数据字典
        Integer dictCategoryId2 = this.mockCategoryDictDate(null, 1, "TESTWARNINGTYPE000001", "使用告警类型");

        // 初始化数据字典表（使用告警类型 0：未知 1：数量 2：百分比）
        this.mockDictDate(null, dictCategoryId2, 1, "0", "未知");
        this.mockDictDate(null, dictCategoryId2, 1, "1", "数量");
        this.mockDictDate(null, dictCategoryId2, 1, "2", "百分比");

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/queryWarningTypeForDict").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试添加客户信息分页查询
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试添加客户信息分页查询")
    public void testQueryCustomerForAdd() throws Exception {

        // 创建仓库信息数据
        int id = this.mockWarehouseDate(null, 1, 0, "测试仓库", "测试仓库", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00", "18:00",
            "1", "10", "96", "测试根据仓库ID获取库容信息");

        // 初始化客户数据
        int customerId = this.mockCustomerDate(null, 1, 0, "测试客户", "测试客户简称", "测试集团", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "李四", "12233445577", "96",
            "测试添加客户信息分页查询");

        int customerId1 = this.mockCustomerDate(null, 1, 0, "测试客户1", "测试客户简称1", "测试集团1", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "李四1", "12233445588", "96",
            "测试添加客户信息分页查询");

        this.mockCustomerDate(null, 1, 0, "测试客户2", "测试客户简称2", "测试集团2", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "李四21", "12233445588", "96", "测试添加客户信息分页查询");

        // 初始化仓库客户关联库数据
        this.mockWarehouseLinkCustomerDate(null, customerId, id, 3);

        // 初始化账户数据
        this.mockUserDate(id, customerId1, "测试人", "test", "1111", "1", "2", "0", "1", "1", "测试账号", "dfsdfs", "测试", "测试");

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", 1);
        map.put("pageRows", 10);
        map.put("warehouseId", id);
        map.put("name", "测试客户");
        map.put("contactorMobile", "3344");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/queryCustomerForAdd").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试保存库容信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试添保存库容信息")
    public void testSaveStorage01() throws Exception {

        // 创建仓库信息数据
        int id = this.mockWarehouseDate(null, 1, 0, "测试仓库", "测试仓库", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00", "18:00",
            "1", "10", "96", "测试添保存库容信息");

        // 初始化仓库客户关联库数据
        int id1 = this.mockWarehouseLinkCustomerDate(null, 1, id, 3);

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("spaceAmount", "3000");
        map.put("warningType", "2");
        map.put("warningOccupiedPercent", "95");

        List<WarehouseLinkCustomerEntity> list = new ArrayList<WarehouseLinkCustomerEntity>();

        WarehouseLinkCustomerEntity vo1 = new WarehouseLinkCustomerEntity();
        vo1.setId(id1);
        vo1.setCustomerId(1);
        vo1.setWarehouseId(id);
        vo1.setSpaceAmount(223);
        vo1.setCreateUser(142);
        vo1.setCreateTime(new Date());
        list.add(vo1);
        WarehouseLinkCustomerEntity vo2 = new WarehouseLinkCustomerEntity();
        vo2.setCustomerId(2);
        vo2.setWarehouseId(id);
        vo2.setSpaceAmount(4444);
        vo2.setCreateUser(142);
        vo2.setCreateTime(new Date());
        list.add(vo2);
        map.put("customerList", list);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/saveStorage").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SPACE_AMOUNT_FAILED_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SPACE_AMOUNT_FAILED));
        System.out.println(responseString);
    }

    /**
     * 测试保存库容信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试添保存库容信息")
    public void testSaveStorage02() throws Exception {

        // 创建仓库信息数据
        int id = this.mockWarehouseDate(null, 1, 0, "测试仓库", "测试仓库", "2000", "333", "上海", "310000", "上海城区", "310100", "杨浦区", "310110", "上海市杨浦区", "/path/back.jpg", "张三", "12233445566", "09:00", "18:00",
            "1", "10", "96", "测试添保存库容信息");

        // 初始化仓库客户关联库数据
        int id1 = this.mockWarehouseLinkCustomerDate(null, 1, id, 3);

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("spaceAmount", "3000");
        map.put("warningType", "2");
        map.put("warningOccupiedPercent", "95");

        List<WarehouseLinkCustomerEntity> list = new ArrayList<WarehouseLinkCustomerEntity>();

        WarehouseLinkCustomerEntity vo1 = new WarehouseLinkCustomerEntity();
        vo1.setId(id1);
        vo1.setCustomerId(1);
        vo1.setWarehouseId(id);
        vo1.setSpaceAmount(223);
        vo1.setCreateUser(142);
        vo1.setCreateTime(new Date());
        list.add(vo1);
        WarehouseLinkCustomerEntity vo2 = new WarehouseLinkCustomerEntity();
        vo2.setCustomerId(2);
        vo2.setWarehouseId(id);
        vo2.setSpaceAmount(444);
        vo2.setCreateUser(142);
        vo2.setCreateTime(new Date());
        list.add(vo2);
        map.put("customerList", list);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/saveStorage").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
        System.out.println(responseString);
    }

    /**
     * 测试删除仓库客户关联信息
     */
    @Test
    @Transactional
    @LoggerManage(description = "测试删除仓库客户关联信息")
    public void testDeleteCustomerById() throws Exception {

        // 初始化仓库客户关联库数据
        int id = this.mockWarehouseLinkCustomerDate(null, 1, 1, 3);
        this.mockWarehouseLinkCustomerDate(null, 2, 3, 4);

        MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseController).build();

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
        map1.put("reqData", map);
        String requestJson = JSON.toJSONString(map1);
        String responseString = mock
            .perform(MockMvcRequestBuilders.post("/business/warehouse/deleteCustomerById").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
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
     * 初始化仓库数据
     * @param id
     * @param status
     *            仓库状态
     * @param parentId
     *            上级仓库ID
     * @param params
     * @throws ParseException
     */
    private int mockWarehouseDate(Integer id, Integer status, Integer parentId, String... params) throws ParseException {
        Assert.assertNotNull(params);
        Assert.assertTrue(params.length == 20);

        WarehouseEntity bean = new WarehouseEntity();
        int i = 0;
        // 仓库名称
        bean.setName(params[i++]);
        // 仓库名简称
        bean.setShortName(params[i++]);
        // 上级仓库id
        bean.setParentId(parentId);
        // 所属公司id
        bean.setCompanyId(0);
        // 总车位数
        bean.setSpaceAmount(Integer.valueOf(params[i++]));
        // 已使用车位数
        bean.setOccupiedAmount(Integer.valueOf(params[i++]));
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
        // 经度
        bean.setLongitude(Float.valueOf("116.376673"));
        // 纬度
        bean.setLatitude(Float.valueOf("39.915378"));
        // 仓库照片路径
        bean.setRegisterPicturePath(params[i++]);
        // 联系人
        bean.setContactor(params[i++]);
        // 联系电话
        bean.setContactorMobile(params[i++]);
        // 营业开始时间
        bean.setBusinessStartTime(params[i++]);
        // 营业结束时间
        bean.setBusinessEndTime(params[i++]);
        // 使用告警类型 0：未知 1：数量 2：百分比
        bean.setWarningType(Integer.valueOf(params[i++]));
        // 告警库存占用警戒线（剩余库位数）
        bean.setWarningEmptyAmount(Integer.valueOf(params[i++]));
        // 告警库存占用警戒线（百分百）
        bean.setWarningOccupiedPercent(Float.valueOf(params[i++]));
        // 描述
        bean.setComment(params[i++]);
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

        return warehouseService.insert(bean);
    }

    /**
     * 初始化仓库客户关联库数据
     * @param id
     * @param customerId
     *            客户id
     * @param warehouseId
     *            仓库ID
     * @param spaceAmount
     *            客户车位数
     * @param params
     * @throws ParseException
     */
    private int mockWarehouseLinkCustomerDate(Integer id, Integer customerId, Integer warehouseId, Integer spaceAmount) throws ParseException {

        WarehouseLinkCustomerEntity bean = new WarehouseLinkCustomerEntity();
        // 客户id
        bean.setCustomerId(customerId);
        // 仓库id
        bean.setWarehouseId(warehouseId);
        // 客户车位数
        bean.setSpaceAmount(spaceAmount);
        // 创建用户
        bean.setCreateUser(112233456);
        // 创建时间
        bean.setCreateTime(new Date());

        if (null != id && id > 0) {
            bean.setId(id);
        }

        return warehouseLinkCustomerService.insert(bean);
    }

    /**
     * 初始化客户数据
     * @param id
     * @param status
     *            仓库状态
     * @param parentId
     *            上级仓库ID
     * @param params
     * @throws ParseException
     */
    private int mockCustomerDate(Integer id, Integer status, Integer parentId, String... params) throws ParseException {
        Assert.assertNotNull(params);
        Assert.assertTrue(params.length == 15);

        CustomerEntity bean = new CustomerEntity();
        int i = 0;
        // 客户名称
        bean.setName(params[i++]);
        // 客户简称
        bean.setShortName(params[i++]);
        // 上级客户id
        bean.setParentId(parentId);
        // 所属集团
        bean.setGroup(params[i++]);
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
        // 经度
        bean.setLongitude(Float.valueOf("116.376673"));
        // 纬度
        bean.setLatitude(Float.valueOf("39.915378"));
        // logo照片路径
        bean.setLogoPicturePath(params[i++]);
        // 联系人
        bean.setContactor(params[i++]);
        // 联系电话
        bean.setContactorMobile(params[i++]);
        // 考核及时率
        bean.setOntimeRate(Float.valueOf(params[i++]));
        // 描述
        bean.setComment(params[i++]);
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

        return customerService.insert(bean);
    }

    /**
     * 初始化账户数据
     * @param id
     * @param status
     *            仓库状态
     * @param customerId
     *            所属客户id
     * @param params
     * @throws ParseException
     */
    private int mockUserDate(Integer id, Integer customerId, String... params) throws ParseException {
        Assert.assertNotNull(params);
        Assert.assertTrue(params.length == 12);

        UserEntity bean = new UserEntity();
        int i = 0;
        // 真实姓名
        bean.setName(params[i++]);
        // 账户名称
        bean.setAccountName(params[i++]);
        // 用户密码
        bean.setPassword(params[i++]);
        // 权限系统用户id
        bean.setPermissionUserId(Integer.valueOf(params[i++]));
        // 所属客户id
        bean.setCustomerId(customerId);
        // 账户类型 0:无效 1：经销商 2：仓库 3：运输公司 4：司机 5：系统管理
        bean.setType(Integer.valueOf(params[i++]));
        // 账户类型 0:普通 1：管理员
        bean.setAccountType(Integer.valueOf(params[i++]));
        // 是否有效
        bean.setIsValid(Integer.valueOf(params[i++]));
        // 是否锁定(0:否 1:是)
        bean.setLocked(Integer.valueOf(params[i++]));
        // 用户描述
        bean.setDescription(params[i++]);
        // 加密盐
        bean.setCredentialsSalt(params[i++]);
        // 创建用户
        bean.setCreateName(params[i++]);
        // 创建时间
        bean.setCreateTime(new Date());
        // 最后更新用户
        bean.setUpdateUser(params[i++]);
        // 更新时间
        bean.setUpdateTime(new Date());

        if (null != id && id > 0) {
            bean.setId(id);
        }

        return userEntityMapper.insert(bean);
    }
}
