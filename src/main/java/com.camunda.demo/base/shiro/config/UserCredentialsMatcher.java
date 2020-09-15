package com.camunda.demo.base.shiro.config;

import com.camunda.demo.base.utils.EncryptUtils;
import com.camunda.demo.base.shiro.jwt.JWTUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import java.util.Base64;

public class UserCredentialsMatcher extends HashedCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String jwtToken = (String) getCredentials(token);
        String salt = new String(Base64.getDecoder().decode(((SaltedAuthenticationInfo) info).getCredentialsSalt().toBase64()));
        String tokenPassword = EncryptUtils.simpleHash(JWTUtil.getPassword(jwtToken),salt,2);
        return super.equals(tokenPassword,info.getCredentials());
    }
}
