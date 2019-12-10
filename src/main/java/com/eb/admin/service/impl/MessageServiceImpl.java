package com.eb.admin.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eb.admin.service.MessageService;
import com.eb.dataservice.dao.CommonDao;
import com.eb.dataservice.dao.EntityPage;
import com.eb.dataservice.dao.PageResultEntity;
import com.eb.dataservice.dao.PageUtils;
import com.eb.dataservice.dao.SqlUtils;

public class MessageServiceImpl implements MessageService {
	
	@Override
	public PageResultEntity messagetemplatelist(String templatename, int type, String content,
			EntityPage page) throws SQLException {
		PageResultEntity result = new PageResultEntity();
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("MessageService.mailtemplatelist");
		
		
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
	public List<Map<String, Object>> isExistTemplateName(String templatename, long templateid) {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("MessageService.isexisttemplatename");

		try {
			return dao.findList(sql, templatename, templateid);
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ArrayList<Map<String, Object>>();
	}

	@Override
	public void addMessageTemplate(String templatename, int templatetype, String content) {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("MessageService.addMessageTemplate");
		dao.execute(sql, templatetype, templatename, content);
	}

	@Override
	public void updateMessageTemplate(long templateid, String templatename, int templatetype, String content) {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("MessageService.updateMessageTemplate");
		dao.execute(sql,  templatetype, templatename, content, templateid);
	}

	@Override
	public Map<String, Object> getMessageTemplate(long templateid) {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("MessageService.getMessageTemplate");
		try {
			return dao.findMap(sql, templateid);
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
