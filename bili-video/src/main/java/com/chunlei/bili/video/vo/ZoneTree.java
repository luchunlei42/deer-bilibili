package com.chunlei.bili.video.vo;

import com.chunlei.bili.video.model.Category;
import lombok.Data;

import java.util.List;

@Data
public class ZoneTree {

    private List<ZoneTreeNode> tree;

    @Data
    public static class ZoneTreeNode extends Category {
        private List<ZoneTreeNode> children;
    }
}
