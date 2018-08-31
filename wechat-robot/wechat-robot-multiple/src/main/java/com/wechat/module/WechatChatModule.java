package com.wechat.module;
  
import java.io.File; 
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

import com.im.base.vo.MediaFileVO;
import com.im.base.wechat.WechatMsg;
import com.im.base.wechat.WechatMsgAppInfo;
import com.wechat.action.WechatCreateChatroomAction;
import com.wechat.action.WechatGetMsgImgAction;
import com.wechat.action.WechatSendAppMsgAction;
import com.wechat.action.WechatSendImgCotentMsgAction;
import com.wechat.action.WechatSendImgMsgAction;
import com.wechat.action.WechatSendTextMsgAction;
import com.wechat.action.WechatSendVideoMsgAction;
import com.wechat.action.WechatUpdateChatroomAction;
import com.wechat.action.WechatUploadFileAction;
import com.wechat.bean.WechatAppidDb;
import com.wechat.bean.WechatMediaInfo;
import com.wechat.bean.WechatSendMsgType;
import com.wechat.service.MediaFileService;

import iqq.im.QQActionListener;
import iqq.im.core.QQModule;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQActionFuture;
import iqq.im.event.future.ProcActionFuture;
import iqq.im.module.AbstractModule;

public class WechatChatModule  extends AbstractModule{

	private static final Logger LOGGER = LoggerFactory.getLogger(WechatChatModule.class); 
	
	
	public QQActionFuture uploadFile(String fileName,String toUser,QQActionListener listener) {
        return pushHttpAction(new WechatUploadFileAction(getContext(), listener,fileName,toUser));
    }
	
	public QQActionFuture getMsgImg(String msgId,String skey,QQActionListener listener) {
        return pushHttpAction(new WechatGetMsgImgAction(getContext(), listener,msgId,skey));
    }
	
	public QQActionFuture sendTextMsg(String fileName,String toUser,QQActionListener listener) {
        return pushHttpAction(new WechatSendTextMsgAction(getContext(),listener, fileName,toUser));
    }
	//有延时文本消息
	public QQActionFuture sendImgContentMsg(String content,String toUser,QQActionListener listener) {
        return pushHttpAction(new WechatSendImgCotentMsgAction(getContext(),listener, content,toUser));
    }
	
	public QQActionFuture sendDelayedTextMsg(String fileName,String toUser,Long delay,QQActionListener listener) {
        return pushHttpAction(new WechatSendTextMsgAction(getContext(),listener, fileName,toUser,delay));
    }
	
	 
	public QQActionFuture sendImgMsg(String fileName,final String toUser,final QQActionListener listener) { 
		final ProcActionFuture future = new ProcActionFuture(listener, true); 
		WechatChatModule chat = getContext().getModule(QQModule.Type.CHAT);
//		MediaFileVO mediaFile=mediaFileService.findMediaFile(fileName);
//		if(mediaFile!=null)
//		{
//			LOGGER.info("文件"+fileName+"已存在，不需重新上传");
//			doSendImgMsg(mediaFile.getMediaId(),toUser,future);
//			
//		}else
//		{ 
			chat.uploadFile(fileName, toUser, new QQActionListener(){ 
				@Override
				public void onActionEvent(QQActionEvent event) {
					if(event.getType()==QQActionEvent.Type.EVT_OK)
					{
						WechatMediaInfo mediaInfo=(WechatMediaInfo)event.getTarget();
						doSendImgMsg(mediaInfo.getMediaId(),toUser,future);
					}else if(event.getType()==QQActionEvent.Type.EVT_ERROR)
					{
						LOGGER.error("图片上传错误:"+event.getTarget());
						future.notifyActionEvent(QQActionEvent.Type.EVT_ERROR, event.getTarget());
					}
					
				}
				
			} );
//		}
		
		
        return future;
    }
	public QQActionFuture sendVideoMsg(String fileName,final String toUser,final QQActionListener listener) { 
		final ProcActionFuture future = new ProcActionFuture(listener, true); 
		WechatChatModule chat = getContext().getModule(QQModule.Type.CHAT);
		chat.uploadFile(fileName, toUser, new QQActionListener(){ 
			@Override
			public void onActionEvent(QQActionEvent event) {
				if(event.getType()==QQActionEvent.Type.EVT_OK)
				{
					WechatMediaInfo mediaInfo=(WechatMediaInfo)event.getTarget();
					doSendVideoMsg(mediaInfo.getMediaId(),toUser,future);
				}else if(event.getType()==QQActionEvent.Type.EVT_ERROR)
				{
					LOGGER.error("视频上传错误");
					future.notifyActionEvent(QQActionEvent.Type.EVT_ERROR, event.getTarget());
				}
				
			}
			
		} );
        return future;
    }
	public QQActionFuture doSendImgMsg(String mediaId,String toUser,final QQActionListener future) { 
		return pushHttpAction(new WechatSendImgMsgAction(getContext(),future, mediaId,toUser)); 
	}
	public QQActionFuture doSendVideoMsg(String mediaId,String toUser,final QQActionListener future) { 
		return pushHttpAction(new WechatSendVideoMsgAction(getContext(),future, mediaId,toUser)); 
	}
	 
	
	
	public QQActionFuture sendFileMsg(final String fileName,final String toUser,final QQActionListener listener) { 
		final ProcActionFuture future = new ProcActionFuture(listener, true); 
		WechatChatModule chat = getContext().getModule(QQModule.Type.CHAT);
		chat.uploadFile(fileName, toUser, new QQActionListener(){ 
			@Override
			public void onActionEvent(QQActionEvent event) {
				if(event.getType()==QQActionEvent.Type.EVT_OK)
				{
					WechatMediaInfo mediaInfo=(WechatMediaInfo)event.getTarget();
					doSendFileMsg(fileName,mediaInfo.getMediaId(),toUser,future);
				}else if(event.getType()==QQActionEvent.Type.EVT_ERROR)
				{
					LOGGER.error("文件上传错误");
					future.notifyActionEvent(QQActionEvent.Type.EVT_ERROR, event.getTarget());
				}
				
			}
			
		} );
		
		return future;
	}
	public QQActionFuture doSendFileMsg(String fileName,String mediaId,String toUser,final QQActionListener future) { 
		
		WechatMsg msg=new WechatMsg(); 
		String fromUser=null;
		fromUser = getContext().getSession().getUser().getUserName();
		 
		msg.setFromUserName(fromUser);
		msg.setToUserName(toUser);
		WechatMsgAppInfo appInfo=new WechatMsgAppInfo();
		appInfo.setAppID(WechatAppidDb.WEIXIN_WEB);
		msg.setAppInfo(appInfo);
		File file=new File(fileName); 
		msg.setFileName(file.getName());
		msg.setFileSize(file.length());
		msg.setMediaId(mediaId); 
		msg.setMsgType(WechatSendMsgType.APP_MSG);
		return sendAppMsg(msg,future);
		//return pushHttpAction(new WechatSendAppMsgAction(getContext(),future, msg)); 
	}
	public QQActionFuture sendAppMsg(WechatMsg msg,QQActionListener listener) {
        return pushHttpAction(new WechatSendAppMsgAction(getContext(), listener,msg));
    }
	
	
	public QQActionFuture createChatroom(String topic,List<String> users,QQActionListener listener) {
        return pushHttpAction(new WechatCreateChatroomAction(topic,users,getContext(), listener));
    }
	
	public QQActionFuture updateChatroom(String chatroomName,List<String> addUsers,boolean needInvite,QQActionListener listener) {
        return pushHttpAction(new WechatUpdateChatroomAction(chatroomName,addUsers,needInvite,getContext(), listener));
    }
}
