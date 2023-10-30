package com.server.pitch.filter;

import com.server.pitch.security.domain.RefreshToken;
import com.server.pitch.security.service.SecurityService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class TokenRequestFillter extends OncePerRequestFilter {


    @Autowired private SecurityService securityService;

    //필터 적용 안시킬 url 추가 (로그인을 안한 상태에서 사용할 url들)
    private static final List<String> EXCLUDE_URL =
            Collections.unmodifiableList(
                    Arrays.asList(
                            "/admin/**",
                            "/login",
                            "/logout",
                            "/auth/**",
                            "/main"
                    )
            );


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("Authorization");
        String userId = null;
        RefreshToken newToken = null;
        if(accessToken ==null){
            filterChain.doFilter(request, response);
            return;
        }

        accessToken = accessToken.substring(7);
        try{
            response.setContentType("application/json;charset=UTF-8");
            userId = securityService.getUserIdFromAccessToken(accessToken);
            log.info("요청중인 유저 :"+userId);
        }catch (SignatureException e){
            log.error("Invalid JWT Signature: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("('message':'signature is not valid')");
        }catch (MalformedJwtException e){
            log.error("Malformed Token: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("('message':'Malformed Token')");
        }catch (ExpiredJwtException e){
            log.error("Access Token is Expired: {}", e.getMessage());
            //response.getWriter().write();
            //refresh 토큰을 redis에 접속해서 확인하는 코드 (t/f로 확인)
            if(securityService.checkedRefreshTokenByAccessToken(accessToken)){
                // 토큰 재발급 하는 코드 짜야함
                newToken=securityService.updateRedisHashToken(accessToken);
                log.info(newToken.toString());
                response.setHeader("accessToken",newToken.getAccessToken());
            }else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("('message': 'Token is not valid'))");
            }
        }catch (UnsupportedJwtException e){
            log.error("This Token is unsupported: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("('message':'Unsupported token')");
        }catch (IllegalArgumentException e){
            log.error("JWT claims string is empty: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("('message':'Empty jwt claims string')");
        }

    filterChain.doFilter(request,response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        log.info(request.getServletPath());
        if(EXCLUDE_URL.stream().anyMatch(exclude -> PatternMatchUtils.simpleMatch(exclude, request.getServletPath()))){
            log.info("true");
        }else{
            log.info("false");
        }
        //PatternMatchUtils.simpleMatch( ,request.getServletPath());
        return EXCLUDE_URL.stream().anyMatch(exclude -> PatternMatchUtils.simpleMatch(exclude, request.getServletPath()));
    }
}
