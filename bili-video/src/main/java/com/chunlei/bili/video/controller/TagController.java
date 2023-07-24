package com.chunlei.bili.video.controller;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.video.model.Tag;
import com.chunlei.bili.video.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TagController {
    @Autowired
    TagService tagService;

    @GetMapping("/tag/{catId}")
    public R getTagsByCatId(@PathVariable("catId") Long catId){
        if (catId!=null){
            List<Tag> tags = tagService.getTagsByCatId(catId);
            return R.success(tags);
        }else {
            return R.failed();
        }
    }
}
