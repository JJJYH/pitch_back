package com.server.pitch.config;

import com.server.pitch.filter.AuthenticationFilter;
import com.server.pitch.filter.TokenFilter;
import com.server.pitch.security.repository.SecurityRepository;
import com.server.pitch.security.service.SecurityService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private SecurityService securityService;

    private Environment env;

    private SecurityRepository securityRepository;


    public SecurityConfig(SecurityService securityService, Environment env, SecurityRepository securityRepository){
        this.securityService = securityService;
        this.env = env;
        this.securityRepository = securityRepository;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/admin/**", "/admin", "/login", "/auth/**" ,"/main").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .addFilter(getAuthenticationFilter())
                    .addFilter(new TokenFilter(authenticationManager(),securityService));
            http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception{
        AuthenticationFilter authenticationFilter =
                new AuthenticationFilter(authenticationManager(), securityService, env, securityRepository);
        return authenticationFilter;
    }
}
