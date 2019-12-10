/*
Navicat MySQL Data Transfer

Source Server         : RDS_生产
Source Server Version : 50616
Source Host           : localhost:23306
Source Database       : ebweb

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2018-05-25 17:08:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_pri
-- ----------------------------
DROP TABLE IF EXISTS `tbl_pri`;
CREATE TABLE `tbl_pri` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pid` varchar(64) NOT NULL COMMENT '父ID：最顶级为0 表示超级管理员',
  `priname` varchar(20) NOT NULL,
  `level` char(1) NOT NULL,
  `haschild` varchar(2) DEFAULT NULL,
  `uri` varchar(200) DEFAULT NULL,
  `priorder` decimal(8,0) DEFAULT NULL,
  `isdel` varchar(2) DEFAULT NULL,
  `createuserid` varchar(64) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=142 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tbl_pri
-- ----------------------------
INSERT INTO `tbl_pri` VALUES ('1', '', '管理首页', '1', '1', '', '10', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('2', '1', '我的主页', '2', '0', '', '6', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('5', '', '用户管理', '1', '1', '', '20', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('6', '5', '机构管理', '2', '1', '', '100', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('7', '6', '机构列表', '3', '0', 'organ/organlist.html', '310', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('8', '6', '机构注册码列表', '3', '0', 'organcode/organcodelist.html', '320', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('11', '5', '账号管理', '2', '1', null, '110', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('12', '11', '注册信息管理', '3', '0', 'user/reguserlist.html', '330', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('13', '11', '使用信息管理', '3', '0', 'user/tradeuserlist.html', '340', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('14', '11', '试用账号列表', '3', '0', 'user/istryoutlist.html', '350', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('15', '11', '大众转正式列表', '3', '0', 'user/trytoformallist.html', '360', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('18', '', '指标管理', '1', '1', null, '30', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('19', '18', '个人用户指标', '2', '1', null, '120', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('20', '19', '指标审核信息', '3', '0', 'indic/list.html', '370', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('21', '19', '免费指标投票信息', '3', '0', 'indic/freeindiclist.html', '380', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('22', '19', '付费指标付费信息', '3', '0', 'indic/payindiclist.html', '390', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('23', '19', '指标开发信息', '3', '0', 'indic/devindiclist.html', '400', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('24', '19', '指标验收信息', '3', '0', 'indic/accindiclist.html', '410', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('30', '18', '机构用户指标', '2', '1', null, '130', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('31', '30', '机构指标信息', '3', '0', 'organ/indexlist.html', '420', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('35', '', '运营管理', '1', '1', null, '40', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('36', '35', '营销管理', '2', '1', null, '140', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('37', '36', '批量发送邮件', '3', '0', 'sale/sendmaillist.html', '430', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('40', '35', '数据统计管理', '2', '0', null, '150', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('45', '35', '产品定价管理', '2', '0', null, '160', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('46', '45', '定价模块管理', '3', '0', null, '501', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('47', '45', '用户定价规则', '3', '0', 'price/userpricelist.html', '500', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('50', '35', '消息管理', '2', '1', null, '170', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('51', '50', '消息模板列表', '3', '0', 'message/messagetemplatelist.html', '440', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('52', '35', '任务调度', '2', '0', 'schedule/schedulelist.html', '180', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('58', '', '财务管理', '1', '1', null, '50', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('59', '58', '交易管理', '2', '1', null, '190', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('60', '59', '查看订单', '3', '0', null, '450', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('65', '58', '补发/分成管理', '2', '1', null, '200', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('66', '65', '查看', '3', '0', null, '460', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('67', '65', '编辑', '3', '0', null, '470', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('70', '', '社区管理', '1', '1', null, '60', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('71', '70', '帖子管理', '2', '0', 'forum/list.html', '210', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('72', '70', '评论管理', '2', '0', 'forum/cmtlist.html', '220', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('73', '70', '板块管理', '2', '0', 'forum/blklist.html', '230', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('79', '', '其他', '1', '1', null, '70', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('80', '79', '用户反馈信息', '2', '0', 'feeback/list.html', '240', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('81', '79', '新上线指标', '2', '0', 'online/list.html', '250', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('82', '79', '辅助功能', '2', '0', 'rests/list.html', '260', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('83', '79', '指标入solr', '2', '0', 'indicstore/list.html', '270', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('90', '', '系统设置', '1', '1', null, '80', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('91', '90', '管理员管理', '2', '0', 'admin/userlist.html', '280', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('92', '90', '角色管理', '2', '0', 'admin/rolelist.html', '290', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('93', '90', '权限管理', '2', '0', 'admin/prilist.html', '300', '0', null, null);
INSERT INTO `tbl_pri` VALUES ('140', '35', '文章管理', '2', '1', '', '180', '0', '7', '2018-05-24 13:50:43');
INSERT INTO `tbl_pri` VALUES ('141', '140', '指标课堂列表', '3', '0', 'article/indexclasslist.html', '1', '0', '7', '2018-05-24 13:53:07');
