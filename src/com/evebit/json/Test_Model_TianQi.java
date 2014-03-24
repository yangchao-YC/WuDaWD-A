package com.evebit.json;

/**
 * username     用户名
 * password 	密码
 * type		        标题
 * errorid      返回错误信息
 * integral     积分
 * email        邮箱
 * rankings     用户排名
 * name         送货姓名
 * phone        送货电话
 * address      送货地址
 * lpid         兑换商品id
 */

public class Test_Model_TianQi {

	private String username;
	private String password;
	private String type;
	private String errorid;
	private String email;
	private String integral;
	private String rankings;		
	private String name; 
	private String phone;
	private String address;
	private String lpid;
	private String error;

	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLpid() {
		return lpid;
	}
	public void setLpid(String lpid) {
		this.lpid = lpid;
	}
	public String getRankings() {
		return rankings;
	}
	public void setRankings(String rankings) {
		this.rankings = rankings;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getErrorid() {
		return errorid;
	}
	public void setErrorid(String errorid) {
		this.errorid = errorid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}
		
	
}
