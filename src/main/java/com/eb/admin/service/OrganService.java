package com.eb.admin.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.eb.dataservice.dao.EntityPage;
import com.eb.dataservice.dao.PageResultEntity;

public interface OrganService {

	String dbkey = "ebweb";
	
	void addOrgan(String organname, String relationer, Long currUserid, Date createdate) throws SQLException;

	PageResultEntity organlist(String organname, EntityPage page) throws SQLException;

	/*void delOrgan(long organcodeid);*/

	Map<String, Object> getOrgan(long organcodeid) throws Exception;

	void updateOrgan(String organname, String relationer, long organid, long created, Date updatedate) throws Exception;

	List<Map<String, Object>> isExistOrganName(String trim, long organid);


}
