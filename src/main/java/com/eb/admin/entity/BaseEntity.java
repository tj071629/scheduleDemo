package com.eb.admin.entity;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class BaseEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	protected long id;
	protected int deleted;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public Timestamp getTimes() {
		return times;
	}

	public void setTimes(Timestamp times) {
		this.times = times;
	}

	protected Timestamp times = new java.sql.Timestamp(
			System.currentTimeMillis());

	public String getTimespan() {
		long from = times.getTime();
		long to = System.currentTimeMillis();
		int year = (int) ((to - from) / (365 * 1000 * 60 * 60 * 24));
		int month = (int) ((to - from) / (30 * 1000 * 60 * 60 * 24));
		int days = (int) ((to - from) / (1000 * 60 * 60 * 24));
		int hours = (int) ((to - from) / (1000 * 60 * 60));
		int minutes = (int) ((to - from) / (1000 * 60));
		if (year > 0) {
			return year + "年前";
		}
		if (month > 0) {
			return month + "月前";
		}
		if (days > 0) {
			return days + "天前";
		}
		if (hours > 0) {
			return hours + "小时前";
		}
		return minutes + "分钟前";
	}

	public String getSimpletime() {
		DateFormat df = new SimpleDateFormat("MM月dd日 HH:mm");
		return df.format(times);
	}
	
	public String getTime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(times);
	}
}
