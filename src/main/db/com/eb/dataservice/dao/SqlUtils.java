package com.eb.dataservice.dao;

import java.util.HashMap;
import java.util.Map;

public class SqlUtils {
	private static final Map<String, String> sqlmap = new HashMap<String, String>();
	static {
		FileProperties props = new FileProperties(SqlUtils.class.getResource(
				"/sql.properties").getFile());
		for (Object key : props.getProp().keySet()) {
			Object v = props.getProperty(key.toString());
			sqlmap.put(key.toString(), v.toString());
		}
	}

	public static String getSql(String key) {
		return "/*" + key.toUpperCase() + "*/ " + sqlmap.get(key);
	}

}
