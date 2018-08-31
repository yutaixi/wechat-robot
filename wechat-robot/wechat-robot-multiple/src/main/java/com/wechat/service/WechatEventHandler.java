package com.wechat.service;   
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import java.util.concurrent.ThreadPoolExecutor;

import iqq.im.QQActionListener;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQNotifyEvent;
import iqq.im.service.AbstractService;  

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  

import com.im.base.wechat.WechatContact;
import com.im.base.wechat.WechatMsg; 
import com.subscription.KeywordVO;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.core.WechatConstants;
import com.wechat.core.WechatContext;
import com.wechat.core.WechatStore;   
public  class WechatEventHandler extends AbstractService implements
		IWechatEventHandler {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WechatEventHandler.class);
	protected static final String FILEHELPER="FILEHELPER"; 
	
	protected List<KeywordVO> keywords=new ArrayList<KeywordVO>();
	
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
    
	public void handleEvent(QQNotifyEvent event) { 
		
		WechatClient mClient = (WechatClient) getContext(); 
		WechatMsg wechatMsg =prepareMsg(event); 
		beforeHandleEvent(event,wechatMsg); 
		 
		switch (event.getType()) {
		case MSGTYPE_TEXT:
			onTextMsg(mClient, wechatMsg);
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
	
	public void onTextMsgFromFilterUsers(WechatClient mClient,WechatMsg msg)
	{
		LOGGER.info("MSGTYPE_TEXT-"+msg.getFrom() + ": " + msg.getContent());
		getContext().output("MSGTYPE_TEXT-"+msg.getFrom() + ": " + msg.getContent());
	}
	public void onTextMsgFromMyself(WechatClient mClient,WechatMsg msg)
	{
		LOGGER.info("MSGTYPE_TEXT-"+msg.getFrom() + ": " + msg.getContent());
		getContext().output("MSGTYPE_TEXT-"+msg.getFrom() + ": " + msg.getContent());
	}
	public void onTextMsgFromGroup(WechatClient mClient,WechatMsg msg)
	{
		String[] peopleContent = msg.getContent().split(":<br/>");
		LOGGER.info("MSGTYPE_TEXT-"+"|" + msg.getFrom() + "| " + peopleContent[0] + ":\n"
				+ peopleContent[1].replace("<br/>", "\n"));
		getContext().output("MSGTYPE_TEXT-"+"|" + msg.getFrom() + "| " + peopleContent[0] + ":\n"
				+ peopleContent[1].replace("<br/>", "\n"));
	}
	public void onTextMsgDefaultProcess(WechatClient mClient,WechatMsg msg)
	{
		LOGGER.info("MSGTYPE_TEXT-"+msg.getFrom() + ": " + msg.getContent()); 
		getContext().output("MSGTYPE_TEXT-"+msg.getFrom() + ": " + msg.getContent());
	}
	public void onTextMsg(WechatClient mClient, WechatMsg msg) {
		String user = mClient.getSession().getUser().getUserName(); 
		if (WechatConstants.FILTER_USERS.contains(msg.getToUserName())) {
			onTextMsgFromFilterUsers(mClient,msg);
		} else if (msg.getFromUserName().equals(user)) {
			onTextMsgFromMyself(mClient,msg);
		} else if (msg.getFromUserName().indexOf("@@") != -1) {
			onTextMsgFromGroup(mClient,msg);
		} else {
			
			if(agreeVerifyMsg(msg))
			{
				refreshContact(mClient,msg.getFromUserName()); 
			}else
			{
				onTextMsgDefaultProcess(mClient,msg);
			}
			
		}
	}
	public void refreshContact(WechatClient mClient,String userName)
	{
		Map<String,WechatContact> contacts=new HashMap<String,WechatContact>();
		WechatContact contact=new WechatContact();
		contact.setUserName(userName);
		contacts.put(contact.getUserName(), contact);
		mClient.batchGetContactInfo(contacts, new QQActionListener() { 
			@Override
			public void onActionEvent(QQActionEvent event) {
				 if(event.getType()==QQActionEvent.Type.EVT_OK)
				 {
					 WechatContext wechatContext=(WechatContext)getContext();
					 WechatStore store=wechatContext.getWechatStore();
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
	 
	public String getUserNameByNickName(String nickName)
	{
		if(nickName==null)
		{
			return null;
		}
		String userName =null;
		WechatStore wechatStore = ((WechatContext) this.getContext())
				.getWechatStore();
		if(wechatStore.getBuddyList()!=null && !wechatStore.getBuddyList().isEmpty())
		{
			for(WechatContact temp : wechatStore.getBuddyList().values())
			{
				if(nickName.equalsIgnoreCase(temp.getNickName()))
				{
					return temp.getUserName(); 
				}
			}
		}
		
		if(wechatStore.getGroupList()!=null && !wechatStore.getGroupList().isEmpty())
		{
			for(WechatContact temp : wechatStore.getGroupList().values())
			{
				if(nickName.equalsIgnoreCase(temp.getNickName()))
				{
					return temp.getUserName(); 
				}
			}
		}
		
		if(wechatStore.getChatRoom()!=null && !wechatStore.getChatRoom().isEmpty())
		{
			for(WechatContact temp : wechatStore.getChatRoom().values())
			{
				if(nickName.equalsIgnoreCase(temp.getNickName()))
				{
					return temp.getUserName(); 
				}
			}
		}
		return userName;
	}
	
	@Override
	public String getNickName(String userName) {
		String name = "这个人物名字未知";
		WechatStore wechatStore = ((WechatContext) this.getContext())
				.getWechatStore();
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
			}else if(getContext().getSession().getUser().getUserName().equalsIgnoreCase(userName))
			{
				name=getContext().getSession().getUser().getUserName();
			}
		}

		return name;
	}
	
	public void getUinInfo(String userName,WechatMsg msg,boolean isFrom) {
		String nickName = "这个人物名字未知";
		String alias=null;
		String remarkName=null;
		Long uid=null;
		WechatStore wechatStore = ((WechatContext) this.getContext())
				.getWechatStore();
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
			}else if(getContext().getSession().getUser().getUserName().equalsIgnoreCase(userName))
			{ 
				nickName = getContext().getSession().getUser().getNickName();
				alias= getContext().getSession().getUser().getAlias();
				remarkName=getContext().getSession().getUser().getRemarkName();
				uid=getContext().getSession().getUser().getUin();
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

	@Override
	public void onImageMsg(WechatMsg msg) { 
		LOGGER.info("MSGTYPE_IMAGE-"+msg.getFrom()+":"+msg.getContent()); 
	}

	@Override
	public void onVoiceMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_VOICE-"+msg.getFrom()+":"+msg.getContent());
		 
	}

	@Override
	public void onVideoMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_VIDEO-"+msg.getFrom()+":"+msg.getContent());
		
	}

	@Override
	public void onMicrovideoMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_MICROVIDEO-"+msg.getFrom()+":"+msg.getContent());
		
	}

	@Override
	public void onEmoticonMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_EMOTICON-"+msg.getFrom()+":"+msg.getContent());
		
	}

	@Override
	public void onAppMsg(WechatMsg msg) { 
		LOGGER.info("MSGTYPE_APP-"+msg.getFrom()+":"+msg.getFileName()); 
		WebWechatClient mClient = (WebWechatClient) getContext(); 
		if(isTransferCashMsg(msg))
		{
			onAppMsgReciveTransferCashMsg(mClient,msg);
		}else
		{
			appMsgDefaultProcess(mClient,msg);
		}
		
	} 
	public void appMsgDefaultProcess(final WebWechatClient mClient,WechatMsg msg)
	{
		
	}
	public void onAppMsgReciveTransferCashMsg(final WebWechatClient mClient,WechatMsg msg)
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
	@Override
	public void onVoipmsgMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_VOIP-"+msg.getFrom()+":"+msg.getContent());
		
	}

	@Override
	public void onvoipnotifyMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_VOIPNOTIFY-"+msg.getFrom()+":"+msg.getContent());
		
	}

	@Override
	public void onVoipinviteMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_VOIPINVITE-"+msg.getFrom()+":"+msg.getContent());
		
	}

	@Override
	public void onLocationMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_LOCATION-"+msg.getFrom()+":"+msg.getContent());
		
	}

	@Override
	public void onStatusnotifyMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_STATUSNOTIFY-"+msg.getFrom()+":"+msg.getContent());
		
	}

	@Override
	public void onSysnoticeMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_SYSNOTICE-"+msg.getFrom()+":"+msg.getContent());
		
	}

	@Override
	public void onPossiblefriendMsgMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_POSSIBLEFRIEND-"+msg.getFrom()+":"+msg.getContent());
		
	}

	@Override
	public void onVerifymsgMsg( WechatMsg msg) {
		LOGGER.info("MSGTYPE_VERIFYMSG-"+msg.getFrom()+":"+msg.getContent()); 
		
	}

	@Override
	public void onSharecardMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_SHARECARD-"+msg.getFrom()+":"+msg.getContent()); 
	}

	@Override
	public void onSysMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_SYS-"+msg.getFrom()+":"+msg.getContent()); 
		WebWechatClient mClient = (WebWechatClient) getContext(); 
		if(msg.getContent()!=null && msg.getContent().contains("现在可以开始聊天了"))
		{
			onSysMsgNewFriend(mClient,msg);
		}else if(msg.getContent()!=null && msg.getContent().contains("收到红包，请在手机上查看"))
		{
			onSysMsgReceiveRedBag(mClient,msg);
		}else if(msg.getContent()!=null && msg.getContent().contains("加入了群聊"))
		{
			onSysMsgNewMemberInGroup(mClient,msg);
		}else
		{
			onSysMsgDefaultProcess(mClient,msg);
		}
		
	}
	

	public void onSysMsgDefaultProcess(final WebWechatClient mClient,WechatMsg msg)
	{
		
	}
	
	public void onSysMsgNewMemberInGroup(final WebWechatClient mClient,WechatMsg msg)
	{
		
	}
	
	public void onSysMsgNewFriend(final WebWechatClient mClient,final WechatMsg msg)
	{
		mClient.syncContactInfo(new QQActionListener(){ 
			@Override
			public void onActionEvent(QQActionEvent event) {
				if(event.getType() == QQActionEvent.Type.EVT_OK)
				{ 
//					WechatStore store=mClient.getWechatStore();  
//					WechatContact user=(WechatUser)mClient.getSession().getUser();  
//					IWechatContactDaoService contactService=new WechatContactDaoService(); 
//					contactService.insertOrUpdateContact(store.getBuddyList(),user.getUin());
//					contactService.insertOrUpdateContact(store.getGroupList(), user.getUin()); 
				} 
			}
    		
    	});
	}
	
	public void onSysMsgReceiveRedBag(final WebWechatClient mClient,WechatMsg msg)
	{
		
	}
	@Override
	public void onRecalledMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_RECALLED-"+msg.getFrom()+":"+msg.getContent());
		
	}

	@Override
	public void setKeyword(List<KeywordVO> keywords) {
		this.keywords=keywords;
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
