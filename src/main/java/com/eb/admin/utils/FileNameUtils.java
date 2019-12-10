package com.eb.admin.utils;

import org.apache.commons.lang3.StringUtils;

public class FileNameUtils {
	public static String encodingFileName(String fileName) {
		String returnFileName = "";
		try {
			returnFileName = new String(fileName.getBytes("GB2312"),
					"ISO8859-1");
			returnFileName = StringUtils.replace(returnFileName, " ", "%20");
		} catch (Exception e) {
			e.printStackTrace();

		}
		return returnFileName;
	}

}
