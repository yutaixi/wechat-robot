package com.im.ui.schedule.task.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iqq.im.QQActionListener;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQNotifyEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.base.wechat.WechatContact;
import com.im.base.wechat.WechatMsg;
import com.im.schedule.queue.ThreadQueueTask;
import com.im.utils.StringHelper;
import com.subscription.KeywordVO;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.bean.WechatMsgType;
import com.wechat.core.WechatConstants;
import com.wechat.core.WechatContext;
import com.wechat.core.WechatStore;

public class WechatDefaultEventHandlerTask extends ThreadQueueTask{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WechatDefaultEventHandlerTask.class);
 
	protected WebWechatClient mClient; 
	protected QQNotifyEvent event;
	protected static final String FILEHELPER="FILEHELPER"; 
	
	public WechatDefaultEventHandlerTask(WebWechatClient mClient,QQNotifyEvent event)
	{
		this.mClient=mClient;
		this.event=event;
	}
	
	protected WechatMsg prepareMsg(QQNotifyEvent event)
    {
    	WechatMsg wechatMsg = (WechatMsg) event.getTarget(); 
    	wechatMsg.setType(String.valueOf(event.getType()));
    	getUinInfo(wechatMsg.getFromUserName(),wechatMsg,true); 
		getUinInfo(wechatMsg.getToUserName(),wechatMsg,false); 
		return wechatMsg;
    }
    
	protected void beforeHandleEvent(QQNotifyEvent event,WechatMsg wechatMsg)
    {
    	
    }
	protected void afterHandleEvent(QQNotifyEvent event,WechatMsg wechatMsg)
    {
    	
    }
	
	
	public void run() {
		 
		WechatMsg wechatMsg =prepareMsg(event); 
		beforeHandleEvent(event,wechatMsg); 
		
		switch (event.getType()) {
		case MSGTYPE_TEXT:
			onTextMsg(wechatMsg);
			break;
		case MSGTYPE_IMAGE:
			onImageMsg(wechatMsg);
			break;
		case MSGTYPE_VOICE:
			onVoiceMsg(wechatMsg);
			break;
		case MSGTYPE_VIDEO:
			onVideoMsg(wechatMsg);
			break;
		case MSGTYPE_MICROVIDEO:
			onMicrovideoMsg(wechatMsg);
			break;
		case MSGTYPE_EMOTICON:
			onEmoticonMsg(wechatMsg);
			break;
		case MSGTYPE_APP:
			onAppMsg(wechatMsg);
			break;
		case MSGTYPE_VOIPMSG:
			onVoipmsgMsg(wechatMsg);
			break;
		case MSGTYPE_VOIPNOTIFY:
			onvoipnotifyMsg(wechatMsg);
			break;
		case MSGTYPE_VOIPINVITE:
			onVoipinviteMsg(wechatMsg);
			break;
		case MSGTYPE_LOCATION:
			onLocationMsg(wechatMsg);
			break;
		case MSGTYPE_STATUSNOTIFY:
			onStatusnotifyMsg(wechatMsg);
			break;
		case MSGTYPE_SYSNOTICE:
			onSysnoticeMsg(wechatMsg);
			break;
		case MSGTYPE_POSSIBLEFRIEND_MSG:
			onPossiblefriendMsgMsg(wechatMsg);
			break;
		case MSGTYPE_VERIFYMSG:
			onVerifymsgMsg(wechatMsg);
			break;
		case MSGTYPE_SHARECARD:
			onSharecardMsg(wechatMsg);
			break;
		case MSGTYPE_SYS:
			onSysMsg(wechatMsg);
			break;
		case MSGTYPE_RECALLED:
			onRecalledMsg(wechatMsg);
			break;
		case MOD_CONTACTLIST:
			onModContactList(wechatMsg);
			break;
		case DEL_CONTACTLIST:
			onDelContactList(wechatMsg);
			break;
		case MOD_CHATROOMMEMBERLIST:
			break;
		} 
		afterHandleEvent(event,wechatMsg);
	}
 
	public void onTextMsgFromFilterUsers(WechatMsg msg)
	{
		LOGGER.info("MSGTYPE_TEXT-"+msg.getFrom() + ": " + msg.getContent());
		mClient.output("MSGTYPE_TEXT-"+msg.getFrom() + ": " + msg.getContent());
	}
	public void onTextMsgFromMyself(WechatMsg msg)
	{
		LOGGER.info("MSGTYPE_TEXT-"+msg.getFrom() + ": " + msg.getContent());
		mClient.output("MSGTYPE_TEXT-"+msg.getFrom() + ": " + msg.getContent());
	}
	public void onTextMsgFromGroup(WechatMsg msg)
	{
		String[] peopleContent = msg.getContent().split(":<br/>");
		LOGGER.info("MSGTYPE_TEXT-"+"|" + msg.getFrom() + "| " + peopleContent[0] + ":\n"
				+ peopleContent[1].replace("<br/>", "\n"));
		mClient.output("MSGTYPE_TEXT-"+"|" + msg.getFrom() + "| " + peopleContent[0] + ":\n"
				+ peopleContent[1].replace("<br/>", "\n"));
	}
	public void onTextMsgDefaultProcess(WechatMsg msg)
	{
		LOGGER.info("MSGTYPE_TEXT-"+msg.getFrom() + ": " + msg.getContent()); 
		mClient.output("MSGTYPE_TEXT-"+msg.getFrom() + ": " + msg.getContent());
	}
	public void onTextMsg( WechatMsg msg) {
		String user = mClient.getSession().getUser().getUserName(); 
		if (WechatConstants.FILTER_USERS.contains(msg.getToUserName())) {
			onTextMsgFromFilterUsers(msg);
		} else if (msg.getFromUserName().equals(user)) {
			onTextMsgFromMyself(msg);
		} else if (msg.getFromUserName().indexOf("@@") != -1) {
			onTextMsgFromGroup(msg);
		} else {
			
			if(agreeVerifyMsg(msg))
			{
//				refreshContact(msg.getFromUserName()); 
			}else
			{
				onTextMsgDefaultProcess(msg);
			}
			
		}
	}
	public void refreshContact(String userName)
	{
		Map<String,WechatContact> contacts=new HashMap<String,WechatContact>();
		WechatContact contact=new WechatContact();
		contact.setUserName(userName);
		contacts.put(contact.getUserName(), contact);
		mClient.batchGetContactInfo(contacts, new QQActionListener() { 
			
			public void onActionEvent(QQActionEvent event) {
				 if(event.getType()==QQActionEvent.Type.EVT_OK)
				 {
					 
					 WechatStore store=mClient.getWechatStore();
					 List<WechatContact> contacts=(List<WechatContact>)event.getTarget(); 
					 for(WechatContact temp : contacts)
					 {
						 if(temp.getUserName()!=null && temp.getUserName().startsWith("@@"))
						 {
							 store.getGroupList().put(temp.getUserName(), temp);
						 }else
						 {
							 store.getBuddyList().put(temp.getUserName(), temp);
						 } 
					 } 
				 }else  if(event.getType()==QQActionEvent.Type.EVT_OK)
				 {
					 LOGGER.error(""+event.getTarget());
				 }
			}
		});
	}
	
	public boolean agreeVerifyMsg(WechatMsg msg)
	{
		if(msg.getContent().contains("通过你的朋友验证请求，现在可以开始聊天了")|| msg.getContent().contains("通过了你的朋友验证请求，现在我们可以开始聊天了"))
		{
			return true;
		}
		return false;
	}
	 
//	public String getUserNameByNickName(String nickName)
//	{
//		if(nickName==null)
//		{
//			return null;
//		}
//		String userName =null;
//		WechatStore wechatStore = mClient.getWechatStore();
//		if(wechatStore.getBuddyList()!=null && !wechatStore.getBuddyList().isEmpty())
//		{
//			for(WechatContact temp : wechatStore.getBuddyList().values())
//			{
//				if(nickName.equalsIgnoreCase(temp.getNickName()) || nickName.equalsIgnoreCase(temp.getRemarkName()))
//				{
//					return temp.getUserName(); 
//				}
//			}
//		}
//		
//		if(wechatStore.getGroupList()!=null && !wechatStore.getGroupList().isEmpty())
//		{
//			for(WechatContact temp : wechatStore.getGroupList().values())
//			{
//				if(nickName.equalsIgnoreCase(temp.getNickName()))
//				{
//					return temp.getUserName(); 
//				}
//			}
//		}
//		
//		if(wechatStore.getChatRoom()!=null && !wechatStore.getChatRoom().isEmpty())
//		{
//			for(WechatContact temp : wechatStore.getChatRoom().values())
//			{
//				if(nickName.equalsIgnoreCase(temp.getNickName()))
//				{
//					return temp.getUserName(); 
//				}
//			}
//		}
//		return userName;
//	}
	 
	public String getNickName(String userName) {
		String name = "这个人物名字未知";
		WechatStore wechatStore = mClient.getWechatStore();
		if (userName.indexOf("@@") > -1) {

			Map<String, WechatContact> groupList = wechatStore.getGroupList();
			Map<String, WechatContact> chatRoom = wechatStore.getChatRoom();
			if (groupList.get(userName) != null) {
				name = groupList.get(userName).getNickName();
			} else if (chatRoom.get(userName) != null) {
				name = chatRoom.get(userName).getNickName();
			}
		} else {
			Map<String, WechatContact> buddyList = wechatStore.getBuddyList();
			if (buddyList.get(userName) != null) {
				name = buddyList.get(userName).getNickName();
			}else if(mClient.getSession().getUser().getUserName().equalsIgnoreCase(userName))
			{
				name=mClient.getSession().getUser().getUserName();
			}
		}

		return name;
	}
	
	public void getUinInfo(String userName,WechatMsg msg,boolean isFrom) {
		String nickName = "这个人物名字未知";
		String alias=null;
		String remarkName=null;
		Long uid=null;
//		if(msg.getMsgType()==WechatMsgType.MSGTYPE_SYS)
//		{
//			msg.setFrom("系统消息");
//		}
		WechatStore wechatStore = mClient.getWechatStore();
		if (userName.indexOf("@@") > -1) {

			Map<String, WechatContact> groupList = wechatStore.getGroupList();
			Map<String, WechatContact> chatRoom = wechatStore.getChatRoom();
			if (groupList.get(userName) != null) {
				nickName = groupList.get(userName).getNickName();
				alias= groupList.get(userName).getAlias();
				remarkName=groupList.get(userName).getRemarkName();
				uid=groupList.get(userName).getUin();
			} else if (chatRoom.get(userName) != null) { 
				nickName = chatRoom.get(userName).getNickName();
				alias= chatRoom.get(userName).getAlias();
				remarkName=chatRoom.get(userName).getRemarkName();
				uid=chatRoom.get(userName).getUin();
			}
		} else {
			Map<String, WechatContact> buddyList = wechatStore.getBuddyList();
			if (buddyList.get(userName) != null) { 
				nickName = buddyList.get(userName).getNickName();
				alias= buddyList.get(userName).getAlias();
				remarkName=buddyList.get(userName).getRemarkName();
				uid=buddyList.get(userName).getUin();
			}else if(mClient.getSession().getUser().getUserName().equalsIgnoreCase(userName))
			{ 
				nickName = mClient.getSession().getUser().getNickName();
				alias= mClient.getSession().getUser().getAlias();
				remarkName=mClient.getSession().getUser().getRemarkName();
				uid=mClient.getSession().getUser().getUin();
			}
		}

		if(isFrom)
		{
			msg.setFrom(nickName);
			msg.setFromAlias(alias);
			msg.setFromRemarkName(remarkName);
			msg.setFromUid(uid);
		}else
		{
			msg.setTo(nickName);
			msg.setToAlias(alias);
			msg.setToRemarkName(remarkName);
			msg.setToUid(uid);
		} 
		return ;
	}
 
	public void onImageMsg(WechatMsg msg) { 
		LOGGER.info("MSGTYPE_IMAGE-"+msg.getFrom()+":"+msg.getContent()); 
	}
 
	public void onVoiceMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_VOICE-"+msg.getFrom()+":"+msg.getContent());
		 
	}

	
	public void onVideoMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_VIDEO-"+msg.getFrom()+":"+msg.getContent());
		
	}

	
	public void onMicrovideoMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_MICROVIDEO-"+msg.getFrom()+":"+msg.getContent());
		
	}

	
	public void onEmoticonMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_EMOTICON-"+msg.getFrom()+":"+msg.getContent());
		
	}

	
	public void onAppMsg(WechatMsg msg) { 
		LOGGER.info("MSGTYPE_APP-"+msg.getFrom()+":"+msg.getFileName()); 
		 
		if(isTransferCashMsg(msg))
		{
			onAppMsgReciveTransferCashMsg(msg);
		}else
		{
			appMsgDefaultProcess(msg);
		}
		
	} 
	public void appMsgDefaultProcess(WechatMsg msg)
	{
		
	}
	public void onAppMsgReciveTransferCashMsg(WechatMsg msg)
	{
		
	}
	
	private boolean isTransferCashMsg(WechatMsg msg)
	{
		if(String.valueOf(QQNotifyEvent.Type.MSGTYPE_APP).equalsIgnoreCase(msg.getType()))
		{
			if(msg.getFileName()!=null && msg.getFileName().contains("微信转账") &&
					msg.getUrl()!=null && msg.getUrl().contains("https://support.weixin.qq.com/cgi-bin/mmsupport-bin/readtemplate"))
			{
				return true;
			}
		}
		return false;
	}
	
	public void onVoipmsgMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_VOIP-"+msg.getFrom()+":"+msg.getContent());
		
	}

	
	public void onvoipnotifyMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_VOIPNOTIFY-"+msg.getFrom()+":"+msg.getContent());
		
	}

	
	public void onVoipinviteMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_VOIPINVITE-"+msg.getFrom()+":"+msg.getContent());
		
	}

	
	public void onLocationMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_LOCATION-"+msg.getFrom()+":"+msg.getContent());
		
	}

	
	public void onStatusnotifyMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_STATUSNOTIFY-"+msg.getFrom()+":"+msg.getContent());
		
	}

	
	public void onSysnoticeMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_SYSNOTICE-"+msg.getFrom()+":"+msg.getContent());
		
	}

	
	public void onPossiblefriendMsgMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_POSSIBLEFRIEND-"+msg.getFrom()+":"+msg.getContent());
		
	}

	
	public void onVerifymsgMsg( WechatMsg msg) {
		LOGGER.info("MSGTYPE_VERIFYMSG-"+msg.getFrom()+":"+msg.getContent()); 
		
	}

	
	public void onSharecardMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_SHARECARD-"+msg.getFrom()+":"+msg.getContent()); 
	}

	
	public void onSysMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_SYS-"+msg.getFrom()+":"+msg.getContent()); 
		this.mClient.output(msg.getFrom()+":"+msg.getContent());
		if(msg.getContent()!=null && msg.getContent().contains("现在可以开始聊天了"))
		{
			onSysMsgNewFriend(msg);
		}else if(msg.getContent()!=null && msg.getContent().contains("收到红包，请在手机上查看"))
		{
			onSysMsgReceiveRedBag(msg);
		}else if(isInviteGroupMsg(msg))
		{
			List<String> who=getInviteGroupUsers(msg);
		    String byWho=getInviteGroupOperator(msg);
			onSysMsgNewMemberInGroup(msg,who,byWho);
			 
		}else if(isRemoveOutOfGroupMsg(msg))
		{
			//移出群聊 
			String who=this.getRemoveOutGroupUser(msg);
			String byWho=this.getRemoveOutGroupOperator(msg);
			onSysMsgSomeoneHasBeenRemovedOutOfGroup(msg,who,byWho);
			 
		}else
		{
			onSysMsgDefaultProcess(msg);
		}
		
	}
	public String getInviteGroupOperator(WechatMsg msg)
	{
		String content=msg.getContent();
		if(content.startsWith("你邀请"))
		{
			return this.mClient.getSession().getUser().getNickName();
		}
		String operator=content.substring(0,content.indexOf("邀请"));
		if(operator.length()>1)
	    {
	    	operator=operator.substring(1,operator.length()-1); 
	    } 
		return operator;
	}
	public List<String> getInviteGroupUsers(WechatMsg msg)
	{
		String content=msg.getContent();
		content=content.substring(content.indexOf("邀请")+2,content.indexOf("加入了群聊"));
		
		if(content.startsWith("\"") && content.endsWith("\""))
		{
			content=content.substring(1,content.length()-1);
		}
		
		String[] userArray=content.split("、");
		List<String> users=new ArrayList<String>();
		for(String temp : userArray)
		{
			if("你".equalsIgnoreCase(temp))
			{
				temp=this.mClient.getSession().getUser().getNickName();
			}
			users.add(temp);
		}
		
		return users;
	}
	
	public String getRemoveOutGroupOperator(WechatMsg msg)
	{
		String content=msg.getContent();
		if(content.startsWith("你将"))
		{
			return this.mClient.getSession().getUser().getNickName();
		}
		content=content.replace("你被", "").replace("移出群聊", "");
		content=content.substring(1,content.length()-1);
		return content;
	}
	
	private String getRemoveOutGroupUser(WechatMsg msg)
	{
		String content=msg.getContent();
		if(content.startsWith("你被"))
		{
			return this.mClient.getSession().getUser().getNickName();
		}
		content=content.replace("你将", "").replace("移出了群聊", "");
		content=content.substring(1,content.length()-1);
		return content;
	}
	
	
	public static void main(String[] args)
	{
		WechatDefaultEventHandlerTask task=new WechatDefaultEventHandlerTask(null,null);
		WechatMsg msg=new WechatMsg();
		msg.setContent("\"白石传媒\"邀请\"测试、樱桃班4月C004\"加入了群聊");
	    String operator=task.getInviteGroupOperator(msg);
	    System.out.println(operator);
	    List<String> users=task.getInviteGroupUsers(msg);
	    for(String temp : users)
	    {
	    	System.out.println(temp);
	    }
	    msg.setContent("你将\"于泰喜\"移出群聊");
//	    String removeOperator=task.getRemoveOutGroupOperator(msg);
	    String removeUser=task.getRemoveOutGroupUser(msg);
//	    System.out.println(removeOperator);
	    System.out.println(removeUser);
	    
	}
	public boolean isInviteGroupMsg(WechatMsg msg)
	{
		if(msg==null || StringHelper.isEmpty(msg.getContent()))
		{
			return false;
		}
		String content=msg.getContent();
		if(content.contains("邀请") && content.contains("加入了群聊"))
		{
			return true;
		}
		return false;
	}
	
	public boolean isRemoveOutOfGroupMsg(WechatMsg msg)
	{
		if(msg==null || StringHelper.isEmpty(msg.getContent()))
		{
			return false;
		}
		String content=msg.getContent();
		if(content.contains("将") && content.endsWith("移出了群聊"))
		{
			return true;
		}else if(content.contains("被") &&  content.endsWith("移出群聊"))
		{
			return true;
		}
		return false;
	}
	
	  
	public void onSysMsgSomeoneHasBeenRemovedOutOfGroup(WechatMsg msg,String who,String byWho)
	{
		
	}
	 

	public void onSysMsgDefaultProcess(WechatMsg msg)
	{
		
	}
	
	public void onSysMsgNewMemberInGroup(WechatMsg msg,List<String> who,String byWho)
	{
		
	}
	
	 
	
	public void onSysMsgNewFriend(final WechatMsg msg)
	{
//		mClient.syncContactInfo(new QQActionListener(){ 
//			
//			public void onActionEvent(QQActionEvent event) {
//				if(event.getType() == QQActionEvent.Type.EVT_OK)
//				{ 
// 
//				} 
//			}
//    		
//    	});
	}
	
	public void onSysMsgReceiveRedBag(WechatMsg msg)
	{
		
	}
	
	public void onRecalledMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_RECALLED-"+msg.getFrom()+":"+msg.getContent());
		
	}

	 
	
	public void onModContactList(WechatMsg msg)
	{ 
		LOGGER.info("MOD_CONTACTLIST-"+msg.getFrom());
	}
	public void onDelContactList(WechatMsg msg)
	{  
		LOGGER.info("DEL_CONTACTLIST-"+msg.getFrom());
	}
}
