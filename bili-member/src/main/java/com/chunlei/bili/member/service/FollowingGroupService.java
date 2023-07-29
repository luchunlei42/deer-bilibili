package com.chunlei.bili.member.service;


import com.chunlei.bili.common.api.R;
import com.chunlei.bili.member.model.FollowingGroup;

import java.util.List;

public interface FollowingGroupService {
    R createFollowingGorupIfNotExists(String groupName);

    FollowingGroup findGroupById(Long groupId);

    List<FollowingGroup> getAllGroupsByIds(List<Long> groupIds);

    void removeFollowingGroup(Long groupId);
}
