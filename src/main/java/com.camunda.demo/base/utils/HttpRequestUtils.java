package com.camunda.demo.base.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class HttpRequestUtils {

    public static final String SESSION_KEY_LOGIN_USER = "loggedUser";

    /**
     * 获取请求的客户端ip地址
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || StringUtils.equalsIgnoreCase(ip, "unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || StringUtils.equalsIgnoreCase(ip, "unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || StringUtils.equalsIgnoreCase(ip, "unknown")) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip) || StringUtils.equalsIgnoreCase(ip, "unknown")) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip) || StringUtils.equalsIgnoreCase(ip, "unknown")) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.isNotBlank(ip) && StringUtils.indexOf(ip, ",") > 0) {
            String[] ipArray = StringUtils.split(ip, ",");
            ip = ipArray[0];
        }
        return ip;
    }

    /**
     * 获取当前URL+Parameter
     * @author lance
     * @since  2014年12月18日 下午3:09:26
     * @param request	拦截请求request
     * @return			返回完整URL
     */
    public static String getRequestUrl(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest)request;
        String queryString = req.getQueryString();

        queryString = StringUtils.isBlank(queryString) ? "" : "?" + queryString;
        return req.getRequestURI() + queryString;
    }
}
