package com.chunlei.bili.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.chunlei.bili.auth.dto.LoginFormDTO;
import com.chunlei.bili.auth.service.UserService;
import com.chunlei.bili.common.api.R;
import com.chunlei.bili.common.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/code")
    public R sendCode(@RequestBody Map<String,String> map){
        return userService.sendCode(map.get("phone"));
    }

    /**
     * 登录功能
     * @param loginForm 登录参数，包含手机号、验证码；或者手机号、密码
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginFormDTO loginForm){
        return userService.login(loginForm);
    }

    /**
     * 登出功能
     * @return 无
     */
    @PostMapping("/logout")
    public R logout(){
        // TODO 实现登出功能
        return R.success(null);
        //return Result.fail("功能未完成");
    }

    @GetMapping("/info")
    public R me(){
        return R.success(UserHolder.getUser());
    }


}
