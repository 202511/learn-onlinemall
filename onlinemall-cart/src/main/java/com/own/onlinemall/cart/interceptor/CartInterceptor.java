package com.own.onlinemall.cart.interceptor;

import com.own.onlinemall.auth.controller.AuthConstant;
import com.own.onlinemall.auth.vo.MemberResponseVO;
import com.own.onlinemall.cart.constant.CartConstant;
import com.own.onlinemall.cart.to.UserInfoTO;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Component
public class CartInterceptor  implements HandlerInterceptor {
    public static  ThreadLocal<UserInfoTO> threadLocal =new ThreadLocal<>();


//    目标方法执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoTO userInfoTO= new UserInfoTO();
        HttpSession session=  request.getSession();
        Object t =session.getAttribute(AuthConstant.LOGIN_USER);
        MemberResponseVO member=(MemberResponseVO) t;
        System.out.println(t);
        if(member != null )
        {
            //用户登录
           userInfoTO.setUserId(member.getId());

        }
        Cookie[] cookies=request.getCookies();
        if(cookies!=null&& cookies.length> 0 ) {
            for (Cookie cookie : cookies) {
                String name =cookie.getName();
                if(name.equals(CartConstant.TEMP_USER_COOKIE_NAME))
                {
                    //未登录状态有userkey
                     userInfoTO.setUserKey(cookie.getValue());
                     userInfoTO.setTempUser(true);
                }
            }
        }
        if(StringUtils.isEmpty(userInfoTO.getUserKey()))
        {
            String w= UUID.randomUUID().toString();
            userInfoTO.setUserKey(w);
        }
       threadLocal.set(userInfoTO);
        return true;
    }
// 业务执行之后
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        UserInfoTO t= threadLocal.get();
        if(!t.isTempUser()) {
            //持续的延长
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, t.getUserKey());
            cookie.setDomain("onlinemall.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);

        }


    }
}
