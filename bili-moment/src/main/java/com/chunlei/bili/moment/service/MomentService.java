package com.chunlei.bili.moment.service;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.moment.model.UserMoments;

import java.util.List;

public interface MomentService {
    void addMoment(UserMoments moments);

    R queryMomentOfFollow(Long max, Long offset);


    List<Long> getFamous(Long memberId);

    void pullMomentsFromFamous(List<Long> ids);
}
