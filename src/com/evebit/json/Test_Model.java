package com.evebit.json;


/**
 * imgurl   	图片链接
 * introtext 	商品介绍
 * title		标题
 * integral     积分
 * username     用户名
 * email        邮箱
 * id           商品id
 */


public class Test_Model {


	private String imgurl;
	private String introtext;
	private String title;
	private String integral;
	private String username;
	private String id;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
		
	public String getImgUrl() {
		return imgurl;
	}

	public void setImgUrl(String imgurl) {
		this.imgurl = imgurl;
	}
	
	public String getIntrotext() {
		return introtext;
	}

	public void setIntrotext(String introtext) {
		this.introtext = introtext;
	}
	

}
