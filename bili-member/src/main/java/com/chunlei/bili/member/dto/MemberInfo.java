package com.chunlei.bili.member.dto;

import lombok.Data;

@Data
public class MemberInfo {
    private Long id;
    private String nickName;
    private String avatar;
    private Integer level;
}
