package com.im.ui.schedule.task;

import iqq.im.QQActionListener;
import iqq.im.event.QQActionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.base.wechat.WechatContact;
import com.im.base.wechat.WechatMsg;
import com.im.base.wechat.WechatMsgRecommendInfo;
import com.im.schedule.queue.ThreadQueueTask;
import com.im.ui.wechatui.utils.ContactUtils;
import com.im.utils.StringHelper;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.action.ActionResponse;
import com.wechat.bean.WechatMsgType;
import com.wechat.service.WechatEventHandler;

public class HandlerOnModContactListTask  extends ThreadQueueTask{
	private static final Logger LOGGER = LoggerFactory.getLogger(HandlerOnModContactListTask.class);
	private WechatMsg msg;
	
	private WebWechatClient mClient;
	
	public HandlerOnModContactListTask(WebWechatClient mClient,WechatMsg msg)
	{
		this.mClient=mClient;
		this.msg=msg;
	}
	
	@Override
	public void run() {
		
		if(msg.getFromUserName().startsWith("@@"))
		{
			return;
		}
		WechatMsgRecommendInfo recInfo=msg.getRecommendInfo();
		if(recInfo==null)
		{
			return ;
		}
		 
		Map<String, WechatContact> buddyList=mClient.getWechatStore().getBuddyList(); 
		Map<String, WechatContact> groupList=mClient.getWechatStore().getGroupList();
		String userName=recInfo.getUserName();
		if(userName==null || "".equalsIgnoreCase(userName))
		{
			return;
		}
		
		if(userName.startsWith("@@"))
		{ 
			groupList.put(userName,recInfo);
		}else 
		{
			buddyList.put(userName,recInfo);
		}
		mClient.output("通讯录中添加联系人："+recInfo.getNickName()); 
		//
		Map<Long, WechatMsg> welcomeMsgMap=mClient.getConfig().getWelcomeMsgMap();
		if(welcomeMsgMap!=null && !welcomeMsgMap.isEmpty())
		{
			for(WechatMsg temp : welcomeMsgMap.values())
			{
//				temp.setToUserName(recInfo.getUserName());
				switch(temp.getMsgType())
				{ 
				case WechatMsgType.MSGTYPE_TEXT:
					mClient.sendTextMsg(temp.getContent(), recInfo.getUserName(), null);
//					SendMsgTask task =new SendMsgTask(mClient,temp,10);
//					ThreadPoolManager.newInstance().addTask(task);
					break;
				case WechatMsgType.MSGTYPE_IMAGE:
					mClient.sendImgMsg(temp.getContent(), recInfo.getUserName(), null);
					break;
				case WechatMsgType.MSGTYPE_VIDEO:
					mClient.sendVideoMsg(temp.getContent(), recInfo.getUserName(), null);
					break;
				case WechatMsgType.MSGTYPE_APP:
					mClient.sendFileMsg(temp.getContent(), recInfo.getUserName(), null);
					break;
				}
			}
		}
		
		 
		if(mClient.getConfig().isAfterVeifyInviteToGroup())
		{
			addToGroup(recInfo);
		}
		
	}
	private void addToGroup(final WechatMsgRecommendInfo recInfo)
	{
		String groupNames=mClient.getConfig().getAfterVeifyInviteToGroupName();
		if(groupNames.endsWith("\n"))
		{
			groupNames=groupNames.substring(0, groupNames.indexOf("\n"));
		}
		String[] groupNameArray={groupNames};
		if(groupNames.indexOf(";")>-1)
		{
			groupNameArray=groupNames.split(";");
		}else if(groupNames.indexOf("；")>-1)
		{
			groupNameArray=groupNames.split("；");
		} 
		final List<String> users=new ArrayList<String>();
		 users.add(recInfo.getUserName());
		for(final String temp : groupNameArray)
		{
			final String groupName=ContactUtils.getUserNameByNickName(temp,mClient);
			if(!StringHelper.isEmpty(groupName))
			{
				mClient.updateChatroom(groupName, users, false, new QQActionListener(){

					@Override
					public void onActionEvent(QQActionEvent event) {
						if (event.getType() == QQActionEvent.Type.EVT_OK) { 
							mClient.output("已邀请"+recInfo.getNickName()+"加入群聊"+temp);
							LOGGER.info("已邀请"+recInfo.getNickName()+"加入群聊"+temp);
						}else if (event.getType() == QQActionEvent.Type.EVT_ERROR) {
							ActionResponse actionResponse=(ActionResponse)event.getTarget();
							int code=actionResponse.getStatus();
							if(code==-23)
							{
								inviteUserToGroup(groupName,users,temp,recInfo);
							}else
							{
								mClient.output("邀请"+recInfo.getNickName()+"加入群聊"+temp+"失败:"+event.getTarget());
								LOGGER.info("邀请"+recInfo.getNickName()+"加入群聊"+temp+"失败:"+event.getTarget());
								
							}
							
						}
						
					}
					
				});
			}else
			{
				mClient.output("邀请加入群聊失败"+"未找到群聊"+temp);
				LOGGER.info("邀请加入群聊失败"+"未找到群聊"+temp);
			}
			
		}
		
	}
	public void inviteUserToGroup(String groupName,List<String> users,final String groupNameStr,final WechatMsgRecommendInfo recInfo)
	{
		mClient.updateChatroom(groupName, users, true,  new QQActionListener(){ 
			@Override
			public void onActionEvent(QQActionEvent event) {
				if (event.getType() == QQActionEvent.Type.EVT_OK) { 
					mClient.output("已邀请"+recInfo.getNickName()+"加入群聊"+groupNameStr);
					LOGGER.info("已邀请"+recInfo.getNickName()+"加入群聊"+groupNameStr);
				}else if (event.getType() == QQActionEvent.Type.EVT_ERROR) { 
					mClient.output("邀请"+recInfo.getNickName()+"加入群聊"+groupNameStr+"失败:"+event.getTarget());
					LOGGER.info("邀请"+recInfo.getNickName()+"加入群聊"+groupNameStr+"失败:"+event.getTarget());
				}
				
			}
			
		});
	}

}
