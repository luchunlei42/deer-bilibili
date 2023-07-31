drop table if exists member;

drop table if exists growth_change_history;

drop table if exists member_level;

drop table if exists member_statistics_info;

/*==============================================================*/
/* Table: member                                            */
/*==============================================================*/
create table member
(
   id                   bigint not null auto_increment comment 'id',
   level_id             bigint comment '会员等级id',
   username             char(64) comment '用户名',
   nickname				varchar(64) comment '昵称',
   password             varchar(64) comment '密码',
   mobile               varchar(20) comment '手机号码',
   email                varchar(64) comment '邮箱',
   avatar               varchar(500) comment '头像',
   gender               tinyint comment '性别',
   birth                date comment '生日',
   city                 varchar(500) comment '所在城市',
   sign                 varchar(255) comment '个性签名',
   growth               int comment '成长值',
   status               tinyint comment '启用状态',
   create_time          datetime comment '注册时间',
   primary key (id)
);

alter table member comment '会员';
/*==============================================================*/
/* Table: ums_growth_change_history                             */
/*==============================================================*/
create table growth_change_history
(
   id                   bigint not null auto_increment comment 'id',
   member_id            bigint comment 'member_id',
   create_time          datetime comment 'create_time',
   change_count         int comment '改变的值（正负计数）',
   note                 varchar(0) comment '备注',
   source_type          tinyint comment '积分来源[0-观看，1-点赞,2-投稿]',
   primary key (id)
);

alter table growth_change_history comment '成长值变化历史记录';
/*==============================================================*/
/* Table: ums_member_level                                      */
/*==============================================================*/
create table member_level
(
   id                   bigint not null comment 'id',
   name                 varchar(100) comment '等级名称',
   growth_point         int comment '等级需要的成长值',
   default_status       tinyint comment '是否为默认等级[0->不是；1->是]',
   note                 varchar(255) comment '备注',
   primary key (id)
);

alter table member_level comment '会员等级';

/*==============================================================*/
/* Table: ums_member_statistics_info                            */
/*==============================================================*/
create table member_statistics_info
(
   id                   bigint not null auto_increment comment 'id',
   member_id            bigint comment '会员id',
   comment_count        int comment '评价数',
   login_count          int comment '登录次数',
   attend_count         int comment '关注数量',
   fans_count           int comment '粉丝数量',
   like_count           int comment  '点赞数量',
   primary key (id)
);

alter table member_statistics_info comment '会员统计信息';

-- ----------------------------
-- Table structure for t_user_following
-- ----------------------------
DROP TABLE IF EXISTS `member_following`;
CREATE TABLE `member_following` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` bigint DEFAULT NULL COMMENT '用户id',
  `following_id` bigint DEFAULT NULL COMMENT '关注用户id',
  `group_id` bigint DEFAULT NULL COMMENT '关注分组id',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户关注表';

-- ----------------------------
-- Table structure for t_following_group
-- ----------------------------
DROP TABLE IF EXISTS `following_group`;
CREATE TABLE `following_group` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` bigint DEFAULT NULL COMMENT '用户id',
  `name` varchar(50) DEFAULT NULL COMMENT '关注分组名称',
  `type` varchar(5) DEFAULT NULL COMMENT '关注分组类型：0特别关注  1悄悄关注 2默认分组  3用户自定义分组',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户关注分组表';


insert into member_level (id,name,growth_point,default_status,note) values
(0,'Lv0',0,1,"解锁滚动弹幕 视频投稿"),
(1,'Lv1',0,1,"解锁滚动弹幕 视频投稿"),
(2,'Lv2',200,0,"解锁高级弹幕 视频评论 彩色弹幕"),
(3,'Lv3',1500,0,"解锁顶部/底部弹幕 视频添加tag"),
(4,'Lv4',4500,0,"解锁视频删除标签"),
(5,'Lv5',10800,0,"可购买邀请码1个/月"),
(6,'Lv6',28800,0,"可购买邀请码2个/月");

insert into following_group (name,type) values ('特别关注',0),('悄悄关注',1),('默认分组',2);


