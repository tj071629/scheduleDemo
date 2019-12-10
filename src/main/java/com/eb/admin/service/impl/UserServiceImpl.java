package com.eb.admin.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.eb.admin.entity.Function;
import com.eb.admin.entity.Pri;
import com.eb.admin.entity.User;
import com.eb.admin.service.UserService;
import com.eb.admin.utils.DateUtils;
import com.eb.dataservice.dao.CommonDao;
import com.eb.dataservice.dao.EntityPage;
import com.eb.dataservice.dao.PageResultEntity;
import com.eb.dataservice.dao.PageUtils;
import com.eb.dataservice.dao.SqlUtils;

public class UserServiceImpl implements UserService {
	@Override
	public PageResultEntity userlist(long accounttype,Date _start, Date _end,long  orgid, long istryout,String phone,String loginid,String nickname, long relationorgan, EntityPage page) throws Exception {
		PageResultEntity result = new PageResultEntity();
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.userlist");
		List<Object> params = new ArrayList<Object>();
		Date start = _start;
		Date end = _end;
		
		// ? and tbl_userinfo.regist_time between ? and ? order by tbl_user.id desc 
		if(accounttype > 0){
			sql += " and tbl_user.orgid > ? ";
			params.add(0);
		}else if(accounttype < 0){
			sql += " and tbl_user.orgid < ? ";
			params.add(0);
		}
		
		if (start == null) {
			start = new Date(0);
		}
		String start_str = DateUtils.convertDate2String("yyyy-MM-dd", start);
		sql += " and tbl_userinfo.regist_time >= ? ";
		params.add(start_str+" 00:00:00");
		
		if (end == null) {
			end = new Date();
		}
		
		String end_str = DateUtils.convertDate2String("yyyy-MM-dd", end);
		sql += " and tbl_userinfo.regist_time <= ? ";
		params.add(end_str+" 23:59:59");
		
		if (orgid != -10) {
			if (orgid == 0) {
				sql += " AND tbl_user.orgid < 0 ";
			}else if(orgid == -1){
				sql += " AND tbl_user.orgid < -1 ";
			}else{
				sql += " AND tbl_user.orgid > 0 ";
			}
		}
		if (istryout != -10) {
			sql += " AND tbl_user.istryout = ? ";
			params.add(istryout);
		}
		if (phone != null && !"".equals(phone.trim())) {
			sql += " AND tbl_user.phone like ? ";
			params.add(phone.trim()+"%");
		}
		if (loginid != null && !"".equals(loginid.trim())) {
			sql += " AND tbl_user.loginid like ?";
			params.add(loginid.trim()+"%");
		}
		if (nickname != null && !"".equals(nickname.trim())) {
			sql += " AND tbl_user.nickname like ?";
			params.add("%"+nickname.trim()+"%");
		}
		if (relationorgan != 0) {
			sql += " AND tbl_user.orgid = ? ";
			params.add(relationorgan);
		}
		sql += " order by tbl_user.id desc";
		List<Map> list = PageUtils.getValueListMap(dao, sql, page, params);
		result.setList(list);
		result.setPageinfo(page);
		return result;
	}
	
	@Override
	public List userfilterlist() throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.userfilterlist");
		return dao.findBeanList(User.class, sql);
	}

	@Override
	public List listAccountIsExpire() throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.listAccountIsExpire");
		return dao.findBeanList(User.class, sql, 0);
	}
	
	@Override
	public List listAccountWillExpire() throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.listAccountWillExpire");
		return dao.findBeanList(User.class, sql);
	}
	
	@Override
	public String getWechatOpenid(long userid) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.getWechatOpenid");
		Map<String, Object> map = dao.findMap(sql, userid, 4);
		if (map != null) {
			return (String) map.get("useropenid");
		}
		return null;
	}

	private int getValue(Object num) {
		if (num instanceof Number) {
			return ((Number) num).intValue();
		}
		if (num instanceof BigDecimal) {
			return ((BigDecimal) num).intValue();
		}
		return 0;
	}

	@Override
	public int getUserRtStockNum(long userid) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.getUserRtStockNum");
		Map<String, Object> map = dao.findMap(sql, userid);
		if (map != null) {
			Object v = map.get("counter");
			return getValue(v);
		}
		return 0;
	}

	@Override
	public int getUserStockpoolNum(long userid) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.getUserStockpoolNum");
		Map<String, Object> map = dao.findMap(sql, userid);
		if (map != null) {
			Object v = map.get("counter");
			return getValue(v);
		}
		return 0;
	}

	@Override
	public int getUserConditionNum(long userid) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.getUserConditionNum");
		Map<String, Object> map = dao.findMap(sql, userid);
		if (map != null) {
			Object v = map.get("counter");
			return getValue(v);
		}
		return 0;
	}

	@Override
	public int getUserConsumption(long userid) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.getUserConsumption");
		Map<String, Object> map = dao.findMap(sql, userid);
		if (map != null) {
			Object v = map.get("counter");
			return getValue(v);
		}
		return 0;
	}

	@Override
	public int getUserBalance(long userid) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.getUserBalance");
		Map<String, Object> map = dao.findMap(sql, userid);
		if (map != null) {
			Object v = map.get("counter");
			return getValue(v);
		}
		return 0;
	}

	@Override
	public long login(String username, String pwd) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.login");
		Map<String, Object> map = dao.findMap(sql, username, pwd);
		if (map != null && map.get("id") != null) {
			return (long) map.get("id");
		}

		return -1;
	}

	@Override
	public void saveToken(long userid, String token) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.saveToken");
		dao.execute(sql, userid, token, token);
	}

	@Override
	public long getuserid(String token) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.getuserid");
		Map<String, Object> map = dao.findMap(sql, token);
		if (map != null && map.containsKey("userid")) {
			return (long) map.get("userid");
		}
		return -1;
	}

	@Override
	public String crtToken() {
		return UUID.randomUUID().toString();
	}

	@Override
	public int getUserOrdersNum(long userid) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.getUserOrdersNum");
		Map<String, Object> map = dao.findMap(sql, userid);
		if (map != null) {
			Object v = map.get("counter");
			return getValue(v);
		}
		return 0;
	}

	@Override
	public Map<String, Object> getIpByUserid(long userid) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.getAddressByIp");
		Map<String, Object> map = dao.findMap(sql, userid);
		return map;
	}

	@Override
	public Map<String, Object> getProvinceCityById(String ip) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.getIpCity");
		Map<String, Object> map = dao.findMap(sql, ip);
		if (map != null) {
			return map;
		}
		return null;
	}

	@Override
	public Map<Long, Map<String, Object>> getUserInfos(Set<Long> userids)
			throws Exception {
		String sql = SqlUtils.getSql("UserService.getUserInfos");
		Map<Long, Map<String, Object>> uinfoMap = new HashMap<Long, Map<String, Object>>();
		CommonDao dao = CommonDao.getDao(dbkey);
		String _sql = sql.replace("ids", set2Str(userids));
		List<Map<String, Object>> list = dao.findList(_sql);
		for (Map<String, Object> map : list) {
			long uid = (long) map.get("id");
			uinfoMap.put(uid, map);
		}
		return uinfoMap;
	}
	

	@Override
	public Map<String, Object> getReguser(long reguserid)
			throws Exception {
		String sql = SqlUtils.getSql("UserService.getUserInfoById");
		CommonDao dao = CommonDao.getDao(dbkey);
		Map<String, Object> reguserVo = dao.findMap(sql, reguserid);
		
		return reguserVo;
	}
	
	@Override
	public void updateUser(long userid, String phone, String loginid) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.updateReguserUser");
		dao.execute(sql, loginid, phone, userid);
	}
	
	@Override
	public void updateUserInfo(long userid, String email) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.updateReguserUserInfo");
		dao.execute(sql, email, userid);
	}

	private String set2Str(Set<Long> ids) {
		StringBuilder sb = new StringBuilder();
		if (ids != null) {
			for (long id : ids) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(id);
			}
		}
		return sb.toString();
	}

	@Override
	public Map<String, Object> getAdminInfo(long adminid) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.getAdminInfo");
		Map<String, Object> map = dao.findMap(sql, adminid);
		return map;
	}
	

	@Override
	public Map<String, Object> getCurrUinfo(long adminid) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.getCurrUinfo");
		Map<String, Object> map = dao.findMap(sql, adminid);
		return map;
	}

	@Override
	public void updatePwd(long adminid, String pwd) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.updatePwd");
		dao.execute(sql, pwd, adminid);
	}

	@Override
	public List<Map<String, Object>> adminlist(String username) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.adminlist");
		StringBuffer sb = new StringBuffer();
		if(StringUtils.isNotBlank(username)){
			sb.append(sql).append(" and username like '%" + username + "%'").append(" order by id asc");
		}else{
			sb.append(sql).append(" order by id asc");
		}
		return dao.findList(sb.toString());
	}

	@Override
	public void addAdmin(long depid, int sex, String loginname, String pwd,
			String phone, String email) {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.addAdmin");
		dao.execute(sql, depid, sex, loginname, pwd, phone, email, depid, sex,
				loginname, pwd, phone, email);
	}

	@Override
	public void delAdmin(long adminid) {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.delAdmin");
		dao.execute(sql, adminid);
	}

	@Override
	public Map<String, Object> getAdminUserInfo(long adminid) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.getAdminUserInfo");
		return dao.findMap(sql, adminid);
	}

	@Override
	public long getAdminid(String adminname) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.getAdminid");
		Map<String, Object> map = dao.findMap(sql, adminname);
		if (map != null && map.containsKey("id")) {
			return (long) map.get("id");
		}
		return -1;
	}

	@Override
	public Map<String, Object> getUserInfo(long uid) throws Exception {
		Set<Long> userids = new HashSet<Long>();
		userids.add(uid);
		Map<Long, Map<String, Object>> list = this.getUserInfos(userids);
		if (list != null && list.size() > 0) {
			return list.get(uid);
		}
		return null;
	}
	@Override
	public User getUserById(long uid) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.getUserById");
		User user =(User) dao.findBean(User.class, sql , uid);
		return user;
	}
	@Override
	public List<Map<String, Object>> userSimCodes() throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.userSimCodes");
		return dao.findList(sql);
	}

	@Override
	public void saveUcode(long uid, String simcode) throws Exception {
		CommonDao dao = CommonDao.getDao("ebtrade");
		String sql = SqlUtils.getSql("UserService.saveUcode");
		dao.execute(sql, uid, simcode);
	}

	
	@Override
	public List<Map<String, Object>> isExistLoginid(String loginid, long reguserid) {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.isexistloginid");

		try {
			return dao.findList(sql, loginid, reguserid);
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ArrayList<Map<String, Object>>();
	}

	
	@Override
	public boolean updateUser(User user) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		final String sql = SqlUtils.getSql("UserService.updateUser");
		int result = dao.executeSql(sql,user.getLoginid(),
				user.getLogintype(), user.getPwd(), user.getPhone(),user.getDeleted(),
				user.getNickname(), user.getPic(), user.getWechatpic(),
				user.getInvitorcode(), user.getMyinvitorcode(),
				user.getOrgid(),user.getStartdate(),user.getEnddate(),user.getIstryout(),user.getFreeze() ,user.getId());
		return result>0?true:false;
	}
	@Override
	public Map<String,Object> getUserLastLoginTime(long userid) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.getUserLastLoginTime");
		Map<String, Object> map = dao.findMap(sql, userid);
		if (map != null) {
			return map;
		}
		return null;
	}

	@Override
	public long getUserLoginNum(long userid) throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.getUserLoginNum");
		Map<String, Object> map = dao.findMap(sql, userid);
		if (map != null) {
			long  loginNum= (long)map.get("loginnum");
			return loginNum;
		}
		return 0;
	}

	@Override
	public void updateUserPwd(String pwd, long reguserid) {
		
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.updateUserPwd");
		dao.execute(sql, pwd, reguserid);
	
	}

	@Override
	public String convertTerminalType(Object obj) throws Exception {
	    /**User Agent中文名为用户代理，简称 UA，它是一个特殊字符串头，使得服务器  能够识别客户使用的操作系统及版本、CPU 类型、浏览器及版本、浏览器渲染引擎、浏览器语言、浏览器插件等*/
		if(obj == null){
			return "";
		}
	    String agent = obj.toString();
	    //客户端类型常量
	    String type = "";
	    if(agent.contains("iPhone")||agent.contains("iPod")||agent.contains("iPad")||agent.contains("MobileSafari")){  
	        type = "苹果客户端";
	    } else if(agent.contains("Android") || agent.contains("Linux")) { 
	        type = "安卓客户端";
	    } else if(agent.indexOf("micromessenger") > 0){ 
	        type = "微信浏览器";
	    } else {
	        type = "pc";
	    }
	    return type;
	}

	@Override
	public List<Map<String, Object>> queryIpsWithoutAddress() throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.syncAddressByIp");
		return dao.findList(sql, 1000);
	}

	@Override
	public void insertIpcacheProvinceCity(String ip, String province, String city) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.insertIpcacheProvinceCity");
		dao.execute(sql, ip, province, city);
	}

	@Override
	public void updateIpcacheProvinceCity(String ip, String province, String city) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.updateIpcacheProvinceCity");
		dao.execute(sql, province, city, ip);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllUsers() throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.getallusers");
		return dao.findBeanList(User.class, sql);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllOragin() throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.getAllOragin");
		return dao.findBeanList(User.class, sql, 0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllPerson() throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.getAllPerson");
		return dao.findBeanList(User.class, sql, 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllMailOragin() throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.getAllMailOragin");
		return dao.findBeanList(User.class, sql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllMailPerson() throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.getAllMailPerson");
		return dao.findBeanList(User.class, sql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllSMSOragin() throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.getAllSMSOragin");
		return dao.findBeanList(User.class, sql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllSMSPerson() throws Exception {
		CommonDao dao = CommonDao.getDao(dbkey);
		String sql = SqlUtils.getSql("UserService.getAllSMSPerson");
		return dao.findBeanList(User.class, sql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pri> selectAllPris() throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.priList");
		return dao.findBeanList(Pri.class, sql, 0);
	}

	@Override
	public Pri getPriById(Map<String, Object> map) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.getPriById");
		return (dao.findBean(Pri.class, sql, map.get("id"))==null?null:(Pri)dao.findBean(Pri.class, sql, map.get("id")));
	}
	
	@Override
	public void updateHasChild(Map<String, Object> map) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.updateHasChild");
		dao.execute(sql, map.get("hasChild"), map.get("id"));
	}
	
	@Override
	public void insertpri(Pri pri) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.insertPri");
		dao.execute(sql, pri.getPriname(), pri.getLevel(), pri.getHaschild(), pri.getUri(), pri.getPid(), pri.getPriorder(), pri.getIsdel(), pri.getCurrUserid(), new Date());
	}
	
	@Override
	public void updatepri(Pri pri) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.updatePri");
		dao.execute(sql, pri.getPriname(), pri.getUri(), pri.getPriorder(), pri.getId());
	}
	
	@Override
	public void deletePriByIds(Set<Long> funcIdSet) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.deletePriById");
		String _sql = sql.replace("delIds", set2Str(funcIdSet));
		
		dao.execute(_sql);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pri> getChildrenById(String... argId) throws Exception {
		
		CommonDao dao = CommonDao.getDao("ebweb");
		if(argId.length != 1){
			String sql = SqlUtils.getSql("UserService.getChildrenById");
			return dao.findBeanList(Function.class, sql, argId[0], argId[1]);
		}else{
			String sql = SqlUtils.getSql("UserService.getChildren");
			return dao.findBeanList(Function.class, sql, argId[0]);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pri> getChildrenByDelId(String delId) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.getChildrenByDelId");
		return dao.findBeanList(Pri.class, sql, delId);
	}
	
	@Override
	public List<Map<String, Object>> isExistPriName(String priName, long id) {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.isexistpriname");

		try {
			return dao.findList(sql, priName, id);
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ArrayList<Map<String, Object>>();
	}

	@Override
	public boolean isExistCurrentIp(String ip) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("UserService.isExistCurrentIp");
		
		List<Map<String, Object>> list = dao.findList(sql, ip);
		if(list.size() != 0){
			return true;
		}else{
			return false;
		}
		
	}


}
