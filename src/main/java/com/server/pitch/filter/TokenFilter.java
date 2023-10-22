package com.server.pitch.filter;

import com.server.pitch.security.service.SecurityService;
import com.server.pitch.users.domain.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class TokenFilter extends BasicAuthenticationFilter {
    private SecurityService securityService;

    private Environment env;

    public TokenFilter(AuthenticationManager authenticationManager, SecurityService securityService){
        super(authenticationManager);
        this.securityService = securityService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader==null||!tokenHeader.startsWith("Bearer")){
            chain.doFilter(request,response);
            return;
        }
        String token = request.getHeader("Authorization").substring(7);
        Claims claims = Jwts.parser().setSigningKey("user_token").parseClaimsJws(token).getBody();
        String userID = claims.getSubject();

        Users user = securityService.findById(userID);
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if(user.getRole()!=null) {
            grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
        }
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user.getUser_email(), null, grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request,response);

    }
}
