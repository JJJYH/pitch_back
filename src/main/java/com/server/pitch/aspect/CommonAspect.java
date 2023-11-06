package com.server.pitch.aspect;

import com.server.pitch.aop.GetUserAccessToken;
import com.server.pitch.security.service.SecurityService;
import com.server.pitch.users.domain.Users;
import com.server.pitch.users.service.UsersService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;


@Aspect
@Component
@Slf4j
public class CommonAspect {

    @Value("${token.asecret}")
    private String accessToken;

    @Autowired
    private SecurityService securityService;


    @Around("@annotation(com.server.pitch.aop.GetUserAccessToken)")
    public Object getUserInfo(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization");
        log.info(token);
                token = token.substring(7);
        Object[] getArgs = joinPoint.getArgs();

        Claims claims = Jwts.parser().setSigningKey(accessToken).parseClaimsJws(token).getBody();
        String user_id = claims.getSubject();
        Users loginUser = securityService.findById(user_id);
        loginUser.setUser_pw(null);
        log.info(loginUser.toString());
        for(int i=0; i<getArgs.length; i++){
            if(getArgs[i].getClass().equals(loginUser.getClass())){
                getArgs[i]=loginUser;
            }
        }
        return joinPoint.proceed(getArgs);
    }
}
