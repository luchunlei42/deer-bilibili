package com.chunlei.bili.moment.controller;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.moment.model.UserMoments;
import com.chunlei.bili.moment.service.MomentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MomentController {
    @Autowired
    MomentService momentService;

    @PostMapping("/add")
    public R addMoment(@RequestBody @Valid UserMoments moments){
        momentService.addMoment(moments);
        return R.success("动态发布成功");
    }

    @GetMapping("/list/follow")
    public R getMoment(@RequestParam("max") Long max, @RequestParam("offset") Long offset){
        return momentService.queryMomentOfFollow(max, offset);
    }
}
