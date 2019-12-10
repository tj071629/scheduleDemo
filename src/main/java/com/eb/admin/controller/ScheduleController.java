package com.eb.admin.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eb.admin.entity.ScheduleJob;
import com.eb.admin.entity.User;
import com.eb.admin.service.ScheduleService;
import com.eb.admin.service.UserService;
import com.eb.admin.utils.RegexFormatCheckUtils;
import com.eb.dataservice.dao.EntityPage;
import com.eb.dataservice.dao.PageResultEntity;

@Controller
@RequestMapping("/schedule")
@Scope("prototype")
public class ScheduleController extends AbstractController {
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = { "schedulelist.html" }, method = {
			RequestMethod.GET, RequestMethod.POST })
	public String schedulelist(final ModelMap modelMap) throws Exception {
		EntityPage page = this.getPageInfo(modelMap);
		String jobname = this.getValue("jobname", modelMap);
		String jobgroup = this.getValue("jobgroup", modelMap);
		String content = this.getValue("content", modelMap);
		PageResultEntity pageResultEntity = scheduleService.schedulelist(jobname, jobgroup, content, page);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = pageResultEntity.getList();
		for (Map<String, Object> map : list) {
			int balance = 0;
			map.put("balance", balance);
		}
		modelMap.put("result", pageResultEntity);
		return "schedule/schedulelist";
	}
	
	@RequestMapping(value = { "selnoticetemplate.html" }, method = {
			RequestMethod.GET, RequestMethod.POST })
	public String selnoticetemplate(final ModelMap modelMap) throws Exception {
		EntityPage page = this.getPageInfo(modelMap);
		String templatename = this.getValue("templatename", modelMap);
		String content = this.getValue("content", modelMap); 
		int type = this.getIntValue("type", modelMap, -1); 

		PageResultEntity pageResultEntity = scheduleService.messagetemplatelist(templatename, type, content, page);
		modelMap.put("result", pageResultEntity);
		modelMap.put("datas", pageResultEntity.getList());
		modelMap.put("page", pageResultEntity.getPageinfo());
		modelMap.put("type", type);
		return "schedule/noticemodallist";
	}
	
	@RequestMapping(value = { "selscheduleuser.html" }, method = {
			RequestMethod.GET, RequestMethod.POST })
	public String selscheduleuser(final ModelMap modelMap) throws Exception {
		EntityPage page = this.getPageInfo(modelMap);
		String jobname = this.getValue("jobname", modelMap);
		long accounttype = this.getLongValue("accounttype", modelMap, 0);
		String loginid = this.getValue("loginid", modelMap);
		String phone = this.getValue("phone", modelMap);
		Date startDate = this.getDateValue("startdate", modelMap);
		Date endDate = this.getDateValue("enddate", modelMap);
		
		PageResultEntity pageResultEntity = userService.userlist(accounttype,startDate, endDate,-10,-10,phone,loginid,null,0,page);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = pageResultEntity.getList();
		for (Map<String, Object> map : list) {
			long userid = (long) map.get("id");
			Map<String, Object> reginfomap = this.userService.getIpByUserid(userid);
			if (reginfomap != null && reginfomap.containsKey("ip")) {
				Map<String, Object> addressMap = userService.getProvinceCityById(reginfomap.get("ip").toString());
				if(addressMap != null){
					map.put("province", addressMap.get("province"));
					map.put("city", addressMap.get("city"));
				}
			}
		}
		modelMap.put("jobname", jobname);
		modelMap.put("result", pageResultEntity);
		return "schedule/scheduleuserlist";

	}
		
	@RequestMapping(value = { "/editschedule.html" }, method = { RequestMethod.GET,RequestMethod.POST })
	public String editschedule(final ModelMap modelMap) throws Exception {
		
		long jobid = this.getLongValue("jobid", modelMap, -1L);
		Map<String, Object> job = this.scheduleService.getSchedule(jobid);
		if (job == null) {
			job = new HashMap<String, Object>();
		}
		
		if(jobid > 0){
			
			if(job.get("cronexpression") != null && job.get("cronexpression").toString().indexOf("0/") == -1){
				// 			28 37 12 19 4 ?
				String[] cronex = job.get("cronexpression").toString().split(" ");
				job.put("triggertime", String.format("%02d",Integer.valueOf(cronex[4]))+"-"+String.format("%02d",Integer.valueOf(cronex[3]))+" "+String.format("%02d",Integer.valueOf(cronex[2]))+":"+String.format("%02d",Integer.valueOf(cronex[1]))+":"+String.format("%02d",Integer.valueOf(cronex[0])));
			}else{
				//  		0 0 0 0/15 * ?
				String str = job.get("cronexpression").toString();
				int special = str.substring(str.indexOf("0/"), str.length()).indexOf(" ");
				String cronex = str.substring(str.indexOf("0/"), (special==-1 ? str.length() : str.indexOf("0/") + special));
				int perunit = Integer.valueOf(cronex.split("/")[1]);
				job.put("perunit", perunit);
				String[] temp = str.split(" ");
				
				for (int i = 0; i < temp.length; i++) {
					if(temp[i].indexOf("0/") != -1){
						if(i==0){
							job.put("triggerfrequency", "second");
							break;
						}else if(i==1){
							job.put("triggerfrequency", "minute");
							break;
						}else if(i==2){
							job.put("triggerfrequency", "hour");
							break;
						}
					}
				}
			}
		}
		
		modelMap.put("job", job);
		modelMap.put("jobid", jobid);
		
		return "schedule/editschedule";
	}

	@RequestMapping(value = { "/isexistjobname" }, produces = "application/json; charset=utf-8", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public String isexistjobname(final ModelMap modelMap) throws Exception {
		
		String msg = null;
		long jobid = this.getLongValue("jobid", modelMap, -1L);
		String jobname = this.getValue("jobname", modelMap);
		
		if (jobname != null && jobname != "") { 
			List<Map<String, Object>> list = scheduleService.isExistJobName(jobname.trim(), jobid);
			if (list.size() > 0) {
				msg = "任务名称已存在，请重新选择";
			}
		}

		return msg;
	}
	
	@RequestMapping(value = { "/getReceiverIdByJobName" }, method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public Object getReceiverIdByJobName(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		String jobname = this.getValue("jobname", modelMap);
		ScheduleJob scheduleJob = null;
		if(jobname != null && jobname != ""){
			scheduleJob = (ScheduleJob) scheduleService.getReceiverIdByJobName(jobname);
		}
		
		rtnmap.put("success", true);
		rtnmap.put("receiverIds", (scheduleJob==null?"":scheduleJob.getReceiver()));
		return rtnmap;
	}
	
	@RequestMapping(value = { "/saveschedule" }, produces = "application/json; charset=utf-8", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public Object saveschedule(final ModelMap modelMap) throws Exception {
		
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		
		long jobid = this.getLongValue("jobid", modelMap, -1L);
		String jobname = this.getValue("jobname", modelMap);
		String jobgroup = this.getValue("jobgroup", modelMap);
		String triggertime = this.getValue("triggertime", modelMap);
		String perunit = this.getValue("perunit", modelMap);
		String triggerfrequency = this.getValue("triggerfrequency", modelMap);
		String quartzclass = this.getValue("quartzclass", modelMap);
		int jobstatus = this.getIntValue("jobstatus", modelMap, -1);
		String description = this.getValue("desc", modelMap);
		
		String isorguser = this.getValue("isorguser", modelMap);
		String isperuser = this.getValue("isperuser", modelMap);
		String receiver = this.getValue("receiver", modelMap);
		
		String cronexpression = null;
		//		04-18 11:40:34
		if(triggertime != null && triggertime != ""){
			int month = Integer.valueOf(triggertime.substring(0, 2));
			int date  = Integer.valueOf(triggertime.substring(3, 5));
			int hour  = Integer.valueOf(triggertime.substring(6, 8));
			int minute = Integer.valueOf(triggertime.substring(9, 11));
			int second = Integer.valueOf(triggertime.substring(12, 14));
			cronexpression = second +" "+ minute +" "+ hour  +" "+ date   +" "+ month + " ?";  
		}else if(triggerfrequency != null && triggerfrequency != ""){
			
			if("second".equals(triggerfrequency)){
				// 		0/30 * * * * ?
				cronexpression = "0/"+ perunit + " * * * * ?";
			}else if("minute".equals(triggerfrequency)){
				//      0 0/5 * * * ?
				cronexpression = "0 0/"+ perunit + " * * * ?";
			}else if("hour".equals(triggerfrequency)){
				//		0 0 0/1 * * ?
				cronexpression = "0 0 0/"+ perunit + " * * ?";
			}
			
		}
		
		
		//实际接收对象
		String senderStr = "";
		
		StringBuffer organStr = new StringBuffer();
		StringBuffer personStr = new StringBuffer();
		
		//如果是站内信或者站内通知
		if("message".equals(jobgroup) || "notice".equals(jobgroup)){
			
			
			//如果发送对象是所有机构
			if(isorguser != null && "checked".equals(isorguser)){
				
				List<User> orgList = this.userService.getAllOragin();
				
				for (User user : orgList) {
					if(organStr.length() == 0){
						organStr.append(user.getId());
					}else{
						organStr.append(",").append(user.getId());
					}
				}
				
			}else if(isperuser != null && "checked".equals(isperuser)){
				//如果发送对象是所有个人用户	
				
				List<User> perList = this.userService.getAllPerson();
				
				for (User user : perList) {
					if(personStr.length() == 0){
						personStr.append(user.getId());
					}else{
						personStr.append(",").append(user.getId());
					}
				}
			}
			
			
			if(organStr.length() != 0 || personStr.length() != 0){
				if(organStr.length() != 0 && personStr.length() != 0){
					senderStr = organStr.append(personStr).toString();
				}else if(organStr.length() != 0){
					senderStr = organStr.toString();
				}else if(personStr.length() != 0){
					senderStr = personStr.toString();
				}
			}else{
				senderStr = receiver.replace(" ", "");
			}
			
			
		}else if("mail".equals(jobgroup)){
			
			
			//如果发送对象是所有机构
			if(isorguser != null && "checked".equals(isorguser)){
				
				List<User> orgList = this.userService.getAllMailOragin();
				
				for (User user : orgList) {
					
					//如果邮箱格式正确，则添加至发送邮件列表
					if(RegexFormatCheckUtils.isEmail(user.getNickname())){
						
						if(organStr.length() == 0){
							organStr.append(user.getId()).append("#").append(user.getNickname());
						}else{
							organStr.append(",").append(user.getId()).append("#").append(user.getNickname());
						}
						
					}
					
					
				}
				
			}
			
			if(isperuser != null && "checked".equals(isperuser)){
				//如果发送对象是所有个人用户	
				
				List<User> perList = this.userService.getAllMailPerson();
				
				for (User user : perList) {
					
					//如果邮箱格式正确，则添加至发送邮件列表
					if(RegexFormatCheckUtils.isEmail(user.getNickname())){
						
						if(personStr.length() == 0){
							personStr.append(user.getId()).append("#").append(user.getNickname());
						}else{
							personStr.append(",").append(user.getId()).append("#").append(user.getNickname());
						}
					}
					
				}
			}
			
			if(organStr.length() != 0 || personStr.length() != 0){
				if(organStr.length() != 0 && personStr.length() != 0){
					senderStr = organStr.append(",").append(personStr).toString();
				}else if(organStr.length() != 0){
					senderStr = organStr.toString();
				}else if(personStr.length() != 0){
					senderStr = personStr.toString();
				}
			}else{
				
				String[] receivers = receiver.replace(" ", "").split(",");
				StringBuffer receiversb = new StringBuffer();
				for (String receiverTemp : receivers) {
					String[] idMail = receiverTemp.split("#");
					
					if(RegexFormatCheckUtils.isEmail(idMail[1])){
						
						if(receiversb.length() == 0){
							receiversb.append(receiverTemp);
						}else{
							receiversb.append(",").append(receiverTemp);
						}
						
					}
					
				}
				senderStr = receiversb.toString();
			}
			
			
		}else if("sms".equals(jobgroup)){
			
			
			//如果发送对象是所有机构
			if(isorguser != null && "checked".equals(isorguser)){
				
				List<User> orgList = this.userService.getAllSMSOragin();
				
				for (User user : orgList) {
					
					//如果手机号满足大陆或香港手机号格式，则添加至发送短信列表
					if(RegexFormatCheckUtils.isPhoneLegal(user.getPhone())){
						
						if(organStr.length() == 0){
							organStr.append(user.getId()).append("#").append(user.getPhone());
						}else{
							organStr.append(",").append(user.getId()).append("#").append(user.getPhone());
						}
						
					}
				}
				
			}
			
			if(isperuser != null && "checked".equals(isperuser)){
				//如果发送对象是所有个人用户	
				
				List<User> perList = this.userService.getAllSMSPerson();
				
				for (User user : perList) {
					
					//如果手机号满足大陆或香港手机号格式，则添加至发送短信列表
					if(RegexFormatCheckUtils.isPhoneLegal(user.getPhone())){
						
						if(personStr.length() == 0){
							personStr.append(user.getId()).append("#").append(user.getPhone());
						}else{
							personStr.append(",").append(user.getId()).append("#").append(user.getPhone());
						}
						
					}
					
				}
			}
			
			
			if(organStr.length() != 0 || personStr.length() != 0){
				if(organStr.length() != 0 && personStr.length() != 0){
					senderStr = organStr.append(",").append(personStr).toString();
				}else if(organStr.length() != 0){
					senderStr = organStr.toString();
				}else if(personStr.length() != 0){
					senderStr = personStr.toString();
				}
			}else{
				String[] receivers = receiver.replace(" ", "").split(",");
				StringBuffer receiversb = new StringBuffer();
				for (String receiverTemp : receivers) {
					String[] idPhone = receiverTemp.split("#");
					
					if(RegexFormatCheckUtils.isPhoneLegal(idPhone[1])){
						
						if(receiversb.length() == 0){
							receiversb.append(receiverTemp);
						}else{
							receiversb.append(",").append(receiverTemp);
						}
						
					}
					
				}
				senderStr = receiversb.toString();
			}
			
		}
		
		if (jobid > 0) {
			this.scheduleService.updateSchedule(jobid, jobname, jobgroup, jobstatus, cronexpression, quartzclass, description, senderStr);
		} else {
			this.scheduleService.addSchedule(jobname, jobgroup, jobstatus, cronexpression, triggerfrequency, quartzclass,  description, senderStr);
		}
		
		rtnmap.put("success", true);
		
		return rtnmap;
	}
	
	//任务启用/禁用
	@ResponseBody
	@RequestMapping(value = { "/updatestatus" }, produces = "application/json; charset=utf-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	public Object updatestatus(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		int jobstatus = this.getIntValue("jobstatus", modelMap, 0);
		long jobid = this.getLongValue("jobid", modelMap, -1);
		try{
			this.scheduleService.updateJobStatus(jobstatus,jobid);
			rtnmap.put("success", true);
		}catch(Exception e){
			rtnmap.put("success", false);
		}
		return rtnmap;
	}
	
	public ScheduleService getScheduleService() {
		return scheduleService;
	}

	public void setScheduleService(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}
	
}
