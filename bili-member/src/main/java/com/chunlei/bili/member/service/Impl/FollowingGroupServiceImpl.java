package com.chunlei.bili.member.service.Impl;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.common.utils.UserHolder;
import com.chunlei.bili.member.constant.FollowingGroupType;
import com.chunlei.bili.member.mapper.FollowingGroupMapper;
import com.chunlei.bili.member.model.FollowingGroup;
import com.chunlei.bili.member.model.FollowingGroupExample;
import com.chunlei.bili.member.service.FollowingGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowingGroupServiceImpl implements FollowingGroupService {
    @Autowired
    FollowingGroupMapper followingGroupMapper;

    @Override
    public R createFollowingGorupIfNotExists(String groupName) {
        FollowingGroupExample example = new FollowingGroupExample();
        example.createCriteria().andNameLike(groupName);
        long count = followingGroupMapper.countByExample(example);
        if (count > 0){
            return R.failed("分组已经存在了喵");
        }
        FollowingGroup followingGroup = new FollowingGroup();
        followingGroup.setName(groupName);
        followingGroup.setCreatetime(new Date());
        followingGroup.setType(String.valueOf(FollowingGroupType.SELF_DEFINE_GROUP));

        //获取用户
        Long memberId = UserHolder.getUser().getId();
        followingGroup.setMemberId(memberId);

        int res = followingGroupMapper.insertSelective(followingGroup);
        if (res == 1){
            return R.success(followingGroup.getId(),"新建分组成功");
        }
        return R.failed("分组新建失败");
    }

    @Override
    public FollowingGroup findGroupById(Long groupId) {
        return followingGroupMapper.selectByPrimaryKey(groupId);
    }

    @Override
    public List<FollowingGroup> getAllGroupsByIds(List<Long> groupIds) {
        FollowingGroupExample example = new FollowingGroupExample();
        example.createCriteria().andIdIn(groupIds);
        List<FollowingGroup> followingGroups = followingGroupMapper.selectByExample(example);
        return followingGroups;
    }

    @Override
    public void removeFollowingGroup(Long groupId) {
        Long memberId = UserHolder.getUser().getId();
        FollowingGroupExample example = new FollowingGroupExample();
        example.createCriteria().andIdEqualTo(groupId).andMemberIdEqualTo(memberId);
        long l = followingGroupMapper.countByExample(example);
        if (l > 0){
            followingGroupMapper.deleteByPrimaryKey(groupId);
        }
    }
}
