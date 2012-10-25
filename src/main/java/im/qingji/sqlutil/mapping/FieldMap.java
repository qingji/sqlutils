package im.qingji.sqlutil.mapping;

import im.qingji.sqlutil.support.ColumnNameComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 专门存放JavaBean的字段的容器。
 * <p>
 * 
 * </P>
 * @author qingji
 * @version 0.1
 * <br>Created on 2011-7-19 下午03:24:04
 */
public class FieldMap {

	private Map<String, FieldColumnPair> fields;
	
	public FieldMap() {
		fields = new HashMap<String, FieldColumnPair>();
	}
	public FieldMap(int capacity) {
		fields = new HashMap<String, FieldColumnPair>(capacity);
	}
	
	public FieldMap(Map<String, FieldColumnPair> fields) {
		this.fields = fields; 
	}
	/**
	 * @return the fields
	 */
	public Map<String, FieldColumnPair> getFields() {
		return fields;
	}
	
	public synchronized FieldColumnPair put(String key, FieldColumnPair value) {
		return fields.put(key, value);
	}
	
	public FieldColumnPair get(String key) {
		return fields.get(key);
	}
	
	public int size() {
		return fields.size();
	}
	
	public Set<Entry<String, FieldColumnPair>> entrySet() {
		return fields.entrySet();
	}
	
	public synchronized void putAll(Map<String,FieldColumnPair> map) {
		fields.putAll(map);
	}
	
	public List<FieldColumnPair> getOrderedFields() {
		List<FieldColumnPair> list = new ArrayList<FieldColumnPair>(getFields().size());
		list.addAll( getFields().values());
		Collections.sort(list, new ColumnNameComparator());
		return list;
	}
	
}
