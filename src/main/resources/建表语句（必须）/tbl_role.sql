/*
Navicat MySQL Data Transfer

Source Server         : RDS_生产
Source Server Version : 50616
Source Host           : localhost:23306
Source Database       : ebweb

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2018-05-25 17:08:46
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_role
-- ----------------------------
DROP TABLE IF EXISTS `tbl_role`;
CREATE TABLE `tbl_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tbl_role
-- ----------------------------
INSERT INTO `tbl_role` VALUES ('15', 'test');
INSERT INTO `tbl_role` VALUES ('3', '一审人员');
INSERT INTO `tbl_role` VALUES ('4', '二审人员');
INSERT INTO `tbl_role` VALUES ('14', '外部运营人员');
INSERT INTO `tbl_role` VALUES ('2', '开发人员');
INSERT INTO `tbl_role` VALUES ('1', '系统管理员');
INSERT INTO `tbl_role` VALUES ('6', '运营人员');
INSERT INTO `tbl_role` VALUES ('5', '验收人员');
