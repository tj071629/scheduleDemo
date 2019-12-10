package com.eb.admin.entity;

import java.util.Collection;

public class EmptyUtil {

	/**
	 * 判断字符串是否为空，长度为0被认为是空字符串.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 判断列表是否为空，列表没有元素也被认为是空
	 * 
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Collection collection) {
		return collection == null || collection.size() == 0;
	}

	/**
	 * 判断数组是否为空
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Object[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 判断对象是否为空
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null){
			return true;
		}
		else{
			return isEmpty(obj.toString());
		}
	}
}
