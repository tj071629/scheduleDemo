package com.eb.admin.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.eb.dataservice.dao.EntityPage;
import com.eb.dataservice.dao.PageResultEntity;

public interface MessageService {

	String dbkey = "forum";
	
	PageResultEntity messagetemplatelist(String templatename, int type,String content,
			EntityPage page) throws SQLException;

	List<Map<String, Object>> isExistTemplateName(String templatename, long templateid);

	void addMessageTemplate(String templatename, int templatetype, String content);
	
	void updateMessageTemplate(long templateid, String templatename, int templatetype, String content);

	Map<String, Object> getMessageTemplate(long templateid);
	
}
