package com.wechat.dao.sqlite.dao;

import org.apache.ibatis.annotations.Param;

import com.im.base.wechat.WechatMsg;

public interface IWechatMsgDao {

	public void saveMsg(@Param("msg")WechatMsg msg);

}
