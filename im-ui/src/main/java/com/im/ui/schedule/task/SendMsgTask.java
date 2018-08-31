package com.im.ui.schedule.task;   
import com.im.base.vo.MediaFileVO;
import com.im.base.wechat.WechatMsg;
import com.im.schedule.queue.ThreadQueueTask;
import com.im.schedule.timer.ScheduleTask;
import com.wechat.WechatClient; 
import com.wechat.bean.WechatMsgType;
import com.wechat.service.MediaFileService;

public class SendMsgTask extends ThreadQueueTask{

	private MediaFileService mediaFileService=new MediaFileService();
	
	private WechatClient client;
	 
	private WechatMsg msg;
	
	private String toUserName;
	
	private Long delay;
	  
	public SendMsgTask(WechatClient client,WechatMsg msg,String toUserName,Long delay)
	{
		this.client=client;
		this.msg=msg;
		this.delay=delay;
		this.toUserName=toUserName;
	}
	
	@Override
	public void run() { 
		  
		if(delay!=null)
		{
			try {
				Thread.sleep(delay*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("SendMsgTask sleep end");
		 switch(msg.getMsgType())
		 {
		 case WechatMsgType.MSGTYPE_TEXT:
			 client.sendTextMsg(msg.getContent(),  toUserName, null);
			 break;
		 case WechatMsgType.MSGTYPE_IMAGE:
			 client.sendImgMsg(msg.getContent(), toUserName, null);
			 break;
		 case WechatMsgType.MSGTYPE_APP:
			 client.sendFileMsg(msg.getContent(), toUserName, null);
			 break;
		 case WechatMsgType.MSGTYPE_VIDEO:
			 client.sendVideoMsg(msg.getContent(), toUserName, null);
			 break;
		 case WechatMsgType.MSGTYPE_IMAGE_FORWARD:
			 MediaFileVO mediaFile=mediaFileService.findMediaFile(msg.getContent());
			 if(mediaFile==null)
			 {
				 break;
			 }
			 String content=mediaFile.getContent();
			 content=content.replaceAll("&lt;", "<");
			 content=content.replaceAll("&gt;", ">"); 
			 client.sendImgContentMsg(content, toUserName, null);
			 break;
		 }
	}
	
	  

}
