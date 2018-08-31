package com.im.ui.schedule.task;

import java.util.List;

import com.im.base.schedule.WechatTimingMsgVO;
import com.im.base.vo.MediaFileVO;
import com.im.schedule.timer.ScheduleTask;
import com.im.ui.util.context.WindowContext;
import com.wechat.WechatClient;
import com.wechat.bean.WechatMsgType;
import com.wechat.service.MediaFileService;

public class WechatTimingMsgTask extends ScheduleTask{

	private List<WechatTimingMsgVO> timingMsgList;
	private List<String> toUserList;
	private WechatClient client;
	private MediaFileService mediaFileService=new MediaFileService();
	
	public WechatTimingMsgTask(WechatClient client,List<WechatTimingMsgVO> timingMsg,List<String> toUserList)
	{
		this.client=client;
		this.timingMsgList=timingMsg;
		this.toUserList=toUserList;
	}
	
	
	@Override
	public void run() {
		  if(timingMsgList==null || timingMsgList.size()==0 || toUserList==null || toUserList.size()<1)
		  {
			  WindowContext.getTimingMsgJobStatusPanel().stopStatus();
			  return;
		  }
		  for(int i=0;i<timingMsgList.size();i++)
		  { 
			  for(String userName: toUserList)
			  {
				  
				  for(int delayIndex=0;delayIndex<timingMsgList.get(i).getPeriod();delayIndex++)
					{
						try {
							Thread.sleep(500);
							WindowContext.getTimingMsgJobStatusPanel().inversStatus();
							Thread.sleep(500);
							WindowContext.getTimingMsgJobStatusPanel().inversStatus();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							return;
						}
					} 
				sendMsg(timingMsgList.get(i),userName);   
			  }
			  if(client.getConfig().getTimingMsgDelayBetweenFriends()!=null)
			  {
				  for(int delayIndex=0;delayIndex<client.getConfig().getTimingMsgDelayBetweenFriends();delayIndex++)
					{
						try {
							Thread.sleep(500);
							WindowContext.getTimingMsgJobStatusPanel().inversStatus();
							Thread.sleep(500);
							WindowContext.getTimingMsgJobStatusPanel().inversStatus();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							return;
						}
					}
			  }
			   
			  WindowContext.getTimingMsgLogUtil().log("已发送消息："+timingMsgList.get(i).getContent());
		  } 
		  WindowContext.getTimingMsgJobStatusPanel().stopStatus();
	}
	
	private void sendMsg(WechatTimingMsgVO timingMsg,String toUser)
	{
		 switch(timingMsg.getMsgType())
		 {
		 case WechatMsgType.MSGTYPE_TEXT:
			 client.sendTextMsg(timingMsg.getContent(),  toUser, null);
			 break;
		 case WechatMsgType.MSGTYPE_IMAGE:
			 client.sendImgMsg(timingMsg.getContent(),  toUser, null);
			 break;
		 case WechatMsgType.MSGTYPE_APP:
			 client.sendFileMsg(timingMsg.getContent(), toUser, null);
			 break;
		 case WechatMsgType.MSGTYPE_VIDEO:
			 client.sendVideoMsg(timingMsg.getContent(), toUser, null);
			 break;
		 case WechatMsgType.MSGTYPE_IMAGE_FORWARD:
			 MediaFileVO mediaFile=mediaFileService.findMediaFile(timingMsg.getContent());
			 if(mediaFile==null)
			 {
				 break;
			 }
			 String content=mediaFile.getContent();
			 content=content.replaceAll("&lt;", "<");
			 content=content.replaceAll("&gt;", ">"); 
			 client.sendImgContentMsg(content, toUser, null);
			 break;
		 default:
			 client.sendTextMsg(timingMsg.getContent(),  toUser, null);
			 break;
		 }
	}
	
	

}
