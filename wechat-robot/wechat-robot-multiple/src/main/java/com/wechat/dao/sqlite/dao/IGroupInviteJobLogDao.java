package com.wechat.dao.sqlite.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.im.base.log.WechatGroupInviteLog;
import com.im.base.log.WechatGroupInviteLogSearchVO;

public interface IGroupInviteJobLogDao {
	
	public void saveLog(@Param("log")WechatGroupInviteLog log);
	
	public List<WechatGroupInviteLog> queryGroupInviteLog(@Param("log")WechatGroupInviteLogSearchVO log);
	 

	public WechatGroupInviteLog findFirstRecord(@Param("log")WechatGroupInviteLogSearchVO log);
}
