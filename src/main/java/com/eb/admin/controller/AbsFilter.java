package com.eb.admin.controller;

import java.util.HashSet;
import java.util.Set;

public class AbsFilter {
	public static final Set<String> skipuri = new HashSet<String>();
	static {
		skipuri.add("/login/");
		skipuri.add("/BJUI/");
		skipuri.add("/js/");
		skipuri.add("/images/");
	}
}
