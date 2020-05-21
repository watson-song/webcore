# ************************************************************
# Sequel Pro SQL dump
# Version 4644
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 192.168.3.173 (MySQL 5.7.27-0ubuntu0.18.04.1)
# Database: ???
# Generation Time: 2020-05-21 07:49:21 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table ref_admin_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ref_admin_role`;

CREATE TABLE `ref_admin_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `admin_id` bigint(20) NOT NULL,
  `created_by` bigint(16) DEFAULT NULL,
  `created_by_name` varchar(20) DEFAULT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_admin_role` (`role_id`,`admin_id`),
  KEY `admin_id` (`admin_id`),
  CONSTRAINT `ref_admin_role_ibfk_2` FOREIGN KEY (`admin_id`) REFERENCES `tb_admin` (`id`) ON DELETE CASCADE,
  CONSTRAINT `ref_admin_role_ibfk_3` FOREIGN KEY (`role_id`) REFERENCES `tb_role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

LOCK TABLES `ref_admin_role` WRITE;
/*!40000 ALTER TABLE `ref_admin_role` DISABLE KEYS */;

INSERT INTO `ref_admin_role` (`id`, `role_id`, `admin_id`, `created_by`, `created_by_name`, `created_time`)
VALUES
	(1,2,1,NULL,'Watson','2020-04-20 00:50:04'),
	(2,1,1,NULL,'Watson','2020-04-20 00:51:51');

/*!40000 ALTER TABLE `ref_admin_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table ref_role_permission
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ref_role_permission`;

CREATE TABLE `ref_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `created_by` bigint(16) DEFAULT NULL,
  `created_by_name` varchar(20) DEFAULT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `permission_id` (`permission_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `ref_role_permission_ibfk_1` FOREIGN KEY (`permission_id`) REFERENCES `tb_permission` (`id`),
  CONSTRAINT `ref_role_permission_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `tb_role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tb_access_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_access_log`;

CREATE TABLE `tb_access_log` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `level` varchar(45) NOT NULL DEFAULT '0' COMMENT '日志级别',
  `title` varchar(65) NOT NULL COMMENT '日志标题',
  `method` varchar(20) DEFAULT '0' COMMENT '请求方式',
  `url` varchar(255) NOT NULL DEFAULT '' COMMENT '用户openid',
  `params` varchar(255) NOT NULL DEFAULT '' COMMENT '提交参数',
  `exception` varchar(1000) DEFAULT '' COMMENT '异常',
  `ip` varchar(25) DEFAULT NULL COMMENT '请求ip地址',
  `total_times` bigint(16) DEFAULT NULL COMMENT '总消耗时间',
  `db_times` bigint(16) DEFAULT NULL COMMENT '数据库访问时间',
  `version` int(11) DEFAULT '1' COMMENT '版本号',
  `created_by` bigint(16) DEFAULT NULL COMMENT '创建人ID',
  `created_by_name` varchar(45) DEFAULT NULL COMMENT '创建人名称',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_by` bigint(16) DEFAULT NULL COMMENT '最后更新人ID',
  `modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table tb_admin
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_admin`;

CREATE TABLE `tb_admin` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `no` varchar(12) DEFAULT NULL COMMENT '编号',
  `username` varchar(65) NOT NULL COMMENT '用户名',
  `nick_name` varchar(65) DEFAULT NULL COMMENT '昵称',
  `gender` varchar(10) DEFAULT '0' COMMENT '性别，0未知，1男，2女',
  `type` int(11) NOT NULL DEFAULT '2' COMMENT '账号类型：1管理员，2运营，3财务',
  `avatar_url` varchar(255) DEFAULT '/images/user.png' COMMENT '头像',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(45) DEFAULT NULL COMMENT '邮箱',
  `address` varchar(65) DEFAULT NULL COMMENT '地址',
  `is_weblogin_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用web登录',
  `password` varchar(64) NOT NULL DEFAULT '' COMMENT '加密密码',
  `expired` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已过期',
  `locked` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已锁定',
  `credentials_expired` tinyint(1) NOT NULL DEFAULT '0' COMMENT '密码是否已过期',
  `login_ip` varchar(45) DEFAULT NULL COMMENT '最后登录ip',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(45) DEFAULT NULL COMMENT '上次登录ip',
  `last_login_date` datetime DEFAULT NULL COMMENT '上次登录日期',
  `extra_data` json DEFAULT NULL COMMENT '微信额外信息',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否已启用',
  `version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
  `created_by` bigint(16) DEFAULT NULL COMMENT '创建人ID',
  `created_by_name` varchar(45) DEFAULT NULL COMMENT '创建人名称',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_by` bigint(16) DEFAULT NULL COMMENT '最后更新人ID',
  `modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `no` (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

LOCK TABLES `tb_admin` WRITE;
/*!40000 ALTER TABLE `tb_admin` DISABLE KEYS */;

INSERT INTO `tb_admin` (`id`, `no`, `username`, `nick_name`, `gender`, `type`, `avatar_url`, `mobile`, `email`, `address`, `is_weblogin_active`, `password`, `expired`, `locked`, `credentials_expired`, `login_ip`, `login_date`, `last_login_ip`, `last_login_date`, `extra_data`, `enabled`, `version`, `created_by`, `created_by_name`, `created_time`, `modified_by`, `modified_time`)
VALUES
	(1,'001','admin','Watson Song','0',2,'/images/user.png','111111111',NULL,NULL,1,'$2a$10$tey2wsYtP9hThflUj9Zaa.obUx.P8rDK2DdaS64ArZcL2B34kD4MO',0,0,0,NULL,NULL,NULL,NULL,NULL,1,1,NULL,NULL,'2020-04-19 23:09:00',NULL,'2020-05-21 15:48:37');

/*!40000 ALTER TABLE `tb_admin` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table tb_admin_message
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_admin_message`;

CREATE TABLE `tb_admin_message` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(16) NOT NULL DEFAULT '0' COMMENT '管理员id',
  `type` varchar(30) NOT NULL DEFAULT '' COMMENT '消息类型',
  `title` varchar(50) NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(1000) DEFAULT NULL COMMENT '内容',
  `state` varchar(20) NOT NULL DEFAULT 'normal' COMMENT '状态',
  `state_desc` varchar(20) NOT NULL DEFAULT '正常' COMMENT '状态名称',
  `extra_data` json DEFAULT NULL COMMENT '额外数据',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
  `created_by` bigint(16) DEFAULT NULL COMMENT '创建人ID',
  `created_by_name` varchar(45) DEFAULT NULL COMMENT '创建人名称',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_by` bigint(16) DEFAULT NULL COMMENT '最后更新人ID',
  `modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `tb_admin_message_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `tb_admin` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table tb_nohelper
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_nohelper`;

CREATE TABLE `tb_nohelper` (
  `id` varchar(25) NOT NULL DEFAULT '_no' COMMENT '业务代码',
  `no` bigint(16) NOT NULL DEFAULT '0' COMMENT '自增编号',
  `version` bigint(16) NOT NULL DEFAULT '1' COMMENT '数据版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tb_permission
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_permission`;

CREATE TABLE `tb_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(140) DEFAULT NULL,
  `name` varchar(32) NOT NULL,
  `parent_id` bigint(11) DEFAULT NULL,
  `weight` int(11) NOT NULL,
  `enabled` tinyint(1) DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT '1',
  `created_by` bigint(16) DEFAULT NULL,
  `created_by_name` varchar(20) DEFAULT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` bigint(16) DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_NAME` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

LOCK TABLES `tb_permission` WRITE;
/*!40000 ALTER TABLE `tb_permission` DISABLE KEYS */;

INSERT INTO `tb_permission` (`id`, `description`, `name`, `parent_id`, `weight`, `enabled`, `version`, `created_by`, `created_by_name`, `created_time`, `modified_by`, `modified_time`)
VALUES
	(1,'所有菜单','*_menu',NULL,0,NULL,1,NULL,'watson','2020-04-14 13:03:42',NULL,NULL);

/*!40000 ALTER TABLE `tb_permission` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table tb_refreshtoken
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_refreshtoken`;

CREATE TABLE `tb_refreshtoken` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `refresh_token` varchar(24) DEFAULT NULL COMMENT '刷新令牌',
  `expire_time` timestamp NULL DEFAULT NULL COMMENT '过期时间',
  `user_type` enum('user','admin','worker') DEFAULT 'worker' COMMENT '用户类型',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0禁用，1可以',
  `version` int(11) DEFAULT '1' COMMENT '版本',
  `created_by` bigint(16) NOT NULL COMMENT '创建人',
  `created_by_name` varchar(45) DEFAULT NULL COMMENT '创建人名称',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` bigint(16) DEFAULT NULL COMMENT '更新人',
  `modified_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `refresh_token` (`refresh_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tb_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_role`;

CREATE TABLE `tb_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(140) DEFAULT NULL,
  `name` varchar(32) NOT NULL,
  `tag` varchar(20) DEFAULT NULL,
  `status` int(11) NOT NULL COMMENT '0禁用，1可以',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '0系统自带，1用户创建的',
  `enabled` tinyint(1) DEFAULT NULL,
  `version` int(11) DEFAULT NULL COMMENT '版本号',
  `created_by` bigint(16) DEFAULT NULL,
  `created_by_name` varchar(20) DEFAULT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` bigint(16) DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

LOCK TABLES `tb_role` WRITE;
/*!40000 ALTER TABLE `tb_role` DISABLE KEYS */;

INSERT INTO `tb_role` (`id`, `description`, `name`, `tag`, `status`, `type`, `enabled`, `version`, `created_by`, `created_by_name`, `created_time`, `modified_by`, `modified_time`)
VALUES
	(1,'超级管理员','admin','backend',1,0,NULL,NULL,0,NULL,'2020-04-20 00:48:28',NULL,NULL),
	(2,'运维人员','operator','backend',1,0,NULL,NULL,0,NULL,'2020-04-20 00:48:28',NULL,NULL),
	(3,'用户','user','backend',1,0,NULL,NULL,0,NULL,'2020-04-20 00:48:28',NULL,'2020-05-21 15:48:08');

/*!40000 ALTER TABLE `tb_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table tb_site_option
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_site_option`;

CREATE TABLE `tb_site_option` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '名称',
  `_key` varchar(140) NOT NULL DEFAULT '' COMMENT '键',
  `value` varchar(500) NOT NULL DEFAULT '' COMMENT '值',
  `type` enum('wxapp','admin','app','backend','backendWxapp','backendApp','backendAdmin') NOT NULL DEFAULT 'wxapp' COMMENT '参数类型',
  `builtin` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否内置',
  `name_editable` tinyint(1) DEFAULT '0' COMMENT '是否可修改名称',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用',
  `version` int(11) DEFAULT NULL COMMENT '版本号',
  `created_by` bigint(16) DEFAULT NULL,
  `created_by_name` varchar(20) DEFAULT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` bigint(16) DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `_key` (`_key`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tb_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `openid` varchar(32) NOT NULL DEFAULT '0' COMMENT '微信openid',
  `username` varchar(65) DEFAULT NULL COMMENT '用户名',
  `nick_name` varchar(65) DEFAULT NULL COMMENT '昵称',
  `gender` varchar(10) DEFAULT '0' COMMENT '性别，0未知，1男，2女',
  `version` int(11) DEFAULT '1' COMMENT '版本号',
  `created_by` bigint(16) DEFAULT NULL COMMENT '创建人ID',
  `created_by_name` varchar(45) DEFAULT NULL COMMENT '创建人名称',
  `modified_by` bigint(16) DEFAULT NULL COMMENT '最后更新人ID',
  `modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `language` varchar(10) DEFAULT '' COMMENT '语言',
  `city` varchar(25) DEFAULT '' COMMENT '城市',
  `province` varchar(20) DEFAULT '' COMMENT '省',
  `country` varchar(40) DEFAULT NULL COMMENT '国家',
  `avatar_url` varchar(255) DEFAULT '/images/user.png' COMMENT '头像',
  `session_key` varchar(65) DEFAULT NULL COMMENT 'sessionkey',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(45) DEFAULT NULL COMMENT '邮箱',
  `address` varchar(65) DEFAULT NULL COMMENT '地址',
  `appid` varchar(45) DEFAULT NULL COMMENT 'appid',
  `unionid` varchar(45) DEFAULT NULL COMMENT 'unionid',
  `is_subscribe` tinyint(1) DEFAULT NULL COMMENT '是否订阅了公众号',
  `subscribe_time` bigint(16) DEFAULT NULL COMMENT '公众号订阅时间',
  `subscribe_scene` varchar(45) DEFAULT NULL COMMENT '公众号订阅场景值',
  `wx_remark` varchar(60) DEFAULT NULL COMMENT '微信备注',
  `wx_tag_ids` varchar(200) DEFAULT NULL COMMENT '微信tagids',
  `logged` tinyint(1) DEFAULT NULL COMMENT '是否已授权获取头像',
  `is_weblogin_active` tinyint(1) DEFAULT NULL COMMENT '是否启用web登录',
  `lat` double(11,7) DEFAULT NULL COMMENT '经度',
  `lng` double(11,7) DEFAULT NULL COMMENT '纬度',
  `os_tags` varchar(200) DEFAULT NULL COMMENT '系统标签',
  `device_tags` varchar(200) DEFAULT NULL COMMENT '设备标签',
  `admin_tags` varchar(200) DEFAULT NULL COMMENT '管理员设置标签',
  `password` varchar(64) DEFAULT NULL COMMENT '加密密码',
  `expired` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已过期',
  `locked` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已锁定',
  `credentials_expired` tinyint(1) NOT NULL DEFAULT '0' COMMENT '密码是否已过期',
  `enabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已启用',
  `login_ip` varchar(45) DEFAULT NULL COMMENT '最后登录ip',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(45) DEFAULT NULL COMMENT '上次登录ip',
  `last_login_date` datetime DEFAULT NULL COMMENT '上次登录日期',
  `extra_data` json DEFAULT NULL COMMENT '微信额外信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `openid_UNIQUE` (`openid`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table tb_user_feedback
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_user_feedback`;

CREATE TABLE `tb_user_feedback` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(16) NOT NULL,
  `user_type` enum('user','worker') DEFAULT 'user' COMMENT '用户类型',
  `image1_url` varchar(250) DEFAULT NULL COMMENT '图片1',
  `image2_url` varchar(250) DEFAULT NULL COMMENT '图片2',
  `image3_url` varchar(250) DEFAULT NULL COMMENT '图片3',
  `content` varchar(2000) NOT NULL DEFAULT '' COMMENT '反馈内容',
  `telephone` varchar(100) DEFAULT NULL COMMENT '联系方式',
  `order_id` bigint(16) DEFAULT NULL COMMENT '订单号',
  `state` varchar(45) DEFAULT '已提交',
  `reviewer_id` bigint(16) DEFAULT NULL COMMENT '审核人',
  `review_notes` varchar(1000) DEFAULT NULL COMMENT '后台审核反馈内容',
  `review_time` datetime DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `created_by` bigint(16) DEFAULT NULL COMMENT '创建人',
  `created_by_name` varchar(45) DEFAULT NULL COMMENT '创建人名称',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` bigint(16) DEFAULT NULL COMMENT '修改人',
  `modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table tb_user_message
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_user_message`;

CREATE TABLE `tb_user_message` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(16) NOT NULL DEFAULT '0' COMMENT '用户id',
  `type` varchar(30) NOT NULL DEFAULT '' COMMENT '消息类型, system/order',
  `title` varchar(50) NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(1000) DEFAULT NULL COMMENT '内容',
  `state` enum('unread','read') NOT NULL DEFAULT 'unread' COMMENT '状态',
  `state_desc` varchar(20) NOT NULL DEFAULT '正常' COMMENT '状态名称',
  `extra_data` json DEFAULT NULL COMMENT '额外数据',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  `version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
  `created_by` bigint(16) DEFAULT NULL COMMENT '创建人ID',
  `created_by_name` varchar(45) DEFAULT NULL COMMENT '创建人名称',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `modified_by` bigint(16) DEFAULT NULL COMMENT '最后更新人ID',
  `modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `tb_user_message_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
