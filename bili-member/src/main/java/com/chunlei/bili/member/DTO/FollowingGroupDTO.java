package com.chunlei.bili.member.DTO;

import lombok.Data;

import java.util.List;

@Data
public class FollowingGroupDTO {
    private Long groupId;
    private String groupName;
    private List<FollowingDTO> followingList;
}
