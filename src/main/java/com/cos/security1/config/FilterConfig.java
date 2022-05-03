package com.cos.security1.config;

import com.cos.security1.filter.MyFilter1;
import com.cos.security1.filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//SecurityFilterChain보다 늦게 실행됨.
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter1> filter() {
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
        bean.addUrlPatterns("/*"); //모든 url에 다 적용
        bean.setOrder(0);// 낮은 번호가 필터 중에서 가장 먼저 실행
        return bean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2() {
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*"); //모든 url에 다 적용
        bean.setOrder(1);// 낮은 번호가 필터 중에서 가장 먼저 실행
        return bean;
    }
}
