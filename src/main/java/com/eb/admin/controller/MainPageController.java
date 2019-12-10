package com.eb.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eb.admin.service.UserService;

@Controller
@RequestMapping("/")
@Scope("prototype")
public class MainPageController extends AbstractController {
	@RequestMapping(value = { "index.html", "/", "index.htm" }, method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView index(final ModelMap modelMap) throws Exception {
		return new ModelAndView("index");
	}

	@Autowired
	private UserService userService;

}
