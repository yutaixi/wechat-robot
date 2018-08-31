package com.im.ui.schedule.task.ui;   
import com.im.schedule.queue.ThreadQueueTask; 
import com.im.ui.util.context.WindowContext;
import com.im.ui.wechatui.pane.FriendVerifyPanel;
import com.im.ui.wechatui.pane.TimingMsgPanel;  
import com.im.ui.wechatui.pane.groupmanage.GroupInviteManage;

public class InitFriendVerifyUITask extends ThreadQueueTask{
  
	private int delay;
	  
	public InitFriendVerifyUITask(int delay)
	{ 
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
		FriendVerifyPanel friendVerifyPanel=WindowContext.getFriendVerifyPanel();
		if(friendVerifyPanel==null)
		{
			return ;
		}
		friendVerifyPanel.refreshGroupList();
		System.out.println("InitFriendVerifyUITask finished");
	}
	
	  

}
