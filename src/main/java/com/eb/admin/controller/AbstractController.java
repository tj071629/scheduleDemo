package com.eb.admin.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.eb.admin.service.UserMsgService;
import com.eb.admin.service.UserService;
import com.eb.dataservice.dao.EntityPage;

public class AbstractController {
	protected static Calendar toDate(long date) {
		int year = (int) (date / 10000);
		int month = (int) ((date % 10000) / 100);
		int day = (int) (date % 100);
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.get(Calendar.DAY_OF_WEEK);
		return calendar;
	}

	public Date getDateValue(String key, ModelMap modelMap) {
		addParamInfo(modelMap);
		String datestr = (String) modelMap.get(key);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (datestr != null && datestr.length() > 0) {
			try {
				return df.parse(datestr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public Date getDateValue(String key, ModelMap modelMap, Date defaultdate) {
		addParamInfo(modelMap);
		String datestr = (String) modelMap.get(key);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (datestr != null && datestr.length() > 0) {
			try {
				return df.parse(datestr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return defaultdate;
	}

	protected static boolean isEmpty(Object value) {
		if (value == null) {
			return true;
		}
		if (value.toString().trim().length() < 1) {
			return true;
		}
		return false;
	}

	protected Long getCurrUserid() {
		//Long uid = (Long) this.request.getAttribute("curruserid");
		Long uid = (Long) this.request.getSession().getAttribute("curruserid");
		if (uid == null) {
			return -1L;
		}
		return uid;
	}

	public static final boolean debug = Boolean.parseBoolean(System
			.getProperty("debugapp", "false"));

	public Boolean getBooleanValue(String key, ModelMap modelMap) {
		addParamInfo(modelMap);
		String v = getValue(key, modelMap);
		try {
			if (v != null && v.trim().length() > 0) {
				return Boolean.parseBoolean(v);
			}
		} catch (Exception ex) {
		}
		return false;
	}

	public int getIntValue(String key, ModelMap modelMap, int nullValue) {
		addParamInfo(modelMap);
		String v = getValue(key, modelMap);
		try {
			if (v != null && v.trim().length() > 0) {
				return Integer.parseInt(v);
			}
		} catch (Exception ex) {
		}
		return nullValue;
	}

	public double getDoubleValue(String key, ModelMap modelMap, double nullValue) {
		addParamInfo(modelMap);
		String v = getValue(key, modelMap);
		if (v != null && v.trim().length() > 0) {
			try {
				return Double.parseDouble(v);
			} catch (Exception ex) {
			}
		}
		return nullValue;
	}

	public long getLongValue(String key, ModelMap modelMap, long nullValue) {
		addParamInfo(modelMap);
		String v = getValue(key, modelMap);
		if (v != null && v.trim().length() > 0) {
			try {
				return Long.parseLong(v.replace("#", ""));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return nullValue;
	}

	public EntityPage getPageInfo(ModelMap modelMap) {
		addParamInfo(modelMap);
		EntityPage page = new EntityPage();
		int pagenum = this.getIntValue("pageCurrent", modelMap, 1);
		int rowinpage = this.getIntValue("pageSize", modelMap, 20);
		if (rowinpage > 100) {
			rowinpage = 100;
		}
		page.setCurrpage(pagenum);
		page.setRowinpage(rowinpage);
		return page;
	}

	public String getValue(String key, ModelMap modelMap) {
		addParamInfo(modelMap);
		return (String) modelMap.get(key);
	}

	public String getValue(String key, ModelMap modelMap, String defaultvalue) {
		addParamInfo(modelMap);
		Object raw = (Object) modelMap.get(key);
		if (raw instanceof List) {
			List<String> list = (List<String>) raw;
			String v = "";
			for (String s : list) {
				v += s + " ";
			}
			return v;
		}
		String v = (String) modelMap.get(key);
		if (v != null) {
			return v;
		}
		return defaultvalue;
	}

	public double getLongValue(String key, ModelMap modelMap, double nullValue) {
		addParamInfo(modelMap);
		String v = getValue(key, modelMap);
		if (v != null && v.trim().length() > 0) {
			return Double.parseDouble(v);
		}
		return nullValue;
	}

	public Object getRequestparamBean(Class cls) throws InstantiationException,
			IllegalAccessException, InvocationTargetException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		Map<String, String[]> params = request.getParameterMap();
		Object bean = cls.newInstance();
		BeanUtils.populate(bean, params);
		return bean;
	}

	public boolean hasValue(String v) {
		return v != null && v.trim().length() > 0;
	}

	protected ModelAndView getErrorview() {
		return new ModelAndView("core/errorrole");
	}

	protected static final void addUserInfo2Map(List list,
			UserService userService) throws Exception {

		Set<Long> userids = new HashSet<Long>();
		for (Object obj : list) {
			Map map = (Map) obj;
			Long uid = (Long) map.get("userid");
			if (uid != null) {
				userids.add(uid);
			}
		}
		userids.add(-1L);
		Map<Long, Map<String, Object>> uinfos = userService
				.getUserInfos(userids);
		for (Object obj : list) {
			Map premap = (Map) obj;
			long uid = (Long) premap.get("userid");
			Map<String, Object> map = uinfos.get(uid);
			if (map == null) {
				map = new HashMap<String, Object>();
				map.put("pic", "/img/actual_username.png");
				map.put("nickname", "�����û�");

			}
			premap.put("pic", map.get("pic"));
			premap.put("nickname", map.get("nickname"));
		}
	}
	protected static final void addorgIndexFileSize2Map(List list) throws Exception {
		DecimalFormat df = new DecimalFormat("0.00");
		for (Object obj : list) {
			String tempsize ="";
			Map premap = (Map) obj;
			Integer size = (Integer) premap.get("size");
			Map	map = new HashMap<String, Object>();
			if (size != null) {
				if(size > 105000.00){
					tempsize = df.format(size/1048576.00)+"M";
		        }else if(size > 100.00){
		        	tempsize = df.format(size/1024.00)+"KB";
		        }else{
		        	tempsize = size+"B";
		        }
				map.put("tempsize", tempsize);
			}
			premap.put("tempsize", map.get("tempsize"));
		}
	}

	protected static final void addUserInfo2Map(Map map, UserService userService)
			throws Exception {
		long uid = (Long) map.get("userid");

		Set<Long> userids = new HashSet<Long>();
		userids.add(uid);
		Map<Long, Map<String, Object>> uinfos = userService
				.getUserInfos(userids);
		Map<String, Object> umap = uinfos.get(uid);
		if (umap == null) {
			umap = new HashMap<String, Object>();
			umap.put("pic", "/img/actual_username.png");
			umap.put("nickname", "�����û�");

		}
		map.put("pic", umap.get("pic"));
		map.put("nickname", umap.get("nickname"));
	}

	protected void addParamInfo(ModelMap modelMap) {
		if (modelMap.containsKey(Constants.PARAMADDED)) {
			return;
		} else {
			modelMap.put(Constants.PARAMADDED, true);
		}
		Map<String, String[]> valueMap = request.getParameterMap();
		for (String key : valueMap.keySet()) {
			String[] values = valueMap.get(key);
			if (values != null) {
				if (values.length > 1) {
					List<String> list = new ArrayList<String>();
					for (String v : values) {
						list.add(v);
					}
					modelMap.put(key, list);
				} else {
					modelMap.put(key, values[0]);
				}

			}
		}
	}

	@Autowired
	private UserMsgService userMsgService;
	
	public  String getTemplate(String templatename,
			Map<String, String> params) throws Exception {
		String template = userMsgService.getTemplate(templatename);
		for (String key : params.keySet()) {
			String v = params.get(key);
			template = template.replace("${"+key+"}", v);
		}
		return template;
	}
	@Autowired
	protected HttpServletRequest request;

	public UserMsgService getUserMsgService() {
		return userMsgService;
	}

	public void setUserMsgService(UserMsgService userMsgService) {
		this.userMsgService = userMsgService;
	}

}
