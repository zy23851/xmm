package com.pn.config;


import com.pn.filter.LoginCheckFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class SerlvetConfig {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Bean
    public FilterRegistrationBean filterRegistrationBean(){

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();

        LoginCheckFilter loginCheckFilter = new LoginCheckFilter();
        loginCheckFilter.setRedisTemplate(redisTemplate);

        filterRegistrationBean.setFilter(loginCheckFilter);

        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;

    }

}
