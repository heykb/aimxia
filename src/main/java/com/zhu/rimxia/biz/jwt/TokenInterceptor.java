package com.zhu.rimxia.biz.jwt;

import com.zhu.rimxia.biz.annotation.Logical;
import com.zhu.rimxia.biz.annotation.LoginRequired;
import com.zhu.rimxia.biz.annotation.RequiresRoles;
import com.zhu.rimxia.biz.exception.BusinessException;
import com.zhu.rimxia.biz.exception.CommonErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Enumeration;

public class TokenInterceptor extends HandlerInterceptorAdapter {

    private final RedisTokenStore redisTokenStore;

    public TokenInterceptor(RedisTokenStore redisTokenStore) {
        this.redisTokenStore = redisTokenStore;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

            if (handler instanceof HandlerMethod) {
            System.out.println("ttttttttttttttttttttttttttt");
            handler = (HandlerMethod) handler;
            Method method = ((HandlerMethod) handler).getMethod();

            //获取LoginRequired注解
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            if (loginRequired == null) {
                loginRequired = method.getDeclaringClass().getAnnotation(LoginRequired.class);
            }
            //获取RequiresRoles注解
            RequiresRoles requiresRoles = method.getAnnotation(RequiresRoles.class);
            if (requiresRoles == null) {
                requiresRoles = method.getDeclaringClass().getAnnotation(RequiresRoles.class);
            }
            //不需要登录即可访问
            if (loginRequired == null || requiresRoles == null) return true;

            //需要用户登录的情况，从header或request拿到token
            String accessToken = request.getParameter("access_token");
            if (accessToken == null) {
                accessToken = request.getHeader("Authorization");
                //返回null，表示没有token
                if (accessToken == null) {
                    throw new BusinessException(CommonErrorCode.SYSTEM_USER_UNLOGED);
                }
            }
            String subject;
            try {
                subject = TokenUtil.parseToken(accessToken, redisTokenStore.getTokeyKey());
            } catch (ExpiredJwtException e) {
                throw new BusinessException(CommonErrorCode.TOKEN_EXPIRED);
            }
            Token token = redisTokenStore.findToken(subject, accessToken);
            if (token == null) {
                throw new BusinessException(CommonErrorCode.NO_TOKEN);
            }

            //需要角色访问
            if (requiresRoles != null) {
                if (!SubjectUtil.hasRole(token, requiresRoles.value(), requiresRoles.logical())) {
                    throw new BusinessException(CommonErrorCode.ROLE_INVALID);
                }
            }
            request.setAttribute(SubjectUtil.REQUEST_TOKEN_NAME, token);
        }
        return true;
    }


}
