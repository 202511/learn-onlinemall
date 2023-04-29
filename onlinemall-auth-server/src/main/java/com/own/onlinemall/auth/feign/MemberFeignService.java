package com.own.onlinemall.auth.feign;


import com.own.onlinemall.auth.controller.SocialUser;
import com.own.onlinemall.auth.r.R;
import com.own.onlinemall.auth.vo.UserLoginVO;
import com.own.onlinemall.auth.vo.UserRegisterVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 会员服务
 * @Author: wanzenghui
 * @Date: 2021/11/28 19:52
 */
@FeignClient("onlinemall-member")
public interface MemberFeignService {
    @PostMapping("/member/member/oauth2/login")
    public R oauthlogin(@RequestBody SocialUser socialUser);

    /**
     * 注册
     */
    @PostMapping("/member/member/regist")
    R regist(@RequestBody UserRegisterVO user);

    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVO vo);

}