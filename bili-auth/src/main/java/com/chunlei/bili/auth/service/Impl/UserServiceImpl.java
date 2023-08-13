package com.chunlei.bili.auth.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.chunlei.bili.auth.dto.LoginFormDTO;
import com.chunlei.bili.auth.dto.UserDTO;
import com.chunlei.bili.member.mapper.MemberMapper;
import com.chunlei.bili.member.model.Member;
import com.chunlei.bili.member.model.MemberExample;
import com.chunlei.bili.auth.service.UserService;
import com.chunlei.bili.common.api.R;
import com.chunlei.bili.common.utils.JwtUtils;
import com.chunlei.bili.common.utils.RegexUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.chunlei.bili.common.constant.WebConstants.TOKEN_HEAD;
import static com.chunlei.bili.common.utils.RedisConstants.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    MemberMapper memberMapper;

    @Override
    public R sendCode(String phone) {
        //1.检验手机号
        if(RegexUtils.isPhoneInvalid(phone)){
            //2.如果不符合，返回错误信息
            return R.failed("手机号格式错误！");
        }
        //3.符合，生成验证码
        String code = RandomUtil.randomNumbers(6);
        //4.保存到redis
        redisTemplate.opsForValue().set(LOGIN_CODE_KEY+phone,code,LOGIN_CODE_TTL, TimeUnit.MINUTES);
        log.info("发送短信验证码成功，验证码：{}",code);
        //返回ok
        return R.success(null);
    }

    @Override
    public R login(LoginFormDTO loginForm) {
        //获取jwt token
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();
        String code = loginForm.getCode();
        if(StringUtils.isEmpty(password)) {
            //校验手机和验证码
            if (RegexUtils.isPhoneInvalid(username)) {
                throw new RuntimeException("手机号不正确");
            }
            String cacheCode = redisTemplate.opsForValue().get(LOGIN_CODE_KEY+username);
            //3.不一致，报错
            if (cacheCode == null || !cacheCode.equals(code)) {
                throw new RuntimeException("验证吗不正确");
            }
        }else{
            //校验密码
        }
        Member member = loadUserByUsername(username);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(member.getId());
        userDTO.setNickName(member.getNickname());
        userDTO.setAvatar(member.getAvatar());
        String tokenKey = LOGIN_USER_KEY + username;
        redisTemplate.opsForValue().set(tokenKey, JSON.toJSONString(userDTO), LOGIN_USER_TTL);
        String token = JwtUtils.getJwtToken(member.getId().toString(), username,member.getNickname(), member.getAvatar());
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead", TOKEN_HEAD);
        return R.success(tokenMap);

    }

    @Override
    public Member findMemberById(Long memberId) {

        return memberMapper.selectByPrimaryKey(memberId);
    }

    public Member loadUserByUsername(String username){
        MemberExample example = new MemberExample();
        example.createCriteria().andMobileEqualTo(username);
        Member user = null;
        List<Member> members = memberMapper.selectByExample(example);
        if (members.isEmpty()){
            user = createUserWithPhone(username);
        }else{
            user = members.get(0);
        }
        return user;
    }

    private Member createUserWithPhone(String username) {
        Member user = new Member();
        user.setMobile(username);
        String nickName = getStringRandom(10);
        user.setUsername(username);
        user.setNickname(nickName);
        user.setLevelId(0L);
        memberMapper.insertSelective(user);
        return user;
    }

    //生成随机用户名，数字和字母组成,
    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

}
