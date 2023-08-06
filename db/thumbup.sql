drop table if exists `like`;
create table `like`(
    `id` bigint not null auto_increment,
    `liked_member_id` bigint not null comment '被点赞的用户id',
    `liked_post_id` bigint not null comment '点赞的用户id',
    `video_id` bigint not null comment '视频id',
    `status` tinyint(1) default '1' comment '点赞状态，0取消，1点赞',
    `create_time` datetime not null default current_timestamp comment '创建时间',
  `update_time` datetime not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key(`id`),
    INDEX `liked_member_id`(`liked_member_id`),
    INDEX `liked_post_id`(`liked_post_id`),
    INDEX `video_id` (`video_id`)
) comment '用户点赞表';
drop table if exists `like_count`;
create table `like_count`(
    `id` bigint not null auto_increment,
    `liked_member_id` bigint not null comment '被点赞的用户id',
    `count` bigint not null default 0,
    `video_id` bigint not null comment '视频id',
    `create_time` timestamp not null default current_timestamp comment '创建时间',
  `update_time` timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key(`id`),
    INDEX `liked_member_id`(`liked_member_id`),
    INDEX `video_id`(`video_id`)
) comment '用户点赞数表';

