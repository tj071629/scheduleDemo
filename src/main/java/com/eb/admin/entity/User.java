package com.eb.admin.entity;

import java.util.Date;

public class User extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String loginid, logintype, pwd, phone, nickname, pic, wechatpic,
			invitorcode, myinvitorcode;
	private long orgid;
	private int istryout,freeze; //账号类型
	private Date startdate;
	
	private Date enddate;	

	public long getOrgid() {
		return orgid;
	}

	public void setOrgid(long orgid) {
		this.orgid = orgid;
	}

	public String getInvitorcode() {
		return invitorcode;
	}

	public void setInvitorcode(String invitorcode) {
		this.invitorcode = invitorcode;
	}

	public String getMyinvitorcode() {
		return myinvitorcode;
	}

	public void setMyinvitorcode(String myinvitorcode) {
		this.myinvitorcode = myinvitorcode;
	}

	public String getWechatpic() {
		return wechatpic;
	}

	public void setWechatpic(String wechatpic) {
		this.wechatpic = wechatpic;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPic() {
		if (pic == null || pic.length() < 1) {
			pic = "/img/actual_username.png";
		}
		try {
			if (pic.startsWith("/")) {
				String cdnpath = System.getProperty("cdndomain",
						"https://mt.quotedatas.com");
				String affix = System
						.getProperty(
								"imgstyle",
								"?imageMogr2/auto-orient/thumbnail/200x/format/jpg/blur/1x0/quality/75|imageslim");
				return cdnpath + pic + affix;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getLoginid() {
		return loginid;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

	public String getLogintype() {
		return logintype;
	}

	public void setLogintype(String logintype) {
		this.logintype = logintype;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getIstryout() {
		return istryout;
	}

	public void setIstryout(int istryout) {
		this.istryout = istryout;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public int getFreeze() {
		return freeze;
	}

	public void setFreeze(int freeze) {
		this.freeze = freeze;
	}

	@Override
	public String toString() {
		return "User [loginid=" + loginid + ", logintype=" + logintype + ", pwd=" + pwd + ", phone=" + phone
				+ ", nickname=" + nickname + ", pic=" + pic + ", wechatpic=" + wechatpic + ", invitorcode="
				+ invitorcode + ", myinvitorcode=" + myinvitorcode + ", orgid=" + orgid + ", istryout=" + istryout
				+ ", freeze=" + freeze + ", startdate=" + startdate + ", enddate=" + enddate + "]";
	}
}
