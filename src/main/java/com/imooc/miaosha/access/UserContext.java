package com.imooc.miaosha.access;

import com.imooc.miaosha.model.MiaoshaUser;

public class UserContext {

	//ThreadLocal是多线程时保证线程安全的一种访问方式。ThreadLocal与当前线程绑定，往ThreadLocal里面放东西，是放到当前线程里面来
	//这里是为了线程资源不共享，即每个线程有每个线程的user
	private static ThreadLocal<MiaoshaUser> userHolder = new ThreadLocal<MiaoshaUser>();
	
	public static void setUser(MiaoshaUser user) {
		userHolder.set(user);
	}
	
	public static MiaoshaUser getUser() {
		return userHolder.get();
	}

}
