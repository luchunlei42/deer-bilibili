package com.chunlei.bili.common.inteceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chunlei.bili.common.dto.UserDTO;
import com.chunlei.bili.common.utils.JwtUtils;
import com.chunlei.bili.common.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.chunlei.bili.common.constant.WebConstants.TOKEN_HEAD;
import static com.chunlei.bili.common.utils.RedisConstants.LOGIN_USER_KEY;
import static com.chunlei.bili.common.utils.RedisConstants.LOGIN_USER_TTL;


@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        log.info("token is :{}",token);
        if (StrUtil.isBlank(token) || !token.startsWith(TOKEN_HEAD)){
            response.setStatus(401);
            return false;
        }
        token = token.substring(TOKEN_HEAD.length());
        boolean verify = JwtUtils.checkToken(token);
        if (verify){
            String phone = JwtUtils.getPhone(token);
            String tokenKey = LOGIN_USER_KEY + phone;
            String s = stringRedisTemplate.opsForValue().get(tokenKey);
            if (StrUtil.isEmpty(s)){
                response.setStatus(401);
                return false;
            }
            UserDTO userDTO = JSON.parseObject(s, UserDTO.class);
            log.info("pass authorization, phone: {}",phone);
            UserHolder.saveUser(userDTO);
            stringRedisTemplate.expire(tokenKey,LOGIN_USER_TTL, TimeUnit.MINUTES);
            return true;

        }else {
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
