package com.im.ui.schedule.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import iqq.im.QQActionListener;
import iqq.im.event.QQActionEvent;

import com.im.base.wechat.WechatContact;
import com.im.base.wechat.WechatMsgRecommendInfo;
import com.im.schedule.queue.ThreadQueueTask; 
import com.im.ui.util.context.WindowContext;
import com.im.ui.wechatui.utils.UserUtils;
import com.im.utils.MathUtil;
import com.im.utils.StringHelper;
import com.wechat.WebWechatClient;
import com.wechat.action.ActionResponse;

public class WechatGroupInviteAfterVerifyTask extends ThreadQueueTask{

	private static final Logger LOGGER = LoggerFactory.getLogger(WechatGroupInviteAfterVerifyTask.class); 
	private WebWechatClient mClient;  
	private WechatMsgRecommendInfo recInfo; 
	
	public WechatGroupInviteAfterVerifyTask(WebWechatClient mClient)
	{
		this.mClient=mClient; 
	}
	
	@Override
	public void run() {
		 
		while(true)
		{
			if(WindowContext.newFriendGroupInviteQueue.isEmpty())
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) { 
					e.printStackTrace();
				} 
			}else
			{
				try {
					Thread.sleep(getDelayTime()*1000); 
					recInfo = WindowContext.newFriendGroupInviteQueue.poll(); 
					addToGroup(recInfo);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch(Exception e)
				{
					LOGGER.error(""+e);
				}
				
				
			}
		}
		
		
		
	}
	
	private void addToGroup(final WechatMsgRecommendInfo recInfo)
	{
//		String groupNames=mClient.getConfig().getAfterVeifyInviteToGroupName();
//		if(groupNames.endsWith("\n"))
//		{
//			groupNames=groupNames.substring(0, groupNames.indexOf("\n"));
//		}
//		String[] groupNameArray={groupNames};
//		if(groupNames.indexOf(";")>-1)
//		{
//			groupNameArray=groupNames.split(";");
//		}else if(groupNames.indexOf("；")>-1)
//		{
//			groupNameArray=groupNames.split("；");
//		} 
		if(this.mClient.getConfig().getInviteToGroupsAfterVeify().isEmpty())
		{
			return;
		}
		List<WechatContact> groupList=new ArrayList<WechatContact>(this.mClient.getConfig().getInviteToGroupsAfterVeify().values());
		 
		List<WechatContact> targetGroupList=null;
		if(this.mClient.getConfig().getInviteToGroupModeAfterVeify()==1 && groupList.size()>1)
		{
			targetGroupList=new ArrayList<WechatContact>();
			int randomIndex=MathUtil.getRandomNum(groupList.size()-1);
			targetGroupList.add(groupList.get(randomIndex));
			
		}else
		{
			targetGroupList=groupList;
		}
		final List<String> users=new ArrayList<String>();
		 users.add(recInfo.getUserName());
		for(final WechatContact temp : targetGroupList)
		{
//			final String groupName=UserUtils.getUserNameByNickName(this.mClient,temp.getUserName());
			final String groupName=temp.getUserName();
			if(!StringHelper.isEmpty(groupName))
			{
				mClient.updateChatroom(groupName, users, false, new QQActionListener(){

					@Override
					public void onActionEvent(QQActionEvent event) {
						if (event.getType() == QQActionEvent.Type.EVT_OK) { 
							mClient.output("已邀请"+recInfo.getNickName()+"加入群聊"+temp.getNickName());
							LOGGER.info("已邀请"+recInfo.getNickName()+"加入群聊"+temp.getNickName());
						}else if (event.getType() == QQActionEvent.Type.EVT_ERROR) {
							ActionResponse actionResponse=(ActionResponse)event.getTarget();
							int code=actionResponse.getStatus();
							if(code==-23)
							{
								inviteUserToGroup(groupName,users,temp.getNickName(),recInfo);
							}else
							{
								mClient.output("邀请"+recInfo.getNickName()+"加入群聊"+temp.getNickName()+"失败:"+actionResponse.getResponseData());
								LOGGER.info("邀请"+recInfo.getNickName()+"加入群聊"+temp.getNickName()+"失败:"+actionResponse.getResponseData());
								
							}
							
						}
						
					}
					
				});
			}else
			{
				mClient.output("邀请加入群聊失败"+"未找到群聊"+temp.getNickName());
				LOGGER.info("邀请加入群聊失败"+"未找到群聊"+temp.getNickName());
			}
			
		}
		
	}
	private void inviteUserToGroup(String groupName,List<String> users,final String groupNameStr,final WechatMsgRecommendInfo recInfo)
	{
		mClient.updateChatroom(groupName, users, true,  new QQActionListener(){ 
			@Override
			public void onActionEvent(QQActionEvent event) {
				if (event.getType() == QQActionEvent.Type.EVT_OK) { 
					mClient.output("已邀请"+recInfo.getNickName()+"加入群聊"+groupNameStr);
					LOGGER.info("已邀请"+recInfo.getNickName()+"加入群聊"+groupNameStr);
				}else if (event.getType() == QQActionEvent.Type.EVT_ERROR) { 
					ActionResponse actionResponse=(ActionResponse)event.getTarget();
					mClient.output("邀请"+recInfo.getNickName()+"加入群聊"+groupNameStr+"失败:"+actionResponse.getResponseData());
					LOGGER.info("邀请"+recInfo.getNickName()+"加入群聊"+groupNameStr+"失败:"+actionResponse.getResponseData());
				}
				
			}
			
		});
	}

	private long getDelayTime()
	{
		Long minDelay=this.mClient.getConfig().getInviteToGroupAfterVeifyMinDelay();
		minDelay=minDelay==null?0L:minDelay;
		Long maxDelay=this.mClient.getConfig().getInviteToGroupAfterVeifyMaxDelay();
		maxDelay=maxDelay==null?0L:maxDelay;
		Random random=new Random();
		if(minDelay==0 && maxDelay==0)
		{
			return 0l;
		}
		long delayTime=random.nextInt((int)Math.abs(minDelay-maxDelay))+minDelay;
		return delayTime;
	}
}
