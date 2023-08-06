-- ----------------------------
-- Table structure for t_danmu
-- ----------------------------
DROP TABLE IF EXISTS `danmaku`;
CREATE TABLE `danmaku`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `video_id` bigint NULL DEFAULT NULL COMMENT '视频Id',
  `content` text NULL COMMENT '弹幕内容',
  `danmaku_time` varchar(50) NULL DEFAULT NULL COMMENT '弹幕出现时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '弹幕记录表' ROW_FORMAT = Dynamic;