package com.imooc.miaosha.dao;

import org.apache.ibatis.annotations.*;

import com.imooc.miaosha.model.MiaoshaUser;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface MiaoshaUserDao {
	
	@Select("select * from miaosha_user where id = #{id}")
	public MiaoshaUser getById(@Param("id") long id);

	@Update("update miaosha_user set password = #{password} where id = #{id}")
	public void update(MiaoshaUser toBeUpdate);

	@Insert("insert into miaosha_user (id , nickname ,password , salt ,head,register_date,last_login_date)value (#{id},#{nickname},#{password},#{salt},#{head},#{registerDate},#{lastLoginDate}) ")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public void insertMiaoshaUser(MiaoshaUser miaoShaUser);
}
