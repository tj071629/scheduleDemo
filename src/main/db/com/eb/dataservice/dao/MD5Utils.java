package com.eb.dataservice.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class MD5Utils {

	private static final String MD5 = "MD5";
	public static final String SALT = "_eBreAd";

	public static String md5(final byte[] message) {

		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdInst = MessageDigest.getInstance(MD5);
			mdInst.update(message);
			byte[] md = mdInst.digest();
			int j = md.length;
			char[] str = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}
	}

	/**
	 * to md5 hex string.
	 * 
	 * @param msg
	 *            msg
	 * @return hex md5
	 */
	public static String md5(final String msg) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5 = md.digest(msg.getBytes());
			StringBuilder sb = new StringBuilder();
			@SuppressWarnings("resource")
			Formatter fmt = new Formatter(sb);
			for (byte b : md5) {
				fmt.format("%02x", b & 0xFF);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}

}
