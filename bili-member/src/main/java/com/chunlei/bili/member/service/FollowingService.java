package com.chunlei.bili.member.service;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.member.dto.FollowingDTO;
import com.chunlei.bili.member.dto.FollowingGroupDTO;
import com.chunlei.bili.member.model.MemberFollowing;

import java.util.List;

public interface FollowingService {
    R addFollowing(MemberFollowing following);

    void removeFollowing(Long followingId);

    List<FollowingGroupDTO> getUserFollowing(Long memberId);

    List<FollowingDTO> getUserFans(Long memberId, Integer ps, Integer pn);

    List<FollowingDTO> getUserFansAll(Long memberId);

    void refreshFamousList(Long memberId);

    List<Long> getFamous(Long memberId);
}
