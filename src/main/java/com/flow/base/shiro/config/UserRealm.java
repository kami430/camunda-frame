package com.flow.base.shiro.config;

import com.flow.base.shiro.jwt.JWTToken;
import com.flow.base.shiro.jwt.JWTUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

public class UserRealm extends AuthorizingRealm {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private ShiroService shiroService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String token = principals.getPrimaryPrincipal().toString();
        String account = JWTUtil.getUsername(token);
        if (StringUtils.isEmpty(account)) throw new UnknownAccountException("账号不能为空");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        List<String> roles = shiroService.getRoles(account);
        List<String> permissions = shiroService.getPermissions(account);
        // 获取角色
        if (roles != null && roles.size() != 0) {
            LOGGER.info("roles:{}", roles);
            authorizationInfo.addRoles(roles);
        } else LOGGER.info("roles:[]");
        // 获取权限
        if (permissions != null && permissions.size() != 0) {
            LOGGER.info("permissions:{}", permissions);
            authorizationInfo.addStringPermissions(permissions);
        } else LOGGER.info("permissions:[]");
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = authenticationToken.getPrincipal().toString();
        String account = JWTUtil.getUsername(token);
        String password = JWTUtil.getPassword(token);
        if (StringUtils.isEmpty(account)) throw new UnknownAccountException("账号不能为空");
        if (StringUtils.isEmpty(password)) throw new IncorrectCredentialsException("账号或密码错误");
        ShiroUser entity = shiroService.getShiroUser(account);
        if (null == entity) {
            throw new IncorrectCredentialsException("账号或密码错误");
        }
        return new SimpleAuthenticationInfo(account, entity.getPassword(), ByteSource.Util.bytes(entity.getSalt()), getName());
    }
}
