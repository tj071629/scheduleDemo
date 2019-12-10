package com.eb.admin.entity;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class PageForJqGrid<T> {
	public static final String ASC = "asc";
	public static final String DESC = "desc";
	protected int page = 1;		//页码
	protected int rows = 10;	//每页显示数量
	protected String sidx = null;	//排序字段
	protected String sord = null;	//排序规则
	protected boolean autoCount = true;

	protected List<T> result = Lists.newArrayList();
	protected long total = -1L;	//总页数
	protected long records = -1L;	//总记录数

	
	private static final ThreadLocal<Integer> resultCountTL = new ThreadLocal<Integer>();

	public static void setResultCount(int count) {
		resultCountTL.set(count);
	}

	public static int getResultCount() {
		return resultCountTL.get();
	}

	public PageForJqGrid() {
	}
	
	public PageForJqGrid(int rows) {
		this.rows = rows;
	}

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;

		if (page < 1)
			this.page = 1;
	}

	public PageForJqGrid<T> page(int thePage) {
		setPage(thePage);
		return this;
	}

	public int getRows() {
		return this.rows;
	}

	public void setRows(int rows) {
		this.rows = rows;

		if (rows < 1)
			this.rows = 1;
	}

	public PageForJqGrid<T> rows(int theRows) {
		setRows(theRows);
		return this;
	}

	public int getFirst() {
		return (this.page - 1) * this.rows + 1;
	}

	public String getSidx() {
		return this.sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public PageForJqGrid<T> sidx(String theSidx) {
		setSidx(theSidx);
		return this;
	}

	public String getSord() {
		return this.sord;
	}

	public void setSord(String sord) {
		String[] Sords = StringUtils.split(StringUtils.lowerCase(sord), ',');
		for (String SordStr : Sords) {
			if ((!StringUtils.equals("desc", SordStr))
					&& (!StringUtils.equals("asc", SordStr))) {
				throw new IllegalArgumentException("排序方向" + SordStr + "不是合法值");
			}
		}

		this.sord = StringUtils.lowerCase(sord);
	}

	public PageForJqGrid<T> sord(String theSord) {
		setSord(theSord);
		return this;
	}

	public boolean isSidxSetted() {
		return (StringUtils.isNotBlank(this.sidx))
				&& (StringUtils.isNotBlank(this.sord));
	}

	public boolean isAutoCount() {
		return this.autoCount;
	}

	public void setAutoCount(boolean autoCount) {
		this.autoCount = autoCount;
	}

	public PageForJqGrid<T> autoCount(boolean theAutoCount) {
		setAutoCount(theAutoCount);
		return this;
	}

	public List<T> getResult() {
		return this.result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public long getTotal() {
		return this.total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getRecords() {
		return records;
	}

	public void setRecords(long records) {
		this.records = records;
	}

	public long getTotalPages() {
		if (this.records < 0L) {
			return -1L;
		}

		long count = this.records / this.rows;
		if (this.records % this.rows > 0L) {
			count += 1L;
		}
		return count;
	}

	public boolean isHasNext() {
		return this.page + 1 <= getTotalPages();
	}

	public int getNextPage() {
		if (isHasNext()) {
			return this.page + 1;
		}
		return this.page;
	}

	public boolean isHasPre() {
		return this.page - 1 >= 1;
	}

	public int getPrePage() {
		if (isHasPre()) {
			return this.page - 1;
		}
		return this.page;
	}
	
	public Map<String, Object> pageToMap(PageForJqGrid<T> page,Map<String, Object> map){
		if (!EmptyUtil.isEmpty(page.getPage()) && !EmptyUtil.isEmpty(page.getRows())) {
			map.put("offset",(page.getPage()-1)*page.getRows());
			map.put("limit", page.getRows());
		}
		if (page.isSidxSetted()) {
			map.put("sidx", page.getSidx());
			if (!PageForJqGrid.ASC.equals(page.getSord())) {
				map.put("sord", PageForJqGrid.DESC);
			}else {
				map.put("sord", PageForJqGrid.ASC);
			}
		}
		return map;
	}
	
	public PageForJqGrid<T> listToPage(PageForJqGrid<T> page,List<T> list){
		page.setRecords(getResultCount());
		page.setResult(list);
		page.setTotal(this.getTotalPages());
		//page.setTotalCount(PaginationInterceptor.getResultCount());
		return page;
	}
}
