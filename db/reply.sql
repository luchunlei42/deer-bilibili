drop table if exists `reply`;
create table `reply`(
    `id` bigint not null auto_increment,
    `member_id` bigint not null comment '用户id',
    `video_id` bigint not null comment '视频id',
    `status` int default '1' comment '评论状态，0删除，1正常，2审核',
    `content` varchar(2000) comment '评论内容',
    `child_count`int comment '子评论数',
    `parent_id` bigint not null comment '母评论',
    `root_id` bigint not null comment '根评论',
    `create_time` datetime not null default current_timestamp comment '创建时间',
  `update_time` datetime not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key(`id`),
    INDEX `member_id`(`member_id`),
    INDEX `root_id` (`root_id`),
    INDEX `parent_id` (`parent_id`),
    INDEX `video_id` (`video_id`)
) comment '用户评论表';