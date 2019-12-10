package com.eb.admin.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FileUtils {

	//private static final String fileurl = "E://IU.txt";
	private static final String dirUrl = "E:/jsonstock/";

	public static void main(String[] args) throws Exception {
		/*FileReader in = new FileReader(new  File("test.properties"));
		FileWriter out = new FileWriter(new File("test.properties"));
		Properties prop = new Properties();
		prop.load(in);
		prop.setProperty("侧睡", "测试");
		prop.store(out, "save");
		System.out.println(prop.getProperty("sun"));
		System.out.println(prop.toString());
		System.out.println("end");
		in.close();
		out.close();*/
		List<String> fileNameList = new ArrayList<>();
		FileUtils.filesName(new File(dirUrl), fileNameList);
		System.out.println(fileNameList.toString());
	}

	public static boolean copyTextFile(File srcFile, File destFile) throws IOException {
		if (srcFile.exists()) {
			BufferedReader in = null;
			BufferedWriter out = null;
			try {
				in = new BufferedReader(new FileReader(srcFile));
				out = new BufferedWriter(new FileWriter(destFile));
				char[] buf = new char[1024]; // 1KB 1024整数倍
				int len = 0;
				while ((len = in.read(buf)) != -1) {
					out.write(buf, 0, len);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			return false;
		}
		return true;
	}

	// 字节流文件复制(文本也可以复制)
	public static boolean fileByteCopy(File srcFile, File destFile) {
		if (srcFile.exists()) {
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			try {
				in =new BufferedInputStream( new FileInputStream(srcFile));
				out = new BufferedOutputStream( new FileOutputStream(destFile));
				byte[] buf = new byte[1024]; // 1KB 1024整数倍
				int len = 0;
				while ((len = in.read(buf)) != -1) {
					out.write(buf, 0, len);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			return false;
		}
		return true;
	}

	// 遍历当前文件夹中文件名
	public static List<String> filesNamelist(String path) {
		List<String> list = new ArrayList<>();
		File dir = new File(path);
		if (dir.exists()) {
			if (dir.isDirectory()) {
				File[] listFiles = dir.listFiles();
				for (File file : listFiles) {
					if (file.isFile()) {
						list.add(file.getName());
					}
				}
				return list;
			}
		}
		return null;
	}

	// 遍历当前文件夹中所有文件的路径(包含子目录)
	public static void filesPathAll(String path, List<String> filePathList) {
		File dir = new File(path);
		filesPathList(dir, filePathList);
	}

	// 获取当前文件夹中所有文件名(包含子目录) 参数为: 文件夹文件对象
	public static void filesNameAll(File dir, List<String> fileNameList) {
		List<String> pathlist = new ArrayList<>();
		filesPathList(dir, pathlist);
		for (String str : pathlist) {
			fileNameList.add(new File(str).getName());
		}
	}
	// 遍历当前文件夹中所有文件的路径(包含子目录)
	public static void filesPathList(File dir, List<String> filePathList) {
		if (dir.exists()) {
			if (dir.isDirectory()) {
				File[] files = dir.listFiles();
				if (files != null) {
					for (File file : files) {
						if (file.isDirectory()) {
							filesPathAll(file.getAbsolutePath(), filePathList);
						} else {
							filePathList.add(file.getAbsolutePath());
						}
					}
				}
			}
		}
	}
	
	
	
	// 获取当前文件夹中文件名(不包含子目录) 参数为: 文件夹文件对象
	public static List<String>  filesName(String dirUrl) {
		List<String> fileNameList = new ArrayList<>();
		filesName(dirUrl,fileNameList);
		return fileNameList;
	}
	
	// 获取当前文件夹中文件名(不包含子目录) 参数为: 文件夹文件对象
	public static void filesName(String dirUrl, List<String> fileNameList) {
		filesName(new File(dirUrl), fileNameList);
	}
	
	// 获取当前文件夹中文件名(不包含子目录) 参数为: 文件夹文件对象
	public static void filesName(File dir, List<String> fileNameList) {
		List<String> pathlist = new ArrayList<>();
		filesPath(dir, pathlist);
		for (String str : pathlist) {
			fileNameList.add(new File(str).getName());
		}
	}
	
	// 遍历当前文件夹中文件的路径(不包含子目录)
	public static List<String>  filesPath(String dirUrl) {
		return filesPath(new File(dirUrl));
	}
	// 遍历当前文件夹中文件的路径(不包含子目录)
	public static List<String>  filesPath(File dir) {
		List<String> filePathList = new ArrayList<String>();
		if (dir.exists()) {
			if (dir.isDirectory()) {
				File[] files = dir.listFiles();
				if (files != null) {
					for (File file : files) {
						if (file.isFile()) {
							filePathList.add(file.getAbsolutePath());
						} 
					}
				}
			}
		}
		return filePathList;
	}
	// 遍历当前文件夹中文件的路径(不包含子目录)
	public static void filesPath(File dir, List<String> filePathList) {
		if (dir.exists()) {
			if (dir.isDirectory()) {
				File[] files = dir.listFiles();
				if (files != null) {
					for (File file : files) {
						if (file.isFile()) {
							filePathList.add(file.getAbsolutePath());
						} 
					}
				}
			}
		}
	}

}
