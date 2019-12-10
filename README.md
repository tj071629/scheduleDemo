# springboot整合Quartz实现动态配置定时任务

​                     原创                                                                                                                                            [各自远扬_JasonYan](https://me.csdn.net/yunhan0806)                                         发布于2018-04-04 11:17:48                                         阅读数 5884                                                                       收藏                                      

更新于2018-05-25 17:20:26

​                             版权声明：本文为博主原创文章，遵循[CC 4.0 BY-SA ](http://creativecommons.org/licenses/by-sa/4.0/)版权协议，转载请附上原文出处链接和本声明。                                                                                         本文链接：https://blog.csdn.net/yunhan0806/article/details/79814903                             

展开                 

[springboot整合Quartz实现动态配置定时任务](https://blog.csdn.net/liuchuanhong1/article/details/60873295)

先展示一下后台管理定时任务效果图：

1、新增任务页面：

![img](https://img-blog.csdn.net/20180404113722397?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3l1bmhhbjA4MDY=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

 

2、列表页（实现任务的禁用启用）

![img](https://img-blog.csdn.net/20180404113953859?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3l1bmhhbjA4MDY=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

 3、后台多任务执行日志（相互之间不干扰，计划任务到点自动执行！）

![img](https://img-blog.csdn.net/20180404113926806?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3l1bmhhbjA4MDY=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

 

4、数据库脚本：

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
`description` varchar(280) DEFAULT NULL COMMENT '描述信息',
PRIMARY KEY (`jobid`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;



(附上述任务数据：)

![img](https://img-blog.csdn.net/20180404114337943?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3l1bmhhbjA4MDY=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

 

5、定时任务后台处理类（核心）：

package com.eb.admin.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.eb.admin.entity.ScheduleJob;
import com.eb.admin.service.ConfigService;

/**
\* 调度工厂类
\* 
\* @author Jason.Yan
\* @since 2018/03/28
\* 
*/
@Configuration
@EnableScheduling
@Component
public class ScheduleFactory {

private static Logger logger = LoggerFactory.getLogger(ScheduleFactory.class);

private Map<String, String> jobUniqueMap = new HashMap<String, String>();	// 当前Trigger使用的 

@Autowired
private SchedulerFactoryBean schedulerFactoryBean;

public SchedulerFactoryBean getSchedulerFactoryBean() {
return schedulerFactoryBean;
}

public void setSchedulerFactoryBean(SchedulerFactoryBean schedulerFactoryBean) {
this.schedulerFactoryBean = schedulerFactoryBean;
}

@Autowired
private ConfigService configService;

public ConfigService getConfigService() {
return configService;
}

public void setConfigService(ConfigService configService) {
this.configService = configService;
}

//TODO 此处暂且注释，后续有后台定时任务逻辑 开启
@Scheduled(fixedRate = 5000) // 每隔5s查库，并根据查询结果决定是否重新设置定时任务
public void scheduleUpdateCronTrigger() throws Exception {

try {
// schedulerFactoryBean 由spring创建注入
Scheduler scheduler = schedulerFactoryBean.getScheduler();
List<ScheduleJob> jobList = configService.findLegalJobList();

// 获取最新删除(禁用)任务列表，将其从调度器中删除，并且从jobUniqueMap中删除
List<ScheduleJob> jobDelList = configService.findDelJobList();
for (ScheduleJob delJob : jobDelList) {
JobKey jobKey = JobKey.jobKey(delJob.getJobName(), delJob.getJobGroup());
scheduler.deleteJob(jobKey);
jobUniqueMap.remove(TriggerKey.triggerKey(delJob.getJobName(), delJob.getJobGroup()));
}

for (ScheduleJob job : jobList) {

TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

String dbCron = job.getCronExpression();	// 该job数据库中的Trigger表达式
// 不存在，创建一个
if (null == trigger) {
//JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(job.getJobName(), job.getJobGroup()).build();
try{
@SuppressWarnings("unchecked")
Class <? extends Job> clazz = (Class<? extends Job>) Class.forName(job.getQuartzClass());
JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(), job.getJobGroup()).build();
jobDetail.getJobDataMap().put("scheduleJob", job);
// 表达式调度构建器
CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
// 按新的cronExpression表达式构建一个新的trigger
trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();

jobUniqueMap.put(triggerKey.toString(), trigger.getCronExpression());
//currentCron = trigger.getCronExpression();

scheduler.scheduleJob(jobDetail, trigger);
}catch(Exception e){
e.printStackTrace();
logger.error(e.getMessage());
}

} else if(!jobUniqueMap.get(triggerKey.toString()).equals(dbCron)){
// Trigger已存在，那么更新相应的定时设置
// 表达式调度构建器
CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(dbCron);
// 按新的cronExpression表达式重新构建trigger

trigger = (CronTrigger) scheduler.getTrigger(triggerKey); 

trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
// 按新的trigger重新设置job执行
scheduler.rescheduleJob(triggerKey, trigger);

jobUniqueMap.put(triggerKey.toString(), dbCron);
}
}

} catch (Exception e) {
e.printStackTrace();
}
}
}

 

6、后台查询sql：

ConfigService.findLegalJobList=select * from schedule_job where jobstatus = 1 and auditstatus = 1

ConfigService.findDelJobList=select * from schedule_job where jobstatus = 0

 

7、后台查询方法实现类：

package com.eb.admin.service.impl;

import java.util.List;

import com.eb.admin.entity.ScheduleJob;
import com.eb.admin.service.ConfigService;
import com.eb.dataservice.dao.CommonDao;
import com.eb.dataservice.dao.SqlUtils;

public class ConfigServiceImpl implements ConfigService {

@SuppressWarnings("unchecked")
@Override
public List<ScheduleJob> findLegalJobList() throws Exception {
CommonDao dao = CommonDao.getDao(dbkey);
String sql = SqlUtils.getSql("ConfigService.findLegalJobList");
return dao.findBeanList(ScheduleJob.class, sql);
}

@Override
public List<ScheduleJob> findDelJobList() throws Exception {
CommonDao dao = CommonDao.getDao(dbkey);
String sql = SqlUtils.getSql("ConfigService.findDelJobList");
return dao.findBeanList(ScheduleJob.class, sql);
}

}



==============================================================================

追加：

有朋友问具体的实现类是怎么写的：我也贴出源码供参考：

这是其中一个任务对应的实现类：（理论上你配置了多少个定时任务，就需要写多少个实现类与之一一匹配，否则后台会报错！）

8、ScheduleTask1.java



import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.eb.admin.SpringContext;
import com.eb.admin.entity.User;
import com.eb.admin.service.UserMsgService;
import com.eb.admin.service.UserService;
import com.eb.admin.utils.DateUtils;


/**
 \* 定时任务实现类（清明节活动专场提醒）
 \* 
 \* @author Jason.Yan
 \* @since 2018/04/3
 \* 
 */
@DisallowConcurrentExecution
public class ScheduleTask1 implements Job  {

private static Logger logger = LoggerFactory.getLogger(ScheduleTask1.class);

public String getTemplate(String templatename, Map<String, String> params) throws Exception {
UserMsgService userMsgService = SpringContext.getBean("userMsgService", UserMsgService.class);
String template = userMsgService.getTemplate(templatename);
for (String key : params.keySet()) {
String v = params.get(key);
template = template.replace("${" + key + "}", v);
}
return template;
}

@Override
public void execute(JobExecutionContext context) throws JobExecutionException {

logger.info("==== 定时任务实现类（清明节活动专场提醒）ScheduleTask1 ====> 开启!" + DateUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));

UserService userService= SpringContext.getBean("userService",UserService.class);
UserMsgService userMsgService= SpringContext.getBean("userMsgService",UserMsgService.class);

try {

List<User> userList = userService.getAllUsers();
String msg = getTemplate("qingmingregard", new HashMap<String, String>());

for (User user : userList) {
userMsgService.addMsg(user.getId(), 1, 1, "【系统通知】", msg);
}

} catch (Exception e) {
logger.error(e.getMessage());
}

}
}



9、上面类中SpringContext.java配置类：（注意：需要放在你的springBoot启动类的同一级）



import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@Component
@Lazy(false)
public class SpringContext implements ApplicationContextAware {
  private static ApplicationContext applicationContext;
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }
  public static Object getBean(String name) {
    return getApplicationContext().getBean(name);
  }
  public static <T> T getBean(Class<T> clazz) {
    return getApplicationContext().getBean(clazz);
  }
  public static <T> T getBean(String name, Class<T> clazz) {
    return getApplicationContext().getBean(name, clazz);
  }
}



最后：我把整体的层级结构列出来供大家参考：

![img](https://img-blog.csdn.net/20180404113052917?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3l1bmhhbjA4MDY=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)





以上为本人根据两篇博文所做的整理修改，原文参见：

https://blog.csdn.net/liuchuanhong1/article/details/60873295#reply

https://www.ktanx.com/blog/p/308

本着尊重原作者的态度，转载请注明出处：

https://blog.csdn.net/yunhan0806/article/details/79814903



看了一些网友的留言，想要我整理一下源码，我也抽出点时间整理了一套可以正常运行的框架，里面包含了以上内容，

这套框架也可以作为企业级开发用，功能已经很完善（数据库可能需要自行调整一下）

部署好后，浏览器地址栏输入：http://localhost:8888/        回车即可！

CSDN没分了，收了3个资源分，不过分吧，觉得有帮助的请加个好评，谢谢~



下载地址：

https://download.csdn.net/download/yunhan0806/10438046