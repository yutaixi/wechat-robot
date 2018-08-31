package com.im.ui.schedule.task.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iqq.im.QQActionListener;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQNotifyEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.base.wechat.WechatContact;
import com.im.base.wechat.WechatMsg;
import com.im.schedule.queue.ThreadQueueTask;
import com.im.ui.util.context.WindowContext;
import com.subscription.KeywordVO;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.core.WechatConstants;
import com.wechat.core.WechatContext;
import com.wechat.core.WechatStore;
import com.wechat.event.FutureEvent;

public class WechatFutureMsgEventHandlerTask extends WechatDefaultEventHandlerTask{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WechatFutureMsgEventHandlerTask.class);
	
	protected static final String ignoreMsgType="MSGTYPE_STATUSNOTIFY"; 
	
	public WechatFutureMsgEventHandlerTask(WebWechatClient mClient,
			QQNotifyEvent event) {
		super(mClient, event);
		 
	}

	@Override
	protected void afterHandleEvent(QQNotifyEvent event, WechatMsg wechatMsg) { 
		super.afterHandleEvent(event, wechatMsg);
		if(ignoreMsgType.contains(wechatMsg.getType()))
		{
			return;
		}
		WindowContext.getLastMsgMap().put(wechatMsg.getFromUserName(), wechatMsg);
	} 
	protected void addFutureEvent(String userName,FutureEvent.Type type,Object target,int order)
	{
		FutureEvent futureEvent=new FutureEvent(userName,type,target,order);
		WindowContext.getFutureEventMap().put(userName, futureEvent); 
	} 
	
	protected void addFutureEvent(String userName,FutureEvent.Type type,Object target)
	{
		FutureEvent futureEvent=new FutureEvent(userName,type,target);
		WindowContext.getFutureEventMap().put(userName, futureEvent); 
	} 
	
	protected void addFutureEvent(String userName,FutureEvent.Type type,Object target,int order,Object data)
	{
		FutureEvent futureEvent=new FutureEvent(userName,type,target,order,data);
		WindowContext.getFutureEventMap().put(userName, futureEvent); 
	} 
	
	protected void removeFutureEvent(String userName)
	{
		WindowContext.getFutureEventMap().remove(userName); 
	} 
	protected boolean processFutureEvent(final WechatMsg msg)
	{
		 return false;
	}
 
	 
}
