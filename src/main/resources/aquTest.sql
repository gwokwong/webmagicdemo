/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50542
 Source Host           : localhost:3306
 Source Schema         : aquTest

 Target Server Type    : MySQL
 Target Server Version : 50542
 File Encoding         : 65001

 Date: 04/11/2017 14:39:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for film
-- ----------------------------
DROP TABLE IF EXISTS `film`;
CREATE TABLE `film` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `film_name` varchar(200) NOT NULL,
  `bt_url` varchar(400) NOT NULL,
  `img_url` longtext NOT NULL,
  `fid` int(4) NOT NULL,
  `source_url` varchar(200) NOT NULL DEFAULT '',
  `actor` varchar(100) NOT NULL DEFAULT '' COMMENT '演员',
  `sn` varchar(50) NOT NULL DEFAULT '' COMMENT '番号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=87072 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
