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
* @ClassName: DateUtils
*	阿里云邮件服务 
* @author Jason.Y
* @date 2018年3月14日下午13:57:54
*
 */
public class MailUtils {
	
	private static Logger log = LoggerFactory.getLogger(MailUtils.class);

	/**
	 * @param from		来自
	 * @param to		发送至
	 * @param title		邮件主题
	 * @param content	邮件正文
	 * @return void
	 */
	public static void sendMail(String from, String to, String title, String content) {

		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			// 配置超时时间
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000)
					.setConnectionRequestTimeout(1000).setSocketTimeout(1000).setRedirectsEnabled(true).build();

			HttpPost httpPost = new HttpPost(Config.send_mail_api);
			// 设置超时时间
			httpPost.setConfig(requestConfig);
			// 装配post请求参数
			List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
			
			list.add(new BasicNameValuePair("appkey", Config.mail_appkey)); 		// 请求参数
			list.add(new BasicNameValuePair("appsecret", Config.mail_appsecret)); 	// 请求参数
			
			list.add(new BasicNameValuePair("from", from)); 						// 请求参数
			list.add(new BasicNameValuePair("to", to)); 							// 请求参数
			list.add(new BasicNameValuePair("title", title)); 						// 请求参数
			list.add(new BasicNameValuePair("content", content)); 					// 请求参数
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
				// 设置post求情参数
				httpPost.setEntity(entity);
				HttpResponse httpResponse = httpClient.execute(httpPost);

				if (httpResponse != null) {
					//System.out.println(httpResponse.getStatusLine().getStatusCode());
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						EntityUtils.toString(httpResponse.getEntity());
						log.info("邮件发送成功!");
					} else if (httpResponse.getStatusLine().getStatusCode() == 400) {
						log.info("邮件发送失败!返回错误码：400");
					} else if (httpResponse.getStatusLine().getStatusCode() == 500) {
						log.info("邮件发送失败!返回错误码：500");
					} else {
						log.info("邮件发送失败!返回错误码：" + httpResponse.getStatusLine().getStatusCode());
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
