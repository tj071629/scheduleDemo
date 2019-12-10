/*
Navicat MySQL Data Transfer

Source Server         : RDS_生产
Source Server Version : 50616
Source Host           : localhost:23306
Source Database       : ebweb

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2018-05-25 17:09:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_admin
-- ----------------------------
DROP TABLE IF EXISTS `tbl_admin`;
CREATE TABLE `tbl_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `pwd` varchar(80) NOT NULL,
  `depid` bigint(20) DEFAULT NULL,
  `sex` tinyint(2) NOT NULL DEFAULT '1',
  `email` varchar(60) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `type` tinyint(2) NOT NULL DEFAULT '1' COMMENT 'type ：  1 系统管理员； 2 渠道销售',
  `enabled` tinyint(2) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tbl_admin
-- ----------------------------
INSERT INTO `tbl_admin` VALUES ('1', 'fjx', '鑫哥', '430c6094a16bde155492946b3e53c733', '-1', '1', '', '', '1', '1');
INSERT INTO `tbl_admin` VALUES ('2', 'ytz', '杨天治', 'f468802f9c993b81b73bba266c3fd20a', '-1', '1', '', '', '1', '1');
INSERT INTO `tbl_admin` VALUES ('3', 'yx', 'yx', 'f468802f9c993b81b73bba266c3fd20a', '-1', '1', '', '', '1', '1');
INSERT INTO `tbl_admin` VALUES ('4', 'fjx1', '鑫哥1', '430c6094a16bde155492946b3e53c733', '-1', '1', 'fengjinxin', '13810654403', '1', '0');
INSERT INTO `tbl_admin` VALUES ('5', 'sff', '飞飞', '625e0243ed1f5e4b3e9e5dff96675757', '2', '1', null, null, '1', '1');
INSERT INTO `tbl_admin` VALUES ('6', 'xmx', '明星', 'f468802f9c993b81b73bba266c3fd20a', '2', '1', null, null, '1', '1');
INSERT INTO `tbl_admin` VALUES ('7', 'yyh', '严欢', '8c07bec2e269f3d7737d3a1f6bc213a7', '-1', '1', 'yanhuan0806@126.com', '18621760390', '1', '1');
INSERT INTO `tbl_admin` VALUES ('11', 'gc', 'gc', 'f468802f9c993b81b73bba266c3fd20a', '-1', '1', '', '', '1', '1');
INSERT INTO `tbl_admin` VALUES ('12', 'hkoffice', '香港办公室', '6933671c560b9722eb131829cc0b3ec4', '2', '1', null, null, '1', '1');
INSERT INTO `tbl_admin` VALUES ('13', '13212740682', '13212740682', 'abecffde0ce340c5425791ee1828faa4', '-1', '1', '', '', '1', '1');
INSERT INTO `tbl_admin` VALUES ('55', 'bx', '白雪', '6933671c560b9722eb131829cc0b3ec4', '-1', '2', 'yanhuan0806@126.com', '18621760390', '2', '1');
INSERT INTO `tbl_admin` VALUES ('58', 'test2', 'test2', '6933671c560b9722eb131829cc0b3ec4', '-1', '1', 'yanhuan0806@126.com', '', '2', '1');
INSERT INTO `tbl_admin` VALUES ('59', 'test3', 'test3', '6933671c560b9722eb131829cc0b3ec4', '-1', '1', 'yanhuan0806@126.com', '13522265544', '1', '1');
