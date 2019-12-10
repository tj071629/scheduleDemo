package com.eb.dataservice.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbPro;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.thoughtworks.xstream.XStream;

public class CommonDao {
	private static Map<String, CommonDao> daomap = new HashMap<String, CommonDao>();

	public static final CommonDao getDao(String dbkey) {
		if (!loaded.get()) {
			new CommonDao().start();
		}
		return daomap.get(dbkey);
	}

	private static final java.util.concurrent.atomic.AtomicBoolean loaded = new AtomicBoolean(
			false);
	private static final java.util.concurrent.locks.Lock lock = new ReentrantLock();

	public void start() {
		lock.lock();
		try {
			if (!loaded.get()) {
				List<DBInfo> dblist = loaddblist();
				for (DBInfo dbinfo : dblist) {
					DataSource ds = createDatasource(dbinfo);
					String dbkey = dbinfo.getKey();
					String array[] = dbkey.split(",");
					for (String key : array) {
						if (key.trim().length() > 0) {
							ActiveRecordPlugin plugin = new ActiveRecordPlugin(
									key.trim(), ds);
							plugin.start();
							CommonDao dao = new CommonDao();
							dao.setDbkey(key.trim());
						}
					}
				}
			}
		} finally {
			loaded.set(true);
			lock.unlock();
		}

	}

	private static final DataSource createDatasource(DBInfo dbinfo) {
		BasicDataSource datasource = new BasicDataSource();
		datasource.setUrl(dbinfo.getUrl());
		datasource.setDriverClassName(dbinfo.getDriver());
		datasource.setMaxActive(dbinfo.getMaxconn());
		datasource.setInitialSize(1);
		datasource.setMaxIdle((int) (dbinfo.getMaxconn() / 5));
		datasource.setUsername(dbinfo.getUsername());
		datasource.setTestOnBorrow(true);
		datasource.setValidationQuery("select 1");
		datasource.setTestWhileIdle(true);
		datasource.setTimeBetweenEvictionRunsMillis(10000);
		datasource.setMinEvictableIdleTimeMillis(10000);
		datasource.setPassword(dbinfo.getPwd());
		return datasource;
	}

	private static final List<DBInfo> loaddblist() {
		XStream xstream = new XStream();
		xstream.alias("db", DBInfo.class);
		xstream.alias("list", List.class);
		InputStream stream = null;
		try {
			stream = CommonDao.class.getResourceAsStream("/config/dbinfo.xml");
			List<DBInfo> dblist = (List<DBInfo>) xstream.fromXML(stream);
			return dblist;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new ArrayList<DBInfo>();
	}

	private String dbkey;

	public String getDbkey() {
		return dbkey;
	}

	public void setDbkey(String dbkey) {
		this.dbkey = dbkey;
		daomap.put(dbkey, this);
	}

	public final List<Map<String, Object>> findList(String sql,
			Object... params) throws InstantiationException,
			IllegalAccessException {
		DbPro db = Db.use(dbkey);
		List<Record> list = db.find(sql, params);
		return toList(list);
	}

	public final Object findBean(Class cls, String sql, Object... params)
			throws Exception {
		DbPro db = Db.use(dbkey);
		Record record = db.findFirst(sql, params);
		return ORMappingUtils.mapping(record, cls);
	}

	public final List findBeanList(Class cls, String sql, Object... params)
			throws Exception {
		DbPro db = Db.use(dbkey);
		List<Record> list = db.find(sql, params);
		return ORMappingUtils.mappingRecords(list, cls);
	}

	public final Map<String, Object> findMap(String sql, Object... params)
			throws InstantiationException, IllegalAccessException {
		DbPro db = Db.use(dbkey);
		Record record = db.findFirst(sql, params);
		Map<String, Object> map = toMap(record);
		return map;
	}

	private static final List<Map<String, Object>> toList(List<Record> records) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Record r : records) {
			Map<String, Object> map = toMap(r);
			list.add(map);
		}
		return list;
	}

	private static final Map<String, Object> toMap(Record r) {
		if (r == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		String[] names = r.getColumnNames();
		Object[] values = r.getColumnValues();
		for (int i = 0; i < names.length; i++) {
			String name = names[i].toLowerCase();
			Object v = values[i];
			map.put(name, v);
		}
		return map;
	}

	public static void main(String[] args) {
		CommonDao dao = CommonDao.getDao("mycat");
		try {
			List list = dao.findList("select * from tbl_user limit 10");
			list.size();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public final int execute(String sql, Object... params) {
		DbPro db = Db.use(dbkey);
		return db.update(sql, params);
	}

	public final boolean transaction(IAtom atom) {
		DbPro db = Db.use(dbkey);
		return db.tx(atom);
	}

	public final int executeSql(String sql, Object... params) {
		DbPro db = Db.use(dbkey);
		return db.update(sql, params);
	}
}
