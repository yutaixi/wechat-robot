package com.wechat.dao.mysql.dao;

import org.apache.ibatis.annotations.Param; 

import com.im.base.wechat.WechatMsg;

public interface IWechatMsgDao {

	public void saveMsg(@Param("msg")WechatMsg msg);
}
