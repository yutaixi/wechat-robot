package com.im.ui.schedule.task;

import com.im.base.wechat.WechatContact;
import com.im.schedule.queue.ThreadQueueTask;
import com.wechat.WechatClient;

public class WechatRemarkTask extends ThreadQueueTask{
	private WechatClient client;
	private String userName;
	private String remark;
	private int delay;
	public WechatRemarkTask(WechatClient client,String userName,String remark,int delay)
	{
		this.client=client;
		this.userName=userName;
		this.remark=remark;
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
		
		WechatContact contact=new WechatContact();
		contact.setUserName(userName);
		
		client.modRemark(contact, remark, null);
	}

}
