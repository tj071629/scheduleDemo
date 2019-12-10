package com.eb.dataservice.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbPro;
import com.jfinal.plugin.activerecord.Record;

public class PageUtils {
	private static int getAll(CommonDao dao, String _sql, Object... params) {
		try {
			DbPro db = Db.use(dao.getDbkey());
			String sql = "select sum(1) as num from (" + _sql + ") as tmptbl";
			Record record = db.findFirst(sql, params);
			if (record != null) {
				if (record.getBigDecimal("num") == null) {
					return 0;
				}
				return record.getBigDecimal("num").intValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static List getValueList(CommonDao dao, String _sql,
			EntityPage entitypage, Class cls, Object... params)
			throws SQLException {
		int num = getAll(dao, _sql, params);
		entitypage.setTotalrow(num);
		String sql = _sql + " limit " + entitypage.getBeginIndex() + ","
				+ entitypage.getRowinpage();
		entitypage.calculate();
		DbPro db = Db.use(dao.getDbkey());
		List<Record> list = db.find(sql, params);
		try {
			List rtnvalues = ORMappingUtils.mappingRecords(list, cls);
			return rtnvalues;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}


	public static List<Map> getValueListMap(CommonDao dao, String _sql,
			EntityPage entitypage, List<Object> params) throws SQLException {
		int num = getAll(dao, _sql, params.toArray());
		entitypage.setTotalrow(num);
		String sql = _sql + " limit " + entitypage.getBeginIndex() + ","
				+ entitypage.getRowinpage();
		entitypage.calculate();
		List<Map> rtnlist = new ArrayList<Map>();
		DbPro db = Db.use(dao.getDbkey());
		List<Record> list = db.find(sql, params.toArray());
		try {
			for (Record record : list) {
				String[] names = record.getColumnNames();
				Object[] values = record.getColumnValues();
				Map<String, Object> valuemap = new HashMap<String, Object>();
				for (int i = 0; i < names.length; i++) {
					String name = names[i];
					Object v = values[i];
					valuemap.put(name.toLowerCase(), v);
				}
				rtnlist.add(valuemap);
			}
			return rtnlist;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}
}
