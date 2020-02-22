package com.imooc.miaosha.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.imooc.miaosha.validator.IsMobile;

public class LoginVo {
	
	@NotNull
	@IsMobile//自定义验证器，就不用在登录函数中验证了
	private String mobile;
	
	@NotNull
	@Length(min=32)
	private String password;
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "LoginVo [mobile=" + mobile + ", password=" + password + "]";
	}
}