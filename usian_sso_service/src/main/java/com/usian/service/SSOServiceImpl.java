package com.usian.service;

import com.usian.mapper.TbUserMapper;
import com.usian.pojo.TbUser;
import com.usian.pojo.TbUserExample;
import com.usian.redis.RedisClient;
import com.usian.utis.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class SSOServiceImpl implements SSOService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private RedisClient redisClient;

    @Value("${USER_INFO}")
    private String USER_INFO;

    @Value("${SESSION_EXPIRE}")
    private Long SESSION_EXPIRE;

    @Override
    public Boolean checkUserInfo(String checkValue, Integer checkFlag) {
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        if(checkFlag==1){
            criteria.andUsernameEqualTo(checkValue);
        }else if (checkFlag==2){
            criteria.andPhoneEqualTo(checkValue);
        }
        List<TbUser> tbUserList = tbUserMapper.selectByExample(tbUserExample);
        if(tbUserList==null || tbUserList.size()==0){
            return true;
        }
        return false;
    }

    @Override
    public Integer userRegister(TbUser tbUser) {
        String pwd = MD5Utils.digest(tbUser.getPassword());
        tbUser.setPassword(pwd);
        Date date = new Date();
        tbUser.setCreated(date);
        tbUser.setUpdated(date);
        return tbUserMapper.insertSelective(tbUser);
    }

    @Override
    public Map userLogin(String username, String password) {
        String pwd = MD5Utils.digest(password);
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andPasswordEqualTo(pwd);
        List<TbUser> tbUserList = tbUserMapper.selectByExample(tbUserExample);
        if(tbUserList==null || tbUserList.size()==0){
            return null;
        }
        TbUser tbUser = tbUserList.get(0);
        String token = UUID.randomUUID().toString();
        tbUser.setPassword(null);
        redisClient.set(USER_INFO+":"+token,tbUser);
        redisClient.expire(USER_INFO+":"+token,SESSION_EXPIRE);

        Map<String, Object> map = new HashMap<>();
        map.put("token",token);
        map.put("userid",tbUser.getId());
        map.put("username",username);
        return map;
    }

    @Override
    public TbUser getUserByToken(String token) {
        TbUser tbUser = (TbUser) redisClient.get(USER_INFO + ":" + token);
        if(tbUser!=null){
            redisClient.expire(USER_INFO+":"+token,SESSION_EXPIRE);
            return tbUser;
        }

        return null;
    }

    @Override
    public Boolean logOut(String token) {
        return redisClient.del(USER_INFO+":"+token);
    }
}
