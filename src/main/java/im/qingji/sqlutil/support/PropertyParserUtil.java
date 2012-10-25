package im.qingji.sqlutil.support;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PropertyParserUtil {

	private Properties props;
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private Lock r = lock.readLock();
	private Lock w = lock.writeLock();

	public PropertyParserUtil(Properties props) {
		w.lock();
		try {
			this.props = props;
		} finally {
			w.unlock();
		}
	}
	
	protected Lock readLock() {
		return r;
	}
	protected Lock writeLock() {
		return w;
	}
	
	private Properties getProps() {
		r.lock();
		try {
			return props;
		} finally {
			r.unlock();
		}
	}

	/**
	 * Get the trimmed String value of the property with the given
	 * <code>name</code>. If the value the empty String (after trimming), then
	 * it returns null.
	 */
	public String getString(String name) {
		return getString(name, null);
	}

	/**
	 * Get the trimmed String value of the property with the given
	 * <code>name</code> or the given default value if the value is null or
	 * empty after trimming.
	 */
	public String getString(String name, String def) {
		String val = null;
		if (getProps() != null) {
			val = getProps().getProperty(name, def);
		}
		if (val == null) {
			return def;
		}

		val = val.trim();

		return (val.length() == 0) ? def : val;
	}

	public String[] getStringArray(String name) {
		return getStringArray(name, null);
	}

	public String[] getStringArray(String name, String[] def) {
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

	public boolean getBoolean(String name) {
		return getBoolean(name, false);
	}

	public boolean getBoolean(String name, boolean def) {
		String val = getString(name);

		return (val == null) ? def : Boolean.valueOf(val).booleanValue();
	}

	public byte getByte(String name) throws NumberFormatException {
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

	public byte getByte(String name, byte def) throws NumberFormatException {
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

	public char getChar(String name) {
		return getChar(name, '\0');
	}

	public char getChar(String name, char def) {
		String param = getString(name);
		return (param == null) ? def : param.charAt(0);
	}

	public double getDouble(String name) throws NumberFormatException {
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

	public double getDouble(String name, double def)
			throws NumberFormatException {
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

	public float getFloat(String name) throws NumberFormatException {
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

	public float getFloat(String name, float def) throws NumberFormatException {
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

	public int getInt(String name) throws NumberFormatException {
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

	public int getInt(String name, int def) throws NumberFormatException {
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

	public int[] getIntArray(String name) throws NumberFormatException {
		return getIntArray(name, null);
	}

	public int[] getIntArray(String name, int[] def)
			throws NumberFormatException {
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

	public long getLong(String name) throws NumberFormatException {
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

	public long getLong(String name, long def) throws NumberFormatException {
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

	public short getShort(String name) throws NumberFormatException {
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

	public short getShort(String name, short def) throws NumberFormatException {
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

	public String[] getPropertyGroups(String prefix) {
		Enumeration<?> keys = getProps().propertyNames();
		HashSet<String> groups = new HashSet<String>(10);

		if (!prefix.endsWith(".")) {
			prefix += ".";
		}

		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if (key.startsWith(prefix)) {
				String groupName = key.substring(prefix.length(),
						key.indexOf('.', prefix.length()));
				groups.add(groupName);
			}
		}

		return groups.toArray(new String[groups.size()]);
	}

	public Properties getPropertyGroup(String prefix) {
		return getPropertyGroup(prefix, false, null);
	}

	public Properties getPropertyGroup(String prefix, boolean stripPrefix) {
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
	public Properties getPropertyGroup(String prefix, boolean stripPrefix,
			String[] excludedPrefixes) {
		Enumeration<?> keys = getProps().propertyNames();
		Properties group = new Properties();

		if (!prefix.endsWith(".")) {
			prefix += ".";
		}

		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if (key.startsWith(prefix)) {

				boolean exclude = false;
				if (excludedPrefixes != null) {
					for (int i = 0; (i < excludedPrefixes.length)
							&& (exclude == false); i++) {
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
}
