package com.eb.admin.service;

public interface UserMsgService {
	String dbkey = "ebread";

	void addMsg(long userid, long sendid, String title, String content)
			throws Exception;
	
	void addMsg(long userid, long sendid, long type,String title, String content)
			throws Exception;
	
	String getTemplate(String name) throws Exception;

}
