package com.own.onlinemall.member.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.own.onlinemall.common.service.impl.CrudServiceImpl;
import com.own.onlinemall.member.constant.MemberConstant;
import com.own.onlinemall.member.dao.MemberDao;
import com.own.onlinemall.member.dto.MemberDTO;
import com.own.onlinemall.member.entity.MemberEntity;
import com.own.onlinemall.member.entity.MemberLevelEntity;
import com.own.onlinemall.member.exception.PhoneException;
import com.own.onlinemall.member.exception.UsernameException;
import com.own.onlinemall.member.service.MemberService;
import com.own.onlinemall.member.vo.MemberUserLoginTO;
import com.own.onlinemall.member.vo.MemberUserRegisterTO;
import com.own.onlinemall.member.vo.SocialUser;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 会员
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2023-04-09
 */
@Service
public class MemberServiceImpl extends CrudServiceImpl<MemberDao, MemberEntity, MemberDTO> implements MemberService {

    @Override
    public QueryWrapper<MemberEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<MemberEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    MemberLevelServiceImpl memberLevelService;
    @Override
    public void regist(MemberUserRegisterTO user) throws InterruptedException {
        // 1.加锁
        RLock lock = redissonClient.getLock(MemberConstant.LOCK_KEY_REGIST_PRE + user.getPhone());
        try {
            lock.tryLock(30L, TimeUnit.SECONDS);
            // 2.校验
            // 校验手机号唯一、用户名唯一
            checkPhoneUnique(user.getPhone());
            checkUserNameUnique(user.getUserName());
            // 3.封装保存
            MemberEntity entity = new MemberEntity();
            entity.setUsername(user.getUserName());
            entity.setMobile(user.getPhone());
            entity.setNickname(user.getUserName());
            // 3.1.设置默认等级信息
            MemberLevelEntity level = memberLevelService.getDefaultLevel();
            entity.setLevelId(level.getId());
            // 3.2.设置密码加密存储
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encode = passwordEncoder.encode(user.getPassword());
            entity.setPassword(encode);
            entity.setCreateTime(new Date());
            this.baseDao.insert(entity);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public MemberEntity login(MemberUserLoginTO user) {
        String loginacct = user.getLoginacct();
        String password = user.getPassword();// 明文

        // 1.查询MD5密文
        MemberEntity entity = baseDao.selectOne(new QueryWrapper<MemberEntity>()
                .eq("username", loginacct)
                .or()
                .eq("mobile", loginacct));
        if (entity != null) {
            // 2.获取password密文进行校验
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(password, entity.getPassword())) {
                // 登录成功
                return entity;
            }
        }
        // 3.登录失败
        return null;
    }

    @Override
    public MemberEntity OAuthlogin(SocialUser socialUser) {
        //登录和注册合并
        String s="https://gitee.com/api/v5/user?access_token="+socialUser.getAccess_token();
        HttpResponse httpResponse=HttpRequest.get(s).execute();
        //成功获取第三方平台用户的id
        if(httpResponse.getStatus()== 200 )
        {
             JSONObject json=JSONObject.parseObject(httpResponse.body());
             String id= json.getLong("id").toString();
            System.out.println("用户的第三方的id为"+id);
             //判断当前用户是否已经用这个账号登录过
            MemberEntity member= baseDao.selectOne(new QueryWrapper<MemberEntity>().eq("gitee_id", id));
            if(member!=null)
            {
                  return  member;
            }
            else
            {
                  String t= (String) json.get("name");
                  member=new MemberEntity();
                  member.setUsername(t);
                  member.setNickname(t);
                  member.setGiteeId(id);
                  baseDao.insert(member);
                  return member;
            }

        }
        System.out.println("请求失败");
        return null;
    }


    /**
     * 校验手机号是否唯一
     */
    public void checkPhoneUnique(String phone) throws PhoneException {
        Long count = baseDao.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (count > 0) {
            throw new PhoneException();
        }
    }

    /**
     * 校验用户名是否唯一
     */
    public void checkUserNameUnique(String userName) throws UsernameException {
        Long count = baseDao.selectCount(new QueryWrapper<MemberEntity>()
                .eq("username", userName));
        if (count > 0) {
            throw new UsernameException();
        }
    }
}