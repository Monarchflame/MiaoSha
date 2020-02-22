package com.imooc.miaosha.redis;

public class UserKey extends BasePrefix{
	private UserKey(String prefix) {
		super(prefix);
	}
	//通过id做key，未设置过期时间，默认为0，详见super
	public static UserKey getById = new UserKey("id");
	public static UserKey getByName = new UserKey("name");
}
