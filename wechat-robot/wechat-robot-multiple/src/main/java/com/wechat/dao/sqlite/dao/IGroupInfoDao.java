package com.wechat.dao.sqlite.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.im.base.wechat.WechatContact;

public interface IGroupInfoDao {

	public void save(@Param("group")WechatContact group);
	
	public void update(@Param("group")WechatContact group);
	
	public List<WechatContact> query();
	
	public WechatContact findGroupByName(@Param("groupName")String groupName);
}
