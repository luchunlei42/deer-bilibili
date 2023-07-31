package com.chunlei.bili.moment.client;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.moment.dto.FollowingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "bili-member",path = "apix/member")
public interface MemberFeignClient {

    @GetMapping("/following/fans")
    public R<List<FollowingDTO>> getUserFansAll(@RequestParam("memberId") Long memberId);
}
