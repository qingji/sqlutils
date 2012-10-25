package im.qingji.sqlutil.support;

import java.util.Comparator;

import im.qingji.sqlutil.mapping.FieldColumnPair;

/**
 * 
 * @author qingji
 * @version 0.1
 * <br>Created on 2011-7-20 下午02:59:24
 */
public class ColumnNameComparator implements Comparator<FieldColumnPair>{

	//按照数据库字段名进行排序。
	@Override
	public int compare(FieldColumnPair o1, FieldColumnPair o2) {
		return o1.getColumnName().compareToIgnoreCase(o2.getColumnName());
	}
}
