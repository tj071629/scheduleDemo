package com.eb.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eb.admin.service.UserService;
import com.eb.dataservice.dao.MD5Utils;

@Controller
@RequestMapping("/login")
@Scope("prototype")
public class LoginController extends AbstractController {
	@RequestMapping(value = { "index.html" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String index(final ModelMap modelMap) {
		return "user/login";
	}

	
	@RequestMapping(value = { "dologin" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public Object dologin(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		String uname = this.getValue("uname", modelMap, "");
		String pwd = this.getValue("pwd", modelMap, "");
		pwd = MD5Utils.md5(pwd + UserService.salt);
		long uid = this.userService.login(uname, pwd);
		rtnmap.put("success", uid > 0);
		if (uid > 0) {
			String token = userService.crtToken();
			userService.saveToken(uid, token);
			rtnmap.put("token", token);
		}
		return rtnmap;
	}
	
	@RequestMapping(value = { "/editadminpwd.html" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String editadminpwd(final ModelMap modelMap) throws Exception {
		return "user/modifyadminpwd";
	}
	
	@RequestMapping(value = { "/isexistoripwd" }, produces = "application/json; charset=utf-8", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public String isexistoripwd(final ModelMap modelMap) throws Exception {
		
		String msg = null;
		String pwd = this.getValue("pwd", modelMap);
		
		pwd = MD5Utils.md5(MD5Utils.md5(pwd) + UserService.salt);
		
		long uid = this.getCurrUserid();
		Map<String, Object> uinfo = this.userService.getCurrUinfo(uid);
		String prepwd = (String) uinfo.get("pwd");
		
		if (!prepwd.equalsIgnoreCase(pwd)) {
			msg = "原密码校验错误，请重新输入";
		} 

		return msg;
	}
	
	@RequestMapping(value = { "modifyadminpwd" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public Object modifyadminpwd(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		String pwd = this.getValue("pwd", modelMap, "");
		String newpwd = this.getValue("npwd", modelMap, "");
		pwd = MD5Utils.md5(MD5Utils.md5(pwd) + UserService.salt);
		newpwd = MD5Utils.md5(MD5Utils.md5(newpwd) + UserService.salt);
		long uid = this.getCurrUserid();
		Map<String, Object> uinfo = this.userService.getCurrUinfo(uid);
		String prepwd = (String) uinfo.get("pwd");
		rtnmap.put("success", -1);
		if (prepwd.equalsIgnoreCase(pwd)) {
			userService.updatePwd(uid, newpwd);
			rtnmap.put("success", true);
		} else {
			rtnmap.put("success", false);
			rtnmap.put("msg", "原密码输入错误");
		}
		return rtnmap;
	}
	
	@RequestMapping(value = { "dologout.html" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String dologout(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		rtnmap.put("token", null);
		request.getSession().setAttribute("curruserid", null);
		rtnmap.put("success", true);
		
		return "user/login";
	}

	@Autowired
	private UserService userService;

}
