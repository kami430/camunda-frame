package com.camunda.demo.base.shiro.jwt;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JWTFilter extends BasicHttpAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    private static final String JWT_VERIFY_FAIL_TYPE = "jwt_verify_fail_type";

    private final String ACCESS_TOKEN_CODE = "access_token";


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String access_token = httpServletRequest.getHeader(ACCESS_TOKEN_CODE);
        if (access_token == null) access_token = httpServletRequest.getParameter(ACCESS_TOKEN_CODE);
        JWTUtil.JwtVerifyResult jwtVerifyResult = null;
        if (access_token !=null){
            String account = JWTUtil.getUsername(access_token);
            jwtVerifyResult= JWTUtil.verify(access_token,account);
        }
        if(jwtVerifyResult == JWTUtil.JwtVerifyResult.pass){
            logger.info(">>>>>>>>>>token验证成功>>>>>>>>>");
        }
        request.setAttribute(JWT_VERIFY_FAIL_TYPE,jwtVerifyResult);
        return jwtVerifyResult == JWTUtil.JwtVerifyResult.pass;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        logger.warn(">>>>>>>>>>token验证失败>>>>>>>>>>",httpServletRequest.getAttribute(JWT_VERIFY_FAIL_TYPE));
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        JWTUtil.JwtVerifyResult verify = (JWTUtil.JwtVerifyResult) httpServletRequest.getAttribute(JWT_VERIFY_FAIL_TYPE);
        if(verify== JWTUtil.JwtVerifyResult.fail_expired){
            httpServletResponse.sendRedirect("/token_expired");
            return false;
        }
        httpServletRequest.getRequestDispatcher("/auth_fail").forward(httpServletRequest, response);
        return false;
    }
}
