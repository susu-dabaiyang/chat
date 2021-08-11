package com.zyh.chat.config;

import com.zyh.chat.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    //配置拦截器
    //声明自己的拦截器和拦截的路径

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //往册拦截器要声明拦截器对象和要拦截的请求

        //声明自己的拦截器
        registry.addInterceptor(jwtInterceptor)
                //声明拦截的路径
                .addPathPatterns("/**")
                //声明不拦截的路径
                .excludePathPatterns("/**/login/**");

    }
}
