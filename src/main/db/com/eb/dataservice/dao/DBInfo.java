package com.eb.dataservice.dao;

public class DBInfo {
	private String key, url, pwd, username, driver;
	private int maxconn = 100;
	private boolean defaultdb = false;

	public boolean isDefaultdb() {
		return defaultdb;
	}

	public void setDefaultdb(boolean defaultdb) {
		this.defaultdb = defaultdb;
	}

	public int getMaxconn() {
		return maxconn;
	}

	public void setMaxconn(int maxconn) {
		this.maxconn = maxconn;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}
}
