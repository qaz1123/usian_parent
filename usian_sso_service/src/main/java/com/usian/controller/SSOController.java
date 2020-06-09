package com.usian.controller;

import com.usian.pojo.TbUser;
import com.usian.service.SSOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/service/sso")
public class SSOController {

    @Autowired
    private SSOService ssoService;

    @RequestMapping("/checkUserInfo/{checkValue}/{checkFlag}")
    Boolean checkUserInfo(@PathVariable String checkValue, @PathVariable Integer checkFlag){
        return  ssoService.checkUserInfo(checkValue,checkFlag);
    }

    @RequestMapping("/userRegister")
    Integer userRegister(@RequestBody TbUser tbUser){
        return  ssoService.userRegister(tbUser);
    }

    @RequestMapping("/userLogin")
    Map userLogin(String username,String password){
        return  ssoService.userLogin(username,password);
    }

    @PostMapping("/getUserByToken/{token}")
    TbUser getUserByToken(@PathVariable String token){
        return ssoService.getUserByToken(token);
    }

    @PostMapping("/logOut")
    Boolean logOut(String token){
        return ssoService.logOut(token);
    }

}
