package com.eb.admin.utils;

import java.io.File;
import java.util.Map;

import com.eb.dataservice.dao.IOUtils;

public class TemplateUtils {
	public static String getTemplateText(String templatename,
			Map<String, String> params) {
		String template = IOUtils.read(new File("msgtemplates" + File.separator
				+ templatename), "utf-8");
		for (String key : params.keySet()) {
			String v = params.get(key);
			template = template.replace("${"+key+"}", v);
		}
		return template;
	}
}
