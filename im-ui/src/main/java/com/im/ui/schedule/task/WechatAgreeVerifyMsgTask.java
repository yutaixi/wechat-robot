package com.im.ui.schedule.task;

import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import iqq.im.QQActionListener;
import iqq.im.event.QQActionEvent;

import com.im.base.wechat.WechatContact;
import com.im.base.wechat.WechatMsgRecommendInfo;
import com.im.schedule.queue.ThreadQueueTask; 
import com.im.ui.util.context.WindowContext;
import com.wechat.WebWechatClient;

public class WechatAgreeVerifyMsgTask extends ThreadQueueTask{

	private static final Logger LOGGER = LoggerFactory.getLogger(WechatAgreeVerifyMsgTask.class); 
	private WebWechatClient mClient;  
	private WechatMsgRecommendInfo recInfo; 
	
	public WechatAgreeVerifyMsgTask(WebWechatClient mClient)
	{
		this.mClient=mClient; 
	}
	
	@Override
	public void run() {
		 
		while(true)
		{
			if(WindowContext.newFriendQueue.isEmpty())
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}else
			{
				
				try {
					
					recInfo = WindowContext.newFriendQueue.poll();
					long delayTime=getDelayTime();
					this.mClient.output(delayTime+"秒后同意"+recInfo.getNickName()+"的好友请求");
					Thread.sleep(delayTime*1000); 
					
					if(isAlreadyInContact(recInfo.getUserName()))
					{
						this.mClient.output("你和"+recInfo.getNickName()+"已经是好友了，无需再添加");
					}else
					{
						mClient.aggreeAddFriend(recInfo.getUserName(), recInfo.getTicket(), new QQActionListener() {
							@Override
							public void onActionEvent(QQActionEvent event) {
								if (event.getType() == QQActionEvent.Type.EVT_OK) {
									mClient.output("已同意" + recInfo.getNickName()+ "的好友请求");
									LOGGER.info("已同意" + recInfo.getNickName()+ "的好友请求");
								} else if (event.getType() == QQActionEvent.Type.EVT_ERROR) {
									mClient.output("添加好友" + recInfo.getNickName()+ "出错："+ String.valueOf(event.getTarget()));
									LOGGER.info("添加好友" + recInfo.getNickName()+ "出错："+ String.valueOf(event.getTarget()));
								} 
							}

				      });
					}
					
					
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
	
	private boolean isAlreadyInContact(String userName)
	{
		Map<String, WechatContact> buddyList=this.mClient.getWechatStore().getBuddyList();
		return buddyList.get(userName)!=null;
	}

	private long getDelayTime()
	{
		Long minDelay=this.mClient.getConfig().getAutoAgreeAddFriendMinDelay();
		minDelay=minDelay==null?0L:minDelay;
		Long maxDelay=this.mClient.getConfig().getAutoAgreeAddFriendMaxDealy();
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
