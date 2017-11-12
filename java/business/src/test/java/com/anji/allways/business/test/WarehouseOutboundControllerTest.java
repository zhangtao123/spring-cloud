/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.test;

import java.util.HashMap;
import java.util.LinkedList;
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
import com.anji.allways.business.controller.WarehouseOutController;
import com.anji.allways.business.entity.DamageEntity;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.DateUtil;
import com.jayway.jsonpath.JsonPath;

/**
 * <pre>
 * </pre>
 *
 * @author zhangtao
 * @version $Id: WarehouseOutboundControllerTest.java, v 0.1 2017年9月5日 下午4:37:13
 *          zhangtao Exp $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BusinessServiceApplication.class)
@WebAppConfiguration
public class WarehouseOutboundControllerTest {

	@Autowired
	private WarehouseOutController warehouseOutController;

	
	
	
	/**
	 * 测试根据过滤信息获取出库计划信息
	 * @throws Exception 
	 * @throws  
	 */
	@Test
	@Transactional
	@LoggerManage(description = "测试查询出库计划单列表")
	public void testSelectDeliveryPlanByCondition() throws Exception{

		MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseOutController).build();
		Map<String, Object> map1 = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		map.put("createStartTime", "2017-09-10 10:25:32");
		map.put("createEndTime", "2017-09-20 10:25:32");
		map.put("pageNum", "1");
		map.put("pageRows", "10");
		
		
		map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 3);
		map1.put("reqData", map);
		String requestJson = JSON.toJSONString(map1);
		String responseString = mock
				.perform(MockMvcRequestBuilders.post("/business/outBound/selectDeliveryPlanByCondition").characterEncoding("UTF-8")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestJson.getBytes()))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn()
					.getResponse()
					.getContentAsString();
		Assert.assertThat(JsonPath.<String>read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
		Assert.assertThat(JsonPath.<String>read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
		System.out.println(responseString);
	}
	
	
	
	
	
	/**
	 * 测试根据ID获取运输司机信息
	 * @throws Exception 
	 * @throws  
	 */
	@Test
	@Transactional
	@LoggerManage(description = "测试根据userId获取仓库名")
	public void testSelectWarehouseNameByUserId() throws Exception{

		MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseOutController).build();
		Map<String, Object> map1 = new HashMap<>();
		map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 3);
		map1.put("reqData", null);
		String requestJson = JSON.toJSONString(map1);
		String responseString = mock
				.perform(MockMvcRequestBuilders.post("/business/outBound/selectWarehouseNameByUserId").characterEncoding("UTF-8")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestJson.getBytes()))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn()
					.getResponse()
					.getContentAsString();
		Assert.assertThat(JsonPath.<String>read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
		Assert.assertThat(JsonPath.<String>read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
		System.out.println(responseString);
	}
	
	
	
	
	
	/**
	 * 测试根据ID获取运输司机信息
	 * @throws Exception 
	 * @throws  
	 */
	@Test
	@Transactional
	@LoggerManage(description = "测试根据mobile获取司机信息")
	public void testSelectDriverInfoByDriverMobile() throws Exception{

		MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseOutController).build();
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("mobile", "13886884488");
		Map<String, Object> map1 = new HashMap<>();
		map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
		map1.put("reqData", map);
		String requestJson = JSON.toJSONString(map1);
		String responseString = mock
				.perform(MockMvcRequestBuilders.post("/business-zt/outBound/selectDriverInfoByMobile").characterEncoding("UTF-8")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestJson.getBytes()))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn()
					.getResponse()
					.getContentAsString();
		Assert.assertThat(JsonPath.<String>read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
		Assert.assertThat(JsonPath.<String>read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
		System.out.println(responseString);
	}
	
	
	
	
	
	/**
	 * 测试获取发运订单信息
	 * @throws Exception 
	 * @throws  
	 */
	@Test
	@Transactional
	@LoggerManage(description = "测试获取发运订单信息")
	public void testSelectOrderListFYByCondition() throws Exception{

		MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseOutController).build();
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("pageNum", "1");
	    map.put("pageRows", "100");
	    map.put("createTimeStart", "2017-09-13");
	    map.put("createTimeEnd", "2017-09-14");
	    map.put("checkIds", "208");
		Map<String, Object> map1 = new HashMap<>();
		map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
		map1.put("reqData", map);
		String requestJson = JSON.toJSONString(map1);
		String responseString = mock
				.perform(MockMvcRequestBuilders.post("/business-zt/outBound/selectOrderFYListByCondition").characterEncoding("UTF-8")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestJson.getBytes()))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn()
					.getResponse()
					.getContentAsString();
		Assert.assertThat(JsonPath.<String>read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
		Assert.assertThat(JsonPath.<String>read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
		System.out.println(responseString);
	}
	
	
	
	
	
	
	/**
	 * 测试获取发运订单信息
	 * @throws Exception 
	 * @throws  
	 */
	@Test
	@Transactional
	@LoggerManage(description = "测试获取自提订单信息")
	public void testSelectOrderListZTByCondition() throws Exception{

		MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseOutController).build();
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("pageNum", "1");
	    map.put("pageRows", "10");
	    map.put("pickupSelfTimeStart", "2017-09-09");
	    map.put("pickupSelfTimeEnd", "2017-09-12");
		Map<String, Object> map1 = new HashMap<>();
		map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 142);
		map1.put("reqData", map);
		String requestJson = JSON.toJSONString(map1);
		String responseString = mock
				.perform(MockMvcRequestBuilders.post("/business-zt/outBound/selectOrderZTListByCondition").characterEncoding("UTF-8")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestJson.getBytes()))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andReturn()
					.getResponse()
					.getContentAsString();
		Assert.assertThat(JsonPath.<String>read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
		Assert.assertThat(JsonPath.<String>read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
		System.out.println(responseString);
	}
	
	
	
	
	
	
	/**
	 * 测试根据ID获取运输司机信息
	 * @throws Exception 
	 * @throws  
	 */
	@Test
	@Transactional
	@LoggerManage(description = "测试新增出库计划单（发运）")
	public void testAddDeliveryPlan() throws Exception{

		MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseOutController).build();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> entity = new HashMap<String,Object>();
		entity.put("deliveryPlanTime", DateUtil.formateStringToDate("2017-09-10 20:10:30",DateUtil.TIMENOWPATTERN));
		entity.put("deliveryType","发运");
		entity.put("transportType","陆运");
		entity.put("warehouseName","万强库");
		entity.put("warehouseId",1);
		entity.put("driverId",179);
	    map.put("orderIds", "67,68,69,70");
	    map.put("type", "1");
	    map.put("deliveryPlanEntity", entity);
		Map<String, Object> map1 = new HashMap<>();
		map1.put("token", "1_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 1);
		map1.put("reqData", map);
		String requestJson = JSON.toJSONString(map1);
		
		
		String responseString = mock
	            .perform(MockMvcRequestBuilders.post("/business/outBound/addDeliveryPlan").characterEncoding("UTF-8")
	            		.contentType(MediaType.APPLICATION_JSON).
	            		content(requestJson.getBytes()))
	            		.andExpect(MockMvcResultMatchers.status().isOk())
	            		.andReturn()
	            		.getResponse()
	            		.getContentAsString();
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
	        System.out.println(responseString);

	}
	
	
	
	
	
	
	
	/**
	 * 测试根据ID获取运输司机信息
	 * @throws Exception 
	 * @throws  
	 */
	@Test
	@Transactional
	@LoggerManage(description = "测试新增出库计划单（自提）")
	public void testAddDeliveryPlanZT() throws Exception{

		MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseOutController).build();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> entity = new HashMap<String,Object>();
		entity.put("deliveryPlanTime", DateUtil.formateStringToDate("2017-09-10 20:10:30",DateUtil.TIMENOWPATTERN));
		entity.put("deliveryType","自提");
		entity.put("transportType","陆运");
		entity.put("warehouseName","万强库");
		entity.put("warehouseId",1);
	    map.put("orderIds", "122");
	    map.put("type", "2");
	    map.put("deliveryPlanEntity", entity);
		Map<String, Object> map1 = new HashMap<>();
		map1.put("token", "1_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 1);
		map1.put("reqData", map);
		String requestJson = JSON.toJSONString(map1);
		String responseString = mock
	            .perform(MockMvcRequestBuilders.post("/business-zt/outBound/addDeliveryPlan").characterEncoding("UTF-8")
	            		.contentType(MediaType.APPLICATION_JSON).
	            		content(requestJson.getBytes()))
	            		.andExpect(MockMvcResultMatchers.status().isOk())
	            		.andReturn()
	            		.getResponse()
	            		.getContentAsString();
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
	        System.out.println(responseString);
	}
	
	
	
	
	
	/**
	 * 测试查询出库计划单详情（自提）
	 * @throws Exception 
	 * @throws  
	 */
	@Test
	@Transactional
	@LoggerManage(description = "测试查询出库计划单详情（自提）")
	public void testSelectDeliveryDetailZT() throws Exception{

		MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseOutController).build();
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("id",145);
		Map<String, Object> map1 = new HashMap<>();
		map1.put("token", "1_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 1);
		map1.put("reqData", map);
		String requestJson = JSON.toJSONString(map1);
		String responseString = mock
	            .perform(MockMvcRequestBuilders.post("/business-zt/outBound/selectDeliveryPlanDetailZT").characterEncoding("UTF-8")
	            		.contentType(MediaType.APPLICATION_JSON).
	            		content(requestJson.getBytes()))
	            		.andExpect(MockMvcResultMatchers.status().isOk())
	            		.andReturn()
	            		.getResponse()
	            		.getContentAsString();
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
	        System.out.println(responseString);
	}
	
	
	
	
	
	

	/**
	 * 测试查询出库计划单详情（发运）
	 * @throws Exception 
	 * @throws  
	 */
	@Test
	@Transactional
	@LoggerManage(description = "测试查询出库计划单详情（发运）")
	public void testSelectDeliveryDetailFY() throws Exception{

		MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseOutController).build();
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("id",172);
		Map<String, Object> map1 = new HashMap<>();
		map1.put("token", "1_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 1);
		map1.put("reqData", map);
		String requestJson = JSON.toJSONString(map1);
		String responseString = mock
	            .perform(MockMvcRequestBuilders.post("/business-zt/outBound/selectDeliveryPlanDetailFY").characterEncoding("UTF-8")
	            		.contentType(MediaType.APPLICATION_JSON).
	            		content(requestJson.getBytes()))
	            		.andExpect(MockMvcResultMatchers.status().isOk())
	            		.andReturn()
	            		.getResponse()
	            		.getContentAsString();
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
	        System.out.println(responseString);
	}
	
	
	
	/**
	 * 测试取消出库计划
	 * @throws Exception 
	 * @throws  
	 */
	@Test
	@Transactional
	@LoggerManage(description = "测试取消出库计划")
	public void testCancelDeliveryPlan() throws Exception{
		MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseOutController).build();
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("id",2);
		Map<String, Object> map1 = new HashMap<>();
		map1.put("token", "1_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 1);
		map1.put("reqData", map);
		String requestJson = JSON.toJSONString(map1);
		String responseString = mock
	            .perform(MockMvcRequestBuilders.post("/business/outBound/cancelDeliveryPlan").characterEncoding("UTF-8")
	            		.contentType(MediaType.APPLICATION_JSON).
	            		content(requestJson.getBytes()))
	            		.andExpect(MockMvcResultMatchers.status().isOk())
	            		.andReturn()
	            		.getResponse()
	            		.getContentAsString();
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
	        System.out.println(responseString);

	}
	
	
	
	
	
	/**
	 * 测试取消出库计划
	 * @throws Exception 
	 * @throws  
	 */
	@Test
	@Transactional
	@LoggerManage(description = "测试取消出库计划")
	public void testUpdateDeliveryPlan() throws Exception{
		MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseOutController).build();
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("id",2);
		Map<String, Object> map1 = new HashMap<>();
		map1.put("token", "1_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 1);
		map1.put("reqData", map);
		String requestJson = JSON.toJSONString(map1);
		String responseString = mock
	            .perform(MockMvcRequestBuilders.post("/business/outBound/updateDeliveryPlan").characterEncoding("UTF-8")
	            		.contentType(MediaType.APPLICATION_JSON).
	            		content(requestJson.getBytes()))
	            		.andExpect(MockMvcResultMatchers.status().isOk())
	            		.andReturn()
	            		.getResponse()
	            		.getContentAsString();
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
	        System.out.println(responseString);

	}
	
	
	
	
	
	/**
	 * 测试取消出库计划
	 * @throws Exception 
	 * @throws  
	 */
	@Test
	@Transactional
	@LoggerManage(description = "测试查询出库计划下运输单")
	public void testSelectDeliveryPlanOrderForEditZt() throws Exception{
		MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseOutController).build();
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("id",132);
		Map<String, Object> map1 = new HashMap<>();
		map1.put("token", "1_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 2);
		map1.put("reqData", map);
		String requestJson = JSON.toJSONString(map1);
		String responseString = mock
	            .perform(MockMvcRequestBuilders.post("/business-zt/outBound/selectDeliveryPlanOrderForEditZt").characterEncoding("UTF-8")
	            		.contentType(MediaType.APPLICATION_JSON).
	            		content(requestJson.getBytes()))
	            		.andExpect(MockMvcResultMatchers.status().isOk())
	            		.andReturn()
	            		.getResponse()
	            		.getContentAsString();
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
	        System.out.println(responseString);

	}
	
	
	
	
	/**
	 * 测试取消出库计划
	 * @throws Exception 
	 * @throws  
	 */
	@Test
	@Transactional
	@LoggerManage(description = "测试查询出库计划下运输单")
	public void testSelectDeliveryPlanOrderForEditFy() throws Exception{
		MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseOutController).build();
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("id",125);
		Map<String, Object> map1 = new HashMap<>();
		map1.put("token", "1_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 1);
		map1.put("reqData", map);
		String requestJson = JSON.toJSONString(map1);
		String responseString = mock
	            .perform(MockMvcRequestBuilders.post("/business-zt/outBound/selectDeliveryPlanOrderForEditFy").characterEncoding("UTF-8")
	            		.contentType(MediaType.APPLICATION_JSON).
	            		content(requestJson.getBytes()))
	            		.andExpect(MockMvcResultMatchers.status().isOk())
	            		.andReturn()
	            		.getResponse()
	            		.getContentAsString();
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
	        System.out.println(responseString);

	}
	
	
	
	/**
	 * 测试取消出库计划
	 * @throws Exception 
	 * @throws  
	 */
	@Test
	@Transactional
	@LoggerManage(description = "测试更新出库计划")
	public void testUpdateDeliveryPlanOrderForEditFy() throws Exception{
		MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseOutController).build();
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("orderIds","40,67,68,69");
	    map.put("cancelIds","39");
	    map.put("deliveryPlanId",2);
		Map<String, Object> map1 = new HashMap<>();
		map1.put("token", "1_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 1);
		map1.put("reqData", map);
		String requestJson = JSON.toJSONString(map1);
		String responseString = mock
	            .perform(MockMvcRequestBuilders.post("/business-zt/outBound/updateDeliveryPlanOrderForEditFy").characterEncoding("UTF-8")
	            		.contentType(MediaType.APPLICATION_JSON).
	            		content(requestJson.getBytes()))
	            		.andExpect(MockMvcResultMatchers.status().isOk())
	            		.andReturn()
	            		.getResponse()
	            		.getContentAsString();
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
	        System.out.println(responseString);

	}
	
	
	
	
	
	/**
	 * 测试新增质损信息
	 * @throws Exception 
	 * @throws  
	 *//*
	@Test
	@Transactional
	@LoggerManage(description = "测试新增质损信息")
	public void testAddDamageEntity() throws Exception{
		MockMvc mock = MockMvcBuilders.standaloneSetup(warehouseOutController).build();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map4 = new HashMap<String, Object>();
		List<String,DamageEntity> list = new LinkedList<String,DamageEntity>();
		
		
		DamageEntity damageEntity = new DamageEntity();
		damageEntity.setNo("ML1709121001");
		damageEntity.setVehicleId(3);
		damageEntity.setConfirmedStatus(0);
		damageEntity.setCreatorName("zz");
		list.add(damageEntity);
	   
	    
	    map.put("no","ML1709121004");
	    map.put("vehicleId","3");
	    map.put("confirmedStatus","0");
	    map.put("creatorName","zz2");
	    
	    
	    list.add(map2);
	    map4.put("damageEntity", list);
		Map<String, Object> map1 = new HashMap<>();
		map1.put("token", "1_f23d380a94804aedb4fca49870f6922b");
        map1.put("userId", 1);
		map1.put("reqData", map);
		String requestJson = JSON.toJSONString(map1);
		String responseString = mock
	            .perform(MockMvcRequestBuilders.post("/business-zt/outBound/updateDeliveryPlanOrderForEditFy").characterEncoding("UTF-8")
	            		.contentType(MediaType.APPLICATION_JSON).
	            		content(requestJson.getBytes()))
	            		.andExpect(MockMvcResultMatchers.status().isOk())
	            		.andReturn()
	            		.getResponse()
	            		.getContentAsString();
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is(RespMsg.SUCCESS_MSG));
	        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is(RespCode.SUCCESS));
	        System.out.println(responseString);

	}*/
	
	
	
	
}
