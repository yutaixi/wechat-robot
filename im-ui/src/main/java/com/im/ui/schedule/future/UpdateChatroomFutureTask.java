package com.im.ui.schedule.future;

import iqq.im.QQActionListener;
import iqq.im.event.QQActionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.im.base.wechat.WechatContact;
import com.im.utils.StringHelper;
import com.wechat.WechatClient;

public class UpdateChatroomFutureTask implements Callable<QQActionEvent>{

	private WechatClient client;
	private String chatroomUserName;
	private List<String> addUsers;
	private boolean needInvite;
	
	
	//
	private boolean finishFlag=false;
	private QQActionEvent resultEvent;
	public UpdateChatroomFutureTask(WechatClient client,String chatroomUserName,List<String> addUsers,boolean needInvite)
	{
		this.client=client;
		this.chatroomUserName=chatroomUserName;
		this.addUsers=addUsers;
		this.needInvite=needInvite;
	}
	
	@Override
	public QQActionEvent call() throws Exception {
		if(client==null || StringHelper.isEmpty(chatroomUserName)|| addUsers==null || addUsers.isEmpty())
		{
			return null;
		}
		 client.updateChatroom(chatroomUserName, addUsers,needInvite,new QQActionListener() { 
				@Override
				public void onActionEvent(QQActionEvent event) { 
					 if (event.getType() == QQActionEvent.Type.EVT_OK) { 
						 resultEvent=event;
						  finishFlag=true;
						 // doGroupInvite();
		                } else if (event.getType() == QQActionEvent.Type.EVT_ERROR) {
		                	resultEvent=event;
		                  finishFlag=true;
		                }
				}
				  
		 });
		 while(!finishFlag)
		 {
			 Thread.sleep(10);
		 }
		 
		return resultEvent;
	}

}
