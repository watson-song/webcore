# ************************************************************
# Sequel Pro SQL dump
# Version 4644
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.10)
# Database: template_project
# Generation Time: 2020-11-30 04:36:25 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table tb_admin
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_admin`;

CREATE TABLE `tb_admin` (
  `id` bigint(18) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(200) DEFAULT '',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint(18) DEFAULT NULL,
  `created_by_name` varchar(20) DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modified_by` bigint(18) DEFAULT NULL,
  `version` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `tb_admin` WRITE;
/*!40000 ALTER TABLE `tb_admin` DISABLE KEYS */;

INSERT INTO `tb_admin` (`id`, `username`, `password`, `created_time`, `created_by`, `created_by_name`, `modified_time`, `modified_by`, `version`)
VALUES
	(1,'admin','$2a$10$xX5xsw1p7BxBhKjvRtzSCOLIjK.dVUurMjRHN55Gj9X/mFH39nSkK','2020-11-30 12:27:51',1,'watson','2020-11-30 12:33:01',NULL,1);

/*!40000 ALTER TABLE `tb_admin` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table tb_article
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_article`;

CREATE TABLE `tb_article` (
  `id` bigint(18) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) DEFAULT NULL,
  `content` text,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint(18) DEFAULT NULL,
  `created_by_name` varchar(20) DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modified_by` bigint(18) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tb_category
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_category`;

CREATE TABLE `tb_category` (
  `id` bigint(18) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `label` varchar(20) DEFAULT NULL,
  `extra_data` json DEFAULT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint(18) DEFAULT NULL,
  `created_by_name` varchar(20) DEFAULT NULL,
  `modified_by` bigint(18) DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
