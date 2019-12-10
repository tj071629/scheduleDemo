package com.eb.admin.service;

import java.util.List;
import java.util.Map;

public interface UserPriService {
	String dbkey = "ebweb";

	List<Map<String, Object>> userPriList(long uid) throws Exception;

	void addUserPri(long uid, long priid) throws Exception;

	void removeUserPri(long uid, long priid) throws Exception;

	List<Map<String, Object>> prilist() throws Exception;

	void addPriInfo(String name, String uri) throws Exception;

	void delPriInfo(String name) throws Exception;
}
