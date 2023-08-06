package com.chunlei.bili.member.controller;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.member.dto.MemberInfo;
import com.chunlei.bili.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    @Autowired
    MemberService memberService;

    @GetMapping("/info/{memberId}")
    public R memberInfo(@PathVariable("memberId") Long memberId){
        if (memberId == null) return R.failed("memberId为空");
        MemberInfo memberInfo = memberService.memberInfo(memberId);
        return R.success(memberInfo);
    }
}
