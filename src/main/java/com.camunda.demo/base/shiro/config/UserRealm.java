package com.camunda.demo.base.shiro.config;

import com.camunda.demo.base.shiro.jwt.JWTToken;
import com.camunda.demo.base.shiro.jwt.JWTUtil;
import com.camunda.demo.dataInterface.entity.authorization.LoginCredential;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private ShiroLoginDao loginDao;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = authenticationToken.getPrincipal().toString();
        String account = JWTUtil.getUsername(token);
        String password = JWTUtil.getPassword(token);
        if (StringUtils.isEmpty(account)) throw new UnknownAccountException("账号不能为空");
        if (StringUtils.isEmpty(password)) throw new IncorrectCredentialsException("账号或密码错误");
        ShiroLoginEntity entity = loginDao.getShiroLoginEntity(account);
        if (null == entity) {
            throw new IncorrectCredentialsException("账号或密码错误");
        }
        return new SimpleAuthenticationInfo(account, entity.getPassword(), ByteSource.Util.bytes(entity.getSalt()), getName());
    }
}
