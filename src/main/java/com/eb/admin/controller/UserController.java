package com.eb.admin.controller;

import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eb.admin.entity.User;
import com.eb.admin.service.MessageService;
import com.eb.admin.service.OrganService;
import com.eb.admin.service.RoleService;
import com.eb.admin.service.UserIndexCountService;
import com.eb.admin.service.UserMsgService;
import com.eb.admin.service.UserService;
import com.eb.dataservice.dao.EntityPage;
import com.eb.dataservice.dao.MD5Utils;
import com.eb.dataservice.dao.PageResultEntity;

@Controller
@RequestMapping("/user")
@Scope("prototype")
public class UserController extends AbstractController {
	@Autowired
	private UserService userService;
	@Autowired
	private UserIndexCountService userIndexCountService;
	@Autowired
	private UserMsgService userMsgService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private OrganService organService;
	@Autowired
	private RoleService roleService;
	
	@RequestMapping(value = { "welcome.html" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public ModelAndView welcome(final ModelMap modelMap) throws Exception {
		long uid = this.getCurrUserid();
		Map<String, Object> uinfo = userService.getCurrUinfo(uid);
		modelMap.put("uinfo", uinfo);
		return new ModelAndView("user/welcome");
	}
	
	@RequestMapping(value = { "uinfo.html" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public ModelAndView index(final ModelMap modelMap) throws Exception {
		long uid = this.getCurrUserid();
		Map<String, Object> uinfo = userService.getCurrUinfo(uid);
		modelMap.put("uinfo", uinfo);
		
		List<Map<String, Object>> rolelist = this.roleService.roleList("");
		List<Map<String, Object>> userrolelist = this.roleService.userroleList(uid);
		Set<Long> roleids = new HashSet<Long>();
		for (Map<String, Object> role : userrolelist) {
			long id = (long) role.get("id");
			roleids.add(id);
		}
		for (Map<String, Object> role : rolelist) {
			long id = (long) role.get("id");
			role.put("checked", false);
			if (roleids.contains(id)) {
				role.put("checked", true);
			}
		}
		modelMap.put("rolelist", rolelist);
		return new ModelAndView("user/uinfo");
	}

	@RequestMapping(value = { "/editpwd.html" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String editpwd(final ModelMap modelMap) throws Exception {
		long reguserid = this.getLongValue("reguserid", modelMap, -1);
		modelMap.put("reguserid", reguserid);
		return "user/modifypwd";
	}
	
	@RequestMapping(value = { "modifypwd" }, produces = "application/json; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public Object modifypwd(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		String pwd = this.getValue("pwd", modelMap, "");
		String newpwd = this.getValue("npwd", modelMap, "");
		pwd = MD5Utils.md5(pwd + UserService.salt);
		newpwd = MD5Utils.md5(newpwd + UserService.salt);
		long uid = this.getCurrUserid();
		Map<String, Object> uinfo = this.userService.getCurrUinfo(uid);
		String prepwd = (String) uinfo.get("pwd");
		rtnmap.put("success", -1);
		if (prepwd.equalsIgnoreCase(pwd)) {
			userService.updatePwd(uid, newpwd);
			rtnmap.put("success", 1);
		} else {
			rtnmap.put("msg", "原密码输入错误");
		}
		return rtnmap;
	}
	
	@RequestMapping(value = { "savepwd" }, produces = "application/json; charset=utf-8", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public Object savepwd(final ModelMap modelMap) throws Exception {
		
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		
		////TODO 此句后期需要删除 （前期只能治哥有统一修改用户密码的权限）
		Long currUserid = this.getCurrUserid();
		if(this.getCurrUserid() == 5 || currUserid == 2){
			String pwd = this.getValue("pwd", modelMap);
			long reguserid = this.getLongValue("reguserid", modelMap, -1);
			pwd = MD5Utils.md5(pwd);
			String newpwd = MD5Utils.md5(pwd + UserService.web_salt);
			this.userService.updateUserPwd(newpwd, reguserid);
			rtnmap.put("success", true);
		}else{
			rtnmap.put("success", false);
			rtnmap.put("msg", "目前只允许治哥账号修改用户密码");
		}
		return rtnmap;
	}
	
	@RequestMapping(value = { "reguserlist.html" }, method = {
			RequestMethod.GET, RequestMethod.POST })
	public String regusers(final ModelMap modelMap) throws Exception {
		EntityPage page = this.getPageInfo(modelMap);
		
		long accounttype = this.getLongValue("accounttype", modelMap, 0);
		long relationorgan = this.getLongValue("relationorgan", modelMap, 0);
		String loginid = this.getValue("loginid", modelMap);
		String phone = this.getValue("phone", modelMap);
		Date startDate = this.getDateValue("startdate", modelMap);
		Date endDate = this.getDateValue("enddate", modelMap);

		PageResultEntity pageResultEntity = userService.userlist(accounttype,startDate, endDate,-10,-10,phone,loginid,null,relationorgan,page);
		List<Map> list = pageResultEntity.getList();
		for (Map<String, Object> map : list) {
			long userid = (long) map.get("id");
			String wxopenid = this.userService.getWechatOpenid(userid);
			map.put("wxopenid", wxopenid);
			map.put("bindwx", wxopenid != null);
			Map<String, Object> reginfomap = this.userService.getIpByUserid(userid);
			if (reginfomap != null && reginfomap.containsKey("ip")) {
				Map<String, Object> addressMap = userService.getProvinceCityById(reginfomap.get("ip").toString());
				if(addressMap != null){
					map.put("province", addressMap.get("province"));
					map.put("city", addressMap.get("city"));
				}
			}
			long orgid = (long) map.get("orgid");
			Map<String,Object> organObj = organService.getOrgan(orgid);
			map.put("organname",organObj==null?"":organObj.get("organname"));
		}
		modelMap.put("relationorgan", relationorgan);
		modelMap.put("result", pageResultEntity);
		return "user/reguserlist";
	}

	@RequestMapping(value = { "tradeuserlist.html" }, method = {
			RequestMethod.GET, RequestMethod.POST })
	public String tradeuserlist(final ModelMap modelMap) throws Exception {
		EntityPage page = this.getPageInfo(modelMap);
		long accounttype = this.getLongValue("accounttype", modelMap, 0);
		long relationorgan = this.getLongValue("relationorgan", modelMap, 0l);
		Date startDate = this.getDateValue("startdate", modelMap);
		Date endDate = this.getDateValue("enddate", modelMap);
		long orgid = this.getLongValue("orgid", modelMap, -10);
		long istryout = this.getLongValue("istryout", modelMap, -10);
		/*long logintype = this.getLongValue("logintype", modelMap, -10);*/
		String phone = this.getValue("phone", modelMap);
		String loginid = this.getValue("loginid", modelMap);
		PageResultEntity pageResultEntity = userService.userlist(accounttype, startDate, endDate, orgid,istryout,phone,loginid,null,relationorgan,page);
		List<Map> list = pageResultEntity.getList();
		for (Map<String, Object> map : list) {
			long userid = (long) map.get("id");
			int balance = userService.getUserBalance(userid);
			int conditionnum = userService.getUserConditionNum(userid);
			int rtstocknum = userService.getUserRtStockNum(userid);
			int poolnum = userService.getUserStockpoolNum(userid);
			int consumption = userService.getUserConsumption(userid);
			int ordernum = userService.getUserOrdersNum(userid);
			long loginnum = userService.getUserLoginNum(userid);
			Map<String,Object> logininfo = userService.getUserLastLoginTime(userid);
			Date lastlogintime = null;
			String channel = null;
			if(logininfo != null){
				lastlogintime = (Date) logininfo.get("times");
				channel = logininfo.get("channel").toString();
			}
			Map<String, Object> indixmap = userIndexCountService.mostUseFullIndex(userid);
			Integer indexcounter =null;
			String indexname =null;
			if (indixmap != null && indixmap.size() > 0 ) {
				Long indexId = (Long) indixmap.get("indexid");
				indexcounter = (Integer) indixmap.get("counter");
				if (indexId != null) {
					indexname = userIndexCountService.getUseIndexName(indexId);
				}
			}
			long orgidT = (long) map.get("orgid");
			Map<String,Object> organObj = organService.getOrgan(orgidT);
			map.put("organname",organObj==null?"":organObj.get("organname"));
			map.put("useragent", userService.convertTerminalType(map.get("useragent")));
			map.put("indexname", indexname);
			map.put("indexcounter", indexcounter);
			map.put("loginnum", loginnum);
			map.put("lastlogintime", lastlogintime);
			map.put("channel", channel);
			map.put("balance", balance);
			map.put("conditionnum", conditionnum);
			map.put("rtstocknum", rtstocknum);
			map.put("poolnum", poolnum);
			map.put("consumption", consumption);
			map.put("ordernum", ordernum);
		}
		modelMap.put("relationorgan", relationorgan);
		modelMap.put("result", pageResultEntity);
		return "user/tradeuserlist";
	}
	@RequestMapping(value = { "/istryoutlist.html" }, method = {
			RequestMethod.GET, RequestMethod.POST })
	public String istryoutlist(final ModelMap modelMap) throws Exception {
		EntityPage page = this.getPageInfo(modelMap);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = this.getDateValue("startdate", modelMap);
		Date endDate = this.getDateValue("enddate", modelMap);
		String phone = this.getValue("phone", modelMap);
		String loginid = this.getValue("loginid", modelMap);
		String nickname = this.getValue("nickname", modelMap);
		long relationorgan = this.getLongValue("relationorgan", modelMap, 0l);
		PageResultEntity pageResultEntity = userService.userlist(0,startDate, endDate, 1,1,phone,loginid,nickname,relationorgan,page);
		List<Map> list = pageResultEntity.getList();
		for (Map<String, Object> map : list) {
			Date now = new Date();
			Date enddate =(Date) map.get("enddate");
			String nowtime = df.format(now);
			Date currdate = df.parse(nowtime);
			long surplusdays = 99999L;
			if (enddate != null) {
				 surplusdays = (long)(enddate.getTime() -currdate.getTime())/(1000 * 60 * 60 * 24);  
			}
			map.put("surplusdays",surplusdays);
			long orgid = (long) map.get("orgid");
			Map<String,Object> organObj = organService.getOrgan(orgid);
			map.put("organname",organObj==null?"":organObj.get("organname"));
		}
		modelMap.put("relationorgan", relationorgan);
		modelMap.put("result", pageResultEntity);
		return "user/istryoutlist";
	}
	@RequestMapping(value = { "/trytoformallist.html" }, method = {
			RequestMethod.GET, RequestMethod.POST })
	public String trytoformallist(final ModelMap modelMap) throws Exception {
		EntityPage page = this.getPageInfo(modelMap);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = this.getDateValue("startdate", modelMap);
		Date endDate = this.getDateValue("enddate", modelMap);
		String phone = this.getValue("phone", modelMap);
		String loginid = this.getValue("loginid", modelMap);
		String nickname = this.getValue("nickname", modelMap);
		long relationorgan = this.getLongValue("relationorgan", modelMap, 0l);
		PageResultEntity pageResultEntity = userService.userlist(0,startDate, endDate, -1,0,phone,loginid,nickname,relationorgan,page);
		List<Map> list = pageResultEntity.getList();
		for (Map<String, Object> map : list) {
			Date now = new Date();
			Date enddate =(Date) map.get("enddate");
			String nowtime = df.format(now);
			Date currdate = df.parse(nowtime);
			long surplusdays = 99999L;
			if (enddate != null) {
				surplusdays = (long)(enddate.getTime() -currdate.getTime())/(1000 * 60 * 60 * 24);  
			}
			map.put("surplusdays",surplusdays);
			long orgid = (long) map.get("orgid");
			Map<String,Object> organObj = organService.getOrgan(orgid);
			map.put("organname",organObj==null?"":organObj.get("organname"));
		}
		modelMap.put("relationorgan", relationorgan);
		modelMap.put("result", pageResultEntity);
		return "user/trytoformallist";
	}
	@RequestMapping(value = { "/organrenewed.html" }, method = {
			RequestMethod.GET, RequestMethod.POST })
	public String organrenewed(final ModelMap modelMap) throws Exception {
		long uid = this.getLongValue("userid", modelMap, -1);
		User user = userService.getUserById(uid);
		modelMap.put("user", user);
		return "user/organrenewed";
	}
	
	@RequestMapping(value = { "/tryformalrenew.html" }, method = {
			RequestMethod.GET, RequestMethod.POST })
	public String tryformalrenew(final ModelMap modelMap) throws Exception {
		long uid = this.getLongValue("userid", modelMap, -1);
		User user = userService.getUserById(uid);
		modelMap.put("user", user);
		return "user/tryformalrenew";
	}
	
	@RequestMapping(value = { "/querytryoutuser.html" }, method = {
			RequestMethod.GET, RequestMethod.POST })
	public String querytryoutuser(final ModelMap modelMap) throws Exception {
		long uid = this.getLongValue("userid", modelMap, -1);
		User user = userService.getUserById(uid);
		if (user != null) {
			Map<String, Object> userinfo = userService.getReguser(uid);
			if (userinfo!= null) {
				String email = (String) userinfo.get("email");
				modelMap.put("email", email);
			}
		}
		
		modelMap.put("user", user);
		return "user/querytryoutuser";
	}

	
	/**
	 * 用户注册
	 */
	@RequestMapping(value = "/register", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public Object register(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnvalue = new HashMap<String, Object>();
		rtnvalue.put("success", -1);
		String loginid = this.getValue("loginid", modelMap);
		String pwd = this.getValue("pwd", modelMap);
		String param = "loginid="+loginid+"&pwd="+pwd+"&organid=10001";
		System.out.println("请求参数为:  " + param );
		Map<String, List<String>> map = sendGet(param);
		if (map != null) {
			System.out.println("响应状态为:   "+ map.toString());
		}
		return rtnvalue;
	}
	
	@RequestMapping(value = { "/editreguser.html" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String editorgancode(final ModelMap modelMap) throws Exception {
		
		long reguserid = this.getLongValue("reguserid", modelMap, -1L);
		
		Map<String, Object> reguser = this.userService.getReguser(reguserid);
		modelMap.put("reguser", reguser);
		modelMap.put("reguserid", reguserid);
		
		return "user/editreguser";
	}
	@RequestMapping(value = { "/switchuser.html" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String switchuser(final ModelMap modelMap) throws Exception {
		long reguserid = this.getLongValue("reguserid", modelMap, -1L);
		modelMap.put("reguserid", reguserid);
		return "user/switchuser";
	}
	
	@RequestMapping(value = { "/isexistloginid" }, produces = "application/json; charset=utf-8", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public String isexistloginid(final ModelMap modelMap) throws Exception {
		
		String msg = null;
		long reguserid = this.getLongValue("reguserid", modelMap, -1L);
		String loginid = this.getValue("loginid", modelMap);
		
		if (loginid != null && loginid != "") {
			List<Map<String, Object>> list = this.userService.isExistLoginid(loginid.trim(), reguserid);
			if (list.size() > 0) {
				msg = "用户名已存在，请重新输入";
			}
		}

		return msg;
	}
	
	@RequestMapping(value = { "/savereguser" }, method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public Object savereguser(final ModelMap modelMap) throws Exception {
		
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		
		long userid = this.getLongValue("reguserid", modelMap, -1L);
		String loginid = this.getValue("loginid", modelMap);
		String phone = this.getValue("phone", modelMap);
		String email = this.getValue("email", modelMap);
		
		try {
			
			if (userid > 0) {
				this.userService.updateUser(userid, phone, loginid);
				this.userService.updateUserInfo(userid, email);
				rtnmap.put("success", true);
			} 
			
		} catch (Exception e) {
			rtnmap.put("success", false);
		}
		
		return rtnmap;
	}
	
	@RequestMapping(value = { "/createreguser.html" }, method = {
			RequestMethod.GET, RequestMethod.POST })
	public String createreguser(final ModelMap modelMap) throws Exception {
		return "user/createreguser";
	}

	/**
     * 向指定URL发送GET方法的请求
     */
	private static final String url = "http://172.17.133.211/batreg"; 
    public static Map<String, List<String>>  sendGet(String param) {
    	Map<String, List<String>> map =null;
        try {
            String urlNameString = url + "?" + param+"&accesskey=lianghua9fbank";
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            // 获取所有响应头字段
            map = connection.getHeaderFields();
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        return map;
    }
    
    //账号删除
	@ResponseBody
	@RequestMapping(value = { "/deleteuser" }, produces = "application/json; charset=utf-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	public Object deleteUserByid(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		rtnmap.put("success", -1);
		long uid = this.getLongValue("userid", modelMap, -1);
		boolean result = false;
		if (uid > 0) {
			User user = userService.getUserById(uid);
			if (user != null) {
				user.setDeleted(1);
				result = userService.updateUser(user);
			}
		}
		if (result) {
			rtnmap.put("success", 1);
		}
		return rtnmap;
	}
	@ResponseBody
	@RequestMapping(value = { "/delusers" }, produces = "application/json; charset=utf-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	public Object delusers(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		String ids = this.getValue("userids", modelMap);
		if (ids.endsWith(",")) {
			 ids = ids.substring(0, ids.length()-1);
		}
		rtnmap.put("success", -1);
		boolean result = true;
		boolean flag =false;
		String[] idArray = ids.split(",");
		for (String str : idArray) {
			long  uid =Long.parseLong(str);
			if (uid > 0) {
				User user = userService.getUserById(uid);
				if (user != null) {
					user.setDeleted(1);
					result = userService.updateUser(user);
				}
			}
			if (!result) {
				flag =true;
			}
		}
		if (!flag) {
			rtnmap.put("success", 1);
		}
		return rtnmap;
	}
	
	@RequestMapping(value = { "seltemplatetrade.html" }, method = {
			RequestMethod.GET, RequestMethod.POST })
	public String seltemplatetrade(final ModelMap modelMap) throws Exception {
		EntityPage page = this.getPageInfo(modelMap);
		long uid = this.getLongValue("userid", modelMap, -1);
		String templatename = this.getValue("templatename", modelMap);
		String content = this.getValue("content", modelMap); 

		PageResultEntity pageResultEntity = messageService.messagetemplatelist(templatename, 0, content, page);
		modelMap.put("userid", uid);
		modelMap.put("result", pageResultEntity);
		modelMap.put("datas", pageResultEntity.getList());
		modelMap.put("page", pageResultEntity.getPageinfo());
		return "user/templatemodalselect";
	}
	
	@RequestMapping(value = { "selcustomtrade.html" }, method = {
			RequestMethod.GET, RequestMethod.POST })
	public String selcustomtrade(final ModelMap modelMap) throws Exception {
		long uid = this.getLongValue("userid", modelMap, -1);
		modelMap.put("userid", uid);
		return "user/editmessagecustom";
	}
	
	//账号冻结
	@ResponseBody
	@RequestMapping(value = { "/freezeuser" }, produces = "application/json; charset=utf-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	public Object freezeuser(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		rtnmap.put("success", -1);
		long uid = this.getLongValue("userid", modelMap, -1);
		boolean result = false;
		if (uid > 0) {
			User user = userService.getUserById(uid);
			if (user != null) {
				user.setFreeze(1);
				result = userService.updateUser(user);
			}
		}
		if (result) {
			rtnmap.put("success", 1);
		}
		return rtnmap;
	}
	//账号解冻
	@ResponseBody
	@RequestMapping(value = { "/unfreezeuser" }, produces = "application/json; charset=utf-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	public Object unfreezeuser(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		rtnmap.put("success", -1);
		long uid = this.getLongValue("userid", modelMap, -1);
		boolean result = false;
		if (uid > 0) {
			User user = userService.getUserById(uid);
			if (user != null) {
				user.setFreeze(0);
				result = userService.updateUser(user);
			}
		}
		if (result) {
			rtnmap.put("success", 1);
		}
		return rtnmap;
	}
	//转为正式账号
	@ResponseBody
	@RequestMapping(value = { "/transform" }, produces = "application/json; charset=utf-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	public Object tryout2official(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		rtnmap.put("success", -1);
		long uid = this.getLongValue("userid", modelMap, -1);
		boolean result = false;
		if (uid > 0) {
			User user = userService.getUserById(uid);
			if (user != null) {
				Long orgid = user.getOrgid();
				if (orgid != null) {
					if (orgid == -1 ) {
						orgid =this.getLongValue("orgid", modelMap, -1);
					}else{
						orgid = Math.abs(orgid);
					}
					user.setOrgid(orgid);
				}
				user.setIstryout(0);
				result = userService.updateUser(user);
			}
		}
		if (result) {
			rtnmap.put("success", 1);
		}
		return rtnmap;
	}
    
	//设置截止日期
	@ResponseBody
	@RequestMapping(value = { "/savedate" }, produces = "application/json; charset=utf-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	public Object savedate(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		rtnmap.put("success", -1);
		int accounttype = this.getIntValue("accounttype", modelMap, -1);
		long uid = this.getLongValue("userid", modelMap, -1);
		Date enddate = this.getDateValue("enddate", modelMap);
		boolean result = false;
		if (uid > 0) {
			User user = userService.getUserById(uid);
			if (user != null) {
				Date dbenddate = user.getEnddate();
				user.setIstryout(accounttype);
				user.setOrgid(-user.getOrgid());
				if (enddate != null) {
					Date dbstartdate = user.getStartdate();
					if (enddate != null) {
						if (dbenddate == null) {
							dbenddate = new Date();
						}
						if (enddate.after(dbenddate)) {
							/*if (dbstartdate == null) {
								user.setStartdate(new Date());
							}*/
							user.setEnddate(enddate);
							result = userService.updateUser(user);
						}else{
							rtnmap.put("success", -2);
						}
					}
				}
			}
		}
		if (result) {
			rtnmap.put("success", 1);
		}
		return rtnmap;
	}
	@ResponseBody
	@RequestMapping(value = { "/sendmsg" }, produces = "application/json; charset=utf-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	public Object sendmsg(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		long userid = this.getLongValue("userid", modelMap, -1);
		String templatename = this.getValue("templatename", modelMap);
		String msg = this.getValue("msg", modelMap);
		rtnmap.put("success", -1);
		if (userid > 0 && msg != null && !"".equals(msg.trim())) {
			this.userMsgService.addMsg(userid, 1, "【管理员通知】", msg);
			rtnmap.put("success", 1);
		}else if(userid > 0 && templatename != null && !"".equals(templatename.trim())){
			this.userMsgService.addMsg(userid, 1, templatename, getTemplate(templatename,new HashMap<String, String>()));
			rtnmap.put("success", 1);
		}
		return rtnmap;
	}
	
	@ResponseBody
	@RequestMapping(value = { "/addorganinfo" }, produces = "application/json; charset=utf-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	public Object addOrganInfo(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		String msg = this.getValue("organname", modelMap);
		rtnmap.put("success", -1);
		return rtnmap;
	}

	
	
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserMsgService getUserMsgService() {
		return userMsgService;
	}

	public void setUserMsgService(UserMsgService userMsgService) {
		this.userMsgService = userMsgService;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public UserIndexCountService getUserIndexCountService() {
		return userIndexCountService;
	}

	public void setUserIndexCountService(UserIndexCountService userIndexCountService) {
		this.userIndexCountService = userIndexCountService;
	}
}
