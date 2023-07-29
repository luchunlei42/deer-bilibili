package com.chunlei.bili.member.DTO;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FollowingDTO {
    private Long id;

    private Long levelId;

    private String nickname;

    private String avatar;

    private String sign;

}
