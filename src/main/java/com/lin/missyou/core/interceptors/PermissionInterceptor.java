package com.lin.missyou.core.interceptors;

import com.auth0.jwt.interfaces.Claim;
import com.lin.missyou.core.LocalUser;
import com.lin.missyou.exception.http.ForbiddenException;
import com.lin.missyou.exception.http.UnAuthenticatedException;
import com.lin.missyou.model.User;
import com.lin.missyou.service.UserService;
import com.lin.missyou.util.JwtToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

public class PermissionInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    public PermissionInterceptor() {
        super();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional<ScopeLevel> scopeLevel = this.getScopeLevel(handler);
        if(!scopeLevel.isPresent()){
            return true;
        }
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.isEmpty(bearerToken)){
            throw new UnAuthenticatedException(10004);
        }
        if(!bearerToken.startsWith("Bearer")){
            throw new UnAuthenticatedException(10004);
        }
        String[] tokens = bearerToken.split(" ");
        if(tokens.length != 2){
            throw new UnAuthenticatedException(10004);
        }
        Optional<Map<String, Claim>> optionalMap = JwtToken.getClaims(tokens[1]);
        Map<String, Claim> map = optionalMap.orElseThrow(()->new UnAuthenticatedException(10004));
        Boolean valid = this.hasPermission(scopeLevel.get(), map);
        if(valid){  // 检验通过，将用户信息存入到LocalUser类中
            this.setToThreadLocal(map);
        }
        return valid;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LocalUser.clear();
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }

    // 获取ScopeLevel注解
    private Optional<ScopeLevel> getScopeLevel(Object handler){
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ScopeLevel scopeLevel = handlerMethod.getMethod().getAnnotation(ScopeLevel.class);  // 获取注解
            if(scopeLevel == null){
                return Optional.empty();
            }
            return Optional.of(scopeLevel);
        }
        return Optional.empty();
    }

    // 比对权限
    private Boolean hasPermission(ScopeLevel scopeLevel, Map<String, Claim> map){
        Integer level = scopeLevel.value();
        Integer scope = map.get("scope").asInt();  // 用户实际的权限等级
        if(level > scope){
            throw new ForbiddenException(10005);
        }
        return true;
    }

    private void setToThreadLocal(Map<String, Claim> map){
        Long uid = map.get("uid").asLong();
        Integer scope = map.get("scope").asInt();
        User user = userService.getUserById(uid);
        LocalUser.set(user, scope);
    }
}
