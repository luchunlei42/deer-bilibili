package com.chunlei.bili.video.service.Impl;

import com.chunlei.bili.video.dao.TagDao;
import com.chunlei.bili.video.mapper.TagMapper;
import com.chunlei.bili.video.model.Tag;
import com.chunlei.bili.video.model.TagExample;
import com.chunlei.bili.video.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    TagDao tagDao;

    @Override
    public List<Tag> getTagsByCatId(Long catId) {
        List<Tag> tags = tagDao.getTagsByCatId(catId);
        return tags.stream().
                sorted(Comparator.comparing(o-> Optional.ofNullable(o.getSort()).orElse(0)))
                .collect(Collectors.toList());
    }
}
