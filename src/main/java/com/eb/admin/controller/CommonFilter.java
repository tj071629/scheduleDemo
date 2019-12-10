package com.eb.admin.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.eb.admin.service.RoleService;
import com.eb.admin.service.UserService;

/**
 * Servlet Filter implementation class CommonFilter
 */
public class CommonFilter extends AbsFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public CommonFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	private UserService userService;
	private RoleService roleService;

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String ctx = req.getContextPath();
		if (!ctx.endsWith("/")) {
			ctx += "/";
		}
		if (this.resourcectx == null || resourcectx.length() < 1) {
			resourcectx = ctx;
		}
		req.setAttribute("ctx", ctx);
		req.setAttribute("resourcectx", resourcectx);
		boolean pass = false;
		String uri = req.getRequestURI();

		for (String suri : skipuri) {
			if (uri.startsWith(suri)) {
				pass = true;
			}
		}
		if (pass) {
			chain.doFilter(req, response);
			return;
		}
		Cookie[] cookies = req.getCookies();
		String tokenvalue = "";
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				String name = cookie.getName();
				String value = cookie.getValue();
				if ("ebadmintoken".equals(name)) {
					tokenvalue = value;
				}
			}
		}
		if (tokenvalue.length() < 1) {
			String ut = request.getParameter("ut");
			if (ut != null) {
				tokenvalue = ut;
			}
		}
		Long uid = -1L;
		if (tokenvalue.length() > 0) {
			try {
				uid = userService.getuserid(tokenvalue);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (uid == null) {
					uid = -1L;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		//req.setAttribute("curruserid", uid);
		//modify by Jason.Yan at 2018/03/08 保证整个会话期间内可以获取到当前登录ID
		req.getSession().setAttribute("curruserid", uid);
		try {
			addUserRoleAndPri(req, uid);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		chain.doFilter(request, response);
	}

	private void addUserRoleAndPri(HttpServletRequest req, Long uid)
			throws Exception {
		if (uid > 0) {
			List<Map<String, Object>> roles = this.roleService
					.userroleList(uid);
			req.setAttribute("roles", roles);
			Map<String, String> primap = new LinkedHashMap<String, String>();
			for (Map<String, Object> role : roles) {
				Object roleid = role.get("id");
				if (roleid != null) {
					Long rid = Long.parseLong(roleid.toString());
					List<Map<String, Object>> prilist = this.roleService
							.rolePrilist(rid);
					for (Map<String, Object> map : prilist) {
						String name = (String) map.get("priname");
						String uri = (String) map.get("uri");
						if (!primap.containsKey(name)) {
							primap.put(name, uri);
						}
					}
				}
			}
			req.getSession().setAttribute("userprimap", primap);
		}
	}

	private static final java.util.concurrent.ExecutorService es = Executors
			.newSingleThreadExecutor();

	private static final void saveUserIpInfo(final String token,
			final String ip, final long uid) {
		if (uid > 0) {
			es.submit(new Runnable() {

				@Override
				public void run() {
					// TokenEntity.addUserIp(token, uid, ip);
				}

			});
		}
	}

	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	private String resourcectx = "";

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		resourcectx = fConfig.getInitParameter("resourcectx");
	}

}
