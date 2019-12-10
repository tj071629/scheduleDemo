package com.eb.admin.service.impl;

import java.util.Map;

import com.eb.admin.service.UserMsgService;
import com.eb.dataservice.dao.CommonDao;
import com.eb.dataservice.dao.SqlUtils;

public class UserMsgServiceImpl implements UserMsgService {

	@Override
	public void addMsg(long userid, long sendid, String title, String content)
			throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserMsgService.addMsg");
		dao.execute(sql, userid, sendid, title, content);
	}
	@Override
	public void addMsg(long userid, long sendid, long type ,String title, String content)
			throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserMsgService.addUserMsg");
		dao.execute(sql, userid, sendid, type, title, content);
	}

	@Override
	public String getTemplate(String name) throws Exception {
		CommonDao dao = CommonDao.getDao("forum");
		String sql = SqlUtils.getSql("UserMsgService.getTemplate");
		Map<String, Object> map = dao.findMap(sql, name);
		if (map != null) {
			return (String) map.get("content");
		}
		return null;
	}
}
