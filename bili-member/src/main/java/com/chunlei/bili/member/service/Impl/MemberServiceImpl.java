package com.chunlei.bili.member.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.chunlei.bili.member.dto.MemberInfo;
import com.chunlei.bili.member.mapper.MemberMapper;
import com.chunlei.bili.member.model.Member;
import com.chunlei.bili.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberMapper memberMapper;
    @Override
    public MemberInfo memberInfo(Long memberId) {
        Member member = memberMapper.selectByPrimaryKey(memberId);
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setAvatar(member.getAvatar());
        memberInfo.setId(memberId);
        memberInfo.setNickName(member.getNickname());
        memberInfo.setLevel(Math.toIntExact(member.getLevelId()));
        return memberInfo;
    }
}
