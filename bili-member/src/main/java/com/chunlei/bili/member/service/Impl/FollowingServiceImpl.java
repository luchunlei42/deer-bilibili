package com.chunlei.bili.member.service.Impl;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.common.utils.UserHolder;
import com.chunlei.bili.member.DTO.FollowingDTO;
import com.chunlei.bili.member.DTO.FollowingGroupDTO;
import com.chunlei.bili.member.constant.FollowingGroupType;
import com.chunlei.bili.member.dao.FollowingDao;
import com.chunlei.bili.member.mapper.MemberFollowingMapper;
import com.chunlei.bili.member.mapper.MemberMapper;
import com.chunlei.bili.member.model.*;
import com.chunlei.bili.member.service.FollowingGroupService;
import com.chunlei.bili.member.service.FollowingService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FollowingServiceImpl implements FollowingService {

    @Autowired
    FollowingGroupService followingGroupService;
    @Autowired
    MemberFollowingMapper followingMapper;
    @Autowired
    MemberMapper memberMapper;
    @Autowired
    FollowingDao followingDao;

    @Override
    @Transactional
    public R addFollowing(MemberFollowing following) {
        Long memberId = UserHolder.getUser().getId();

        Long groupId = following.getGroupId();
        if (groupId == null){
            groupId = FollowingGroupType.DEFAULT_FOLLOW;
            following.setFollowingId(groupId);
        }else{
            if(groupId != FollowingGroupType.SPECIAL_FOLLOW
                    || groupId != FollowingGroupType.SPECIAL_FOLLOW
                    || groupId != FollowingGroupType.DEFAULT_FOLLOW){
                FollowingGroup group = followingGroupService.findGroupById(groupId);
                if (group == null) {
                    return R.failed("分组不存在");
                }
            }
        }
        following.setMemberId(memberId);

        Long followingId = following.getFollowingId();
        Member follower = memberMapper.selectByPrimaryKey(Long.valueOf(followingId));
        if (follower == null){
            return R.failed("用户不存在");
        }

        followingMapper.insertSelective(following);
        return R.success("关注成功");
    }

    @Override
    public void removeFollowing(Long followingId) {
        if (followingId == null){
            return;
        }
        Member follow = memberMapper.selectByPrimaryKey(followingId);
        if (follow == null){
            return;
        }
        Long memberId = UserHolder.getUser().getId();
        followingDao.removeFollowing(memberId, followingId);
    }

    @Override
    public List<FollowingGroupDTO> getUserFollowing(Long memberId) {
        List<FollowingGroupDTO> res = new ArrayList<>();

        Member member = memberMapper.selectByPrimaryKey(memberId);
        if (member == null){
            throw new RuntimeException("用户不存在");
        }
        MemberFollowingExample example = new MemberFollowingExample();
        example.createCriteria().andMemberIdEqualTo(memberId);
        List<MemberFollowing> followings = followingMapper.selectByExample(example);
        if (followings==null || followings.isEmpty()){
            return res;
        }
        List<Long> followingIds = new ArrayList<>();
        List<Long> groupIds = new ArrayList<>();
        Map<Long,Long> followToGroupMap = new HashMap<>();
        for (MemberFollowing following : followings) {
            followingIds.add(following.getFollowingId());
            groupIds.add(following.getGroupId());
            followToGroupMap.put(following.getFollowingId(), following.getGroupId());
        }

        List<FollowingGroup> followingGroups = followingGroupService.getAllGroupsByIds(groupIds);
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andIdIn(followingIds);
        List<Member> followingMembers = memberMapper.selectByExample(memberExample);
        Map<Long,List<FollowingDTO>> map = new HashMap<>();
        for (Member item : followingMembers) {
            FollowingDTO followingDTO = new FollowingDTO();
            followingDTO.setNickname(item.getNickname());
            followingDTO.setId(item.getId());
            followingDTO.setAvatar(item.getAvatar());
            followingDTO.setLevelId(item.getLevelId());
            followingDTO.setSign(item.getSign());
            Long groupId = followToGroupMap.get(item.getId());
            map.computeIfAbsent(groupId,v->new ArrayList<>()).add(followingDTO);
        }
        for (FollowingGroup group : followingGroups) {
            FollowingGroupDTO groupDTO = new FollowingGroupDTO();
            groupDTO.setGroupName(group.getName());
            groupDTO.setGroupId(group.getId());
            groupDTO.setFollowingList(map.get(groupDTO.getGroupId()));
            res.add(groupDTO);
        }

        return res;
    }

    @Override
    public List<FollowingDTO> getUserFans(Long memberId, Integer ps, Integer pn) {
        PageHelper.startPage(pn,ps);
        MemberFollowingExample example = new MemberFollowingExample();
        example.createCriteria().andFollowingIdEqualTo(memberId);
        List<MemberFollowing> followings = followingMapper.selectByExample(example);
        List<Long> memberIds = followings.stream().map(MemberFollowing::getMemberId).collect(Collectors.toList());
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andIdIn(memberIds);
        List<Member> followingMembers = memberMapper.selectByExample(memberExample);
        List<FollowingDTO> collect = followingMembers.stream().map(item -> {
            FollowingDTO followingDTO = new FollowingDTO();
            followingDTO.setNickname(item.getNickname());
            followingDTO.setId(item.getId());
            followingDTO.setAvatar(item.getAvatar());
            followingDTO.setLevelId(item.getLevelId());
            followingDTO.setSign(item.getSign());
            return followingDTO;
        }).collect(Collectors.toList());
        return collect;
    }
}
