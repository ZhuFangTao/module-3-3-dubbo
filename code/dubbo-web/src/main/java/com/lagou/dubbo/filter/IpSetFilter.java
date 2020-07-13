package com.lagou.dubbo.filter;

import org.springframework.stereotype.Component;
import util.TheadLocalUtil;

import javax.servlet.*;
import java.io.IOException;

/**
 * \* @Author: ZhuFangTao
 * \* @Date: 2020/7/13 下午9:05
 * \
 */
@Component
public class IpSetFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        TheadLocalUtil.setIp(servletRequest.getRemoteHost());
        filterChain.doFilter(servletRequest,servletResponse);
    }
}