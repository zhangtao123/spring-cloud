/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.test;

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
import com.anji.allways.business.BusinessServiceApplication;
import com.anji.allways.business.controller.UserController;
import com.anji.allways.business.entity.UserEntity;
import com.jayway.jsonpath.JsonPath;

/**
 * <pre>
 *
 * </pre>
 * @author wangyanjun
 * @version $Id: UserController.java, v 0.1 2017年8月23日 下午2:41:48 wangyanjun Exp $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BusinessServiceApplication.class)
@WebAppConfiguration
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Test
    @Transactional
    public void login() throws Exception {
        MockMvc mock = MockMvcBuilders.standaloneSetup(userController).build();
        // 创建测试数据
        UserEntity userEntity = new UserEntity();
        userEntity.setAccountName("wyj");
        userEntity.setPassword("123456");

        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "142_f23d380a94804aedb4fca49870f6922b");
        map1.put("reqData", userEntity);

        String requestJson = JSON.toJSONString(map1);
        // requestJson = "{\"reqData\":" + requestJson + "}"; //增加JSON数据的头

//        String responseString = mock.perform(MockMvcRequestBuilders.post("/user/login/name").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).content(requestJson.getBytes()))
//            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
//        Assert.assertThat(JsonPath.<String> read(responseString, "$.repMsg"), CoreMatchers.is("成功"));
//        Assert.assertThat(JsonPath.<String> read(responseString, "$.repCode"), CoreMatchers.is("1"));
//        System.out.println(responseString);
    }
}
