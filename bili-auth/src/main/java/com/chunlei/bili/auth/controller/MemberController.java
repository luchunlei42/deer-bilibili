package com.chunlei.bili.auth.controller;

import com.chunlei.bili.auth.service.UserService;
import com.chunlei.bili.common.api.R;
import com.chunlei.bili.member.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {
    @Autowired
    private UserService userService;

    @GetMapping("/member/{memberId}")
    public R findMemberById(@PathVariable("memberId") Long memberId){
        Member member = userService.findMemberById(memberId);
        return R.success(member);
    }
}
