package com.eb.admin.entity;

import java.math.BigDecimal;

/**
 * @author: Jason.Yan
 * @pageName: com.eb.admin.entity
 * @fileName: Function.java
 * @date: 2018-05-09
 * @doc: Function 实体类
 */
public class Pri extends BaseEntity {

	private static final long serialVersionUID = 5987668401617260164L;

    private String pid;
    
	private String priname;

    private String level;
    
    private String haschild;

    private String uri;

    private BigDecimal priorder;

    private String isdel;
    
    private long currUserid;
    
    public Pri(){
    	
    }

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPriname() {
		return priname;
	}

	public void setPriname(String priname) {
		this.priname = priname;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getHaschild() {
		return haschild;
	}

	public void setHaschild(String haschild) {
		this.haschild = haschild;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public BigDecimal getPriorder() {
		return priorder;
	}

	public void setPriorder(BigDecimal priorder) {
		this.priorder = priorder;
	}

	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

	public long getCurrUserid() {
		return currUserid;
	}

	public void setCurrUserid(long currUserid) {
		this.currUserid = currUserid;
	}

    


}