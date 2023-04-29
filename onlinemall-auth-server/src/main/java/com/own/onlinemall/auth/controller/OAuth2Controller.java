package com.own.onlinemall.auth.controller;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.common.http.HttpUtils;
import com.alibaba.nacos.common.http.param.Query;
import com.own.onlinemall.auth.feign.MemberFeignService;
import com.own.onlinemall.auth.r.R;
import com.own.onlinemall.auth.vo.MemberResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

//处理社交登录
@Controller
public class OAuth2Controller {
    String urlPrefix="https://gitee.com/oauth/token?grant_type=authorization_code&code={code}&client_id={client_id}&redirect_uri={redirect_uri}&client_secret={client_secret}";
    @Autowired
    MemberFeignService memberFeignService;
     @GetMapping("/oauth2.0/gitee/success")
    public String gitee(@RequestParam("code") String code , HttpSession session) throws URISyntaxException {
         Map<String ,Object> map= new HashMap<String , Object>();
         map.put("grant_type", "authorization_code");
         map.put("code",code);
         map.put("client_id", "f70b9775e55efa47f74daede81803e6696711adb2ab1c691aa16ec603ce6adb4");
         map.put("redirect_uri", "http://auth.onlinemall.com/oauth2.0/gitee/success");
         map.put("client_secret" , "a173b86b081eedf0339c08eaec786f1c6299618defc1a1cf0a5c02a6b829eb02");
         // 根据code换取Access token
         HttpResponse result= HttpRequest.post("https://gitee.com/oauth/token").form(map).execute();
         System.out.println(result.getStatus());
         if(result.getStatus()== 200)
         {
              //顺利获取到了token
             SocialUser socialUser=JSONObject.parseObject(result.body(),SocialUser.class);
             System.out.println(socialUser.getAccess_token());
              //知道了当前的用户信息
              R t= memberFeignService.oauthlogin(socialUser);

              if(t.getCode() ==  0 )
              {
                  MemberResponseVO loginUser = t.getData(new TypeReference<MemberResponseVO>() {
                  });

                  // 3.信息存储到session中，并且放大作用域（指定domain=父级域名）
                  session.setAttribute(AuthConstant.LOGIN_USER, loginUser);
                   //登录成功就跳回首页
                  return "redirect:http://onlinemall.com";
              }
              else {
                  return "login";
              }
             // 没有注册的自动注册（生成一个会员信息， 以后这个社交账号对应这个会员）
         }
         else
         {
              return "login";
         }

     }
}
