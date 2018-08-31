package com.wechat.dao.mysql.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wechat.bean.WechatUser;

public interface IUserDao {

	public List<WechatUser> selectAll(); 
	
	public void saveUser(@Param("user")WechatUser user);
	
}
