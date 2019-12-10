package com.eb.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.eb.admin.entity.PageForJqGrid;
import com.eb.admin.entity.Pri;
import com.eb.admin.service.RoleService;
import com.eb.admin.service.UserService;
import com.eb.admin.utils.jqgrid.TreeOptions;
import com.eb.admin.utils.jqgrid.TreeUtils;
import com.eb.dataservice.dao.MD5Utils;

@Controller
@RequestMapping("/admin")
@Scope("prototype")
public class AdminController extends AbstractController {
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = { "/rolelist.html" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String rolelist(final ModelMap modelMap) throws Exception {
		String rolename = this.getValue("rolename", modelMap);
		List<Map<String, Object>> roles = roleService.roleList(StringUtils.isNotBlank(rolename)?rolename.trim():null);
		modelMap.put("roleses", roles);
		return "admin/rolelist";
	}

	@RequestMapping(value = { "/editrole.html" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String editrole(final ModelMap modelMap) throws Exception {
		long roleid = this.getLongValue("roleid", modelMap, -1L);
		Map<String, Object> role = this.roleService.getRole(roleid);
		if (role == null) {
			role = new HashMap<String, Object>();
		}
		modelMap.put("role", role);
		modelMap.put("roleid", roleid);
		return "admin/editrole";
	}

	@RequestMapping(value = { "/edituser.html" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String edituser(final ModelMap modelMap) throws Exception {
		long userid = this.getLongValue("userid", modelMap, -1L);
		Map<String, Object> admin = this.userService.getAdminUserInfo(userid);
		if (admin == null) {
			admin = new HashMap<String, Object>();
			admin.put("sex", 0);
		}
		modelMap.put("admin", admin);
		modelMap.put("userid", userid);

		List<Map<String, Object>> rolelist = this.roleService.roleList("");
		List<Map<String, Object>> userrolelist = this.roleService
				.userroleList(userid);
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
		return "admin/edituser";
	}

	@RequestMapping(value = { "/editpri.html" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String editpri(final ModelMap modelMap) throws Exception {
		long priid = this.getLongValue("priid", modelMap, -1L);
		Map<String, Object> pri = this.roleService.getPri(priid);
		if (pri == null) {
			pri = new HashMap<String, Object>();
		}
		modelMap.put("pri", pri);
		modelMap.put("priid", priid);
		return "admin/editpri";
	}

	@RequestMapping(value = { "/saverole" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public Object saverole(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		long roleid = this.getLongValue("roleid", modelMap, -1L);
		String name = this.getValue("name", modelMap);
		if (roleid > 0) {
			roleService.updateRole(roleid, name);
		} else {
			roleService.addRole(name);
		}
		rtnmap.put("success", true);
		return rtnmap;
	}

	@RequestMapping(value = { "/savepri" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public Object savepri(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		long priid = this.getLongValue("priid", modelMap, -1L);
		String name = this.getValue("priname", modelMap);
		String uri = this.getValue("uri", modelMap);
		if (priid > 0) {
			roleService.updatePri(priid, name, uri);
		} else {
			roleService.addPri(name, uri);
		}
		rtnmap.put("success", true);
		return rtnmap;
	}

	@RequestMapping(value = { "/delrole" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public Object delrole(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		long roleid = this.getLongValue("roleid", modelMap, -1L);
		this.roleService.delRole(roleid);
		rtnmap.put("success", true);
		return rtnmap;
	}

	@RequestMapping(value = { "/rolepri.html" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String rolepri(final ModelMap modelMap) throws Exception {
		long roleid = this.getLongValue("roleid", modelMap, -1L);
		List<Map<String, Object>> prilist = roleService.prilist();
		List<Map<String, Object>> roleprilist = roleService.rolePrilist(roleid);
		modelMap.put("prilist", prilist);
		Set<Long> rolepriids = new HashSet<Long>();
		for (Map<String, Object> pri : roleprilist) {
			Object priid = pri.get("id");
			if (priid == null) {
				continue;
			}
			long id = (long) pri.get("id");
			rolepriids.add(id);
		}
		for (Map<String, Object> pri : prilist) {
			long id = (long) pri.get("id");
			pri.put("checked", false);
			if (rolepriids.contains(id)) {
				pri.put("checked", true);
			}
		}
		return "admin/rolepri";
	}

	@RequestMapping(value = { "/prilist.html" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String prilist(final ModelMap modelMap) throws Exception {
		List<Map<String, Object>> prilist = roleService.prilist();
		modelMap.put("prilist", prilist);
//		return "admin/prilist";
		return "admin/pri-index";
	}
	
	@RequestMapping(value = { "/prilist.json" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public Object prilistjson(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		List<Map<String, Object>> prilist = roleService.prilist();
		rtnmap.put("list", JSONArray.toJSON(prilist));
		return rtnmap;
	}
	
	/**
	 * @author: Jason.Yan
	 * @date: 2018-3-27
	 * @doc: 取所有树的数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listForTree.json")
	@ResponseBody
	public List getTreeList(PageForJqGrid<Pri> page, Pri pri) throws Exception{
		
		List<Pri> list = userService.selectAllPris();
		
		TreeOptions options = new TreeOptions(list);
		options.setId("id");
		options.setText("priname"); 
		options.setNodeLevel("level");
		options.setHasChild("haschild");
		options.setParentId("pid");
		options.setExtendedFields(new String[]{"uri","priorder"});
		options.setShowRoot(true);
		options.setOpenAll(true);
		options.setRootText("所有");
		
		return TreeUtils.getListForJqgridTree(options);
	}
	
	
	
	
	
	
	
	
	
	/**
	 * @author: Jason.Yan
	 * @date: 2018-3-27
	 * @doc: 新增
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/inputNew" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String inputNew(final ModelMap modelMap) throws Exception {
		long selectId = this.getLongValue("selectId", modelMap, -1L);
		modelMap.put("id", selectId);
		Pri pri = userService.getPriById(modelMap);
		modelMap.put("superPri", pri);
		return "admin/pri-input";
	}
	
	
	/**
	 * @author: Jason.Yan
	 * @date: 2018-3-27
	 * @doc: 修改
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/inputEdit" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String inputEdit(final ModelMap modelMap) throws Exception {
		long priId = this.getLongValue("priId", modelMap, -1L);
		Map<String, Object> map =new HashMap<String, Object>();
		map.put("id", priId);
		Pri pri = userService.getPriById(map);
		map.put("id", pri.getPid());
		Pri superPri = userService.getPriById(map);
		modelMap.put("pri", pri);
		modelMap.put("superPri", superPri);
		return "admin/pri-input";
		
	}
	
	@RequestMapping(value = { "/isexistpriname" }, produces = "application/json; charset=utf-8", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public String isexistpriname(final ModelMap modelMap) throws Exception {
		
		String msg = null;
		long id = this.getLongValue("id", modelMap, -1L);
		String priname = this.getValue("priname", modelMap);
		
		if (priname != null && priname != "") {
			List<Map<String, Object>> list = userService.isExistPriName(priname.trim(), id);
			if (list.size() > 0) {
				msg = "权限名称已存在，请重新输入";
			}
		}

		return msg;
	}
	
	/**
	 * @author: Jason.Yan
	 * @date: 2018-3-27
	 * @doc: 按ID取出AuPri对象
	 * @param pri
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/savepri" }, produces = "application/json; charset=utf-8", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public Object savepri(Pri pri) throws Exception {
		
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		
		pri.setCurrUserid(getCurrUserid());
		
		if (pri.getId() == 0){
			pri.setIsdel("0");
			pri.setHaschild("0");
			
			if (pri.getPid() != null){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("hasChild", "1");
				map.put("id", pri.getPid());
				userService.updateHasChild(map);
				
				//处理Pri.priLevel值 
				map.put("id", pri.getPid());
				Pri superPri = userService.getPriById(map);
				if (superPri != null && superPri.getLevel() != null){
					pri.setLevel(String.valueOf(Integer.valueOf(superPri.getLevel()) + 1));
				}else{
					pri.setLevel("1");
				}
			}else{
				pri.setLevel("1");
			}
			
			userService.insertpri(pri);
		}else{
			userService.updatepri(pri);
		}
		
		
		rtnmap.put("success", true);
		
		return rtnmap;
		
	}
	
	
	/**
	 * @author: Jason.Yan
	 * @date: 2018-5-11
	 * @doc: 删除
	 * @param delId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/deletepri" }, produces = "application/json; charset=utf-8", method = { RequestMethod.GET,RequestMethod.POST })
	@ResponseBody
	public Object deletepri(final ModelMap modelMap) throws Exception{

		String delIds = this.getValue("delIds", modelMap);
		
		Map<String, Object> rtnmap = new HashMap<String, Object>();

		String[] ids = delIds.split(",");
		
		Long currUserid = this.getCurrUserid();
		
		if(currUserid == 2 || currUserid == 7){
			
			try{
				
				//判断这些节点的父节点是否还有子项， 没有了就把haschild改为0
				for (int i = 0; i < ids.length; i++) {
					String priId = ids[i];
					//查询删除项对象
					Map<String, Object> map =new HashMap<String, Object>();
					map.put("id", priId);
					Pri pri =  userService.getPriById(map);
					
					if(pri.getPid()!=null && !"".equals(pri.getPid())){
						
						List<Pri> list = new ArrayList<Pri>();
						
						if(StringUtils.isNotBlank(pri.getPid())){
							//查询父节点对象旗下的子节点
							list = userService.getChildrenById(pri.getPid(), priId); 
						}else{
							//查询父节点对象旗下的子节点
							list = userService.getChildrenById(priId);
						}
						
						//子节点为空，就把haschild改为0
						if(list==null || list.size()==0){
							Map<String, Object> map2 =new HashMap<String, Object>();
							map2.put("id", pri.getPid());
							map2.put("hasChild", "0");
							userService.updateHasChild(map2);
						}
					}
				}
				
				Set<Long> funcIdSet = new HashSet<Long>();
				//将当前节点包含至删除列表
				funcIdSet.add(Long.valueOf(delIds));
				//级联删除，删除当前所选delId下的所有层级菜单
				cascadeDeleteFuncById(delIds, funcIdSet);
				
				//如果没有下级则删除当前节点
				userService.deletePriByIds(funcIdSet);
				
				rtnmap.put("success", true);
				
			}catch(Exception e){
				
				rtnmap.put("success", false);
				rtnmap.put("msg", "删除失败，请联系管理员！");
			}
			
		}else{
			rtnmap.put("success", false);
			rtnmap.put("msg", "目前只允许治哥账号删除菜单权限！");
		}
		
		return rtnmap;
	}
	
	
	private void cascadeDeleteFuncById(String delId, Set<Long> funcIdSet) throws Exception {
		//查找父ID为当前所选delId的 子集合
		List<Pri> list = userService.getChildrenByDelId(delId); 
		
		//遍历子集
		for (Pri function : list) {
			
			funcIdSet.add(function.getId());
			
			cascadeDeleteFuncById(function.getId()+"", funcIdSet);
		}
	}
	

	@RequestMapping(value = { "/userlist.html" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public String userlist(final ModelMap modelMap) throws Exception {
		String username = this.getValue("username", modelMap);
		List<Map<String, Object>> adminlist = this.userService.adminlist(StringUtils.isNotBlank(username)?username.trim():null);
		modelMap.put("adminlist", adminlist);
		for (Map<String, Object> admin : adminlist) {
			long id = (long) admin.get("id");
			String roles = this.getRoles(id);
			admin.put("roles", roles);
		}
		return "admin/userlist";
	}

	private String getRoles(long adminid) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Map<String, Object>> list = this.roleService.userroleList(adminid);
		for (Map<String, Object> role : list) {
			sb.append(role.get("name"));
			sb.append(",");
		}
		return sb.toString();
	}

	@RequestMapping(value = { "/userrolelist.html" }, method = {
			RequestMethod.GET, RequestMethod.POST })
	public String userrolelist(final ModelMap modelMap) throws Exception {
		long userid = this.getLongValue("userid", modelMap, -1L);
		List<Map<String, Object>> rolelist = this.roleService.roleList("");
		List<Map<String, Object>> userrolelist = this.roleService
				.userroleList(userid);
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
		return "admin/userrolelist";
	}

	@RequestMapping(value = { "/addadmin" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public Object addadmin(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		String name = this.getValue("name", modelMap);
		String email = this.getValue("email", modelMap);
		String phone = this.getValue("phone", modelMap);
		int sex = this.getIntValue("sex", modelMap, 1);
		long depid = this.getLongValue("depid", modelMap, -1L);
		String pwd = this.getValue("pwd", modelMap, "");
		pwd = MD5Utils.md5(MD5Utils.md5(pwd) + UserService.salt);
		this.userService.addAdmin(depid, sex, name, pwd, phone, email);
		rtnmap.put("success", true);
		return rtnmap;
	}

	@RequestMapping(value = { "/deladmin" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public Object deladmin(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		long adminid = this.getLongValue("id", modelMap, -1L);
		this.userService.delAdmin(adminid);
		rtnmap.put("success", true);
		return rtnmap;
	}

	@RequestMapping(value = { "/adduserrole" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public Object adduserrole(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		String adminname = this.getValue("adminname", modelMap);
		long userid = this.userService.getAdminid(adminname);
		long roleid = this.getLongValue("roleid", modelMap, -1L);
		this.roleService.addUserRole(userid, roleid);
		rtnmap.put("success", true);
		return rtnmap;
	}

	@RequestMapping(value = { "/deluserrole" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public Object deluserrole(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		String adminname = this.getValue("adminname", modelMap);
		long userid = this.userService.getAdminid(adminname);
		long roleid = this.getLongValue("roleid", modelMap, -1L);
		this.roleService.delUserRole(userid, roleid);
		rtnmap.put("success", true);
		return rtnmap;
	}

	@RequestMapping(value = { "/addpri" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public Object addpri(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		long priid = this.getLongValue("priid", modelMap, -1L);
		String name = this.getValue("priname", modelMap);
		String uri = this.getValue("uri", modelMap);
		if (priid > 0) {
			this.roleService.updatePri(priid, name, uri);
		} else {
			this.roleService.addPri(name, uri);
		}
		rtnmap.put("success", true);
		return rtnmap;
	}

	@RequestMapping(value = { "/delpri" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public Object delpri(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		long priid = this.getLongValue("priid", modelMap, -1L);
		this.roleService.delPri(priid);
		rtnmap.put("success", true);
		return rtnmap;
	}

	@RequestMapping(value = { "/addrolepri" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public Object addrolepri(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		long roleid = this.getLongValue("roleid", modelMap, -1L);
		long priid = this.getLongValue("priid", modelMap, -1L);
		this.roleService.addRolePri(roleid, priid);
		rtnmap.put("success", true);
		return rtnmap;
	}

	@RequestMapping(value = { "/delrolepri" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public Object delrolepri(final ModelMap modelMap) throws Exception {
		Map<String, Object> rtnmap = new HashMap<String, Object>();
		long roleid = this.getLongValue("roleid", modelMap, -1L);
		long priid = this.getLongValue("priid", modelMap, -1L);
		this.roleService.delRolePri(roleid, priid);
		rtnmap.put("success", true);
		return rtnmap;
	}
}
