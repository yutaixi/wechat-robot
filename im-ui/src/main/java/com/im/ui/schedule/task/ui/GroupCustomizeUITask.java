package com.im.ui.schedule.task.ui;   
import com.im.schedule.queue.ThreadQueueTask; 
import com.im.ui.util.context.WindowContext;
import com.im.ui.wechatui.pane.TimingMsgPanel;  
import com.im.ui.wechatui.pane.groupmanage.GroupCustomizePanel;
import com.im.ui.wechatui.pane.groupmanage.GroupInviteManage;

public class GroupCustomizeUITask extends ThreadQueueTask{
  
	private int delay;
	  
	public GroupCustomizeUITask(int delay)
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
		GroupCustomizePanel groupCustomizePanel=WindowContext.getGroupCustomizePanel();
		if(groupCustomizePanel==null)
		{
			return;
		}
		groupCustomizePanel.refreshFriendList(); 
		System.out.println("GroupCustomizeUITask finished");
	}
	
	  

}
