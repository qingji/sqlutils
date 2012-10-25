package im.qingji.sqlutil.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class SQLUtilConfigurator {

	private static Properties props;
	private static SQLUtilConfigurator _INSTANCE;
	private static final String DEFAULT_PROPERTIE_NAME = "sqlutil.properties";
	private static int DEFAULT_FOUND_LEVEL = 3;
	private static final ReadWriteLock lock = new ReentrantReadWriteLock();
	private static final Lock r = lock.readLock();
	private static final Lock w = lock.writeLock();

	public synchronized SQLUtilConfigurator getInstance() {
		if (_INSTANCE == null) {
			_INSTANCE = new SQLUtilConfigurator();
		}
		return _INSTANCE;
	}

	private static Properties getProperties() {
		if (props == null) {
			try {
				w.lock();
				props = new Properties();
			} finally {
				w.unlock();
			}
		}
		return props;
	}

	static {
		try {
			URL is = ClassLoader.getSystemClassLoader().getResource((DEFAULT_PROPERTIE_NAME));
			if(is != null) {
				load(is);
			} else {
				File file = findFromClassPath(DEFAULT_PROPERTIE_NAME);
				if(file == null || !file.exists()) {
					file = findFromRoot(DEFAULT_PROPERTIE_NAME);
				}
				if(file == null || !file.exists()) {
					file = findFromCurrentPath(DEFAULT_PROPERTIE_NAME);
				}
				if(file != null && file.exists()) {
					load(file);
				} else {
					System.err.println("Could not read default configuration file '"+DEFAULT_PROPERTIE_NAME+"'");
				}
			}
		} catch (Exception e) {
			System.err.println("Could not read default configuration file '"+DEFAULT_PROPERTIE_NAME+"'");
		}
	}

	private SQLUtilConfigurator() {}

	public static final void load(Properties properties) {
		w.lock();
		try {
			props = properties;
		} finally {
			w.unlock();
		}
	}

	public static final void load(URL url) {
		load(url.getFile());
	}

	public static final void load(File file) {
		System.out.println("loading config file:" + file.getPath());
		FileInputStream istream = null;
		try {
			if (!file.exists()) {
				throw new RuntimeException(file + "is not exists!");
			} else if (!file.isFile()) {
				throw new RuntimeException(file + "is not file!");
			} else {
				istream = new FileInputStream(file);
				load(istream);
			}
		} catch (Exception e) {
			if (e instanceof InterruptedIOException || e instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
			throw new RuntimeException("Could not read configuration file "+file.getPath()+" .");
		} finally {
			if (istream != null) {
				try {
					istream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static final void load(String path) {
		load(new File(path));
	}

	public static final void load(InputStream istream) {
		if (istream == null) {
			throw new NullPointerException();
		}
		w.lock();
		try {
			if (istream.available() <= 0) {
				System.err.println("I/O channel is not available.");
			}
			getProperties().load(istream);
		} catch (Exception e) {
			if (e instanceof InterruptedIOException || e instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
			System.err.println("Could not read configuration file .");
		} finally {
			w.unlock();
			if (istream != null) {
				try {
					istream.close();
				} catch (InterruptedIOException ignore) {
					Thread.currentThread().interrupt();
				} catch (Throwable ignore) {
				}
			}
		}
	}

	/**
	 * Get the trimmed String value of the property with the given
	 * <code>name</code>. If the value the empty String (after trimming), then
	 * it returns null.
	 */
	public static final String getString(String name) {
		return getString(name, null);
	}

	/**
	 * Get the trimmed String value of the property with the given
	 * <code>name</code> or the given default value if the value is null or
	 * empty after trimming.
	 */
	public static final String getString(String name, String def) {
		r.lock();
		String val = null;
		try {
			if (getProperties() != null) {
				val = getProperties().getProperty(name, def);
			}
		} finally {
			r.unlock();
		}
		if (val == null) {
			return def;
		}

		val = val.trim();

		return (val.length() == 0) ? def : val;
	}

	public static final String[] getStringArray(String name) {
		return getStringArray(name, null);
	}

	public static final String[] getStringArray(String name, String[] def) {
		String vals = getString(name);
		if (vals == null) {
			return def;
		}

		StringTokenizer stok = new StringTokenizer(vals, ",");
		ArrayList<String> strs = new ArrayList<String>();
		try {
			while (stok.hasMoreTokens()) {
				strs.add(stok.nextToken().trim());
			}

			return strs.toArray(new String[strs.size()]);
		} catch (Exception e) {
			return def;
		}
	}

	public static final boolean getBoolean(String name) {
		return getBoolean(name, false);
	}

	public static final boolean getBoolean(String name, boolean def) {
		String val = getString(name);

		return (val == null) ? def : Boolean.valueOf(val).booleanValue();
	}

	public static final byte getByte(String name) throws NumberFormatException {
		String val = getString(name);
		if (val == null) {
			throw new NumberFormatException(" null string");
		}
		try {
			return Byte.parseByte(val);
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException(" '" + val + "'");
		}
	}

	public static final byte getByte(String name, byte def) throws NumberFormatException {
		String val = getString(name);
		if (val == null) {
			return def;
		}

		try {
			return Byte.parseByte(val);
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException(" '" + val + "'");
		}
	}

	public static final char getChar(String name) {
		return getChar(name, '\0');
	}

	public static final char getChar(String name, char def) {
		String param = getString(name);
		return (param == null) ? def : param.charAt(0);
	}

	public static final double getDouble(String name) throws NumberFormatException {
		String val = getString(name);
		if (val == null) {
			throw new NumberFormatException(" null string");
		}

		try {
			return Double.parseDouble(val);
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException(" '" + val + "'");
		}
	}

	public static final double getDouble(String name, double def) throws NumberFormatException {
		String val = getString(name);
		if (val == null) {
			return def;
		}

		try {
			return Double.parseDouble(val);
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException(" '" + val + "'");
		}
	}

	public static final float getFloat(String name) throws NumberFormatException {
		String val = getString(name);
		if (val == null) {
			throw new NumberFormatException(" null string");
		}

		try {
			return Float.parseFloat(val);
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException(" '" + val + "'");
		}
	}

	public static final float getFloat(String name, float def) throws NumberFormatException {
		String val = getString(name);
		if (val == null) {
			return def;
		}

		try {
			return Float.parseFloat(val);
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException(" '" + val + "'");
		}
	}

	public static final int getInt(String name) throws NumberFormatException {
		String val = getString(name);
		if (val == null) {
			throw new NumberFormatException(" null string");
		}

		try {
			return Integer.parseInt(val);
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException(" '" + val + "'");
		}
	}

	public static final int getInt(String name, int def) throws NumberFormatException {
		String val = getString(name);
		if (val == null) {
			return def;
		}

		try {
			return Integer.parseInt(val);
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException(" '" + val + "'");
		}
	}

	public static final int[] getIntArray(String name) throws NumberFormatException {
		return getIntArray(name, null);
	}

	public static final int[] getIntArray(String name, int[] def) throws NumberFormatException {
		String vals = getString(name);
		if (vals == null) {
			return def;
		}

		StringTokenizer stok = new StringTokenizer(vals, ",");
		ArrayList<Integer> ints = new ArrayList<Integer>();
		try {
			while (stok.hasMoreTokens()) {
				try {
					ints.add(new Integer(stok.nextToken().trim()));
				} catch (NumberFormatException nfe) {
					throw new NumberFormatException(" '" + vals + "'");
				}
			}

			int[] outInts = new int[ints.size()];
			for (int i = 0; i < ints.size(); i++) {
				outInts[i] = ints.get(i).intValue();
			}
			return outInts;
		} catch (Exception e) {
			return def;
		}
	}

	public static final long getLong(String name) throws NumberFormatException {
		String val = getString(name);
		if (val == null) {
			throw new NumberFormatException(" null string");
		}

		try {
			return Long.parseLong(val);
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException(" '" + val + "'");
		}
	}

	public static final long getLong(String name, long def) throws NumberFormatException {
		String val = getString(name);
		if (val == null) {
			return def;
		}

		try {
			return Long.parseLong(val);
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException(" '" + val + "'");
		}
	}

	public static final short getShort(String name) throws NumberFormatException {
		String val = getString(name);
		if (val == null) {
			throw new NumberFormatException(" null string");
		}

		try {
			return Short.parseShort(val);
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException(" '" + val + "'");
		}
	}

	public static final short getShort(String name, short def) throws NumberFormatException {
		String val = getString(name);
		if (val == null) {
			return def;
		}

		try {
			return Short.parseShort(val);
		} catch (NumberFormatException nfe) {
			throw new NumberFormatException(" '" + val + "'");
		}
	}

	public static final String[] getPropertyGroups(String prefix) {
		Enumeration<?> keys = null;
		if (getProperties() != null) {
			r.lock();
			try {
				keys = getProperties().propertyNames();
			} finally {
				r.unlock();
			}
		} else {
			return null;
		}
		HashSet<String> groups = new HashSet<String>(10);

		if (!prefix.endsWith(".")) {
			prefix += ".";
		}

		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if (key.startsWith(prefix)) {
				String groupName = key
						.substring(prefix.length(), key.indexOf('.', prefix.length()));
				groups.add(groupName);
			}
		}

		return groups.toArray(new String[groups.size()]);
	}

	public static final Properties getPropertyGroup(String prefix) {
		return getPropertyGroup(prefix, false, null);
	}

	public static final Properties getPropertyGroup(String prefix, boolean stripPrefix) {
		return getPropertyGroup(prefix, stripPrefix, null);
	}
	
	/**
	 * Get all properties that start with the given prefix.
	 * 
	 * @param prefix
	 *            The prefix for which to search. If it does not end in a "."
	 *            then one will be added to it for search purposes.
	 * @param stripPrefix
	 *            Whether to strip off the given <code>prefix</code> in the
	 *            result's keys.
	 * @param excludedPrefixes
	 *            Optional array of fully qualified prefixes to exclude. For
	 *            example if <code>prefix</code> is "a.b.c", then
	 *            <code>excludedPrefixes</code> might be "a.b.c.ignore".
	 * 
	 * @return Group of <code>Properties</code> that start with the given
	 *         prefix, optionally have that prefix removed, and do not include
	 *         properties that start with one of the given excluded prefixes.
	 */
	public static final Properties getPropertyGroup(String prefix, boolean stripPrefix, String[] excludedPrefixes) {
		Enumeration<?> keys = null;
		if (getProperties() != null) {
			r.lock();
			try {
				keys = getProperties().propertyNames();
			} finally {
				r.unlock();
			}
		} else {
			return null;
		}
		Properties group = new Properties();

		if (!prefix.endsWith(".")) {
			prefix += ".";
		}

		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if (key.startsWith(prefix)) {

				boolean exclude = false;
				if (excludedPrefixes != null) {
					for (int i = 0; (i < excludedPrefixes.length) && (exclude == false); i++) {
						exclude = key.startsWith(excludedPrefixes[i]);
					}
				}

				if (exclude == false) {
					String value = getString(key, "");

					if (stripPrefix) {
						group.put(key.substring(prefix.length()), value);
					} else {
						group.put(key, value);
					}
				}
			}
		}
		return group;
	}
	
	/**
	 * 从classpath中找寻配置文件
	 * @param fileName 需要查找的文件名
	 * @return
	 */
	static File findFromClassPath(final String fileName) {
		String classpath = System.getProperty("java.class.path");
		String[] filePath = classpath.split(";");
		return find(fileName, filePath, DEFAULT_FOUND_LEVEL);
	}
	
	/**
	 * 从classpath中找寻配置文件
	 * @param fileName 需要查找的文件名
	 * @return
	 */
	static File findFromRoot(final String fileName) {
		URL path = SQLUtilConfigurator.class.getClassLoader().getResource("/");
		if(path != null) {
			return find(fileName, new String[]{path.getPath()}, DEFAULT_FOUND_LEVEL);
		}
		return null;
	}
	/**
	 * 从classpath中找寻配置文件
	 * @param fileName 需要查找的文件名
	 * @return
	 */
	static File findFromCurrentPath(final String fileName) {
		URL path = SQLUtilConfigurator.class.getClassLoader().getResource(".");
		if(path != null) {
			return find(fileName, new String[]{path.getPath()}, DEFAULT_FOUND_LEVEL);
		}
		return null;
	}
	
	/**
	 * 遍历查询文件，当遇到目录时，深入level层查找。
	 * @param fileName
	 * @param filePath
	 * @param level
	 * @return
	 */
	static File find(final String fileName, String[] filePath, int level) {
		File [] files = new File[filePath.length];
		for (int i = 0; i < filePath.length; i++) {
			files[i] = new File(filePath[i]);
		}
		return find(fileName, files, level);
	}

	static File find(final String fileName, final File[] files, int level) {
		if(level-- < 0) {
			return null;
		}
		for (File file : files) {
			if(file == null || !file.exists()) {
				continue;
			}
			if (file.isFile()) {
				if (file.getName().equals(fileName)) {
					return file;
				}
			} else if(file.isDirectory()) {
				File[] childFiles = file.listFiles();
				File f = find(fileName, childFiles, level);
				if(f != null) 
					return f;
			}
		}
		return null;
	}
	
	public static void setConfigFindLevel(int level) {
		DEFAULT_FOUND_LEVEL = level;
	}
}
