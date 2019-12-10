package com.eb.admin.service;

import java.util.List;

import com.eb.admin.entity.ScheduleJob;

public interface ConfigService {
	String dbkey = "oper";

	List<ScheduleJob> findLegalJobList() throws Exception;

	List<ScheduleJob> findDelJobList() throws Exception;

}
