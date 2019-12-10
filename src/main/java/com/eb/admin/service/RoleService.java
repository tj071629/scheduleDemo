package com.eb.admin.service;

import java.util.List;
import java.util.Map;

import com.eb.dataservice.dao.EntityPage;
import com.eb.dataservice.dao.PageResultEntity;

public interface RoleService {
	List<Map<String, Object>> roleList(String rolename) throws Exception;

	List<Map<String, Object>> userroleList(long userid) throws Exception;

	void addUserRole(long userid, long roleid) throws Exception;

	void delUserRole(long userid, long roleid) throws Exception;

	void addRole(String name) throws Exception;

	void delRole(long roleid) throws Exception;

	Map<String, Object> getRole(long roleid) throws Exception;

	void updateRole(long roleid, String name) throws Exception;

	List<Map<String, Object>> prilist() throws Exception;

	List<Map<String, Object>> rolePrilist(long roleid) throws Exception;

	Map<String, Object> getPri(long priid) throws Exception;

	void addPri(String priname, String uri) throws Exception;

	void updatePri(long priid, String priname, String uri) throws Exception;

	void delPri(long priid) throws Exception;

	void addRolePri(long roleid, long priid) throws Exception;

	void delRolePri(long roleid, long priid) throws Exception;

	PageResultEntity prilist(EntityPage page) throws Exception;

}
