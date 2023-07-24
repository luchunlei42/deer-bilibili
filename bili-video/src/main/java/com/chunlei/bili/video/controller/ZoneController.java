package com.chunlei.bili.video.controller;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.video.service.CategoryService;
import com.chunlei.bili.video.vo.ZoneTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZoneController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/zone/tree")
    public R zoneTree(){
        ZoneTree zoneTree =  categoryService.getZoneTree();
        return R.success(zoneTree);
    }
}
