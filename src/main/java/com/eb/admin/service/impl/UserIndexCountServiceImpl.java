package com.eb.admin.service.impl;

import java.util.List;
import java.util.Map;

import com.eb.admin.service.UserIndexCountService;
import com.eb.dataservice.dao.CommonDao;
import com.eb.dataservice.dao.SqlUtils;

public class UserIndexCountServiceImpl implements
		UserIndexCountService {

	@Override
	public Map<String, Object> mostUseFullIndex(long userid) throws Exception {
		final String sql = SqlUtils.getSql("UserIndexCountService.mostUseFullIndex");
		CommonDao dao = CommonDao.getDao(dbkey);
		 List<Map<String, Object>> findList = dao.findList(sql, userid);
		 Map<String, Object> map  = null;
		 if (findList != null && findList.size() > 0 ) {
			 map = findList.get(0);
		}
		return map;
	}

	@Override
	public String getUseIndexName(long indexid) throws Exception {
		final String sql = SqlUtils.getSql("UserIndexCountService.getUseIndexName");
		CommonDao dao = CommonDao.getDao("ebweb");
		Map<String, Object> map = dao.findMap(sql, indexid);
		if (map != null ) {
			return (String)map.get("name");
		}
		return null;
	}

}
