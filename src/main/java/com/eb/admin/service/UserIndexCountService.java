package com.eb.admin.service;

import java.util.Map;

public interface UserIndexCountService {
	String dbkey = "ebread";
	Map<String, Object> mostUseFullIndex(long userid) throws Exception;
	
	String getUseIndexName(long indexid) throws Exception;
}
