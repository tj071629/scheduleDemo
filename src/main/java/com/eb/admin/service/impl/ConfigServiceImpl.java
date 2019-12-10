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
