/*
Navicat MySQL Data Transfer

Source Server         : RDS_生产
Source Server Version : 50616
Source Host           : localhost:23306
Source Database       : ebweb

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2018-05-25 17:08:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_rolepri
-- ----------------------------
DROP TABLE IF EXISTS `tbl_rolepri`;
CREATE TABLE `tbl_rolepri` (
  `roleid` bigint(20) NOT NULL,
  `priid` bigint(20) NOT NULL,
  PRIMARY KEY (`roleid`,`priid`),
  KEY `userid` (`roleid`),
  KEY `priid` (`priid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tbl_rolepri
-- ----------------------------
INSERT INTO `tbl_rolepri` VALUES ('1', '1');
INSERT INTO `tbl_rolepri` VALUES ('1', '2');
INSERT INTO `tbl_rolepri` VALUES ('1', '5');
INSERT INTO `tbl_rolepri` VALUES ('1', '6');
INSERT INTO `tbl_rolepri` VALUES ('1', '7');
INSERT INTO `tbl_rolepri` VALUES ('1', '8');
INSERT INTO `tbl_rolepri` VALUES ('1', '11');
INSERT INTO `tbl_rolepri` VALUES ('1', '12');
INSERT INTO `tbl_rolepri` VALUES ('1', '13');
INSERT INTO `tbl_rolepri` VALUES ('1', '14');
INSERT INTO `tbl_rolepri` VALUES ('1', '15');
INSERT INTO `tbl_rolepri` VALUES ('1', '18');
INSERT INTO `tbl_rolepri` VALUES ('1', '19');
INSERT INTO `tbl_rolepri` VALUES ('1', '20');
INSERT INTO `tbl_rolepri` VALUES ('1', '21');
INSERT INTO `tbl_rolepri` VALUES ('1', '22');
INSERT INTO `tbl_rolepri` VALUES ('1', '23');
INSERT INTO `tbl_rolepri` VALUES ('1', '24');
INSERT INTO `tbl_rolepri` VALUES ('1', '30');
INSERT INTO `tbl_rolepri` VALUES ('1', '31');
INSERT INTO `tbl_rolepri` VALUES ('1', '35');
INSERT INTO `tbl_rolepri` VALUES ('1', '36');
INSERT INTO `tbl_rolepri` VALUES ('1', '37');
INSERT INTO `tbl_rolepri` VALUES ('1', '40');
INSERT INTO `tbl_rolepri` VALUES ('1', '45');
INSERT INTO `tbl_rolepri` VALUES ('1', '46');
INSERT INTO `tbl_rolepri` VALUES ('1', '47');
INSERT INTO `tbl_rolepri` VALUES ('1', '50');
INSERT INTO `tbl_rolepri` VALUES ('1', '51');
INSERT INTO `tbl_rolepri` VALUES ('1', '52');
INSERT INTO `tbl_rolepri` VALUES ('1', '58');
INSERT INTO `tbl_rolepri` VALUES ('1', '59');
INSERT INTO `tbl_rolepri` VALUES ('1', '60');
INSERT INTO `tbl_rolepri` VALUES ('1', '65');
INSERT INTO `tbl_rolepri` VALUES ('1', '66');
INSERT INTO `tbl_rolepri` VALUES ('1', '67');
INSERT INTO `tbl_rolepri` VALUES ('1', '70');
INSERT INTO `tbl_rolepri` VALUES ('1', '71');
INSERT INTO `tbl_rolepri` VALUES ('1', '72');
INSERT INTO `tbl_rolepri` VALUES ('1', '73');
INSERT INTO `tbl_rolepri` VALUES ('1', '78');
INSERT INTO `tbl_rolepri` VALUES ('1', '79');
INSERT INTO `tbl_rolepri` VALUES ('1', '80');
INSERT INTO `tbl_rolepri` VALUES ('1', '81');
INSERT INTO `tbl_rolepri` VALUES ('1', '82');
INSERT INTO `tbl_rolepri` VALUES ('1', '83');
INSERT INTO `tbl_rolepri` VALUES ('1', '90');
INSERT INTO `tbl_rolepri` VALUES ('1', '91');
INSERT INTO `tbl_rolepri` VALUES ('1', '92');
INSERT INTO `tbl_rolepri` VALUES ('1', '93');
INSERT INTO `tbl_rolepri` VALUES ('1', '140');
INSERT INTO `tbl_rolepri` VALUES ('1', '141');
INSERT INTO `tbl_rolepri` VALUES ('2', '1');
INSERT INTO `tbl_rolepri` VALUES ('2', '2');
INSERT INTO `tbl_rolepri` VALUES ('2', '5');
INSERT INTO `tbl_rolepri` VALUES ('2', '6');
INSERT INTO `tbl_rolepri` VALUES ('2', '7');
INSERT INTO `tbl_rolepri` VALUES ('2', '8');
INSERT INTO `tbl_rolepri` VALUES ('2', '11');
INSERT INTO `tbl_rolepri` VALUES ('2', '12');
INSERT INTO `tbl_rolepri` VALUES ('2', '13');
INSERT INTO `tbl_rolepri` VALUES ('2', '14');
INSERT INTO `tbl_rolepri` VALUES ('2', '15');
INSERT INTO `tbl_rolepri` VALUES ('2', '18');
INSERT INTO `tbl_rolepri` VALUES ('2', '19');
INSERT INTO `tbl_rolepri` VALUES ('2', '20');
INSERT INTO `tbl_rolepri` VALUES ('2', '21');
INSERT INTO `tbl_rolepri` VALUES ('2', '22');
INSERT INTO `tbl_rolepri` VALUES ('2', '23');
INSERT INTO `tbl_rolepri` VALUES ('2', '24');
INSERT INTO `tbl_rolepri` VALUES ('2', '30');
INSERT INTO `tbl_rolepri` VALUES ('2', '31');
INSERT INTO `tbl_rolepri` VALUES ('2', '35');
INSERT INTO `tbl_rolepri` VALUES ('2', '36');
INSERT INTO `tbl_rolepri` VALUES ('2', '37');
INSERT INTO `tbl_rolepri` VALUES ('2', '40');
INSERT INTO `tbl_rolepri` VALUES ('2', '45');
INSERT INTO `tbl_rolepri` VALUES ('2', '46');
INSERT INTO `tbl_rolepri` VALUES ('2', '47');
INSERT INTO `tbl_rolepri` VALUES ('2', '50');
INSERT INTO `tbl_rolepri` VALUES ('2', '51');
INSERT INTO `tbl_rolepri` VALUES ('2', '52');
INSERT INTO `tbl_rolepri` VALUES ('2', '58');
INSERT INTO `tbl_rolepri` VALUES ('2', '59');
INSERT INTO `tbl_rolepri` VALUES ('2', '60');
INSERT INTO `tbl_rolepri` VALUES ('2', '65');
INSERT INTO `tbl_rolepri` VALUES ('2', '66');
INSERT INTO `tbl_rolepri` VALUES ('2', '67');
INSERT INTO `tbl_rolepri` VALUES ('2', '70');
INSERT INTO `tbl_rolepri` VALUES ('2', '71');
INSERT INTO `tbl_rolepri` VALUES ('2', '72');
INSERT INTO `tbl_rolepri` VALUES ('2', '73');
INSERT INTO `tbl_rolepri` VALUES ('2', '78');
INSERT INTO `tbl_rolepri` VALUES ('2', '79');
INSERT INTO `tbl_rolepri` VALUES ('2', '80');
INSERT INTO `tbl_rolepri` VALUES ('2', '81');
INSERT INTO `tbl_rolepri` VALUES ('2', '82');
INSERT INTO `tbl_rolepri` VALUES ('2', '83');
INSERT INTO `tbl_rolepri` VALUES ('2', '90');
INSERT INTO `tbl_rolepri` VALUES ('2', '91');
INSERT INTO `tbl_rolepri` VALUES ('2', '92');
INSERT INTO `tbl_rolepri` VALUES ('2', '93');
INSERT INTO `tbl_rolepri` VALUES ('6', '5');
INSERT INTO `tbl_rolepri` VALUES ('6', '6');
INSERT INTO `tbl_rolepri` VALUES ('6', '7');
INSERT INTO `tbl_rolepri` VALUES ('6', '8');
INSERT INTO `tbl_rolepri` VALUES ('14', '5');
INSERT INTO `tbl_rolepri` VALUES ('14', '6');
INSERT INTO `tbl_rolepri` VALUES ('14', '7');
INSERT INTO `tbl_rolepri` VALUES ('14', '8');
INSERT INTO `tbl_rolepri` VALUES ('14', '11');
INSERT INTO `tbl_rolepri` VALUES ('14', '13');
INSERT INTO `tbl_rolepri` VALUES ('15', '5');
INSERT INTO `tbl_rolepri` VALUES ('15', '6');
INSERT INTO `tbl_rolepri` VALUES ('15', '8');
INSERT INTO `tbl_rolepri` VALUES ('15', '35');
INSERT INTO `tbl_rolepri` VALUES ('15', '50');
INSERT INTO `tbl_rolepri` VALUES ('15', '51');
INSERT INTO `tbl_rolepri` VALUES ('15', '52');
