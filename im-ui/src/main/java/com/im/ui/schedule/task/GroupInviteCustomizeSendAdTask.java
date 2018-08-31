package com.im.ui.schedule.task;    
import java.util.concurrent.ConcurrentHashMap;  

import com.im.base.customize.GroupSendAdVO;
import com.im.schedule.queue.ThreadQueueTask;
import com.im.utils.StringHelper;
import com.im.utils.SysUtil;
import com.wechat.WechatClient;  

public class GroupInviteCustomizeSendAdTask extends ThreadQueueTask{
 
	private WechatClient client; 
	
	private boolean stopFlag=false;
	private static ConcurrentHashMap<String, GroupSendAdVO> groupToSendMsgMap=new ConcurrentHashMap<String,GroupSendAdVO>();
	 
	public GroupInviteCustomizeSendAdTask(WechatClient client)
	{
		this.client=client; 
	}
	
	@Override
	public void run() { 
		  
		while(true && !stopFlag )
		{
			Long delay=client.getConfig().getGroupCustomizeSendAdDelay();
			delay=delay==null?1L:delay; 
			Long times=this.client.getConfig().getGroupCustomizeSendAdTimes();
			times=times==null?0L:times;
			if(times==0)
			{
				continue;
			}
			
			if(groupToSendMsgMap.isEmpty())
			{
				SysUtil.sleep(1000);
			}else
			{
				for(GroupSendAdVO temp : groupToSendMsgMap.values())
				{
					if(System.currentTimeMillis()-temp.getLastSendTime()>delay*1000 )
					{
						if(temp.getTimes()<times)
						{
							sendAd(temp.getUserName());
						} 
						temp.setTimes(temp.getTimes()+1);
						temp.setLastSendTime(System.currentTimeMillis());
						if(temp.getTimes()>=times)
						{
							groupToSendMsgMap.remove(temp.getUserName());
						}
						
					}
					
					
				}
			}
			
			
			
			
		}
		
		  
		  
		   
	}
	
	private void sendAd(String userName)
	{
		String textAd=this.client.getConfig().getGroupCustomizeSendAdContent();
		if(!StringHelper.isEmpty(textAd))
		{
			this.client.sendTextMsg(textAd, userName, null);
		}
		
		String picAd=this.client.getConfig().getGroupCustomizeSendAdPic();
		SysUtil.sleep(5000);
		if(!StringHelper.isEmpty(picAd))
		{
			this.client.sendImgMsg(picAd, userName, null);
		}
		SysUtil.sleep(2000);
	}
	
	  
	public static void addGroup(String groupUserName)
	{
		GroupSendAdVO groupSendAdVO=new GroupSendAdVO();
		groupSendAdVO.setUserName(groupUserName);
		groupToSendMsgMap.put(groupUserName,groupSendAdVO);  
	}
	
	public static void main(String[] args)
	{
		Long start=System.currentTimeMillis();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Long end=System.currentTimeMillis(); 
		System.out.println((end-start)/1000);
	}

}
