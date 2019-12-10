package com.eb.dataservice.dao;

import java.util.TreeSet;

public class EntityPage implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int totalrow, totalpage, currpage = 1, rowinpage = 7;

	public TreeSet<Integer> getPagelist() {
		TreeSet<Integer> pages = new TreeSet<Integer>();
		int startpage = 1;
		if (currpage > 3) {
			startpage = currpage - 2;
		}
		if (currpage > totalpage - 9) {
			startpage = totalpage - 9;
		}
		if (startpage < 1) {
			startpage = 1;
		}
		for (int i = startpage; i <= totalpage && i < startpage + 10; i++) {
			pages.add(i);
		}
		return pages;
	}

	public int getNextPage() {
		return currpage + 1;
	}

	public int getPrePage() {
		return currpage - 1;
	}

	public boolean isPre() {
		return currpage > 1;
	}

	public boolean isNext() {
		return totalpage > currpage;
	}

	public int getTotalrow() {
		return totalrow;
	}

	public void setTotalrow(int totalrow) {
		this.totalrow = totalrow;
	}

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}

	public int getCurrpage() {
		return currpage;
	}

	public void setCurrpage(int currpage) {
		this.currpage = currpage;
	}

	public int getRowinpage() {
		return rowinpage;
	}

	public void setRowinpage(int rowinpage) {
		this.rowinpage = rowinpage;
	}

	public int getBeginIndex() {
		return (currpage - 1) * rowinpage;
	}

	public void calculate() {
		totalpage = totalrow / rowinpage;
		if (totalrow % rowinpage != 0) {
			totalpage += 1;
		}
	}
}
