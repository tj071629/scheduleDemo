/*
 * $Id: FileProperties.java,v 1.1 2008/09/17 14:40:32 jackyren Exp $
 *
 * ��Ȩ���� 2005 ������Ѷ������Ϣ��ѯ�������޹�˾
 */
package com.eb.dataservice.dao;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Provide the ability to use properties file.Each property is in the form
 * 
 * <pre>
 * Key = Value
 * </pre>
 * 
 * Setting property values will automatically persist those value to disk.
 * 
 * @author Jacky Ren
 */
public class FileProperties {
	// ========================== Atrributes ============================
	private File file = null;
	private Properties prop = new Properties();

	public Properties getProp() {
		return prop;
	}

	// ========================= Constructors ===========================
	public FileProperties(String file) {
		this.file = new File(file);
		load();
	}

	// ======================== Public methods ==========================

	public String getProperty(String key) {
		return (String) prop.getProperty(key);
	}

	/**
	 * Searches for the property with the specified key in this property file.
	 * The method returns the default value argument if the property is not
	 * found.
	 * 
	 * @param key
	 *            - property key.
	 * @param defaultValue
	 *            - a default value.
	 * @return the value in this property file with the specified key value.
	 */
	public String getProperty(String key, String defaultValue) {
		return prop.getProperty(key, defaultValue);
	}

	/**
	 * Returns an enumeration of all the keys in this property list, including
	 * distinct keys in the default property list if a key of the same name has
	 * not already been found from the main properties list.
	 * 
	 * @return an enumeration of all the keys in this property file, including
	 *         the keys in the default property list.
	 */
	public Enumeration propertyNames() {
		return prop.propertyNames();
	}

	/**
	 * Provided for parallelism with the getProperty method. Enforces use of
	 * strings for property keys and values
	 * 
	 * @param key
	 *            - the key to be replaced into this property list.
	 * @param value
	 *            - the value corresponding to key.
	 * @return the previous value of the specified key in this property file.or
	 *         null if it did not have one.
	 */
	public String setProperty(String key, String value) {
		String s = (String) prop.setProperty(key, value);
		store();
		return s;
	}

	// ==================== Private utility methods =====================

	// Load properties from file.
	private void load() {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Can't access properties file - " + this.file);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Save the properties to file.
	 */
	private void store() {
		OutputStream out = null;

		try {
			out = new FileOutputStream(this.file);
			prop.store(out, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e1) {

				}
				out = null;
			}
		}
	}
}

// ======================= Getters & Setters ========================

