package com.eb.admin.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eb.admin.entity.ScheduleJob;
import com.eb.admin.service.ScheduleService;
import com.eb.dataservice.dao.CommonDao;
import com.eb.dataservice.dao.EntityPage;
import com.eb.dataservice.dao.PageResultEntity;
import com.eb.dataservice.dao.PageUtils;
import com.eb.dataservice.dao.SqlUtils;

public class ScheduleServiceImpl implements ScheduleService {
	
	@Override
	public PageResultEntity schedulelist(String jobname, String jobgroup, String content,
			EntityPage page) throws SQLException {
		PageResultEntity result = new PageResultEntity();
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("ScheduleService.schedulelist");
		
		
		List<Object> params = new ArrayList<Object>();
		
		if (jobname != null && !"".equals(jobname.trim())) {
			sql += " AND job.jobname like ? ";
			params.add("%"+jobname.trim()+"%");
		}
		
		if (jobgroup != null && !"99".equals(jobgroup.trim())) {
			sql += " AND job.jobgroup = ? ";
			params.add(jobgroup.trim());
		}
		
		if (content != null && !"".equals(content.trim())) {
			sql += " AND job.description like ? ";
			params.add("%"+content.trim()+"%");
		}
		
		sql += " order by jobid desc ";
		
		@SuppressWarnings("rawtypes")
		List<Map> list = PageUtils.getValueListMap(dao, sql, page, params);
		result.setList(list);
		result.setPageinfo(page);
		return result;
	}
	
	@Override
	public PageResultEntity messagetemplatelist(String templatename, int type, String content,
			EntityPage page) throws SQLException {
		PageResultEntity result = new PageResultEntity();
		CommonDao dao = CommonDao.getDao("forum");
		String sql = SqlUtils.getSql("ScheduleService.noticetemplatelist");
		
		
		List<Object> params = new ArrayList<Object>();
		
		if (templatename != null && !"".equals(templatename.trim())) {
			sql += " AND template.templatename like ? ";
			params.add("%"+templatename.trim()+"%");
		}
		
		if (type != 99) {
			sql += " AND template.type = ? ";
			params.add(type);
		}
		
		if (content != null && !"".equals(content.trim())) {
			sql += " AND template.content like ? ";
			params.add("%"+content.trim()+"%");
		}
		
		sql += " order by id desc ";
		
		@SuppressWarnings("rawtypes")
		List<Map> list = PageUtils.getValueListMap(dao, sql, page, params);
		result.setList(list);
		result.setPageinfo(page);
		return result;
	}
	
	@Override
	public List<Map<String, Object>> isExistJobName(String jobname, long jobid) {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("ScheduleService.isexistjobname");

		try {
			return dao.findList(sql, jobname, jobid);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return new ArrayList<Map<String, Object>>();
	}
	
	@Override
	public Map<String, Object> getSchedule(long jobid) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("ScheduleService.getSchedule");
		return dao.findMap(sql, jobid);
	}

	@Override
	public void addSchedule(String jobname, String jobgroup, int jobstatus, String cronexpression,
			String triggerfrequency, String quartzclass, String description, String senderStr) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("ScheduleService.addSchedule");
		dao.execute(sql, jobname, jobgroup, jobstatus, 1, cronexpression, quartzclass, description, senderStr);
	}

	@Override
	public void updateSchedule(long jobid, String jobname, String jobgroup, int jobstatus, String cronexpression,
		 String quartzclass, String description, String senderStr) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("ScheduleService.updateSchedule");

		dao.execute(sql,  jobname, jobgroup, jobstatus, 1, cronexpression, quartzclass, description, senderStr, jobid);
	}

	@Override
	public void updateJobStatus(int jobstatus, long jobid) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("ScheduleService.updateScheduleStatus");
		dao.execute(sql, jobstatus, jobid);
		
	}

	@Override
	public Object getReceiverIdByJobName(String jobname) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("ScheduleService.getReceiverIdByJobName");
		return dao.findBean(ScheduleJob.class, sql, jobname);
		
	}

}
