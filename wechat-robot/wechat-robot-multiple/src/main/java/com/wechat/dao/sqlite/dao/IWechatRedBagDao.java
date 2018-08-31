package com.wechat.dao.sqlite.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.im.base.log.WechatRedBagLogVO;

public interface IWechatRedBagDao {

	
	public void saveRedBagMsg(@Param("redBag")WechatRedBagLogVO redBag);
	
	public void updateRedBagMsg(@Param("redBag")WechatRedBagLogVO redBag);
	
	public List<WechatRedBagLogVO> queryBagMsg();
	
//	public void updateRedBagMsg();
}
