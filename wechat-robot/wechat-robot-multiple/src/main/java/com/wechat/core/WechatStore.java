package com.wechat.core; 
import java.util.HashMap;
import java.util.Map; 
import java.util.concurrent.ConcurrentHashMap;

import iqq.im.QQException;
import iqq.im.core.QQContext;
import iqq.im.core.QQLifeCycle; 

import com.blade.kit.json.JSONObject;
import com.im.base.wechat.WechatContact;
import com.wechat.bean.WechatUser;

public class WechatStore implements QQLifeCycle{
	   
	//private Map<String,JSONObject> buddyList;
	private Map<String,WechatContact> chatRoom;
	private Map<String,WechatContact> groupList; 
	private Map<String,WechatContact> buddyList;
	private WechatUser user;
	
	public WechatStore()
	{
		 
		//buddyList=new HashMap<String,JSONObject>();
		buddyList=new ConcurrentHashMap<String,WechatContact>();
		chatRoom=new ConcurrentHashMap<String,WechatContact>();
		groupList=new ConcurrentHashMap<String,WechatContact>();
	}
	 
	
	
	public WechatUser getUser() {
		return user;
	}



	public void setUser(WechatUser user) {
		this.user = user;
	}

 
 
	public Map<String, WechatContact> getGroupList() {
		return groupList;
	}



	public void setGroupList(Map<String, WechatContact> groupList) {
		this.groupList = groupList;
	}



	public Map<String, WechatContact> getBuddyList() {
		return buddyList;
	}



	public void setBuddyList(Map<String, WechatContact> buddyList) {
		this.buddyList = buddyList;
	}

 
	 
	@Override
	public void init(QQContext context) throws QQException {
		 
		
	}
	@Override
	public void destroy() throws QQException {
		// TODO Auto-generated method stub
		
	}



	public Map<String, WechatContact> getChatRoom() {
		return chatRoom;
	}



	public void setChatRoom(Map<String, WechatContact> chatRoom) {
		this.chatRoom = chatRoom;
	}
	
	
	
}
