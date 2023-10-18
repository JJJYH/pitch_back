package com.server.pitch.config;

import com.server.pitch.filter.MyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class FilterConfig {
    Environment env;
    @Bean
    public FilterRegistrationBean<MyFilter> filter(){
        FilterRegistrationBean<MyFilter> bean = new FilterRegistrationBean<>(new MyFilter(env));
        bean.addUrlPatterns("/*");
        bean.setOrder(0);
        return bean;
    }
}
