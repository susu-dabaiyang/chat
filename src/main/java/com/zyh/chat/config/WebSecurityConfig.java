package com.zyh.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全配置类
 */
@Configuration  /*让系统知道这是一个配置类*/
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //authorizeRequests所有security全注解配置实现的开端，表示开始说明需要的权限。
        //需要的权限分两部分，第一部分是拦截的路径，第二部分访问该路径需要的权限。
        //antMlatchers表示拦截什么路径，permitA1任何权限都可以访问，直接放行所有。
        //anyRequest（）任何的请求，authenticated认证后才能访问
        //.and（）.csrf（）.disable（）；固定写法，表示使csrf拦截失效。
        http
                .authorizeRequests()//固定
                //路径匹配,允许所有人反问
                .antMatchers("/**").permitAll()
                //.antMatchers("/u/**").hasRole("user")//u路径下必须有usr角色才可以访问
                .anyRequest().authenticated()
                .and().csrf().disable();
    }
}
