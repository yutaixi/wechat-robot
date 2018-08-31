package com.im.ui.schedule.task;   
import java.util.Map;

import com.im.base.wechat.WechatContact;
import com.im.schedule.queue.ThreadPoolManager;
import com.im.schedule.queue.ThreadQueueTask; 
import com.im.ui.schedule.task.ui.GroupCustomizeUITask;
import com.im.ui.schedule.task.ui.InitFriendVerifyUITask;
import com.im.ui.schedule.task.ui.InitGroupInviteUITask;
import com.im.ui.schedule.task.ui.InitUITask;
import com.im.ui.util.context.WindowContext;
import com.im.ui.wechatui.pane.TimingMsgPanel;  
import com.im.ui.wechatui.utils.UserUtils;
import com.im.utils.StringHelper;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.core.WechatStore;

public class InitContactTask extends ThreadQueueTask{
  
	private int delay;
	  
	private WechatClient client;
	
	public InitContactTask(WechatClient client,int delay)
	{ 
		this.client=client;
		this.delay=delay;
	}
	
	@Override
	public void run() {
		  
		try {
			Thread.sleep(delay*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("InitContactTask delay end");
		
		if(client==null)
		{
			return;
		}
		
		WebWechatClient webWechatClient=(WebWechatClient)this.client;
		WechatStore store=webWechatClient.getWechatStore();
		Map<String, WechatContact> buddyContactMap=store.getBuddyList();
		Map<String, WechatContact> groupList=store.getGroupList();
		Map<String, WechatContact> chatRoom=store.getChatRoom();
		String name="";
		for(WechatContact temp : buddyContactMap.values())
		{
			if(StringHelper.isEmpty(temp.getNickName()) || temp.getNickName().indexOf("<span")<0)
			{
				continue;
			} 
			name=UserUtils.removeEmoji(temp.getNickName());
			temp.setNickName(name);
		}
		
		for(WechatContact temp : groupList.values())
		{
			if(StringHelper.isEmpty(temp.getNickName()) || temp.getNickName().indexOf("<span")<0)
			{
				continue;
			} 
			name=UserUtils.removeEmoji(temp.getNickName());
			temp.setNickName(name);
		}
		for(WechatContact temp : chatRoom.values())
		{
			if(StringHelper.isEmpty(temp.getNickName()) || temp.getNickName().indexOf("<span")<0)
			{
				continue;
			} 
			name=UserUtils.removeEmoji(temp.getNickName());
			temp.setNickName(name);
		}
		
		ThreadPoolManager.newInstance().addTask(new InitUITask(0));
		ThreadPoolManager.newInstance().addTask(new InitGroupInviteUITask(0));
		ThreadPoolManager.newInstance().addTask(new InitFriendVerifyUITask(0));
		ThreadPoolManager.newInstance().addTask(new GroupCustomizeUITask(0));
		
		this.client.beginPollMsg();
	}
	
	  
	public static void main(String[] args)
	{ 
		System.out.println(UserUtils.removeEmoji("test<span emojisdfsdf></span>世界覅"));
	}

}
