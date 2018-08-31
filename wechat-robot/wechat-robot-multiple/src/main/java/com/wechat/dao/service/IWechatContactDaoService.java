package com.wechat.dao.service;

import java.util.Map;

import com.im.base.wechat.WechatContact;

public interface IWechatContactDaoService {

	public void insertOrUpdateContact(Map<String,WechatContact> contactMap,Long owner);
	
	
	
}
