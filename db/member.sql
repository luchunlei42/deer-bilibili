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
   id                   bigint not null auto_increment comment 'id',
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