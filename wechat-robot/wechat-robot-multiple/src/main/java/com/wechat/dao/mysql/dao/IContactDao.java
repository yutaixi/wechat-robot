package com.wechat.dao.mysql.dao;

import org.apache.ibatis.annotations.Param;

import com.im.base.wechat.WechatContact;

public interface IContactDao {

	public void insertOrUpdateContact(@Param("contact")WechatContact contact,@Param("owner")Long owner);
	
	public WechatContact getContact(@Param("contact")WechatContact contact);
}
