/**

 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.filter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * <pre>
 *
 * </pre>
 * @author WangGuangYuan
 * @version $Id: ErrorFilter.java, v 0.1 2017年5月27日 上午8:46:02 Administrator Exp $
 */
public class ErrorFilter extends ZuulFilter {

    private Logger log = LoggerFactory.getLogger(ErrorFilter.class);

    /**
     * @return
     * @see com.netflix.zuul.ZuulFilter#filterType()
     */
    @Override
    public String filterType() {
        return "error";
    }

    /**
     * @return
     * @see com.netflix.zuul.ZuulFilter#filterOrder()
     */
    @Override
    public int filterOrder() {
        return 10;
    }

    /**
     * @return
     * @see com.netflix.zuul.IZuulFilter#shouldFilter()
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * @return
     * @see com.netflix.zuul.IZuulFilter#run()
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable throwable = ctx.getThrowable();
        log.error("this is a ErrorFilter : {}", throwable.getCause().getMessage());
        ctx.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        ctx.set("error.exception", throwable.getCause());
        return null;
    }
}
