package com.own.onlinemall.auth.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.own.onlinemall.auth.feign.MemberFeignService;
import com.own.onlinemall.auth.feign.ThirdPartFeignService;
import com.own.onlinemall.auth.r.BizCodeEnume;
import com.own.onlinemall.auth.r.R;
import com.own.onlinemall.auth.vo.MemberResponseVO;
import com.own.onlinemall.auth.vo.UserLoginVO;
import com.own.onlinemall.auth.vo.UserRegisterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class LoginController {
    @GetMapping("/login.html")
    public  String loginPage(HttpSession session)
    {
        Object t=  session.getAttribute(AuthConstant.LOGIN_USER);
        if(t!=null)
        {
             return "redirect:http://onlinemall.com";
        }


        return "login";
    }

    @GetMapping("/reg.html")
    public String regPage() {
        return "reg";
    }
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ThirdPartFeignService thirdPartFeignService;
    @ResponseBody
    @GetMapping("sms/sendCode")
    public R sendCode(@RequestParam("phone") String phone)
    {
        String s = redisTemplate.opsForValue().get(AuthConstant.SMS_CODE_CACHE_PREFIX + phone);
        if(s!=null)
        {
            long l = Long.parseLong(s.split("_")[1]);
            long l1 = System.currentTimeMillis() - l;
            if (l1 < 60000) {
                return R.error(BizCodeEnume.SMS_CODE_EXCEPTION);
            }
        }
        String substring = "1234" + "_" + System.currentTimeMillis();
        boolean b = thirdPartFeignService.sendCode(phone, substring);
        redisTemplate.opsForValue().set(AuthConstant.SMS_CODE_CACHE_PREFIX + phone, substring, 10, TimeUnit.MINUTES);
        return R.ok();

    }
    @Autowired
    MemberFeignService memberFeignService;
    @PostMapping("/register")
    public String register(@Valid UserRegisterVO user, BindingResult result , RedirectAttributes attributes)
    {
        // 1.参数校验
        if (result.hasErrors()) {
            // 校验出错，返回注册页
            Map<String, String> errMap = new HashMap<>();
            result.getFieldErrors().forEach(err -> errMap.put(err.getField(), err.getDefaultMessage()));
            // 封装异常返回前端显示
            attributes.addFlashAttribute("errors", errMap);// flash，session中的数据只使用一次
            return "redirect:http://auth.onlinemall.com/reg.html";// 采用重定向有一定防刷功能
            // 1、return "redirect:http://auth.onlinemall.com/reg.html"【采用】 重定向Get请求【配合RedirectAttributes共享数据】
            // 2、return "redirect:http:/reg.html"                   【采用】 重定向Get请求，省略当前服务url【配合RedirectAttributes共享数据】
            // 3、return "redirect:/reg.html"                                重定向Get请求，使用视图控制器拦截请求并映射reg视图【配合RedirectAttributes共享数据】【bug：会以ip+port来重定向】
            // 4、return "forward:http://auth.onlinemall.com/reg.html";        请求转发与当前请求方式一致（Post请求）【配合Model共享数据】【异常404：当前/reg.html不存在post请求】
            // 5、return "forward:http:/reg.html";                           请求转发与当前请求方式一致（Post请求），省略当前服务url 【配合Model共享数据】【异常404：当前/reg.html不存在post请求】
            // 6、return "forward:/reg.html";                                请求转发与当前请求方式一致（Post请求），使用视图控制器拦截请求并映射reg视图【配合Model共享数据】【异常405：Request method 'POST' not supported，视图控制器必须使用GET请求访问，而当前请求转发使用post方式，导致异常】
            // 7、return "reg";                                              视图解析器前后拼串查找资源返回【配合Model共享数据】
        }

        // 2.验证码校验
        String code = user.getCode();
        String redisCode = redisTemplate.opsForValue().get(AuthConstant.SMS_CODE_CACHE_PREFIX + user.getPhone());
        if (StringUtils.isBlank(redisCode)) {
            // 验证码过期
            Map<String, String> errMap = new HashMap<>();
            errMap.put("code", "验证码失效");
            // 封装异常返回前端显示
            attributes.addFlashAttribute("errors", errMap);// flash，session中的数据只使用一次
            return "redirect:http://auth.onlinemall.com/reg.html";// 采用重定向有一定防刷功能
        }
        if (!code.equals(redisCode.split("_")[0])) {
            // 验证码错误
            Map<String, String> errMap = new HashMap<>();
            errMap.put("code", "验证码错误");
            // 封装异常返回前端显示
            attributes.addFlashAttribute("errors", errMap);// flash，session中的数据只使用一次
            return "redirect:http://auth.onlinemall.com/reg.html";// 采用重定向有一定防刷功能
        }

        // 3.调用login实现注册
        redisTemplate.delete(AuthConstant.SMS_CODE_CACHE_PREFIX + user.getPhone());
        R r = memberFeignService.regist(user);
        if (r.getCode() == 0) {
            // 注册成功，重定向到登录页
            return "redirect:http://auth.onlinemall.com/login.html";// 重定向
        } else {
            //　注册失败，封装异常
            HashMap<String, String> errMap = new HashMap<>();
            errMap.put("msg", r.getData("msg", new TypeReference<String>() {
            }));
            attributes.addFlashAttribute("errors", errMap);// flash，session中的数据只使用一次
            return "redirect:http://auth.onlinemall.com/reg.html";// 采用重定向有一定防刷功能
        }
    }



    @PostMapping(value = "/login")
    public String login(UserLoginVO user, RedirectAttributes attributes,HttpSession session) {
        // 1.远程调用登录
        R r = memberFeignService.login(user);
        if (r.getCode() == 0) {
            // 2.登录成功，设置session值
            MemberResponseVO data = r.getData(new TypeReference<MemberResponseVO>() {
            });
            session.setAttribute(AuthConstant.LOGIN_USER, data);
            // 3.重定向，视图可以从session中拿到用户信息
            return "redirect:http://onlinemall.com";
        } else {
            // 4.登录失败，封装异常信息重定向返回
            Map<String, String> errors = new HashMap<>();
            errors.put("msg", r.getData("msg", new TypeReference<String>() {}));
            attributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.onlinemall.com/login.html";
        }
    }
}
