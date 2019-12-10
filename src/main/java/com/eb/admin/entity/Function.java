package com.eb.admin.entity;

import java.math.BigDecimal;

/**
 * @author: Jason.Yan
 * @pageName: com.eb.admin.entity
 * @fileName: Function.java
 * @date: 2018-05-09
 * @doc: Function 实体类
 */
public class Function extends BaseEntity {

	private static final long serialVersionUID = 5987668401617260164L;

	private String funcName;

    private String funcCode;
    
    private String funcLevel;
    
    private String hasChild;

    private String iconCode;

    private String funcPath;

    private String superId;

    private BigDecimal funcOrder;

    private String isValid;

    private String isDel;
    
    private long currUserid;
    
    public Function(){
    	
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName == null ? null : funcName.trim();
    }

    public String getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode == null ? null : funcCode.trim();
    }

    public String getIconCode() {
        return iconCode;
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode == null ? null : iconCode.trim();
    }

    public String getFuncPath() {
        return funcPath;
    }

    public void setFuncPath(String funcPath) {
        this.funcPath = funcPath == null ? null : funcPath.trim();
    }

    public String getSuperId() {
        return superId;
    }

    public void setSuperId(String superId) {
        this.superId = superId == null ? null : superId.trim();
    }

    public BigDecimal getFuncOrder() {
        return funcOrder;
    }

    public void setFuncOrder(BigDecimal funcOrder) {
        this.funcOrder = funcOrder;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid == null ? null : isValid.trim();
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel == null ? null : isDel.trim();
    }

	public long getCurrUserid() {
		return currUserid;
	}

	public void setCurrUserid(long currUserid) {
		this.currUserid = currUserid;
	}

	public String getFuncLevel() {
		return funcLevel;
	}

	public void setFuncLevel(String funcLevel) {
		this.funcLevel = funcLevel;
	}

	public String getHasChild() {
		return hasChild;
	}

	public void setHasChild(String hasChild) {
		this.hasChild = hasChild;
	}


}