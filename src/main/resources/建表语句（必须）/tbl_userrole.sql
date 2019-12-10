/*
Navicat MySQL Data Transfer

Source Server         : RDS_生产
Source Server Version : 50616
Source Host           : localhost:23306
Source Database       : ebweb

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2018-05-25 17:09:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_userrole
-- ----------------------------
DROP TABLE IF EXISTS `tbl_userrole`;
CREATE TABLE `tbl_userrole` (
  `userid` bigint(20) NOT NULL AUTO_INCREMENT,
  `roleid` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`userid`,`roleid`),
  KEY `userid` (`userid`),
  KEY `roleid` (`roleid`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tbl_userrole
-- ----------------------------
INSERT INTO `tbl_userrole` VALUES ('-1', '14');
INSERT INTO `tbl_userrole` VALUES ('-1', '15');
INSERT INTO `tbl_userrole` VALUES ('1', '1');
INSERT INTO `tbl_userrole` VALUES ('1', '2');
INSERT INTO `tbl_userrole` VALUES ('1', '3');
INSERT INTO `tbl_userrole` VALUES ('1', '4');
INSERT INTO `tbl_userrole` VALUES ('1', '5');
INSERT INTO `tbl_userrole` VALUES ('2', '1');
INSERT INTO `tbl_userrole` VALUES ('3', '1');
INSERT INTO `tbl_userrole` VALUES ('5', '1');
INSERT INTO `tbl_userrole` VALUES ('6', '1');
INSERT INTO `tbl_userrole` VALUES ('7', '1');
INSERT INTO `tbl_userrole` VALUES ('11', '1');
INSERT INTO `tbl_userrole` VALUES ('12', '1');
INSERT INTO `tbl_userrole` VALUES ('13', '6');
INSERT INTO `tbl_userrole` VALUES ('42', '14');
INSERT INTO `tbl_userrole` VALUES ('43', '15');
INSERT INTO `tbl_userrole` VALUES ('51', '14');
INSERT INTO `tbl_userrole` VALUES ('51', '15');
INSERT INTO `tbl_userrole` VALUES ('55', '15');
INSERT INTO `tbl_userrole` VALUES ('58', '14');
INSERT INTO `tbl_userrole` VALUES ('59', '14');
