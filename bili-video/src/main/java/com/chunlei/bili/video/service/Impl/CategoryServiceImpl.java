package com.chunlei.bili.video.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.chunlei.bili.video.mapper.CategoryMapper;
import com.chunlei.bili.video.model.Category;
import com.chunlei.bili.video.model.CategoryExample;
import com.chunlei.bili.video.service.CategoryService;
import com.chunlei.bili.video.vo.ZoneTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ZoneTree getZoneTree() {
        CategoryExample example = new CategoryExample();
        List<Category> categories = categoryMapper.selectByExample(example);
        ZoneTree zoneTree = new ZoneTree();
        if (categories != null && categories.size()>0){
            List<ZoneTree.ZoneTreeNode> collect = categories.stream().filter(category -> category.getParentCid() == 0).map(menu -> {
                ZoneTree.ZoneTreeNode node = new ZoneTree.ZoneTreeNode();
                BeanUtil.copyProperties(menu, node);
                node.setChildren(getChildren(menu, categories));
                return node;
            }).sorted((Comparator.comparingInt(o -> Optional.ofNullable(o.getSort()).orElse(0)))).collect(Collectors.toList());
            zoneTree.setTree(collect);
        }
        return zoneTree;
    }

    private List<ZoneTree.ZoneTreeNode> getChildren(Category parent, List<Category> categories) {
        List<ZoneTree.ZoneTreeNode> collect = categories.stream().filter(menu -> menu.getParentCid() == parent.getCatId())
                .map(menu -> {
                    ZoneTree.ZoneTreeNode node = new ZoneTree.ZoneTreeNode();
                    BeanUtil.copyProperties(menu, node);
                    return node;
                }).sorted(Comparator.comparingInt(menu -> Optional.ofNullable(menu.getSort()).orElse(0))).collect(Collectors.toList());
        return collect;
    }
}
