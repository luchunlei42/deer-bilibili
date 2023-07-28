package com.chunlei.bili.search.controller;

import com.chunlei.bili.common.api.R;
import com.chunlei.bili.search.dto.PublishDTO;
import com.chunlei.bili.search.entity.EsVideo;
import com.chunlei.bili.search.service.EsSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private EsSearchService searchService;

    @PostMapping("/publish")
    public R publishVideo(@RequestBody PublishDTO publishDTO){
        searchService.save(publishDTO);
        return R.success(null);
    }

    @GetMapping("/region")
    public R searchByRegion(@RequestParam("rid") Long rid, @RequestParam("ps") Integer ps, @RequestParam("pn") Integer pn) throws IOException {
        List<EsVideo> r =  searchService.searchByRegion(rid,ps,pn);
        return R.success(r);
    }

}
