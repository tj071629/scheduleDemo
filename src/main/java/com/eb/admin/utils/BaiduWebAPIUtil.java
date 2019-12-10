package com.eb.admin.utils;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * @Description: BaiduWebAPIUtil
 * @author: Jason.Yan
 * @date: 2018/3/23 14:38
 */
public class BaiduWebAPIUtil {
    private static Logger logger = LoggerFactory.getLogger(BaiduWebAPIUtil.class);
    private static String lbsBdAk = "SMj2dYUcxKZiP5pmpDI30mbgopL3KiSm";
    private static final String PARAM_KEY = "AK";

    public static void setLbsBuAk(String lbsBdAk) {
        BaiduWebAPIUtil.lbsBdAk = lbsBdAk;
    }

    /**
     * 根据IP地址获取地址
     * @param ip
     * @return
     */
	public static JSONObject getAddress(String ip) {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 实例化get方法
		HttpGet httpget = new HttpGet(Config.baidu_address_api.replace("IP", ip).replace(PARAM_KEY, lbsBdAk));
		// 请求结果
		CloseableHttpResponse response = null;
		String result = "";

		try {
			// 执行get方法
			response = httpClient.execute(httpget);

			if (response.getStatusLine().getStatusCode() == 200) {

				result = EntityUtils.toString(response.getEntity(), "utf-8");
				System.out.println(result);

				JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
				
				JSONObject addressDetail = null;
						
				if(!"1".equals(jsonObject.get("status").toString())){
					String content = jsonObject.getString("content");
					JSONObject jsonObj = (JSONObject) JSONObject.parse(content);
					String address_detail = jsonObj.getString("address_detail");
					addressDetail = (JSONObject) JSONObject.parse(address_detail);
				}
				
				return addressDetail;

			} else {
				logger.error("百度地图接口调用返回结果为空：可能是，配额已经用完");
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

		return null;
	}


    private static final Double PI = Math.PI;

    private static final Double PK = 180 / PI;

    /**
     * @Description: 第一种方法
     * @param latA
     * @param lngA
     * @param latB
     * @param lngB
     * @param @return
     * @return double
     * @author 钟志铖
     * @date 2014-9-7 上午10:11:35
     */
    public static double getDistanceFromTwoPoints(double latA, double lngA, double latB, double lngB) {
        double t1 = Math.cos(latA / PK) * Math.cos(lngA / PK) * Math.cos(latB / PK) * Math.cos(lngB / PK);
        double t2 = Math.cos(latA / PK) * Math.sin(lngA / PK) * Math.cos(latB / PK) * Math.sin(lngB / PK);
        double t3 = Math.sin(latA / PK) * Math.sin(latB / PK);

        double tt = Math.acos(t1 + t2 + t3);
        return 6366000 * tt;
    }




    // 地球半径
    private static final double EARTH_RADIUS = 6370996.81;

    // 弧度
    private static double radian(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * @Description: 第二种方法
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return void
     * @author 钟志铖
     * @date 2014-9-7 上午10:11:55
     */
    public static double distanceOfTwoPoints(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = radian(lat1);
        double radLat2 = radian(lat2);
        double a = radLat1 - radLat2;
        double b = radian(lng1) - radian(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000D;
        return  s;
    }

    public static void main(String[] args){
    	JSONObject address = getAddress("116.226.35.234");
        logger.debug(address.toJSONString());

        /*double d = getDistanceFromTwoPoints(23.5539530, 114.8903920, 22.547760826187206, 114.1297952177893);
        logger.debug("{}",d);
        d = distanceOfTwoPoints(23.5539530, 114.8903920, 23.5554550, 114.8868890);
        logger.debug("{}",d);*/
    }
}
