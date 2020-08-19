package com.camunda.demo.base.shiro.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Date;

public class JWTUtil {
    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    public static final String ACCESS_TOKEN_CODE = "access_token";

    public static final String PASSWORD_CODE = "access_password";

    public static final String EXPIRE_TIME_CODE = "expire_time";

    public static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;

    private static final String ACCESS_KEY = "9b979da7e0c54de2ae1a548a8442b156";

    public static String sign(String username, String sessionId) {
        return sign(username, sessionId, null);
    }

    public static String sign(String username, String sessionId, String password) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(ACCESS_KEY);
        JWTCreator.Builder builder = JWT.create().withJWTId(sessionId).withIssuer(username).withExpiresAt(date);
        if (!StringUtils.isEmpty(password)) {
            builder.withClaim(PASSWORD_CODE, password);
        }
        return builder.sign(algorithm);
    }

    public static String getSessionId(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getId();
    }
    public static String getUsername(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getIssuer();
    }

    public static String getPassword(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim(PASSWORD_CODE).asString();
    }

    public static JwtVerifyResult verify(String token, String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(ACCESS_KEY);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(username).build();
            verifier.verify(token);
            logger.info(">>>>>>>>>>jwt token 验证通过");
            return JwtVerifyResult.pass;
        } catch (IllegalArgumentException e) {
            logger.warn(">>>>>>>>>>token验证失败：IllegalArgumentException", e.getMessage());
            return JwtVerifyResult.fail_other;
        } catch (JWTVerificationException e) {
            logger.warn(">>>>>>>>>>token验证失败：JWTVerificationException", e.getMessage());
            if (e instanceof TokenExpiredException) {
                return JwtVerifyResult.fail_expired;
            }
            return JwtVerifyResult.fail_other;
        }
    }

    public enum JwtVerifyResult {
        pass("验证通过"), fail_expired("验证失败，token过期"), fail_other("验证失败,参数有误或编码错误");

        private final String title;

        JwtVerifyResult(String title) {
            this.title = title;
        }

        public String title() {
            return title;
        }

    }
}
