package com.eb.admin.service.impl;

import java.util.List;
import java.util.Map;

import com.eb.admin.service.UserPriService;
import com.eb.dataservice.dao.CommonDao;
import com.eb.dataservice.dao.SqlUtils;

public class UserPriServiceImpl implements UserPriService {

	@Override
	public List<Map<String, Object>> userPriList(long uid) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserPriService.userPriList");
		return dao.findList(sql, uid);
	}

	@Override
	public void addUserPri(long uid, long priid) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserPriService.addUserPri");
		dao.execute(sql, uid, priid);
	}

	@Override
	public void removeUserPri(long uid, long priid) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserPriService.removeUserPri");
		dao.execute(sql, uid, priid);

	}

	@Override
	public List<Map<String, Object>> prilist() throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserPriService.prilist");
		return dao.findList(sql);
	}

	@Override
	public void addPriInfo(String name, String uri) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserPriService.addPriInfo");
		dao.execute(sql, name, uri);
	}

	@Override
	public void delPriInfo(String name) throws Exception {
		// TODO Auto-generated method stub

	}

}
