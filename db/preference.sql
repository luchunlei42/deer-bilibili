DROP TABLE IF EXISTS `member_video_preference`;
CREATE TABLE `member_video_preference` (
  `member_id` bigint COMMENT '用户id',
  `video_id` bigint COMMENT '视频id',
  `score`  int DEFAULT 0 COMMENT '得分',
  `time`    datetime,
  `view`   tinyint default 0 comment '观看6分',
  `like`   tinyint default 0 comment '点赞2分',
  `reply`   tinyint default 0 comment '评论2分',
  PRIMARY KEY (`member_id`,`video_id`)
) ENGINE=InnoDB COMMENT='视频推荐评分表';