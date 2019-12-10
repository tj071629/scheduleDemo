package com.eb.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.eb.admin.service.RoleService;
import com.eb.dataservice.dao.CommonDao;
import com.eb.dataservice.dao.EntityPage;
import com.eb.dataservice.dao.PageResultEntity;
import com.eb.dataservice.dao.PageUtils;
import com.eb.dataservice.dao.SqlUtils;

public class RoleServiceImpl implements RoleService {

	@Override
	public List<Map<String, Object>> roleList(String rolename) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		
		String sql = SqlUtils.getSql("RoleService.roleList");
		StringBuffer sb = new StringBuffer();
		if(StringUtils.isNotBlank(rolename)){
			sb.append(sql).append(" where name like '%" + rolename + "%'").append(" order by id asc");
		}else{
			sb.append(sql).append(" order by id asc");
		}
		return dao.findList(sb.toString());
	}

	@Override
	public List<Map<String, Object>> userroleList(long userid) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("RoleService.userroleList");
		return dao.findList(sql, userid);
	}

	@Override
	public void addUserRole(long userid, long roleid) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("RoleService.addUserRole");
		dao.execute(sql, userid, roleid);
	}

	@Override
	public void delUserRole(long userid, long roleid) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("RoleService.delUserRole");
		dao.execute(sql, userid, roleid);
	}

	@Override
	public void addRole(String name) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("RoleService.addRole");
		dao.execute(sql, name);
	}

	@Override
	public void delRole(long roleid) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("RoleService.delUserRoleByRid");
		dao.execute(sql, roleid);
		sql = SqlUtils.getSql("RoleService.delRole");
		dao.execute(sql, roleid);
	}

	@Override
	public Map<String, Object> getRole(long roleid) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("RoleService.getRole");
		return dao.findMap(sql, roleid);
	}

	@Override
	public void updateRole(long roleid, String name) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("RoleService.updateRole");
		dao.execute(sql, name, roleid);
	}

	@Override
	public List<Map<String, Object>> prilist() throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("RoleService.prilist");
		return dao.findList(sql);
	}

	@Override
	public List<Map<String, Object>> rolePrilist(long roleid) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("RoleService.rolePrilist");
		return dao.findList(sql, roleid);
	}

	@Override
	public Map<String, Object> getPri(long priid) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("RoleService.getPri");
		return dao.findMap(sql, priid);
	}

	@Override
	public void addPri(String priname, String uri) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("RoleService.addPri");
		dao.execute(sql, priname, uri);

	}

	@Override
	public void updatePri(long priid, String priname, String uri)
			throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("RoleService.updatePri");
		dao.execute(sql, priname, uri, priid);
	}

	@Override
	public void delPri(long priid) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("RoleService.delPri");
		dao.execute(sql, priid);
		sql = SqlUtils.getSql("RoleService.delRolePri");
		dao.execute(sql, priid);
	}

	@Override
	public void addRolePri(long roleid, long priid) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("RoleService.addRolePri");
		dao.execute(sql, roleid, priid);

	}

	@Override
	public void delRolePri(long roleid, long priid) throws Exception {
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("RoleService.delRolePri");
		dao.execute(sql, roleid, priid);
	}

	@Override
	public PageResultEntity prilist(EntityPage page) throws Exception {
		PageResultEntity result = new PageResultEntity();
		CommonDao dao = CommonDao.getDao("ebweb");
		String sql = SqlUtils.getSql("RoleService.prilist");
		
		
		List<Object> params = new ArrayList<Object>();
		
		@SuppressWarnings("rawtypes")
		List<Map> list = PageUtils.getValueListMap(dao, sql, page, params);
		result.setList(list);
		result.setPageinfo(page);
		return result;
	}

	
	
}
