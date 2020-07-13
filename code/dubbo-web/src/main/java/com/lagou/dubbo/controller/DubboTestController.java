package com.lagou.dubbo.controller;

import util.TheadLocalUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import service.TellMyIpService;
import service.TellMyIpService1;

import javax.servlet.http.HttpServletRequest;

/**
 * \* @Author: ZhuFangTao
 * \* @Date: 2020/7/12 下午3:45
 * \
 */
@Controller
@RequestMapping("/test")
public class DubboTestController {

    @Reference(filter = {"filter"})
    private TellMyIpService tellMyIpService;

    @Reference(filter = {"filter"})
    private TellMyIpService1 tellMyIpService1;

    @RequestMapping
    @ResponseBody
    public String testInvoke(HttpServletRequest request) {
        //接受到客户端请求后获取客户端ip，并调用server接口。
        tellMyIpService.tellMyIp();
        tellMyIpService1.tellMyIp();
        return "invoke end";
    }
}