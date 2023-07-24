package com.chunlei.bili.video.service;

import com.chunlei.bili.video.model.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getTagsByCatId(Long catId);
}
