package com.eb.dataservice.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {

	public static List<String> readFile(File file) {
		return readFile(file, "utf-8");
	}

	public static void write(File file, String code, String value) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			os.write(value.getBytes(code));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String read(File file, String code) {
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		try {
			if (file.exists()) {
				is = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, code));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
					sb.append("\r\n");
				}
				is.close();
			} else {
				System.out.println(file.getAbsolutePath() + " is not exist!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	public static List<String> readFile(File file, String code) {
		List<String> lines = new ArrayList<String>();
		InputStream is = null;
		try {
			if (file.exists()) {
				is = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, code));
				String line = null;
				while ((line = br.readLine()) != null) {
					lines.add(line);
				}
				is.close();
			} else {
				System.out.println(file.getAbsolutePath() + " is not exist!");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return lines;
	}
}
