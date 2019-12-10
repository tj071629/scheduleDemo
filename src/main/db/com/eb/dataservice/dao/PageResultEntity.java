package com.eb.dataservice.dao;

import java.io.Serializable;
import java.util.List;

public class PageResultEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EntityPage pageinfo;

	public EntityPage getPageinfo() {
		return pageinfo;
	}

	public void setPageinfo(EntityPage pageinfo) {
		this.pageinfo = pageinfo;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	private List list;
}
