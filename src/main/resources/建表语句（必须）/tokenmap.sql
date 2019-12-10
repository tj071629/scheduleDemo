/*
Navicat MySQL Data Transfer

Source Server         : RDS_生产
Source Server Version : 50616
Source Host           : localhost:23306
Source Database       : ebweb

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2018-05-25 17:09:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tokenmap
-- ----------------------------
DROP TABLE IF EXISTS `tokenmap`;
CREATE TABLE `tokenmap` (
  `userid` bigint(20) NOT NULL,
  `token` varchar(60) NOT NULL,
  `times` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`userid`),
  UNIQUE KEY `token` (`token`) USING BTREE,
  KEY `times` (`times`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tokenmap
-- ----------------------------
INSERT INTO `tokenmap` VALUES ('1', 'ae6c35fb-17d7-4799-816e-a5d0b1f1651a', '2018-02-02 09:29:25');
INSERT INTO `tokenmap` VALUES ('2', 'b22db8af-d090-4141-b27e-3ff3e3074ec4', '2018-05-21 20:07:24');
INSERT INTO `tokenmap` VALUES ('3', '265e6f08-5981-4b01-b423-c8ac10c66d07', '2018-05-24 09:13:12');
INSERT INTO `tokenmap` VALUES ('5', '56cfdaf1-a96a-436e-8d9d-80a655a711a5', '2018-05-25 13:59:06');
INSERT INTO `tokenmap` VALUES ('6', 'e6fa118c-249d-4fd6-98a3-7de5a2a17ac1', '2018-05-22 17:04:42');
INSERT INTO `tokenmap` VALUES ('7', 'f4d4ad84-0234-4974-8545-f1e6d9e8e904', '2018-05-25 17:04:59');
INSERT INTO `tokenmap` VALUES ('11', '4e3ac3a8-ec8f-4527-9948-5e17bf503b64', '2018-05-24 17:28:06');
INSERT INTO `tokenmap` VALUES ('12', '32108e2e-b605-499e-bd50-54a9b0da17ac', '2018-05-22 19:44:18');
INSERT INTO `tokenmap` VALUES ('13', '9271920a-4940-40b7-a75f-ef1900816cda', '2018-05-25 16:00:31');
INSERT INTO `tokenmap` VALUES ('42', '658b0979-e70c-4dbb-b888-df315fb70395', '2018-05-23 10:56:58');
INSERT INTO `tokenmap` VALUES ('43', '3f727f2a-cd1a-4b3b-ba6e-6adc8666e2a8', '2018-05-22 17:00:11');
INSERT INTO `tokenmap` VALUES ('49', 'f4c2ab9a-d469-475f-a3b5-19f6782310ea', '2018-05-23 18:38:30');
INSERT INTO `tokenmap` VALUES ('55', 'ce94acf9-ea03-4036-92a3-44817a3614c5', '2018-05-23 19:05:48');
