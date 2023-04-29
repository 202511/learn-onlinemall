package com.own.onlinemall.order.interceptor;


import com.own.onlinemall.auth.controller.AuthConstant;
import com.own.onlinemall.auth.vo.MemberResponseVO;
import org.apache.shiro.util.AntPathMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginUserInterceptor implements HandlerInterceptor {
    public  static  ThreadLocal<MemberResponseVO>  loginUser=new ThreadLocal<>();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object attribute =request.getSession().getAttribute(AuthConstant.LOGIN_USER);
        String requestURI = request.getRequestURI();
        boolean match = new AntPathMatcher().match("/order/order/status/**", requestURI);
        boolean match1 = new AntPathMatcher().match("/payed/notify", requestURI);
        if(match||match1)
        {
            return true;
        }

        if(attribute != null )
        {
            loginUser.set((MemberResponseVO) attribute);
             //登录通过
            return  true;
        }
        else
        {
            // 没登录就去登录
            response.sendRedirect("http://auth.onlinemall.com/login.html");
            request.getSession().setAttribute("msg", "请先进行登录");
            return false ;
        }



    }
}
