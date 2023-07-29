package com.chunlei.bili.member.service;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.member.DTO.FollowingDTO;
import com.chunlei.bili.member.DTO.FollowingGroupDTO;
import com.chunlei.bili.member.model.MemberFollowing;

import java.util.List;

public interface FollowingService {
    R addFollowing(MemberFollowing following);

    void removeFollowing(Long followingId);

    List<FollowingGroupDTO> getUserFollowing(Long memberId);

    List<FollowingDTO> getUserFans(Long memberId, Integer ps, Integer pn);
}
