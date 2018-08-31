package com.im.ui.schedule.task;   
import java.io.File;

import com.im.base.wechat.WechatMsg;
import com.im.schedule.queue.ThreadQueueTask; 
import com.im.utils.FileUtil;
import com.im.utils.StringHelper;
import com.wechat.WechatClient; 
import com.wechat.bean.WechatMsgType;

public class AutoReplyVideoMsgTask extends ThreadQueueTask{

	private WechatClient client;
	 
	private WechatMsg msg;
	
	private int delay;
	  
	public AutoReplyVideoMsgTask(WechatClient client,WechatMsg msg,int delay)
	{
		this.client=client;
		this.msg=msg;
		this.delay=delay;
	}
	
	@Override
	public void run() { 
		  
		String targetFilePath=findTargetFile();
		if(StringHelper.isEmpty(targetFilePath))
		{
			try {
				Thread.sleep(delay*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("SendMsgTask sleep end");
		} 
		targetFilePath=findTargetFile();
		if(StringHelper.isEmpty(targetFilePath))
		{
			client.sendTextMsg(client.getConfig().getDefaultReply(),  msg.getToUserName(), null);
			return ;
		}
        
		this.msg.setMsgType(WechatMsgType.getMsgTypeFromFileName(targetFilePath));
		this.msg.setContent(targetFilePath);
		 switch(msg.getMsgType())
		 {
		 case WechatMsgType.MSGTYPE_TEXT:
			 client.sendTextMsg(msg.getContent(),  msg.getToUserName(), null);
			 break;
		 case WechatMsgType.MSGTYPE_IMAGE:
			 client.sendImgMsg(msg.getContent(),  msg.getToUserName(), null);
			 break;
		 case WechatMsgType.MSGTYPE_APP:
			 client.sendFileMsg(msg.getContent(), msg.getToUserName(), null);
			 break;
		 case WechatMsgType.MSGTYPE_VIDEO:
			 client.sendVideoMsg(msg.getContent(), msg.getToUserName(), null);
			 break;
		 }
	}
	
	  private String findTargetFile()
	  {
		  String lookupPath=client.getConfig().getAutoReplyVideoLookupDirectory();
		  if(StringHelper.isEmpty(lookupPath))
		  {
			  return null;
		  }
		  File file= FileUtil.traverseFolder(lookupPath, msg.getContent());
		  if(file==null)
		  {
			  return null;
		  }
		  return file.getAbsolutePath();
	  }

}
