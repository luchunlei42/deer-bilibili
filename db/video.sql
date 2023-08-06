drop table if exists `tag`;

drop table if exists category;

drop table if exists video;

drop table if exists category_tag_relation;

drop table if exists video_tag_relation;

drop table if exists video_detail;

/*==============================================================*/
/* Table: video                                        */
/*==============================================================*/
create table video
(
   id                   bigint not null auto_increment comment '商品id',
   member_id            bigint comment '用户id',
   video_title             varchar(200) comment '视频标题',
   video_description      varchar(1000) comment '视频简介',
   img_url              varchar(255) comment '封面地址',
   catalog_id           bigint comment '分区',
   type                 int comment  '自制/转载',
   scheduled             int comment '是否定时发布',
   weight               decimal(18,4),
   publish_status       tinyint comment '上架状态[0 - 下架，1 - 上架]',
   create_time          datetime,
   update_time          datetime,
   duration				bigint comment '视频时长单位s',
   primary key (id)
);

alter table video comment '视频';
/*==============================================================*/
/* Table: category                                          */
/*==============================================================*/
create table category
(
   cat_id               bigint not null auto_increment comment '分类id',
   name                 char(50) comment '分类名称',
   `code`               char(50) comment '代号',
   parent_cid           bigint comment '父分类id',
   cat_level            int comment '层级',
   show_status          tinyint comment '是否显示[0-不显示，1显示]',
   sort                 int comment '排序',
   icon                 char(255) comment '图标地址',
   primary key (cat_id)
);

alter table `category` comment '分区';
/*==============================================================*/
/* Table: tag                                             */
/*==============================================================*/
create table  `tag`
(
   tag_id             bigint not null auto_increment comment '标签id',
   name                 char(50) comment '标签名',
   descript             longtext comment '介绍',
   show_status          tinyint comment '显示状态[0-不显示；1-显示]',
   first_letter         char(1) comment '检索首字母',
   sort                 int comment '排序',
   primary key (tag_id)
);

alter table `tag` comment '标签';

/*==============================================================*/
/* Table: category_tag_relation                           */
/*==============================================================*/
create table category_tag_relation
(
   id                   bigint not null auto_increment,
   tag_id             bigint comment '品牌id',
   catelog_id           bigint comment '分类id',
   tag_name           varchar(255),
   catelog_name         varchar(255),
   primary key (id)
);

alter table category_tag_relation comment '便签分区关联';

/*==============================================================*/
/* Table: category_tag_relation                           */
/*==============================================================*/
create table video_tag_relation
(
   id                   bigint not null auto_increment,
   tag_id             bigint comment '标签id',
   video_id           bigint comment '视频id',
   tag_name           varchar(255),
   primary key (id)
);

alter table video_tag_relation comment '便签视频关联';

/*==============================================================*/
/* Table: category_tag_relation                           */
/*==============================================================*/
create table video_detail
(
   id                   bigint not null auto_increment,
   video_id           bigint comment '视频id',
   bucket_name           varchar(255),
   location				varchar(255),
   `video_key`			varchar(255),
   primary key (id),
   INDEX video_id (video_id)
);

alter table video_detail comment '视频储存地址';

/*==============================================================*/
/* Table: category_tag_relation                           */
/*==============================================================*/
create table video_stat
(
   id                   bigint not null auto_increment,
   video_id           	bigint comment '视频id',
   `view`				bigint comment '播放数',
   `danmaku`			bigint comment '弹幕数',
   `reply`				bigint comment '评论数',
   `favorite`			bigint comment '收藏数',
   `like`				bigint comment '受赞次数',
   primary key (id),
   INDEX video_id (video_id)
);

alter table video_stat comment '视频流量数据';

insert into category (name,code,parent_cid,cat_level,show_status) values( '动画','douga',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '番剧','anime',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '音乐','music',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '舞蹈','dance',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '游戏','game',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '知识','knowledge',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '运动','sports',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '汽车','car',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '生活','life',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '美食','food',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '动物圈','animal',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '鬼畜','kichiku',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '资讯','information',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '时尚','fashion',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '娱乐','ent',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '影视','cinephile',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '纪录片','documentary',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '电影','movie',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '电视剧','tv',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '国创','guochuang',0,1,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '科技','tech',0,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( 'MAD·AMV','mad',1,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( 'MMD·3D','mmd',1,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '短片·手书·配音','voice',1,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '手办·模玩','garage_kit',1,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '特摄','tokusatsu',1,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '动漫杂谈','acgntalks',1,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '动画综合','other',1,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '资讯','information',2,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '官方延伸','offical',2,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '完结动画','finish',2,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '连载动画','serial',2,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '原创音乐','original',3,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '翻唱','cover',3,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( 'VOCALOID·UTAU','vocaloid',3,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '演奏','perform',3,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( 'MV','mv',3,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '音乐现场','live',3,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '音乐综合','other',3,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '乐评盘点','commentary',3,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '宅舞','otaku',4,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '舞蹈综合','three_d',4,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '舞蹈教程','demo',4,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '街舞','hiphop',4,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '明星舞蹈','star',4,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '中国舞','china',4,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '单机游戏','stand_alone',5,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '电子竞技','esports',5,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '手机游戏','mobile',5,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '网络游戏','online',5,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '桌游棋牌','board',5,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( 'GMV','gmv',5,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '音游','music',5,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( 'Mugen','mugen',5,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '科学科普','science',6,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '社科·法律·心理','social_science',6,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '人文历史','humanity_history',6,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '财经商业','business',6,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '校园学习','campus',6,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '职业职场','career',6,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '设计·创意','design',6,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '野生技术协会','skill',6,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '篮球','basketball',7,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '足球','football',7,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '健身','aerobics',7,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '竞技体育','athletic',7,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '运动文化','culture',7,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '运动综合','comprehensive',7,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '赛车','racing',8,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '改装玩车','modifiedvehicle',8,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '新能源车','newenergyvehicle',8,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '房车','touringcar',8,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '摩托车','motorcycle',8,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '购车攻略','strategy',8,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '汽车生活','life',8,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '搞笑','funny',9,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '出行','travel',9,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '三农','rurallife',9,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '家居房产','home',9,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '手工','handmake',9,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '绘画','painting',9,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '日常','daily',9,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '美食制作','make',10,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '美食侦探','detective',10,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '美食测评','measurement',10,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '田园美食','rural',10,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '美食记录','record',10,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '喵星人','cat',11,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '汪星人','dog',11,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '大熊猫','panda',11,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '野生动物','wild_animal',11,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '爬宠','reptiles',11,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '动物综合','animal_composite',11,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '鬼畜调教','guide',12,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '音MAD','mad',12,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '人力VOCALOID','manual_vocaloid',12,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '鬼畜剧场','theatre',12,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '教程演示','course',12,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '美妆护肤','makeup',13,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '仿妆cos','cos',13,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '穿搭','clothing',13,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '时尚潮流','catwalk',13,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '热点','hotspot',14,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '环球','global',14,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '社会','social',14,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '综合','multiple',14,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '资讯综艺','variety',15,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '娱乐杂谈','talker',15,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '粉丝创作','fans',15,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '明星综合','celebrity',15,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '影视杂谈','cinecism',16,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '影视剪辑','montage',16,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '小剧场','shortfilm',16,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '预告·资讯','trailer_info',16,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '人文·历史','history',17,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '科学·探索·自然','science',17,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '军事','military',17,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '社会·美食·旅行','travel',17,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '华语电影','chinese',18,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '欧美电影','west',18,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '日本电影','japan',18,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '其他国家','movie',18,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '国产剧','mainland',19,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '海外剧','overseas',19,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '国产动画','chinese',20,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '国产原创相关','original',20,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '布袋戏','puppetry',20,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '资讯','information',20,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '动态漫·广播剧','motioncomic',20,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '数码','digital',21,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '软件应用','application',21,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '计算机技术','computer_tech',21,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '科工机械 ','industry',21,2,1);
insert into category (name,code,parent_cid,cat_level,show_status) values( '极客DIY','diy',21,2,1);

-- INSERT INTO `bili_videodb`.`tag` (`name`, `descript`, `show_status`, `first_letter`, `sort`) VALUES ('王者荣耀', '王者荣耀是一款..', '1', 'w', '1');
-- INSERT INTO `bili_videodb`.`tag` (`name`, `descript`, `show_status`, `first_letter`, `sort`) VALUES ('MOBA', 'MOBA 即 Multiplayer Online Battle Arena：多人在线战斗竞技场游戏', '1', 'm', '2');
-- INSERT INTO `bili_videodb`.`tag` (`name`, `descript`, `show_status`, `first_letter`, `sort`) VALUES ('电子竞技', '电子竞技也被称为电竞或eSports，是一种电子游戏的竞技活动，玩家在这里与其他人或团队对 …', '1', 'd', '3');

-- INSERT INTO `bili_videodb`.`category_tag_relation` (`tag_id`, `catelog_id`, `tag_name`, `catelog_name`) VALUES ('1', '49', '王者荣耀', '手机游戏');
-- INSERT INTO `bili_videodb`.`category_tag_relation` (`tag_id`, `catelog_id`, `tag_name`, `catelog_name`) VALUES ('2', '49', 'MOBA', '手机游戏');
-- INSERT INTO `bili_videodb`.`category_tag_relation` (`tag_id`, `catelog_id`, `tag_name`, `catelog_name`) VALUES ('3', '49', '电子竞技', '手机游戏');
