package com.eb.admin.utils;

import java.io.InputStream;
import java.util.Properties;

public class Config {

	public static final String CHARSET = "utf-8";
	// 普通邮件发送
	//public static String send_api = "http://api.sendcloud.net/apiv2/mail/send";
	// 地址列表发送
	//public static String send_template_api = "http://api.sendcloud.net/apiv2/mail/sendtemplate";
	// 邮件发送
	public static String send_mail_api = null;
	// 邮件key
	public static String mail_appkey = null;
	// 邮件secret
	public static String mail_appsecret = null;
	
	// 短信发送
	public static String send_sms_api = null;
	
	// 百度地址定位
	public static String baidu_address_api = null;
	
	// 最大收件人数
	public static final int MAX_RECEIVERS = 100;
	// 最大地址列表数
	public static final int MAX_MAILLIST = 5;
	// 邮件内容大小
	public static final int MAX_CONTENT_SIZE = 1024 * 1024;

	static {
		try {
			InputStream f = Config.class.getClassLoader().getResourceAsStream("config/config.properties");
			Properties pros = new Properties();
			pros.load(f);
			send_mail_api = pros.getProperty("send_mail_api");
			mail_appkey = pros.getProperty("mail_appkey");
			mail_appsecret = pros.getProperty("mail_appsecret");
			send_sms_api = pros.getProperty("send_sms_api");
			baidu_address_api = pros.getProperty("baidu_address_api");
			f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}