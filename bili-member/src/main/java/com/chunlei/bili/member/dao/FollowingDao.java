package com.chunlei.bili.member.dao;

public interface FollowingDao {
    void removeFollowing(Long memberId, Long followingId);
}
