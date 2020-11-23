package com.camunda.demo.base.shiro;

import com.camunda.demo.base.response.ResponseEntity;
import com.camunda.demo.base.shiro.jwt.JWTToken;
import com.camunda.demo.base.shiro.jwt.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginUtils {

    private static final Logger log = LoggerFactory.getLogger(LoginUtils.class);

    public static ResponseEntity login(String account, String pasword) {
        try {
            Subject subject = SecurityUtils.getSubject();
            String sessionId = subject.getSession().getId().toString();
            String access_token = JWTUtil.sign(account, sessionId, null);
            String auth_token = JWTUtil.sign(account, sessionId, pasword);
            JWTToken token = new JWTToken(auth_token);
            subject.login(token);
            return ResponseEntity.ok(data -> {
                data.put(JWTUtil.ACCESS_TOKEN_CODE, access_token);
                data.put(JWTUtil.EXPIRE_TIME_CODE, JWTUtil.EXPIRE_TIME);
            });
        } catch (UnknownAccountException uae) {
            log.warn("登陆名不存在", uae);
            return ResponseEntity.error("账号或密码错误");         // 登陆名不存在
        } catch (IncorrectCredentialsException ice) {
            log.warn("登录密码错误", ice);
            return ResponseEntity.error("账号或密码错误");         // 登录密码错误
        } catch (LockedAccountException lae) {
            log.warn("账号[\" + account + \"]已锁定！", lae);
            return ResponseEntity.error("账号[" + account + "]已锁定！");
        } catch (ExcessiveAttemptsException eae) {
            log.warn("账号[" + account + "]登录次数过多，已锁定！", eae);
            return ResponseEntity.error("账号[" + account + "]登录次数过多，已锁定！");
        } catch (AuthenticationException ae) {
            log.warn("对用户[" + account + "]验证未通关过，请联系系统管理员", ae);
            return ResponseEntity.error("对用户[" + account + "]验证未通关过，请联系系统管理员");
        }
    }
}