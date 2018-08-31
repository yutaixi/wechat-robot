package com.im.ui.schedule.future;

import iqq.im.QQActionListener;
import iqq.im.event.QQActionEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.im.base.wechat.WechatContact;
import com.wechat.WechatClient;

public class BatchGetContactInfoFutureTask implements Callable<List<WechatContact>>{

	private WechatClient client;
	private Map<String,WechatContact> contacts;
	
	
	//
	private boolean finishFlag=false;
	private List<WechatContact> resultContacts;
	public BatchGetContactInfoFutureTask(WechatClient client,Map<String,WechatContact> contacts)
	{
		this.client=client;
		this.contacts=contacts;
	}
	
	@Override
	public List<WechatContact> call() throws Exception {
		if(client==null || contacts==null || contacts.isEmpty())
		{
			return null;
		}
		 client.batchGetContactInfo(contacts, new QQActionListener() { 
				@Override
				public void onActionEvent(QQActionEvent event) {
					// TODO Auto-generated method stub
					 if (event.getType() == QQActionEvent.Type.EVT_OK) { 
						  resultContacts=(List<WechatContact>)event.getTarget();
						  finishFlag=true;
						 // doGroupInvite();
		                } else if (event.getType() == QQActionEvent.Type.EVT_ERROR) {
		                  finishFlag=true;
		                }
				}
				  
		 });
		 while(!finishFlag)
		 {
			 Thread.sleep(10);
		 }
		 
		return resultContacts;
	}

}
