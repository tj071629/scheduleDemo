package com.eb.admin.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
* @ClassName: SmsUtils
*	阿里云短信服务
* @author Jason.Y
* @date 2018年3月14日 上午9:57:54
*
 */
public class SmsUtils {
	
	private static Logger log = LoggerFactory.getLogger(SmsUtils.class);
	
	/**
	 * 
	 * @param mobile
	 * @param scene	reg（注册验证码）/login（登录验证码）/modify（修改验证码-暂停使用）
	 * @param msg	验证码（一般为6位数字）
	 * @return void
	 */
	public static void sendSms(String mobile, String scene, String msg) {

		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			// 配置超时时间
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000)
					.setConnectionRequestTimeout(1000).setSocketTimeout(1000).setRedirectsEnabled(true).build();

			HttpPost httpPost = new HttpPost(Config.send_sms_api);
			// 设置超时时间
			httpPost.setConfig(requestConfig);
			// 装配post请求参数
			List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
			
			list.add(new BasicNameValuePair("appkey", Config.mail_appkey)); 		// 请求参数
			list.add(new BasicNameValuePair("appsecret", Config.mail_appsecret)); 	// 请求参数
			
			list.add(new BasicNameValuePair("mobile", mobile)); 					// 请求参数
			list.add(new BasicNameValuePair("scene", scene)); 						// 请求参数
			list.add(new BasicNameValuePair("msg", msg)); 							// 请求参数
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
				// 设置post求情参数
				httpPost.setEntity(entity);
				HttpResponse httpResponse = httpClient.execute(httpPost);

				if (httpResponse != null) {
					System.out.println(httpResponse.getStatusLine().getStatusCode());
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						EntityUtils.toString(httpResponse.getEntity());
						log.info("短信发送成功!");
					} else if (httpResponse.getStatusLine().getStatusCode() == 400) {
						log.info("短信发送失败!返回错误码:400");
					} else if (httpResponse.getStatusLine().getStatusCode() == 500) {
						log.info("短信发送失败!返回错误码:500");
					} else {
						log.info("短信发送失败!返回错误码:" + httpResponse.getStatusLine().getStatusCode());
					}
				} else {

				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (httpClient != null) {
						httpClient.close(); // 释放资源
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
