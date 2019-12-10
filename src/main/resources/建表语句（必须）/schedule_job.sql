/*
Navicat MySQL Data Transfer

Source Server         : RDS_生产
Source Server Version : 50616
Source Host           : localhost:23306
Source Database       : oper

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2018-05-25 16:51:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for schedule_job
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job`;
CREATE TABLE `schedule_job` (
  `jobid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `jobname` varchar(40) DEFAULT NULL COMMENT '任务名称',
  `jobgroup` varchar(40) DEFAULT NULL COMMENT '任务分组',
  `jobstatus` char(1) NOT NULL DEFAULT '1' COMMENT '任务状态 0禁用 1启用',
  `auditstatus` char(1) NOT NULL DEFAULT '0' COMMENT '审核状态 0 已创建 1 审核通过 2 审核驳回',
  `cronexpression` varchar(40) NOT NULL COMMENT '任务运行时间表达式',
  `quartzclass` varchar(255) DEFAULT NULL COMMENT '定时任务处理类',
  `description` varchar(500) DEFAULT NULL COMMENT '描述信息',
  `receiver` text,
  PRIMARY KEY (`jobid`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of schedule_job
-- ----------------------------
INSERT INTO `schedule_job` VALUES ('1', 'qingmingregard', 'notice', '0', '1', '0 29 11 10 4 ?', 'com.eb.admin.task.FestivalGreetJobFactory', 'admincontact<span style=\"color:#555555;font-family:\" font-size:14px;background-color:#f8feff;\"=\"\"></span>', null);
INSERT INTO `schedule_job` VALUES ('30', 'systemmaintain', 'notice', '0', '1', '0 5 10 10 4 ?', 'com.eb.admin.task.ScheduleSystemMaintainTask', '如发生系统维护、系统暂时无法使用等于用户使用相关的问题,触发当日8点,针对系统的问题所处描述，给用户带来的不便敬请谅解等', null);
INSERT INTO `schedule_job` VALUES ('31', 'systemupgrade', 'notice', '0', '1', '0 3 11 10 4 ?', 'com.eb.admin.task.ScheduleSystemUpgradeTask', '如发生系统升级、更新时,触发当日8点,针对系统更新后的功能或新产品做出描述，请用户体验等', null);
INSERT INTO `schedule_job` VALUES ('32', 'admincontact', 'notice', '0', '1', '0 0 10 9 4 ?', 'com.eb.admin.task.ScheduleAdminContactTask', '如管理员需与用户进行联系确认相关事宜时,触发当日10点,针对管理员需要确认是的事宜进行问题描述', null);
INSERT INTO `schedule_job` VALUES ('33', 'test51activity', 'notice', '0', '1', '20 47 16 23 4 ?', 'com.eb.admin.task.FestivalGreetJobFactory', '51劳动节问候！', '3521498,2580931,357216733');
INSERT INTO `schedule_job` VALUES ('34', '一筐馒头', 'mail', '0', '1', '10 1 19 23 4 ?', 'com.eb.admin.task.FestivalGreetJobFactory', 'test61activity', '3521498#yhyan@quantfrontier.com.cn,3551606#53874248@qq.com');
INSERT INTO `schedule_job` VALUES ('43', '召回邮件', 'mail', '0', '1', '20 28 15 2 5 ?', 'com.eb.admin.task.FestivalGreetJobFactory', '召回邮件', '3521498#yhyan@quantfrontier.com.cn');
INSERT INTO `schedule_job` VALUES ('44', '测试短信模板', 'sms', '0', '1', '30 50 17 30 6 ?', 'com.eb.admin.task.FestivalGreetJobFactory', '测试短信模板，呵呵呵~~', '3521498#18621760390,3575064#18796225607');
