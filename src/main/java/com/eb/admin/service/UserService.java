package com.eb.admin.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eb.admin.entity.Pri;
import com.eb.admin.entity.User;
import com.eb.dataservice.dao.EntityPage;
import com.eb.dataservice.dao.PageResultEntity;

public interface UserService {
	String dbkey = "ebread";
	String salt = "_ebBgAdmin^$&";
	//web前台加盐值
	String web_salt = "_eBreAd";

	PageResultEntity userlist(long accounttype,
			Date start, Date end, long  orgid,long istryout,String phone,String loginid,String nickname,long relationorgan,EntityPage page) throws Exception;

	String getWechatOpenid(long userid) throws Exception;

	int getUserRtStockNum(long userid) throws Exception;

	int getUserStockpoolNum(long userid) throws Exception;

	int getUserConditionNum(long userid) throws Exception;

	int getUserConsumption(long userid) throws Exception;

	int getUserBalance(long userid) throws Exception;

	int getUserOrdersNum(long userid) throws Exception;
	
	long getUserLoginNum(long userid) throws Exception;
	
	Map<String, Object> getUserLastLoginTime(long userid) throws Exception;
	
	long login(String username, String pwd) throws Exception;

	void saveToken(long userid, String token) throws Exception;

	long getuserid(String token) throws Exception;

	Map<String, Object> getIpByUserid(long userid) throws Exception;

	Map<String, Object> getProvinceCityById(String ip) throws Exception;

	String crtToken();

	Map<Long, Map<String, Object>> getUserInfos(Set<Long> userids)
			throws Exception;

	Map<String, Object> getUserInfo(long uid) throws Exception;
	
	User getUserById(long uid) throws Exception;

	Map<String, Object> getAdminInfo(long adminid) throws Exception;

	Map<String, Object> getCurrUinfo(long adminid) throws Exception;

	void updatePwd(long adminid, String pwd) throws Exception;

	List<Map<String, Object>> adminlist(String username) throws Exception;

	void addAdmin(long depid, int sex, String loginname, String pwd,
			String phone, String email);

	void delAdmin(long adminid);

	Map<String, Object> getAdminUserInfo(long adminid) throws Exception;

	long getAdminid(String adminname) throws Exception;

	List<Map<String, Object>> userSimCodes() throws Exception;

	void saveUcode(long uid, String simcode) throws Exception;
	
	List<Map<String, Object>> isExistLoginid(String loginid, long reguserid);
	
	boolean updateUser(User user) throws Exception;

	Map<String, Object> getReguser(long reguserid) throws Exception;

	void updateUser(long userid, String phone, String loginid) throws Exception;

	void updateUserInfo(long userid, String phone) throws Exception;

	void updateUserPwd(String pwd, long reguserid);

	List<User> listAccountIsExpire() throws Exception;

	List<User> listAccountWillExpire() throws Exception;

	List<User> userfilterlist() throws Exception;

	String convertTerminalType(Object object) throws Exception;

	List<Map<String, Object>> queryIpsWithoutAddress() throws Exception;

	void insertIpcacheProvinceCity(String ip, String province, String city) throws Exception;

	void updateIpcacheProvinceCity(String ip, String province, String city) throws Exception;
	
	List<User> getAllUsers() throws Exception;

	List<User> getAllOragin() throws Exception;

	List<User> getAllPerson() throws Exception;

	List<User> getAllMailOragin() throws Exception;

	List<User> getAllMailPerson() throws Exception;

	List<User> getAllSMSOragin() throws Exception;

	List<User> getAllSMSPerson() throws Exception;
	
	List<Pri> selectAllPris() throws Exception;
	
	Pri getPriById(Map<String, Object> map) throws Exception;
	
	void updateHasChild(Map<String, Object> map) throws Exception;

	void insertpri(Pri pri) throws Exception;

	void updatepri(Pri pri) throws Exception;
	
	void deletePriByIds(Set<Long> funcIdSet) throws Exception;

	List<Pri> getChildrenById(String... superId) throws Exception;

	List<Pri> getChildrenByDelId(String delId) throws Exception;

	List<Map<String, Object>> isExistPriName(String trim, long id) throws Exception;

	boolean isExistCurrentIp(String ip) throws Exception;

}
