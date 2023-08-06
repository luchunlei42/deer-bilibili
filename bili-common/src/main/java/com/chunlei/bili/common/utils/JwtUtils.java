package com.chunlei.bili.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtils {

    public static final long EXPIRE = 1000 * 60 * 60 * 12; // token过期时间  12小时
    public static final String APP_SECRET = "test_key"; // 加密的密钥

    // 生成token字符串
    public static String getJwtToken(String id, String phone, String nickname,String avatar){

        String JwtToken = Jwts.builder()
                // 头信息
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256") // 加密方式
                // 设置过期时间
                .setSubject("xxx") // 项目名
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))

                // 设置用户信息 可以加多个
                .claim("phone", phone)
                .claim("id", id)
                .claim("nickname", nickname)
                .claim("avatar", avatar)
                // 签名方式
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();
        return JwtToken;
    }

    // 判断token的合法性、有效期等进行判断，直接对token进行判断
    // 注意这里的异常直接交给拦截器中去处理
    public static boolean checkToken(String jwtToken) {
        if(StringUtils.isEmpty(jwtToken)) return false;
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            throw e; // 抛出异常交给拦截器处理
        }
        return true;
    }

    // 判断token是否存在与有效，从请求头中获取token
    public static boolean checkToken(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader("token");
            if(StringUtils.isEmpty(jwtToken)) return false;
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 根据token获取用户信息
    public static String getMemberIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        if(StringUtils.isEmpty(jwtToken)) return "";
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody(); // 得到用户数据的主体
        return (String)claims.get("id");
    }

    // 根据token获取用户信息 从请求头中获取token
    public static String getMemberNickNameByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        if(StringUtils.isEmpty(jwtToken)) return "";
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody(); // 得到用户数据的主体
        return (String)claims.get("nickname");
    }
    public static String getPhone(String jwtToken){
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody(); // 得到用户数据的主体
        return (String)claims.get("phone");
    }

    public static String getAvatar(String jwtToken){
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody(); // 得到用户数据的主体
        return (String)claims.get("avatar");
    }

    public static String getId(String jwtToken){
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody(); // 得到用户数据的主体
        return (String)claims.get("id");
    }
}
