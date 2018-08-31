package com.im.ui.handler;

import iqq.im.QQActionListener;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQNotifyEvent;  

import java.util.ArrayList; 
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.base.wechat.WechatContact;
import com.im.base.wechat.WechatMsg;
import com.im.base.wechat.WechatMsgRecommendInfo;
import com.im.robot.MoLiRobot;
import com.im.robot.Robot;
import com.im.utils.DateUtils;
import com.im.utils.FileUtil;
import com.im.utils.Matchers;
import com.im.utils.StringHelper;
import com.subscription.KeywordVO;
import com.subscription.Subscription;
import com.subscription.content.CategoriedContent;
import com.subscription.content.ContentCategory;
import com.subscription.content.ContentType;
import com.subscription.content.SubscriptionContent;
import com.subscription.service.impl.CategoryContentService;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.bean.WechatSendMsgType;
import com.wechat.core.WechatConstants;
import com.wechat.core.WechatContext;
import com.wechat.core.WechatStore;
import com.wechat.dao.mysql.service.SubscriptionDaoService;
import com.wechat.dao.mysql.service.WechatContactDaoService;
import com.wechat.dao.mysql.service.WechatMsgDaoService;
import com.wechat.dao.service.IWechatContactDaoService;
import com.wechat.dao.service.IWechatMsgDaoService;
import com.wechat.event.FutureEvent;
import com.wechat.service.WechatEventHandler;
import com.wechat.service.WechatFutureEventHandler;

public class ImEventHandler extends WechatFutureEventHandler{
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WechatEventHandler.class); 
	private Robot robot = new MoLiRobot();  
    private IWechatMsgDaoService msgDaoService=new WechatMsgDaoService();
	private SubscriptionDaoService subscriptionService=new SubscriptionDaoService();
	private List<KeywordVO> keywords=new ArrayList<KeywordVO>();
	private IWechatContactDaoService contactService=new WechatContactDaoService(); 
	private void refreshKeywors()
	{
		keywords=subscriptionService.getKeywordList(getContext().getSession().getUser().getUin());
	}
	  
    @Override
	public void afterHandleEvent(QQNotifyEvent event, WechatMsg wechatMsg) {
		// TODO Auto-generated method stub
		super.afterHandleEvent(event, wechatMsg);
		if(this.ignoreMsgType.contains(wechatMsg.getType()))
		{
			return;
		}
		saveMsg(event);
	} 
	@Override
	public void beforeHandleEvent(QQNotifyEvent event, WechatMsg wechatMsg) {
		// TODO Auto-generated method stub
		super.beforeHandleEvent(event, wechatMsg);
		if(keywords.isEmpty())
		{
			refreshKeywors();
		}
	}
 
	private void saveMsg(QQNotifyEvent event)
	{
		String ignoreTypeFilter="MSGTYPE_STATUSNOTIFY"; 
		try{
			
			if(ignoreTypeFilter.contains(String.valueOf(event.getType())))
			{
				return;
			}
			WechatMsg wechatMsg =prepareMsg(event); 
			msgDaoService.saveMsg(wechatMsg);
			if("这个人物名字未知".equalsIgnoreCase(wechatMsg.getFrom()))
			{
				return;
			}
			
		}catch(Exception e)
		{
			LOGGER.info("保存消息出错"+e);
		} 
		
	}
 
	@Override
	public void onTextMsgFromFilterUsers(WechatClient mClient, WechatMsg msg) { 
		super.onTextMsgFromFilterUsers(mClient, msg);
	}

	@Override
	public void onTextMsgFromMyself(WechatClient mClient, WechatMsg msg) { 
		super.onTextMsgFromMyself(mClient, msg);
		processMsg(mClient, msg);
	}

	@Override
	public void onTextMsgFromGroup(WechatClient mClient, WechatMsg msg) { 
		super.onTextMsgFromGroup(mClient, msg);
		String[] peopleContent = msg.getContent().split(":<br/>");
		peopleContent[1]=peopleContent[1].replace("<br/>", "\n"); 
		if(peopleContent[1]!=null && (peopleContent[1].startsWith("[微信红包]") || peopleContent[1].startsWith("［微信红包］")||  peopleContent[1].startsWith("【微信红包】")))
		{
			mClient.sendTextMsg("收到假红包消息，请大家警惕", msg.getFromUserName(), null);
		}
	}

	
	
	@Override
	public void onTextMsgDefaultProcess(WechatClient mClient, WechatMsg msg) { 
		super.onTextMsgDefaultProcess(mClient, msg);
		processMsg(mClient, msg);
	}
 
	private List<SubscriptionContent> preprocessKeywords(List<KeywordVO> keywordList,String msg)
	{
		List<SubscriptionContent> contentList=new ArrayList<SubscriptionContent>();
		if(keywordList==null || keywordList.isEmpty() || msg==null || StringHelper.isEmpty(msg))
		{
			return null;
		}
		//查找完全匹配的关键字
		List<KeywordVO> keywordEqualList=new ArrayList<KeywordVO>();
		for(KeywordVO temp : keywordList)
		{
			if(temp.getKeyword().equalsIgnoreCase(msg))
			{
				keywordEqualList.add(temp);
			}
		}
		if(keywordEqualList!=null && !keywordEqualList.isEmpty())
		{
			for(KeywordVO temp:keywordEqualList)
			{
				if(temp!=null && temp.getContentList()!=null && !temp.getContentList().isEmpty())
				contentList.addAll(temp.getContentList());
			}
		}else  //查找匹配关键字长度最长的关键字
		{ 
			int wordLength=0;
			List<KeywordVO> maxLengthKeywords=new ArrayList<KeywordVO>(); 
			for(KeywordVO temp : keywordList)
			{
				contentList.addAll(temp.getContentList());
//				if(temp.getKeyword().length()>wordLength)
//				{
//					wordLength=temp.getKeyword().length();
//					maxLengthKeywords.clear();
//					maxLengthKeywords.add(temp);
//				}else if(temp.getKeyword().length()==wordLength)
//				{
//					maxLengthKeywords.add(temp);
//				}
			}
//			for(KeywordVO temp : maxLengthKeywords)
//			{
//				contentList.addAll(temp.getContentList());
//			}
			
		}
		if(contentList==null || contentList.isEmpty())
		{
			return null;
		}
		//去重
		 contentList = new ArrayList<SubscriptionContent>(new HashSet<SubscriptionContent>(contentList));
		 return contentList;
	}
	
	private CategoriedContent getCategoriedContent(List<KeywordVO> keywordList,String msg,WechatContact contact)
	{
		//缩小关键词范围并去重
		List<SubscriptionContent> contentList=preprocessKeywords(keywordList,msg);
		//对关键词关联的内容进行分组分类 
		CategoryContentService categoryContentService=new CategoryContentService();
		List<Subscription> subscriptions=subscriptionService.batchGetSubscription(contact, contentList);
		CategoriedContent categoriedContent=categoryContentService.categorySubscriptions(contentList, subscriptions); 
		return categoriedContent;
	}
	
	private boolean adminProcess(final WechatClient mClient, WechatMsg wechatMsg)
	{
		String msg = wechatMsg.getContent(); 
		String toUser=FILEHELPER;
		if (msg.contains("测试图片")) {
			// WechatSendImgMsgAction.sendImgMsg(wechatMeta,
			// "F:/Downloads/IMG_4907.jpg", wechatMsg.getFromUserName());
			mClient.sendImgMsg("F:/Downloads/IMG_4907.jpg",
					toUser, null);
		}else
		if (msg.contains("测试音乐")) {
			// WechatSendAppMsgAction.sendFileMsg(wechatMeta,
			// "D:/record/std1.wav", wechatMsg.getFromUserName());
			mClient.sendFileMsg("D:/record/std1.wav",
					toUser, null);
		}else
		if (msg.contains("测试word")) {
			// WechatSendAppMsgAction.sendFileMsg(wechatMeta,
			// "F:/Downloads/测试上传.docx", wechatMsg.getFromUserName());
			mClient.sendFileMsg("F:/Downloads/测试上传.docx",
					wechatMsg.getFromUserName(), null);
		}else
		if (msg.contains("测试ppt")) {
			// WechatSendAppMsgAction.sendFileMsg(wechatMeta,
			// "F:/Downloads/2015.pptx", toUser);
			mClient.sendFileMsg("F:/Downloads/2015.pptx",
					toUser, null);
		}else
		if (msg.contains("测试excel")) {
			// WechatSendAppMsgAction.sendFileMsg(wechatMeta,
			// "F:/Downloads/test.xlsx", toUser);
			mClient.sendFileMsg("F:/Downloads/test.xlsx",
					toUser, null);
		}else
		if (msg.contains("测试txt")) {
			// WechatSendAppMsgAction.sendFileMsg(wechatMeta,
			// "F:/Downloads/test.txt", toUser);
			mClient.sendFileMsg("F:/Downloads/test.txt",
					toUser, null);
		}else
		if (msg.contains("测试lic")) {
			// WechatSendAppMsgAction.sendFileMsg(wechatMeta,
			// "D:/keys/generator/lic/key_2C600C1CB88B.lic",
			// toUser);
			mClient.sendFileMsg("D:/keys/generator/lic/key_2C600C1CB88B.lic",
					toUser, null);
		}else
		if (msg.contains("测试zip")) {
			// WechatSendAppMsgAction.sendFileMsg(wechatMeta,
			// "F:/Downloads/fullpageJsDemo-master.zip",
			// toUser);
			mClient.sendFileMsg("F:/Downloads/fullpageJsDemo-master.zip",
					toUser, null);
		}else
		if(msg.contains("测试url"))
		{
			 
			String from=toUser;
			wechatMsg.setFrom(wechatMsg.getToUserName());
			wechatMsg.setToUserName(from);
			wechatMsg.setContent(null);
			wechatMsg.setMsgType(WechatSendMsgType.APP_URL_MSG); 
			mClient.sendAppMsg(wechatMsg, null);
		}else
		if (msg.contains("测试大文件")) {
			// WechatSendAppMsgAction.sendFileMsg(wechatMeta,
			// "F:/Downloads/fullpageJsDemo-master.zip",
			// toUser);
			mClient.sendFileMsg("F:/Downloads/迅雷下载/valgrind-3.2.1.tar.bz2 ",
					toUser, null);
		}else
		
		if (msg.contains("测试创建chatroom")) {
			// WechatSendAppMsgAction.sendFileMsg(wechatMeta,
			// "F:/Downloads/fullpageJsDemo-master.zip",
			// toUser);
			WechatStore wechatStore = ((WechatContext) this.getContext())
					.getWechatStore();
			 Map<String, WechatContact> contacts=wechatStore.getBuddyList();
			 List<String> users=new ArrayList<String>();
			 users.add(this.getContext().getSession().getUser().getUserName());
			 for(WechatContact temp : contacts.values())
			 {
				 if("于泰喜".contains(temp.getNickName()))
				 {
					 users.add(temp.getUserName());
				 }
			 }
			mClient.createChatroom("2017新年快乐", users, new QQActionListener(){

				@Override
				public void onActionEvent(QQActionEvent event) {
					if (event.getType() == QQActionEvent.Type.EVT_OK) { 
						mClient.sendTextMsg("新年快乐", event.getTarget().toString(), null);
						WechatContact chatroom=new WechatContact();
						chatroom.setUserName(event.getTarget().toString());
						chatroom.setNickName("2017新年快乐");
						WechatStore wechatStore = ((WechatContext) getContext())
								.getWechatStore();
						wechatStore.getChatRoom().put(chatroom.getUserName(), chatroom);
					}
					
				}
				
				
			});
		}else
		
		if (msg.contains("测试更新chatroom")) {
			WechatStore wechatStore = ((WechatContext) getContext())
					.getWechatStore();
			if(msg.indexOf(":")<0)
			{
				return false;
			}
			String[] key=msg.split(":");
			for(WechatContact temp : wechatStore.getChatRoom().values())
			{
				if("2017新年快乐".equalsIgnoreCase(temp.getNickName()))
				{
					List<String> users=new ArrayList<String>();
					
					for(WechatContact user : wechatStore.getBuddyList().values())
					{
						if(key[1].equalsIgnoreCase(user.getNickName()))
						{
							users.add(user.getUserName());
							break;
						}
					}
					
					mClient.updateChatroom(temp.getUserName(), users,false, null);
				}
			}
			
		}else
		
		if (msg.contains("收录")) {
			///FutureEvent futureEvent=new FutureEvent(toUser,FutureEvent.Type.SUBSCRIPTION_CONTENT_WAIT_TO_RECORD,wechatMsg);
			//futureEventMap.put(toUser, futureEvent); 
			WechatMsg lastMsg=lastMsgMap.get(wechatMsg.getFromUserName());
			if(processSubscription(msg,lastMsg))
			{
				refreshKeywors();
				mClient.sendTextMsg(lastMsg.getFileName()+"已收录", toUser, null);
				LOGGER.info(lastMsg.getFileName()+"已收录");
				getContext().output(lastMsg.getFileName()+"已收录");
			}else
			{
				mClient.sendTextMsg(lastMsg.getFileName()+"未收录", toUser, null);
				LOGGER.info(lastMsg.getFileName()+"未收录");
				getContext().output(lastMsg.getFileName()+"未收录");
			}
			
		}else
		{
			return false;
		}
		return true;
	}
	
	private boolean processKeyword(final WechatClient mClient, WechatMsg wechatMsg)
	{
		boolean hasResponed=false;
		
		String msg=wechatMsg.getContent();
		List<KeywordVO> possibleKeyword=new ArrayList<KeywordVO>();
		if(keywords!=null && !keywords.isEmpty())
		{
			 for(KeywordVO keyword : keywords)
			 {
				 if(keyword.getKeyword()!=null && msg.contains(keyword.getKeyword()))
				 {
					 possibleKeyword.add(keyword);
					  
				 }
			 }
		}
		if(possibleKeyword!=null && !possibleKeyword.isEmpty())
		{
			WechatContext wechatContext = (WechatContext) getContext();
			WechatContact contact = wechatContext.getWechatStore()
					.getBuddyList().get(wechatMsg.getFromUserName());
			CategoriedContent contents = getCategoriedContent(possibleKeyword,msg, contact);
			hasResponed=processCategoriedContent(mClient,wechatMsg,contents);
			 
		}
		return hasResponed;
	}
	public boolean processCategoriedContent(final WechatClient mClient, WechatMsg wechatMsg,CategoriedContent contents)
	{
		String talk="";
		boolean hasResponed=false;
		//根据关键字匹配图片
		if (contents.getPicContentPaid() != null && !contents.getPicContentPaid().isEmpty()) {
			 
			mClient.sendTextMsg(getTextUrlMsg(contents.getPicContentPaid()), wechatMsg.getFromUserName(), null);
			hasResponed = true;
		} 
		//根据关键字匹配软件
		if(contents.getSoftwareContentPaid()!=null && !contents.getSoftwareContentPaid().isEmpty())
		{ 
			 mClient.sendTextMsg(getTextUrlMsg(contents.getSoftwareContentPaid()), wechatMsg.getFromUserName(), null);
			 hasResponed=true;
		}
		//根据关键字匹配到视频
		if(contents.getVideoContentPaid()!=null && !contents.getVideoContentPaid().isEmpty())
		{ 
			mClient.sendTextMsg(getTextUrlMsg(contents.getVideoContentPaid()), wechatMsg.getFromUserName(), null);
			hasResponed=true;
		}
		//根据关键字匹配到单选内容，需要继续选择
		if(contents.isHasNeedSingle())
		{
			talk="";
			List<SubscriptionContent> chooseList=new ArrayList<SubscriptionContent>(); 
			if(contents.getPicContentNotPaid()!=null && !contents.getPicContentNotPaid().isEmpty())
			{
				chooseList.addAll(contents.getPicContentNotPaid());
			}
			if(contents.getSoftwareContentNotPaid()!=null && !contents.getSoftwareContentNotPaid().isEmpty())
			{
				chooseList.addAll(contents.getSoftwareContentNotPaid());
			}
			if(contents.getVideoContentNotPaid()!=null && !contents.getVideoContentNotPaid().isEmpty())
			{
				chooseList.addAll(contents.getVideoContentNotPaid());
			} 
			talk=getTalk(chooseList,true);
			mClient.sendTextMsg(talk+"\r\n请选择所需内容序号", wechatMsg.getFromUserName(), null);   
			addFutureEvent(wechatMsg.getFromUserName(),FutureEvent.Type.CONTENT_WAIT_TO_CHOOSE,chooseList,0);
			hasResponed = true;
		}else
		{
			if (contents.getSoftwareContentNotPaid() != null && !contents.getSoftwareContentNotPaid().isEmpty()) {

				talk = getTalk(contents.getSoftwareContentNotPaid(),false); 
				mClient.sendTextMsg(talk+"\r\n"+getNeedPayWords(contents.getSoftwareContentNotPaid()), wechatMsg.getFromUserName(), null); 
				addFutureEvent(wechatMsg.getFromUserName(),FutureEvent.Type.SOFTWARE_WAIT_FOR_PAY,contents.getSoftwareContentNotPaid(),0);
				hasResponed = true;
			}
			if (contents.getPicContentNotPaid() != null && !contents.getPicContentNotPaid().isEmpty()) {

				talk = getTalk(contents.getPicContentNotPaid(),false); 
				mClient.sendTextMsg(talk+"\r\n"+getNeedPayWords(contents.getPicContentNotPaid()), wechatMsg.getFromUserName(), null); 
				hasResponed = true;
			}
			if(contents.getVideoContentNotPaid()!=null && !contents.getVideoContentNotPaid().isEmpty())
			{
				 talk=getTalk(contents.getVideoContentNotPaid(),false); 
				 mClient.sendTextMsg(talk+"\r\n"+getNeedPayWords(contents.getVideoContentNotPaid()), wechatMsg.getFromUserName(), null);
				 addFutureEvent(wechatMsg.getFromUserName(),FutureEvent.Type.FILM_WAIT_FOR_PAY,contents.getVideoContentNotPaid(),0);
				 hasResponed=true;
			}
		}
		return hasResponed;
		
	}
	
	
	private String getTextUrlMsg(List<SubscriptionContent> contents)
	{
		if(contents==null || contents.isEmpty())
		{
			return "";
		}
		StringBuffer buffer=new StringBuffer();
		for(SubscriptionContent temp : contents)
		{
			buffer.append("《"+temp.getName()+"》\r\n"+temp.getUrl()+"\r\n");
		}
		return buffer.toString();
	}
	
	private String getNeedPayWords(List<SubscriptionContent> contentList)
	{
		int price=0;
		String words=null;
		for(SubscriptionContent temp : contentList)
		{
			if(temp.getPrice()>price)
			{
				price=temp.getPrice();
			}
		    if(words==null)
			{
				 if(ContentCategory.PIC.equalsIgnoreCase(temp.getCategory()))
				 {
					 words="需要付费后才能查看，如需查看";
				 }else if(ContentCategory.SOFTWARE.equalsIgnoreCase(temp.getCategory()))
				 {
					 words="需要付费后才能使用，如需使用";
				 }else if(ContentCategory.VIDEO.equalsIgnoreCase(temp.getCategory()))
				 {
					 words="需要付费后才能观看，如需观看";
				 }else
				 {
					 words="需要付费后才能继续，";
				 }
			}
			
		}
		if(price<1)
		{
			price=5;
		}
		words+="请发"+price+"元红包，谢谢";
		return words;
	}
	
	private void processMsg(final WechatClient mClient, WechatMsg wechatMsg) {
		String msg = wechatMsg.getContent();
		boolean hasResponed=false;
		if("这个人物名字未知".equalsIgnoreCase(wechatMsg.getFrom()) || msg==null)
		{
			return;
		}
		if(msgFromAdmin(wechatMsg))
		{
			hasResponed=adminProcess(mClient,wechatMsg);
		} 
		hasResponed=processFutureEvent(wechatMsg);
		if(!hasResponed )
		{
			hasResponed=processKeyword(mClient,wechatMsg); 
		}
		if(!hasResponed)
		{
			mClient.sendTextMsg("二蛋不太懂您的意思，请试试其他关键词", wechatMsg.getFromUserName(), null);
		}

	}

	private String getTalk(List<SubscriptionContent> contentList,boolean SerialNumber)
	{
		if(contentList==null || contentList.isEmpty())
		{
			return null;
		}
		StringBuffer buffer=new StringBuffer();
		int index=1;
		for(SubscriptionContent temp : contentList)
		{
			if(SerialNumber)
			{
				buffer.append(index+"、《"+temp.getName()+"》\r\n");
				index++;
			}else
			{
				buffer.append("《"+temp.getName()+"》\r\n"); 
			}
			
		}
		return buffer.toString();
	}
	
	private boolean processSubscription(String command,WechatMsg wechatMsg)
	{
		String[] commands=command.split(","); 
		SubscriptionContent subscription=new SubscriptionContent(); 
		List<KeywordVO> keywords=processCommand(commands,subscription,wechatMsg);
		if(wechatMsg.getAppInfo()!=null)
		{
			String appId=wechatMsg.getAppInfo().getAppID();
			subscription.setAppId(appId);
		} 
		if(StringHelper.isEmpty(subscription.getName()))
		{
			if(StringHelper.isEmpty(wechatMsg.getFileName()))
			{
				return false;
			}
			subscription.setName(wechatMsg.getFileName());
		} 
		subscription.setContent(wechatMsg.getContent()); 
		subscription.setVersion(DateUtils.nowTimestamp()); 
		subscription.setFileSize(wechatMsg.getFileSize());
		subscription.setMediaId(wechatMsg.getMediaId());
		subscription.setMsgId(wechatMsg.getMsgId()); 
		subscription.setOwner(getContext().getSession().getUser().getUin());
		String url=wechatMsg.getUrl();
		if(url!=null)
		{
			//http://pan.baidu.com/share/link?shareid=2409917927&amp;uk=490397695
			url= StringEscapeUtils.unescapeHtml4(url);
		}
		subscription.setUrl(url);
		String fileExt=FileUtil.getFileExt(wechatMsg.getFileName());
		subscription.setFileext(fileExt);
		if(!StringHelper.isEmpty(subscription.getMediaId()))
		{
			subscription.setType(ContentType.FILE);
		}else if(!StringHelper.isEmpty(subscription.getUrl()))
		{
			subscription.setType(ContentType.URL);
		}else
		{
			subscription.setType(ContentType.TEXT);
		}
		if(subscription.getName()!=null && subscription.getName().indexOf(" ")>-1)
		{
			subscription.setKeyWord(subscription.getName().split(" ")[0]);
		}else
		{
			subscription.setKeyWord(subscription.getName());
			
		}
		if(StringHelper.isEmpty(subscription.getCategory()))
		{
			subscription.setCategory(ContentCategory.VIDEO);
		}
		if(subscription.getPrice()<0)
		{
			subscription.setPrice(5);
		}
		subscriptionService.insertOrUpdateContent(subscription); 
		KeywordVO fileNameKeyword=new KeywordVO(subscription.getName());
		subscriptionService.saveKeyword(fileNameKeyword); 
		subscriptionService.saveKeywordContentMatch(fileNameKeyword, subscription);
		if(keywords!=null && !keywords.isEmpty())
		{
			subscriptionService.saveKeywords(keywords);
			subscriptionService.saveKeywordExt(fileNameKeyword, keywords);
		}
		 
		return true;
	}
	
	private List<KeywordVO> processCommand(String[] commands,SubscriptionContent subscription,WechatMsg wechatMsg)
	{
		List<KeywordVO> keywords=new ArrayList<KeywordVO>(); 
		subscription.setNeedPaid(true);
		for(String command: commands)
		{
			if(command.equalsIgnoreCase("收录"))
			{
				
			}
			if(command.equalsIgnoreCase("免费"))
			{
				subscription.setNeedPaid(false);
			} 
			if(command.contains("category:"))
			{
				subscription.setCategory(command.split(":")[1]); 
			} 
			if(command.contains("price:"))
			{
				subscription.setPrice(Integer.parseInt(command.split(":")[1])); 
			}
			if(command.contains("name:"))
			{
				subscription.setName(command.split(":")[1]); 
			}
			
			if(command.contains("key:"))
			{
				String keywordStr=command.split(":")[1];
				String[] keys=keywordStr.split(" ");
				for(String key : keys)
				{
					keywords.add(new KeywordVO(key));
				}
			}
			if(command.contains("single"))
			{
				subscription.setNeedSingle(true);
			}
		}
		return keywords;
	}
	private String getKeywordFromRawName(String rawName)
	{
	
		if(StringHelper.isEmpty(rawName))
		{
			return null;
		}
		if(rawName.indexOf(" ")>-1)
		{
			return rawName.split(" ")[0];
		}
		return rawName;
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
	public void onAppMsg(WechatMsg msg) {
		WechatClient mClient = (WechatClient) getContext();  
		if(msgFromAdmin(msg))
		{
			//mClient.sendTextMsg("收录api", FILEHELPER, null);
			mClient.sendTextMsg("收录，name:"+msg.getFileName()+",key:"+getKeywordFromRawName(msg.getFileName())+",price:5,category:video,single", FILEHELPER, null);
		}
		LOGGER.info("MSGTYPE_APP-"+msg.getFrom()+":"+msg.getFileName());
		getContext().output(msg.getFrom()+":"+msg.getFileName());
		processFutureEvent(msg); 
		
	}

	public boolean isRedBagMsg(WechatMsg msg)
	{
		if(String.valueOf(QQNotifyEvent.Type.MSGTYPE_SYS).equalsIgnoreCase(msg.getType()))
		{
			if(msg.getContent()!=null && msg.getContent().contains("收到红包，请在手机上查看"))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isTransferCashMsg(WechatMsg msg)
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
	
	 public boolean saveSubscriptionAfterPaid(WechatMsg msg,FutureEvent futureEvent)
	 {
		 Subscription subscription=null;
		 WechatContext wechatContext=(WechatContext)getContext();
		 WechatContact contact=wechatContext.getWechatStore().getBuddyList().get(msg.getFromUserName()); 
		List<SubscriptionContent> contents=(List<SubscriptionContent>)futureEvent.getTarget(); 
			for(SubscriptionContent temp:contents)
			{
				 
				if(contact.getId()<=0)
				{
					 Map<String, WechatContact> contactMap=new HashMap<String, WechatContact>();
					 contactMap.put(contact.getUserName(), contact);
					contactService.insertOrUpdateContact(contactMap, getContext().getSession().getUser().getUin());
				}
				subscription=new Subscription(); 
				subscription.setContactId(contact.getId());
				subscription.setContent(temp);
				subscription.setHasPaid(true);
				subscription.setPushVersion(temp.getVersion());
				subscriptionService.saveSubscription(subscription);  
			}
		 return true;
	 }
	 
	 public void deliveryAfterPaid(WechatMsg msg,FutureEvent futureEvent,WechatClient mClient)
	 {
		 List<SubscriptionContent> contents=(List<SubscriptionContent>)futureEvent.getTarget(); 
		 CategoriedContent categoriedContent=new CategoriedContent();
		 for(SubscriptionContent temp : contents)
		 {
			 if(ContentCategory.PIC.equalsIgnoreCase(temp.getCategory()))
			 {
				 categoriedContent.getPicContentPaid().add(temp);
			 }else if(ContentCategory.VIDEO.equalsIgnoreCase(temp.getCategory()))
			 {
				 categoriedContent.getVideoContentPaid().add(temp);
			 }else if(ContentCategory.SOFTWARE.equalsIgnoreCase(temp.getCategory()))
			 {
				 categoriedContent.getSoftwareContentPaid().add(temp);
			 }
		 }
		 processCategoriedContent(mClient,msg,categoriedContent);
	 }
	
	@Override
	public boolean processFutureEvent(final WechatMsg msg)
	{
		FutureEvent futureEvent=futureEventMap.get(msg.getFromUserName());
		if(futureEvent==null)
		{
			return false;
		}
		WechatClient mClient = (WechatClient) getContext();
		switch(futureEvent.getType())
		{
		case SUBSCRIPTION_CONTENT_WAIT_TO_RECORD:  
			return false;
		case SOFTWARE_WAIT_FOR_PAY:
			 if(isRedBagMsg(msg) || isTransferCashMsg(msg))
			 { 
				 saveSubscriptionAfterPaid(msg,futureEvent);
				 deliveryAfterPaid(msg,futureEvent,mClient);
				 return true; 
			 }
			return false;  
		case FILM_WAIT_FOR_PAY: 
			 if(isRedBagMsg(msg) || isTransferCashMsg(msg))
			 { 
				 saveSubscriptionAfterPaid(msg,futureEvent);
				 deliveryAfterPaid(msg,futureEvent,mClient);
				 return true; 
			 }
			return false;
		case CONTENT_WAIT_TO_CHOOSE:
			List<SubscriptionContent> chooseList=(List<SubscriptionContent>)futureEvent.getTarget();
			SubscriptionContent targetContent=null;
			//如果内容为空返回
			if(StringHelper.isEmpty(msg.getContent()))
			{
				return false;
			}
			//如果内容是数字，获取对应序号的内容
			if(StringHelper.isNumeric(msg.getContent()))
			{
				targetContent=chooseList.get(Integer.parseInt(msg.getContent())-1);
			}else//如果内容不是数字，根据内容查找
			{
				for(SubscriptionContent temp : chooseList)
				{
					if(temp.getName().equalsIgnoreCase(msg.getContent()))
					{
						targetContent=temp;
						break;
					}
				}
			}
			//如果没找到内容
			if(targetContent==null)
			{
				return false;
			}
			//如果需要付费
			if(targetContent.isNeedPaid())
			{
				List<SubscriptionContent> needPayContents=new ArrayList<SubscriptionContent>();
				needPayContents.add(targetContent);
				String talk=getTalk(needPayContents, false);
				mClient.sendTextMsg(talk+"\r\n"+getNeedPayWords(needPayContents), msg.getFromUserName(), null);
				addFutureEvent(msg.getFromUserName(),FutureEvent.Type.FILM_WAIT_FOR_PAY,needPayContents,0);
			}else//不需要付费
			{
				mClient.sendTextMsg(targetContent.getUrl(), msg.getFromUserName(),null);
				removeFutureEvent(msg.getFromUserName());    
			}
			
			return true;
		default:
				break;
		}
		//futureEventMap.remove(msg.getFromUserName()); 
		return false;
	}
	  
	@Override
	public void onVerifymsgMsg( WechatMsg msg) {
		LOGGER.info("MSGTYPE_VERIFYMSG-"+msg.getFrom()+":"+msg.getContent());
		final WechatClient mClient = (WechatClient) getContext(); 
		if(!mClient.getConfig().isAutoAgreeAddFriend())
		{
			return;
		}
		if(msg.getRecommendInfo()==null)
		{
			return ;
		}
		 
		final WechatMsgRecommendInfo recInfo=msg.getRecommendInfo();
		getContext().output("收到来自"+recInfo.getNickName()+"的好友请求:"+recInfo.getContent());
		LOGGER.info("收到来自"+recInfo.getNickName()+"的好友请求:"+recInfo.getContent());
		if(recInfo.getOpCode()==2)
		{ 
			mClient.aggreeAddFriend(recInfo.getUserName(), recInfo.getTicket(), new QQActionListener(){ 
				@Override
				public void onActionEvent(QQActionEvent event) {
					if (event.getType() == QQActionEvent.Type.EVT_OK) { 
						if(mClient.getConfig().isSendTextMsgAfterVerify())
						{
							mClient.sendTextMsg(mClient.getConfig().getAfterVerifySendTextMsg(), recInfo.getUserName(), null);
						}
						if(mClient.getConfig().isSendImgMsgAfterVerify())
						{
							mClient.sendImgMsg(mClient.getConfig().getAfterVerifySendImgMsg(), recInfo.getUserName(), null);
						}
						getContext().output("已同意"+recInfo.getNickName()+"的好友请求");
					}
					
				}
				
			});
		}
	}

	@Override
	public void onSharecardMsg(WechatMsg msg) {
		LOGGER.info("MSGTYPE_SHARECARD-"+msg.getFrom()+":"+msg.getContent());
		if(msg.getRecommendInfo()==null)
		{
			return;
		}
		
		WechatMsgRecommendInfo rec=msg.getRecommendInfo();
		//保存内存中
		 WechatContext wechatContext=(WechatContext)getContext();
		 if(rec.getUserName().indexOf("@@")>-1)
		 {
			 wechatContext.getWechatStore().getGroupList().put(rec.getUserName(), rec);
		 }else
		 {
			 wechatContext.getWechatStore().getBuddyList().put(rec.getUserName(), rec);
		 }
		getContext().output("收到来自"+msg.getFrom()+"的好友推荐:"+rec.getNickName());
		LOGGER.info("收到来自"+msg.getFrom()+"的好友推荐:"+rec.getNickName());
//		WechatClient mClient = (WechatClient) getContext(); 
//		mClient.addFriend(rec, null, null);
//		LOGGER.info("已向"+rec.getNickName()+"发送好友邀请");
//		getContext().output("已向"+rec.getNickName()+"发送好友邀请");
	}

	 
	 

	@Override
	public void onSysMsgDefaultProcess(WebWechatClient mClient, WechatMsg msg) {
		// TODO Auto-generated method stub
		super.onSysMsgDefaultProcess(mClient, msg);
	}

	@Override
	public void onSysMsgNewMemberInGroup(WebWechatClient mClient, WechatMsg msg) {
		// TODO Auto-generated method stub
		super.onSysMsgNewMemberInGroup(mClient, msg); 
		String newMemberNickName=Matchers.match("邀请\"(.+?)\"加入了群聊", msg.getContent());
		mClient.sendTextMsg("欢迎新朋友-"+newMemberNickName, msg.getFromUserName(), null);
	}

	@Override
	public void onSysMsgNewFriend(WebWechatClient mClient, WechatMsg msg) {
		// TODO Auto-generated method stub
		super.onSysMsgNewFriend(mClient, msg);
	}

	@Override
	public void onSysMsgReceiveRedBag(WebWechatClient mClient, WechatMsg msg) {
		// TODO Auto-generated method stub
		super.onSysMsgReceiveRedBag(mClient, msg);
		processFutureEvent(msg);
	}

	private boolean msgFromAdmin(WechatMsg msg)
	{
		return getContext().getSession().getUser().getUserName().equalsIgnoreCase(msg.getFromUserName());
	}
	 
	

}
