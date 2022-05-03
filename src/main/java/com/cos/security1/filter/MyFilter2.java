package com.cos.security1.filter;

import javax.servlet.*;
import java.io.IOException;

public class MyFilter2 implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("필터2");
        // 아래 반드시 해줘야 chain process 계속 진행
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
