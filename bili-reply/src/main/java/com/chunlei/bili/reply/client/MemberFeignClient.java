package com.chunlei.bili.reply.client;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.reply.dto.MemberInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("bili-member")
public interface MemberFeignClient {

    @GetMapping("/info/{memberId}")
    R<MemberInfo> memberInfo(@PathVariable("memberId") Long memberId);
}
