package com.chunlei.bili.member.controller;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.member.dto.FollowingDTO;
import com.chunlei.bili.member.dto.FollowingGroupDTO;
import com.chunlei.bili.member.model.MemberFollowing;
import com.chunlei.bili.member.service.FollowingGroupService;
import com.chunlei.bili.member.service.FollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MemberFollowingController {
    @Autowired
    FollowingGroupService followingGroupService;
    @Autowired
    FollowingService followingService;

    @PostMapping("/followingGroup/create")
    public R createFollowingGroup(@RequestBody String groupName){
        return followingGroupService.createFollowingGorupIfNotExists(groupName);
    }

    @PostMapping("/followingGroup/remove")
    public R removeFollowingGroup(@RequestBody Long groupId){
        if (groupId != null){
            followingGroupService.removeFollowingGroup(groupId);
        }
        return R.success(null);
    }

    @PostMapping("/following")
    public R addFollowing(@RequestBody MemberFollowing following){
        return followingService.addFollowing(following);
    }

    @PostMapping("/following/remove")
    public R removeFollowing(@RequestBody Long followingId){
        followingService.removeFollowing(followingId);
        return R.success(null);
    }

    @GetMapping("/following/{memberId}")
    public R getUserFollowing(@PathVariable("memberId") Long memberId){
        if (memberId == null) {
            return R.success(null);
        }
        List<FollowingGroupDTO> res = followingService.getUserFollowing(memberId);
        return R.success(res);
    }

    @GetMapping("/following/fans")
    public R getUserFans(@RequestParam("memberId") Long memberId,@RequestParam(value = "ps", defaultValue = "20") Integer ps, @RequestParam(value = "pn", defaultValue = "1") Integer pn){
        if (memberId == null) {
            return R.success(null);
        }
        List<FollowingDTO> fans = followingService.getUserFans(memberId, ps, pn);
        return R.success(fans);
    }

    @GetMapping("/following/fans/all")
    public R getUserFans(@RequestParam("memberId") Long memberId){
        List<FollowingDTO> fans = followingService.getUserFansAll(memberId);
        return R.success(fans);
    }



}
