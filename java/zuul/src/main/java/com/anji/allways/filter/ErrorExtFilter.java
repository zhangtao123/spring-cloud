/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * <pre>
 * 错误处理扩展过滤器
 * </pre>
 * @author WangGuangYuan
 * @version $Id: ErrorExtFilter.java, v 0.1 2017年5月25日 上午10:40:43 Administrator Exp $
 */
@Component
public class ErrorExtFilter extends SendErrorFilter {
    private static Logger log = LoggerFactory.getLogger(ErrorExtFilter.class);

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 30; // 大于SendErrorFilter的值
    }

    @Override
    public boolean shouldFilter() {
        // 判断：仅处理来自post过滤器引起的异常
        RequestContext ctx = RequestContext.getCurrentContext();
        ZuulFilter failedFilter = (ZuulFilter) ctx.get("failed.filter");
        if (failedFilter != null && failedFilter.filterType().equals("post")) {
            return true;
        }
        return false;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String serviceId = null == ctx.get("serviceId") ? "" : ctx.get("serviceId").toString();
        log.error("zuul 异常处理过滤器,服务{}调用产生异常", serviceId);
        return null;
    }
}
