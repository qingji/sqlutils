package im.qingji.sqlutil.examples;

import im.qingji.sqlutil.mapping.annotation.Column;
import im.qingji.sqlutil.mapping.annotation.Table;

@Table(name="test_user")
public class User {
	@Column
	private long id;
	@Column
	private String name;
	@Column
	private String password;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
