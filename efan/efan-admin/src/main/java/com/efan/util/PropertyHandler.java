package com.efan.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A property util to load/store properties from/to property file
 * 
 * @author wangjw1
 * 
 */
public final class PropertyHandler {
	private final static String DEFAULT_FILE_NAME = "application.properties";

	private final static ConcurrentMap<String, Propertiez> HANDLER_CACHE = new ConcurrentHashMap<String, Propertiez>();

	/**
	 * Get Properties from default property file named
	 * {@code application.properties}
	 * <p>
	 * Notes: U should make sure there is just one file named
	 * {@code application.properties} can find on your class path
	 * 
	 * @return
	 * @see #getInstance(String)
	 * @throws Exception
	 */
	public static Propertiez getInstance() throws Exception {
		return getInstance(DEFAULT_FILE_NAME);
	}

	/**
	 * Get Properties from a fix file
	 * <p>
	 * Notes: U should make sure there is just one file named {@code fileName}
	 * can find on your class path
	 * 
	 * @param fileName
	 *            property file name
	 * @return
	 *         A instance of Properties to fileName
	 * @throws Exception
	 */
	public static Propertiez getInstance(String fileName) throws Exception {
		URL filePath = getURL(fileName);
		Propertiez hanler = HANDLER_CACHE.get(filePath.getFile());
		if (hanler == null) {
			hanler = new Propertiez(filePath);
			Propertiez instance = HANDLER_CACHE.putIfAbsent(hanler.getPropertyFilePath().getFile(), hanler);
			if (instance != null) {
				hanler = instance;
			}
		}
		return hanler;
	}

	/**
	 * Get Properties from a fix file
	 * <p>
	 * Notes: U should make sure there is just the file named
	 * {@code directFilePath} can find on system
	 * 
	 * @param directFilePath
	 *            Absolute file path
	 * @return
	 * @throws Exception
	 */
	public static Propertiez getInstanceByDirectFilePath(String directFilePath) throws Exception {
		URL filePath = getURLDirectly(directFilePath);
		Propertiez hanler = HANDLER_CACHE.get(directFilePath);
		if (hanler == null) {
			hanler = new Propertiez(filePath);
			Propertiez instance = HANDLER_CACHE.putIfAbsent(hanler.getPropertyFilePath().getFile(), hanler);
			if (instance != null) {
				hanler = instance;
			}
		}
		return hanler;
	}

	/**
	 * Extends Properties, supply some convenient method for property get/store
	 * 
	 * @author kevin
	 * 
	 */
	public static class Propertiez extends Properties {
		private static final long serialVersionUID = 1L;
		private URL propertyFilePath;

		public URL getPropertyFilePath() {
			return propertyFilePath;
		}

		public void setPropertyFilePath(URL propertyFilePath) {
			this.propertyFilePath = propertyFilePath;
		}

		public Propertiez() {
			super();
		}

		public Propertiez(URL filePath) {
			this();
			try {
				propertyFilePath = filePath;
				loadAllProperties(this, filePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * Searches for the property with the specified key in this property
		 * list.
		 * If the key is not found in this property list, the default property
		 * list,
		 * and its defaults, recursively, are then checked. The method returns
		 * null
		 * if the property is not found.
		 * 
		 * @param key
		 *            the property key.
		 * @return the value in this property list with the specified key value
		 * @see #getValue(String, String)
		 */
		public String getValue(String key) {
			return getProperty(key);
		}

		/**
		 * Searches for the property with the specified key in this property
		 * list.
		 * If the key is not found in this property list, the default property
		 * list,
		 * and its defaults, recursively, are then checked. The method returns
		 * the
		 * default value argument if the property is not found.
		 * 
		 * @param key
		 *            the property key.
		 * @param defaultValue
		 *            a default value.
		 * 
		 * @return the value in this property list with the specified key value
		 */
		public String getValue(String key, String defaultValue) {
			return getProperty(key, defaultValue);
		}

		/**
		 * 
		 * @param key
		 *            key the property key.
		 * @return the value in this property list with the specified key value
		 * @see #getValue(String)
		 */
		public int getInt(String key) {
			return Integer.valueOf(getProperty(key));
		}

		/**
		 * 
		 * @param key
		 *            the property key.
		 * @param defaultValue
		 *            a default value.
		 * @return the value in this property list with the specified key value
		 * @see #getValue(String, String)
		 */
		public int getInt(String key, int defaultValue) {
			return Integer.valueOf(getProperty(key, String.valueOf(defaultValue)));
		}

		/**
		 * 
		 * @param key
		 *            the property key.
		 * @param defaultValue
		 *            a default value.
		 * @return the value in this property list with the specified key value
		 * @see #getValue(String, String)
		 */
		public long getLong(String key, long defaultValue) {
			return Integer.valueOf(getProperty(key, String.valueOf(defaultValue)));
		}

		/**
		 * 
		 * @param key
		 * @param value
		 */
		public void store(String key, String value) {
			FileWriter fw = null;
			try {
				fw = new FileWriter(getProFileName(this));
				String oldValue = getProperty(key);
				setProperty(key, value);
				store(fw, "Update: key=" + key + ", newValue=" + value + ", oldValue=" + oldValue);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fw != null) {
					try {
						fw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		private String getProFileName(Propertiez handler) {
			String fileName = "";
			Set<Entry<String, Propertiez>> handlers = HANDLER_CACHE.entrySet();
			for (Entry<String, Propertiez> entry : handlers) {
				if (handler == entry.getValue()) {
					fileName = entry.getKey();
					break;
				}
			}
			return fileName;
		}
	}

	/**
	 * Load all properties from the specified class path resource
	 * (in ISO-8859-1 encoding), using the default class loader.
	 * <p>
	 * Merges properties if more than one resource of the same name found in the
	 * class path.
	 * 
	 * @param filePath
	 *            the URL of the class path resource
	 * @return the populated Properties instance
	 * @throws Exception
	 */
	public static void loadAllProperties(Propertiez props, URL filePath) throws Exception {
		loadAllProperties(props, filePath, null);
	}

	/**
	 * Load all properties from the specified class path resource
	 * (in ISO-8859-1 encoding), using the given class loader.
	 * 
	 * @param filePath
	 *            the URL of the class path resource
	 * @param classLoader
	 *            the ClassLoader to use for loading
	 *            (or {@code null} to use the default class loader)
	 * @return the populated Properties instance
	 * @throws Exception
	 */
	public static void loadAllProperties(Propertiez props, URL filePath, ClassLoader classLoader) throws Exception {
		InputStream is = filePath.openStream();
		try {
			if (is == null)
				throw new IllegalArgumentException("Resource: " + filePath
						+ " can not found or have no access privillage.");
			props.load(is);
		} finally {
			if (is != null)
				is.close();
		}
	}

	/**
	 * Return the default ClassLoader to use: typically the thread context
	 * ClassLoader, if available; the ClassLoader that loaded the
	 * PropertyHandler
	 * class will be used as fallback.
	 * <p>
	 * Call this method if you intend to use the thread context ClassLoader in a
	 * scenario where you absolutely need a non-null ClassLoader reference: for
	 * example, for class path resource loading (but not necessarily for
	 * {@code Class.forName}, which accepts a {@code null} ClassLoader reference
	 * as well).
	 * 
	 * @return the default ClassLoader (never {@code null})
	 * @see Thread#getContextClassLoader()
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system
			// class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = PropertyHandler.class.getClassLoader();
		}
		return cl;
	}

	public static URL getURL(String resourceName, ClassLoader classLoader) throws IllegalArgumentException {
		if (resourceName == null)
			throw new IllegalArgumentException("Resource name must not be null");
		ClassLoader clToUse = classLoader;
		if (clToUse == null) {
			clToUse = getDefaultClassLoader();
		}
		URL url = clToUse.getResource(resourceName);
		if (url == null)
			throw new IllegalArgumentException("Resource: " + resourceName + " can not found.");
		return url;
	}

	public static URL getURL(String resourceName) throws IllegalArgumentException {
		return getURL(resourceName, null);
	}

	public static URL getURLDirectly(String directFilePath) throws Exception {
		if (directFilePath == null)
			throw new IllegalArgumentException("DirectFilePath must not be null");
		File file = new File(directFilePath);
		if (file.isDirectory())
			throw new IllegalArgumentException("DirectFilePath must not be directory: " + directFilePath);
		return new File(directFilePath).toURI().toURL();
	}

	private PropertyHandler() {
	}
}
