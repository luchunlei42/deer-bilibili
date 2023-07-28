package com.chunlei.bili.video.client;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.video.model.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "bili-auth",path = "apix/auth")
public interface BiliMemberFeignClient {

    @GetMapping("/member/{memberId}")
    R<Member> findMemberById(@PathVariable("memberId") Long memberId);
}
