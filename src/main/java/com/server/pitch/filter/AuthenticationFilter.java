package com.server.pitch.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.pitch.security.domain.RequestUser;
import com.server.pitch.security.repository.SecurityRepository;
import com.server.pitch.security.service.SecurityService;
import com.server.pitch.users.domain.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private SecurityService securityService;
    private Environment env;

    private SecurityRepository securityRepository;

    public AuthenticationFilter(AuthenticationManager authenticationManager, SecurityService securityService,
                                Environment env, SecurityRepository securityRepository) {
        super.setAuthenticationManager(authenticationManager);
        this.securityService = securityService;
        this.env = env;
        this.securityRepository = securityRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            RequestUser creds = new ObjectMapper().readValue(request.getInputStream(), RequestUser.class);
            log.info("injection login info creds");
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    creds.getUser_email(),
                    creds.getUser_pw(),
                    new ArrayList<>());
            log.info("check token");
            log.info(creds.getUser_email());

            Authentication authentication = getAuthenticationManager().authenticate(token);

            System.out.println(authentication.getPrincipal());
            //log.info((String) authentication.getPrincipal());
            return authentication;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        String userEmail = ((User) authResult.getPrincipal()).getUsername();
        Users user = securityService.findByEmail(userEmail);
        log.info(user.getUser_id());
        log.info(user.getUser_nm());

        if(Objects.equals(user.getStatus(), "app")){
        String accessToken = Jwts.builder()
                .setSubject(user.getUser_id())
                .setExpiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.asecret"))
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(accessToken)
                .setExpiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(env.getProperty("token.refreshToken_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.rsecret"))
                .compact();

        log.info(accessToken);
        log.info(refreshToken);

        securityService.saveToken(refreshToken, user.getUser_id(), accessToken);
        //securityRepository.save(accessToken, refreshToken);


        response.addHeader("accessToken", accessToken);
    }else{
            log.info("1");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("This Account is not approved");
            //response.addHeader("error", "This Account is not approval");
            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"This Account is not approval");
        }

    }
}
