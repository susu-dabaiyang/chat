package com.zyh.chat.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.zyh.chat.util.JwtUtil;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器拦截方式和处理
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("经过了拦截器");
        //无论如何都放行。具体能不能操作还是在具体的操作中去判断。
        //拦截器只是负责把头请求头中包含token的令牌进行一个解析验证。
        String header = request.getHeader("Authorization");
        if (header!=null&&!"".equals(header)){
            if (!header.startsWith("Bearer")){
                String token=header.substring(7);
                try{
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles=(String)claims.get("roles");
                    if (header!=null&&roles.equals("admin")){
                    request.setAttribute("claims_admin",token);
                    }

                    if (header!=null&&roles.equals("user")){
                        request.setAttribute("claims_user",token);
                    }
                }catch (Exception e)
                {
                    //可能前台传过来的这个token已经过期了
                    throw new RuntimeException("令牌有误");
                }
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
