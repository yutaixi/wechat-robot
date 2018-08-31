package com.wechat.service;  
import iqq.im.event.QQNotifyEvent;

import java.util.HashMap; 
import java.util.Map; 

import com.im.base.wechat.WechatMsg;
import com.wechat.event.FutureEvent;

public class WechatFutureEventHandler extends WechatEventHandler  {

	protected Map<String,FutureEvent> futureEventMap =new HashMap<String,FutureEvent>();
	protected Map<String,WechatMsg> lastMsgMap =new HashMap<String,WechatMsg>(); 
	protected static final String ignoreMsgType="MSGTYPE_STATUSNOTIFY"; 
	
	@Override
	protected void afterHandleEvent(QQNotifyEvent event, WechatMsg wechatMsg) { 
		super.afterHandleEvent(event, wechatMsg);
		if(ignoreMsgType.contains(wechatMsg.getType()))
		{
			return;
		}
		lastMsgMap.put(wechatMsg.getFromUserName(), wechatMsg);
	} 
	protected void addFutureEvent(String userName,FutureEvent.Type type,Object target,int order)
	{
		FutureEvent futureEvent=new FutureEvent(userName,type,target,order);
		futureEventMap.put(userName, futureEvent); 
	} 
	protected void removeFutureEvent(String userName)
	{
		futureEventMap.remove(userName); 
	} 
	protected boolean processFutureEvent(final WechatMsg msg)
	{
		FutureEvent futureEvent=futureEventMap.get(msg.getFromUserName());
		if(futureEvent==null)
		{
			return false;
		} 
		switch(futureEvent.getType())
		{
		case SUBSCRIPTION_CONTENT_WAIT_TO_RECORD:  
			break;
		case FILM_WAIT_FOR_PAY:
			break;
		case CONTENT_WAIT_TO_CHOOSE:
			break;
		default:
			break;
		}  
		return false;
	}
}
