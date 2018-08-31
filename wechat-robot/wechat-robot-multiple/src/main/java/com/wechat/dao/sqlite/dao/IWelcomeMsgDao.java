package com.wechat.dao.sqlite.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.im.base.wechat.WechatMsg;

public interface IWelcomeMsgDao {

	public void saveWelcomeMsg(@Param("msg")WechatMsg msg);
	
	public void updateWelcomeMsg(@Param("msg")WechatMsg msg);
	
	public void deleteWelcomeMsg(@Param("msg")WechatMsg msg);
	
	public List<WechatMsg> queryAllWelcomeMsg();
}
