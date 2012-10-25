package im.qingji.sqlutil.convert.impl;

import im.qingji.sqlutil.convert.ConvertException;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 
 * @author qingji
 * @version 0.1 <br>
 *          Created on 2011-7-19 上午11:30:00
 */
public class TimestampConverter extends AbstractConverter {

	private String dateFormatPattern = "yyyy-MM-dd HH:mm:ss";
	private DateFormat dateFormat;

	@Override
	public Object convert(Class<?> targetType, Object obj) throws ConvertException {
		if(obj == null) return null;
		try {
			if(targetType.equals(obj.getClass())) {
				return obj;
			}
			if(targetType.equals(obj.getClass())) {
				return obj;
			}
			if (isSame(Timestamp.class, obj.getClass())) {
				Timestamp date = (Timestamp) obj;
				if (isSame(Timestamp.class, targetType)) {
					return date;
				} else if (isSame(String.class, targetType)) {
					return getDateFormat().format(date);
				} else if (isSame(Long.class, targetType)) {
					return date.getTime();
				} else if (isSame(Date.class, targetType)) {
					return new Date(date.getTime());
				} else if (isSame(java.util.Date.class, targetType)) {
					return new java.util.Date(date.getTime());
				}
			}
			throw unsupport(targetType, obj);
		} catch (Exception e) {
			throw caseThrowable(targetType, obj, e);
		}
	}

	private DateFormat getDateFormat() {
		if (dateFormat == null) {
			dateFormat = new SimpleDateFormat(dateFormatPattern);
		}
		return dateFormat;
	}

	/**
	 * @param dateFormat
	 *            the dateFormat to set
	 */
	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * @param dateFormatPattern
	 *            the date format pattern to set
	 */
	public void setDateFormatPattern(String dateFormatPattern) {
		this.dateFormatPattern = dateFormatPattern;
	}

	/**
	 * The "yyyy-MM-dd HH:mm:ss" is default date format pattern.
	 * 
	 * @return the date format pattern
	 */
	public String getDateFormatPattern() {
		return dateFormatPattern;
	}

}
