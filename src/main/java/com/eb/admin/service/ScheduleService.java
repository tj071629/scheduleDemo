package com.eb.admin.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.eb.dataservice.dao.EntityPage;
import com.eb.dataservice.dao.PageResultEntity;

public interface ScheduleService {

	String dbkey = "oper";
	
	PageResultEntity schedulelist(String jobname, String jobgroup, String content,
			EntityPage page) throws SQLException;

	Map<String, Object> getSchedule(long jobid) throws Exception;

	void addSchedule(String jobname, String jobgroup, int jobstatus, String cronexpression, String triggerfrequency, String quartzclass, String desc, String senderStr) throws Exception;

	void updateSchedule(long jobid, String jobname, String jobgroup, int jobstatus, String cronexpression, String quartzclass, String description, String senderStr) throws Exception;

	void updateJobStatus(int jobstatus, long jobid) throws Exception;

	List<Map<String, Object>> isExistJobName(String trim, long jobid) throws Exception;

	PageResultEntity messagetemplatelist(String templatename, int type,String content, EntityPage page) throws SQLException;

	Object getReceiverIdByJobName(String jobname) throws Exception;
	
}
