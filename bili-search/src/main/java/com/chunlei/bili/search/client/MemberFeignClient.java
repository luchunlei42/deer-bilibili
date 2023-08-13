package com.chunlei.bili.search.client;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.search.dto.MemberInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "bili-member",path = "apix/member")
public interface MemberFeignClient {

    @GetMapping("/info/{memberId}")
    public R<MemberInfo> memberInfo(@PathVariable("memberId") Long memberId);
}
