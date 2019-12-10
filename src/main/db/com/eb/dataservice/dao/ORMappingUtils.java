package com.eb.dataservice.dao;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Record;

public class ORMappingUtils {
	private static final java.util.Map<Class, Map<String, Field>> clsFieldsMap = new java.util.concurrent.ConcurrentHashMap<Class, Map<String, Field>>();

	public static List mapping(List<Map> valuelist, Class cls)
			throws InstantiationException, IllegalAccessException {
		if (!clsFieldsMap.containsKey(cls)) {
			ORMappingUtils.regedit(cls);
		}
		List result = new ArrayList();
		for (Map<String, Object> values : valuelist) {
			Object obj = mapping(values, cls);
			if (obj != null) {
				result.add(obj);
			}
		}
		return result;
	}

	public static List mappingRecords(List<Record> valuelist, Class cls)
			throws InstantiationException, IllegalAccessException {
		if (!clsFieldsMap.containsKey(cls)) {
			ORMappingUtils.regedit(cls);
		}
		List result = new ArrayList();
		for (Record record : valuelist) {
			Object obj = mapping(record, cls);
			if (obj != null) {
				result.add(obj);
			}
		}
		return result;
	}

	public static Object mapping(Record record, Class cls)
			throws InstantiationException, IllegalAccessException {
		if (record == null) {
			return null;
		}
		String[] names = record.getColumnNames();
		Object[] values = record.getColumnValues();
		Map<String, Object> valuemap = new HashMap<String, Object>();
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			Object v = values[i];
			valuemap.put(name, v);
		}
		return mapping(valuemap, cls);
	}

	public static Object mapping(Map<String, Object> values, Class cls)
			throws InstantiationException, IllegalAccessException {
		if (!ORMappingUtils.clsFieldsMap.containsKey(cls)) {
			try {
				ORMappingUtils.regedit(cls);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		Object newObj = cls.newInstance();
		Map<String, Field> fieldmap = clsFieldsMap.get(cls);
		for (String key : values.keySet()) {
			String lkey = key.toLowerCase();
			if (fieldmap.containsKey(lkey)) {
				Field field = fieldmap.get(lkey);
				Object v = values.get(key);
				if (v != null) {
					try {
						if(v instanceof BigInteger){
							v=((BigInteger)v).longValue();
						}
						field.set(newObj, v);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		return newObj;
	}

	public static void regedit(Class cls) {
		Field[] fields = cls.getDeclaredFields();
		Map<String, Field> newFields = new HashMap<String, Field>();
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				newFields.put(field.getName().toLowerCase(), field);
			}
			clsFieldsMap.put(cls, newFields);
		}
		Class parentcls = cls.getSuperclass();
		fields = parentcls.getDeclaredFields();
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				newFields.put(field.getName().toLowerCase(), field);
			}
			clsFieldsMap.put(cls, newFields);
		}
	}

	public static final Map<String, Object> toMap(BaseEntity entity) {
		Map<String, Object> valuemap = new HashMap<String, Object>();
		if (entity == null) {
			return null;
		}
		regedit(entity.getClass());
		Map<String, Field> fieldmap = clsFieldsMap.get(entity.getClass());
		for (String key : fieldmap.keySet()) {
			Field field = fieldmap.get(key);
			Object v;
			try {
				v = field.get(entity);
				if (v != null) {
					try {
						valuemap.put(key.toLowerCase(), v);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return valuemap;

	}


}
