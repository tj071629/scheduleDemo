package com.eb.admin.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.eb.admin.service.OrganService;
import com.eb.dataservice.dao.CommonDao;
import com.eb.dataservice.dao.EntityPage;
import com.eb.dataservice.dao.PageResultEntity;
import com.eb.dataservice.dao.PageUtils;
import com.eb.dataservice.dao.SqlUtils;

public class OrganServiceImpl implements OrganService {
	
	@Override
	public List<Map<String, Object>> isExistOrganName(String organname, long organid) {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("OrganService.isexistorganname");

		try {
			return dao.findList(sql, organname, organid);
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ArrayList<Map<String, Object>>();
	}
	
	@Override
	public void addOrgan(String organname, String relationer, Long currUserid, Date createdate) {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("OrganService.addOrgan");
		dao.execute(sql, organname, relationer, currUserid, createdate);
	}
	
	@Override
	public PageResultEntity organlist(String organname, EntityPage page) throws SQLException {
		PageResultEntity result = new PageResultEntity();
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("OrganService.organlist");
		
		List<Object> params = new ArrayList<Object>();
		
		if (organname != null && !"".equals(organname.trim())) {
			sql += " AND organname like ? ";
			params.add("%"+organname.trim()+"%");
		}
		
		sql += " order by organid desc ";
		
		@SuppressWarnings("rawtypes")
		List<Map> list = PageUtils.getValueListMap(dao, sql, page, params);
		result.setList(list);
		result.setPageinfo(page);
		return result;
	}

	/*@Override
	public void delOrgan(long organid) {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("OrganService.delOrgan");
		dao.execute(sql, organid);
	}*/
	
	@Override
	public Map<String, Object> getOrgan(long organid) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("OrganService.getOrgan");
		return dao.findMap(sql, organid);
	}
	
	@Override
	public void updateOrgan(String organname, String relationer, long organid, long updater, Date updatedate) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("OrganService.updateOrgan");
		dao.execute(sql,  organname, relationer, updater, updatedate, organid);
	}

}
