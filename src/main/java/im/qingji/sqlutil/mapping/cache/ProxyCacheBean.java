package im.qingji.sqlutil.mapping.cache;

/**
 * 
 * @author qingji
 * @version 0.1
 * <br>Created on 2011-7-19 下午05:40:34
 */
public class ProxyCacheBean<E> {

	private long createTime;
	private E cacheData;
	
	/**
	 * @param cacheData
	 */
	public ProxyCacheBean(E cacheData) {
		this.createTime = System.currentTimeMillis();
		this.cacheData = cacheData;
	}
	public long getCreateTime() {
		return createTime;
	}
	public E getCacheData() {
		return cacheData;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public void setCacheData(E cacheData) {
		this.cacheData = cacheData;
	}
}
