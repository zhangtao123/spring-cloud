/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anji.allways.base.redis.RedisUtils;
import com.anji.allways.business.service.SerialNumberBuildService;
import com.anji.allways.common.util.DateUtil;

/**
 * <pre>
 * </pre>
 * @author wangyanjun
 * @version $Id: SerialNumberBuildServiceImpl.java, v 0.1 2017年9月5日 下午10:14:43 wangyanjun Exp $
 */
@Service
public class SerialNumberBuildServiceImpl implements SerialNumberBuildService {

    @Autowired
    private RedisUtils redis;

    /**
     * @param type
     * @return
     * @see com.anji.allways.business.service.SerialNumberBuildService#getSerialNumberByType(java.lang.String)
     */
    @Override
    public String getSerialNumberByType(String type) {
        /**
         * 流水号使用自增模式，使用redis，对流水号计数，每次生成value加1，key:为流水号类型加日期：TO170606,value:1++ 运输订单号规则:TO1706061011(电子)TO+日期+1000开始自增 出库订单号规则:OP1706061011(电子)OP+日期+1000开始自增 其它类似
         */
        String serialNumber = "";
        // 当前日期yyyyMMddHHmmss
        String nowDate = DateUtil.getNowTimeNoMinus();
        String d1 = nowDate.substring(0, 8); // yyyyMMdd
        String d2 = nowDate.substring(2, 8); // yyMMdd
        String key = type + d1;
        if (redis.exists(key)) {
            Integer count = Integer.valueOf(redis.get(key));
            // type:流水号类型，运输订单：TO；出库订单：OP；入库计划单：WP；
            // 止损单：ML；交接单号：T；
            switch (type) {
                case "TO":
                    serialNumber = "TO" + d2 + String.format("%04d", count + 1);
                    break;
                case "OP":
                    serialNumber = "OP" + d2 + String.format("%04d", count + 1);
                    break;
                case "WP":
                    serialNumber = "WP" + d2 + String.format("%04d", count + 1);
                    break;
                case "ML":
                    serialNumber = "ML" + d2 + String.format("%04d", count + 1);
                    break;
                case "T":
                    serialNumber = "T" + d2 + String.format("%04d", count + 1);
                    break;
                default:
                    break;
            }
            redis.set(key, Integer.toString(count + 1), (long) DateUtil.getRemainSecondsToday());
        } else {
            switch (type) {
                case "TO":
                    serialNumber = "TO" + d2 + String.format("%04d", 1000);
                    break;
                case "OP":
                    serialNumber = "OP" + d2 + String.format("%04d", 1000);
                    break;
                case "WP":
                    serialNumber = "WP" + d2 + String.format("%04d", 1000);
                    break;
                case "ML":
                    serialNumber = "ML" + d2 + String.format("%04d", 1000);
                    break;
                case "T":
                    serialNumber = "T" + d2 + String.format("%04d", 1000);
                    break;
                default:
                    break;
            }
            // 每日重新计算
            redis.set(key, Integer.toString(1000), (long) DateUtil.getRemainSecondsToday());
        }
        return serialNumber;
    }

}
