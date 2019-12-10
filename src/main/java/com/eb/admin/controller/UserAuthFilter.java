package com.eb.admin.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eb.admin.service.UserPriService;

public class UserAuthFilter extends AbsFilter implements Filter {

	@Override
	public void destroy() {

	}

	private UserPriService userPriService;

	public UserPriService getUserPriService() {
		return userPriService;
	}

	public void setUserPriService(UserPriService userPriService) {
		this.userPriService = userPriService;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String uri = req.getRequestURI();
		boolean pass = false;
		for (String suri : skipuri) {
			if (uri.startsWith(req.getContextPath() + suri)) {
				pass = true;
			}
		}
		if (!pass) {
			//long uid = (long) request.getAttribute("curruserid");
			//modify by Jason.Yan at 2018/03/08  保证整个会话期间内可以获取到当前登录ID
			long uid = (long) req.getSession().getAttribute("curruserid");
			pass = uid > 0;
		}
		if (pass) {
			chain.doFilter(req, response);
		} else {
			String ctx = req.getContextPath();
			res.sendRedirect(ctx + "/login/index.html");
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
