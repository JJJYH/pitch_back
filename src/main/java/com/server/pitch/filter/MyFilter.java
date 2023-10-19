package com.server.pitch.filter;


import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyFilter implements Filter {

    private Environment env;

    public MyFilter(Environment env) {
        this.env = env;
    }

    @Override
    public void doFilter(ServletRequest requestF,
                         ServletResponse responseF,
                         FilterChain chain) throws IOException, ServletException {
        log.info("Filter 1");
        HttpServletRequest request = (HttpServletRequest) requestF;
        HttpServletResponse response = (HttpServletResponse) responseF;

        String authorizationHeader = request.getHeader("AUTHORIZATION");

        if (authorizationHeader == null) {
            //onError(response, "UNAUTHORIZATION");
        } else {
            String jwt = authorizationHeader.replace("Bearer ", "").trim(); // 공백 제거
            if (!isJwtValid(jwt)) {
                //onError(response, "UNAUTHORIZATION2");
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isJwtValid(String jwt) {
        boolean result = true;
        String subject = null;

        try {
            subject = Jwts.parser().setSigningKey(env.getProperty("token.asecret"))
                    .parseClaimsJws(jwt).getBody().getSubject();
        } catch (Exception e) {
            result = false;
        }
        System.out.println(subject);

        if(subject == null || subject.isEmpty()){
            result = false;
        }

        return result;
    }

    private void onError(HttpServletResponse response, String httpStatus)
            throws IOException{
        response.addHeader("error", httpStatus);
        response.sendError(HttpServletResponse.SC_FORBIDDEN, httpStatus);
    }
}
