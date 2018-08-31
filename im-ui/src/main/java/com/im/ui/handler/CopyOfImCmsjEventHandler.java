package com.im.ui.handler;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import iqq.im.QQActionListener;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQNotifyEvent;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.base.wechat.WechatContact;
import com.im.base.wechat.WechatMsg;
import com.im.base.wechat.WechatMsgRecommendInfo;
import com.im.schedule.queue.ThreadPoolManager;
import com.im.ui.schedule.future.BatchGetContactInfoFutureTask;
import com.im.ui.schedule.task.AutoReplyVideoMsgTask;
import com.im.ui.schedule.task.HandlerOnModContactListTask;
import com.im.ui.schedule.task.SendMsgTask;
import com.im.ui.schedule.task.handler.WechatProcessMsgTask;
import com.im.ui.service.AutoDistributeService;
import com.im.utils.DateUtils;
import com.im.utils.FileUtil;
import com.im.utils.StringHelper;
import com.subscription.KeywordVO;
import com.subscription.Subscription;
import com.subscription.content.CategoriedContent;
import com.subscription.content.ContentCategory;
import com.subscription.content.SubscriptionContent;
import com.subscription.service.impl.CategoryContentService;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.action.ActionResponse;
import com.wechat.bean.WechatMsgType;
import com.wechat.core.WechatContext; 
import com.wechat.dao.sqlite.service.WechatMsgDaoService;
import com.wechat.event.FutureEvent;
import com.wechat.service.WechatFutureEventHandler;

public class CopyOfImCmsjEventHandler extends WechatFutureEventHandler{
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CopyOfImCmsjEventHandler.class);
//	private AutoDistributeService autoDistributeService =new AutoDistributeService();
	  
	private WechatMsgDaoService wechatMsgDaoService=new WechatMsgDaoService();
	private Map<String,Long> opMap=new HashMap<String,Long>();
	
	 

	@Override
	protected void beforeHandleEvent(QQNotifyEvent event, WechatMsg wechatMsg) {
		// TODO Auto-generated method stub
		super.beforeHandleEvent(event, wechatMsg);
		wechatMsgDaoService.saveMsg(wechatMsg);
	}

	@Override
	public void onTextMsgFromFilterUsers(WechatClient mClient, WechatMsg msg) {
		// TODO Auto-generated method stub
		super.onTextMsgFromFilterUsers(mClient, msg);
	}

	@Override
	public void onTextMsgFromMyself(WechatClient mClient, WechatMsg msg) {
		// TODO Auto-generated method stub
		super.onTextMsgFromMyself(mClient, msg);
		processMsg(mClient,msg);
	}

	@Override
	public void onTextMsgFromGroup(WechatClient mClient, WechatMsg msg) {
		// TODO Auto-generated method stub
		super.onTextMsgFromGroup(mClient, msg);
	}

	@Override
	public void onTextMsgDefaultProcess(WechatClient mClient, WechatMsg msg) {
		// TODO Auto-generated method stub
		super.onTextMsgDefaultProcess(mClient, msg);
		processMsg(mClient,msg);
	}
	
	private void processMsg(WechatClient mClient, WechatMsg wechatMsg) {
//		String msg = wechatMsg.getContent();
//		boolean hasResponed=false;
//		if( StringHelper.isEmpty(msg))
//		{
//			return;
//		}  
//		if("这个人物名字未知".equalsIgnoreCase(wechatMsg.getFrom()) || true)
//		{
//			syncContact(mClient,wechatMsg.getFromUserName(),wechatMsg);
//		}
//		if("这个人物名字未知".equalsIgnoreCase(wechatMsg.getFrom()))
//		{
//			return;
//		}
//		hasResponed=processDistributeCode(mClient,wechatMsg); 
//		if(!hasResponed )
//		{
//			hasResponed=processKeyword(mClient,wechatMsg); 
//		}
//		if(!hasResponed)
//		{
//			hasResponed=processAutoReplyVideo(mClient,wechatMsg);
//		}
//		if(!hasResponed && !StringHelper.isEmpty(mClient.getConfig().getDefaultReply()))
//		{
//			mClient.sendTextMsg(mClient.getConfig().getDefaultReply(), wechatMsg.getFromUserName(), null);
//		}
		 
		WebWechatClient webClient=(WebWechatClient)mClient;
		WechatProcessMsgTask processMsgTask=new WechatProcessMsgTask(webClient,wechatMsg);
		ThreadPoolManager.newInstance().addTask(processMsgTask);
	}
	
//	private boolean syncContact(WechatClient mClient,String userName,WechatMsg wechatMsg)
//	{
//		List<WechatContact> contactList=null;
//		 Map<String,WechatContact> contacts=new HashMap<String,WechatContact>();
//		 WechatContact wechatContact=new WechatContact();
//		 wechatContact.setUserName(userName);
//		  contacts.put(userName, wechatContact);
//		  FutureTask<List<WechatContact>> futureTask=new FutureTask<List<WechatContact>>(new BatchGetContactInfoFutureTask(mClient,contacts));
//		  ExecutorService executor=Executors.newFixedThreadPool(1);
//		  executor.execute(futureTask); 
//		  try {
//			   contactList=futureTask.get();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		  if(contactList==null || contactList.isEmpty())
//		  {
//			  return false;
//		  }
//		  WechatContact contact=contactList.get(0);
//		  WebWechatClient webClient=(WebWechatClient)mClient;
//		  webClient.getWechatStore().getBuddyList().put(contact.getUserName(), contact);
//		  wechatMsg.setFrom(contact.getNickName());
//         return true;
//	}
//	private boolean processAutoReplyVideo(final WechatClient mClient, WechatMsg wechatMsg)
//	{
//		boolean hasResponed=false;
//		
//		if(!mClient.getConfig().isEnableAutoReplyVideo())
//		{
//			return hasResponed;
//		}
//		String msg=wechatMsg.getContent();
//		if(StringHelper.isEmpty(msg) || !StringUtils.isNumeric(msg))
//		{
//			return  hasResponed;
//		} 
//		WechatMsg taskMsg=new WechatMsg();
//		taskMsg.setToUserName(wechatMsg.getFromUserName()); 
//		taskMsg.setContent(wechatMsg.getContent());
//		AutoReplyVideoMsgTask task =new AutoReplyVideoMsgTask(mClient,taskMsg,mClient.getConfig().getAutoReplyVideoDelayTime());
//		ThreadPoolManager.newInstance().addTask(task); 
//		return true;
//	}
//	private boolean processDistributeCode(final WechatClient mClient, WechatMsg wechatMsg)
//	{ 
//		String msg=wechatMsg.getContent();
//		if(msg!=null && !msg.equals(mClient.getConfig().getDistributeCommand()))
//		{
//			return false;
//		}
//		
//		if(mClient.getConfig().getDistributeMsg()!=null && !"".equalsIgnoreCase(mClient.getConfig().getDistributeMsg())
//				&& mClient.getConfig().getCodeToDistribute()!=null && !mClient.getConfig().getCodeToDistribute().isEmpty()
//				&&mClient.getConfig().getDistributeTimesPerFriendPerDay()>0)
//		{
//			String dateStr=DateUtils.getNowTimeStr().split(" ")[0]; 
//			if(!dateStr.equalsIgnoreCase(mClient.getConfig().getLastCountDate()))
//			{
//				mClient.getConfig().setDistributeTimeCounter(new HashMap<String,Long>());
//				mClient.getConfig().setLastCountDate(dateStr);
//			}
//			Long distributeTimes=mClient.getConfig().getDistributeTimeCounter().get(wechatMsg.getFrom());
//			distributeTimes=distributeTimes==null?0:distributeTimes;
//			if(distributeTimes!=null && distributeTimes>=mClient.getConfig().getDistributeTimesPerFriendPerDay())
//			{
//				mClient.sendTextMsg("今日领取次数已用完，请明天再来。[微笑]", wechatMsg.getFromUserName(), null);
//				return true;
//			}
//			String nextCode=autoDistributeService.getNextCode(mClient.getConfig());
//			autoDistributeService.appendCodesDistributed(mClient.getConfig(), nextCode, wechatMsg.getFrom());
//			String reply=mClient.getConfig().getDistributeMsg().replace("%s%", nextCode);
//			 
//			getContext().refreshCodeLeft(mClient.getConfig().getCodeToDistribute().size());
//			 
//			mClient.sendTextMsg(reply, wechatMsg.getFromUserName(), null);
//			return true;
//			 
//		}
//		return false;
//	}
	
//	private boolean processKeyword(final WechatClient mClient, WechatMsg wechatMsg)
//	{
//		boolean hasResponed=false;
//		
//		String msg=wechatMsg.getContent();
//		List<KeywordVO> possibleKeyword=new ArrayList<KeywordVO>();
//		if(mClient.getConfig().getKeywordMap()!=null && !mClient.getConfig().getKeywordMap().isEmpty())
//		{
//			 for(KeywordVO keyword : mClient.getConfig().getKeywordMap().values())
//			 {
//				 if(keyword.getKeyword()!=null && msg.contains(keyword.getKeyword()))
//				 {
//					 possibleKeyword.add(keyword);
//					  
//				 }
//			 }
//		}
//		if(possibleKeyword!=null && !possibleKeyword.isEmpty())
//		{
//			WechatContext wechatContext = (WechatContext) getContext();
//			WechatContact contact = wechatContext.getWechatStore().getBuddyList().get(wechatMsg.getFromUserName());
//			CategoriedContent contents = getCategoriedContent(possibleKeyword,msg, contact);
//			hasResponed=processCategoriedContent(mClient,wechatMsg,contents);
//			 
//		}
//		return hasResponed;
//	}
//	
	
//	public boolean processCategoriedContent(final WechatClient mClient, WechatMsg wechatMsg,CategoriedContent contents)
//	{
//		String talk="";
//		boolean hasResponed=false;
//		//根据关键字匹配图片
//		if (contents.getPicContentPaid() != null && !contents.getPicContentPaid().isEmpty()) {
//			 
//			mClient.sendTextMsg(getTextUrlMsg(contents.getPicContentPaid()), wechatMsg.getFromUserName(), null);
//			hasResponed = true;
//		} 
//		// 根据关键字匹配文字
//		if (contents.getTextContentPaid() != null && !contents.getTextContentPaid().isEmpty()) {
//
//			mClient.sendTextMsg(getTextMsg(contents.getTextContentPaid()), wechatMsg.getFromUserName(), null);
//			hasResponed = true;
//		}
//		//根据关键字匹配软件
//		if(contents.getSoftwareContentPaid()!=null && !contents.getSoftwareContentPaid().isEmpty())
//		{ 
//			 mClient.sendTextMsg(getTextUrlMsg(contents.getSoftwareContentPaid()), wechatMsg.getFromUserName(), null);
//			 hasResponed=true;
//		}
//		//根据关键字匹配到视频
//		if(contents.getVideoContentPaid()!=null && !contents.getVideoContentPaid().isEmpty())
//		{ 
//			mClient.sendTextMsg(getTextUrlMsg(contents.getVideoContentPaid()), wechatMsg.getFromUserName(), null);
//			hasResponed=true;
//		}
//		//根据关键字匹配到单选内容，需要继续选择
//		if(contents.isHasNeedSingle())
//		{
//			talk="";
//			List<SubscriptionContent> chooseList=new ArrayList<SubscriptionContent>(); 
//			if(contents.getPicContentNotPaid()!=null && !contents.getPicContentNotPaid().isEmpty())
//			{
//				chooseList.addAll(contents.getPicContentNotPaid());
//			}
//			if(contents.getSoftwareContentNotPaid()!=null && !contents.getSoftwareContentNotPaid().isEmpty())
//			{
//				chooseList.addAll(contents.getSoftwareContentNotPaid());
//			}
//			if(contents.getVideoContentNotPaid()!=null && !contents.getVideoContentNotPaid().isEmpty())
//			{
//				chooseList.addAll(contents.getVideoContentNotPaid());
//			} 
//			talk=getTalk(chooseList,true);
//			mClient.sendTextMsg(talk+"\r\n请选择所需内容序号", wechatMsg.getFromUserName(), null);   
//			addFutureEvent(wechatMsg.getFromUserName(),FutureEvent.Type.CONTENT_WAIT_TO_CHOOSE,chooseList);
//			hasResponed = true;
//		}else
//		{
//			if (contents.getSoftwareContentNotPaid() != null && !contents.getSoftwareContentNotPaid().isEmpty()) {
//
//				talk = getTalk(contents.getSoftwareContentNotPaid(),false); 
//				mClient.sendTextMsg(talk+"\r\n"+getNeedPayWords(contents.getSoftwareContentNotPaid()), wechatMsg.getFromUserName(), null); 
//				addFutureEvent(wechatMsg.getFromUserName(),FutureEvent.Type.SOFTWARE_WAIT_FOR_PAY,contents.getSoftwareContentNotPaid());
//				hasResponed = true;
//			}
//			if (contents.getPicContentNotPaid() != null && !contents.getPicContentNotPaid().isEmpty()) {
//
//				talk = getTalk(contents.getPicContentNotPaid(),false); 
//				mClient.sendTextMsg(talk+"\r\n"+getNeedPayWords(contents.getPicContentNotPaid()), wechatMsg.getFromUserName(), null); 
//				hasResponed = true;
//			}
//			if(contents.getVideoContentNotPaid()!=null && !contents.getVideoContentNotPaid().isEmpty())
//			{
//				 talk=getTalk(contents.getVideoContentNotPaid(),false); 
//				 mClient.sendTextMsg(talk+"\r\n"+getNeedPayWords(contents.getVideoContentNotPaid()), wechatMsg.getFromUserName(), null);
//				 addFutureEvent(wechatMsg.getFromUserName(),FutureEvent.Type.FILM_WAIT_FOR_PAY,contents.getVideoContentNotPaid());
//				 hasResponed=true;
//			}
//		}
//		return hasResponed;
//		
//	}
//	private String getTalk(List<SubscriptionContent> contentList,boolean SerialNumber)
//	{
//		if(contentList==null || contentList.isEmpty())
//		{
//			return null;
//		}
//		StringBuffer buffer=new StringBuffer();
//		int index=1;
//		for(SubscriptionContent temp : contentList)
//		{
//			if(SerialNumber)
//			{
//				buffer.append(index+"、《"+temp.getName()+"》\r\n");
//				index++;
//			}else
//			{
//				buffer.append("《"+temp.getName()+"》\r\n"); 
//			}
//			
//		}
//		return buffer.toString();
//	}
//	
//	private String getTextMsg(List<SubscriptionContent> contents)
//	{
//		if(contents==null || contents.isEmpty())
//		{
//			return "";
//		}
//		StringBuffer buffer=new StringBuffer();
//		for(SubscriptionContent temp : contents)
//		{
//			buffer.append(temp.getContent()+"\r\n");
//		}
//		String msg=buffer.toString();
//		if(msg.endsWith("\r\n"))
//		{
//			msg=msg.substring(0, buffer.lastIndexOf("\r\n"));
//		}
//		return msg;
//	}
//	
//	private String getTextUrlMsg(List<SubscriptionContent> contents)
//	{
//		if(contents==null || contents.isEmpty())
//		{
//			return "";
//		}
//		StringBuffer buffer=new StringBuffer();
//		for(SubscriptionContent temp : contents)
//		{
//			buffer.append("《"+temp.getName()+"》\r\n"+temp.getUrl()+"\r\n");
//		}
//		return buffer.toString();
//	}
//	
//	private String getNeedPayWords(List<SubscriptionContent> contentList)
//	{
//		int price=0;
//		String words=null;
//		for(SubscriptionContent temp : contentList)
//		{
//			if(temp.getPrice()>price)
//			{
//				price=temp.getPrice();
//			}
//		    if(words==null)
//			{
//				 if(ContentCategory.PIC.equalsIgnoreCase(temp.getCategory()))
//				 {
//					 words="需要付费后才能查看，如需查看";
//				 }else if(ContentCategory.SOFTWARE.equalsIgnoreCase(temp.getCategory()))
//				 {
//					 words="需要付费后才能使用，如需使用";
//				 }else if(ContentCategory.VIDEO.equalsIgnoreCase(temp.getCategory()))
//				 {
//					 words="需要付费后才能观看，如需观看";
//				 }else
//				 {
//					 words="需要付费后才能继续，";
//				 }
//			}
//			
//		}
//		if(price<1)
//		{
//			price=5;
//		}
//		words+="请发"+price+"元红包，谢谢";
//		return words;
//	}
	
//	private CategoriedContent getCategoriedContent(List<KeywordVO> keywordList,String msg,WechatContact contact)
//	{
//		//缩小关键词范围并去重
//		List<SubscriptionContent> contentList=preprocessKeywords(keywordList,msg);
//		//对关键词关联的内容进行分组分类 
//		CategoryContentService categoryContentService=new CategoryContentService(); 
//		CategoriedContent categoriedContent=categoryContentService.categorySubscriptions(contentList, null); 
//		return categoriedContent;
//	}

//	private List<SubscriptionContent> preprocessKeywords(List<KeywordVO> keywordList,String msg)
//	{
//		List<SubscriptionContent> contentList=new ArrayList<SubscriptionContent>();
//		if(keywordList==null || keywordList.isEmpty() || msg==null || StringHelper.isEmpty(msg))
//		{
//			return null;
//		}
//		//查找完全匹配的关键字
//		List<KeywordVO> keywordEqualList=new ArrayList<KeywordVO>();
//		for(KeywordVO temp : keywordList)
//		{
//			if(temp.getKeyword().equalsIgnoreCase(msg))
//			{
//				keywordEqualList.add(temp);
//			}
//		}
//		if(keywordEqualList!=null && !keywordEqualList.isEmpty())
//		{
//			for(KeywordVO temp:keywordEqualList)
//			{
//				if(temp!=null && temp.getContentList()!=null && !temp.getContentList().isEmpty())
//				contentList.addAll(temp.getContentList());
//			}
//		}else  //查找匹配关键字长度最长的关键字
//		{ 
//			int wordLength=0;
//			List<KeywordVO> maxLengthKeywords=new ArrayList<KeywordVO>(); 
//			for(KeywordVO temp : keywordList)
//			{
//				contentList.addAll(temp.getContentList()); 
//			} 
//			
//		}
//		if(contentList==null || contentList.isEmpty())
//		{
//			return null;
//		}
//		//去重
//		 contentList = new ArrayList<SubscriptionContent>(new HashSet<SubscriptionContent>(contentList));
//		 return contentList;
//	}
	
	
	@Override
	public void onVerifymsgMsg(final WechatMsg msg) {
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
						getContext().output("已同意"+recInfo.getNickName()+"的好友请求");
						LOGGER.info("已同意"+recInfo.getNickName()+"的好友请求");
//						if(mClient.getConfig().isAfterVeifyInviteToGroup())
//						{
//							addToGroup(mClient,recInfo);
//						}
						
					}else if (event.getType() == QQActionEvent.Type.EVT_ERROR) { 
						getContext().output("添加好友"+recInfo.getNickName()+"出错："+String.valueOf(event.getTarget()));
						LOGGER.info("添加好友"+recInfo.getNickName()+"出错："+String.valueOf(event.getTarget()));
					}
					
				}
				
			});
		}
	}
	 
	public void addToGroup(final  WechatClient mClient,final WechatMsgRecommendInfo recInfo)
	{
		String groupNames=mClient.getConfig().getAfterVeifyInviteToGroupName();
		if(groupNames.endsWith("\n"))
		{
			groupNames=groupNames.substring(0, groupNames.indexOf("\n"));
		}
		String[] groupNameArray={groupNames};
		if(groupNames.indexOf(";")>-1)
		{
			groupNameArray=groupNames.split(";");
		}else if(groupNames.indexOf("；")>-1)
		{
			groupNameArray=groupNames.split("；");
		} 
		final List<String> users=new ArrayList<String>();
		 users.add(recInfo.getUserName());
		for(final String temp : groupNameArray)
		{
			final String groupName=this.getUserNameByNickName(temp);
			if(!StringHelper.isEmpty(groupName))
			{
				mClient.updateChatroom(groupName, users, false, new QQActionListener(){

					@Override
					public void onActionEvent(QQActionEvent event) {
						if (event.getType() == QQActionEvent.Type.EVT_OK) { 
							getContext().output("已邀请"+recInfo.getNickName()+"加入群聊"+temp);
							LOGGER.info("已邀请"+recInfo.getNickName()+"加入群聊"+temp);
						}else if (event.getType() == QQActionEvent.Type.EVT_ERROR) {
							ActionResponse actionResponse=(ActionResponse)event.getTarget();
							int code=actionResponse.getStatus();
							if(code==-23)
							{
								inviteUserToGroup(mClient,groupName,users,temp,recInfo);
							}else
							{
								getContext().output("邀请"+recInfo.getNickName()+"加入群聊"+temp+"失败:"+event.getTarget());
								LOGGER.info("邀请"+recInfo.getNickName()+"加入群聊"+temp+"失败:"+event.getTarget());
								
							}
							
						}
						
					}
					
				});
			}else
			{
				getContext().output("邀请加入群聊失败"+"未找到群聊"+temp);
				LOGGER.info("邀请加入群聊失败"+"未找到群聊"+temp);
			}
			
		}
		
	}
	public void inviteUserToGroup(final  WechatClient mClient,String groupName,List<String> users,final String groupNameStr,final WechatMsgRecommendInfo recInfo)
	{
		mClient.updateChatroom(groupName, users, true,  new QQActionListener(){ 
			@Override
			public void onActionEvent(QQActionEvent event) {
				if (event.getType() == QQActionEvent.Type.EVT_OK) { 
					getContext().output("已邀请"+recInfo.getNickName()+"加入群聊"+groupNameStr);
					LOGGER.info("已邀请"+recInfo.getNickName()+"加入群聊"+groupNameStr);
				}else if (event.getType() == QQActionEvent.Type.EVT_ERROR) { 
					getContext().output("邀请"+recInfo.getNickName()+"加入群聊"+groupNameStr+"失败:"+event.getTarget());
					LOGGER.info("邀请"+recInfo.getNickName()+"加入群聊"+groupNameStr+"失败:"+event.getTarget());
				}
				
			}
			
		});
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
		WechatClient mClient = (WechatClient) getContext(); 
		if(mClient.getConfig().isAutoAddFriendOnSharecardMsg())
		{
			mClient.addFriend(rec, null, null);
			LOGGER.info("已向"+rec.getNickName()+"发送好友邀请");
			getContext().output("已向"+rec.getNickName()+"发送好友邀请");
		}
		
		
	}

	public List<KeywordVO> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<KeywordVO> keywords) {
		this.keywords = keywords;
	}

	@Override
	public void onSysMsgReceiveRedBag(WebWechatClient mClient, WechatMsg msg) {
		// TODO Auto-generated method stub
		super.onSysMsgReceiveRedBag(mClient, msg);
		String content=DateUtils.getNowTimeStr()+"收到来自"+msg.getFrom()+"的红包，请在手机上查收\r\n"; 
		getContext().output("收到来自"+msg.getFrom()+"的红包，请在手机上查收");
		FileUtil.appendToTxt("./config/redbag.txt", content);
		
		if(mClient.getConfig().isSendMsgAfterReceiveRedbag())
		{ 
			mClient.sendTextMsg(mClient.getConfig().getMsgToSendAfterReceiveRedbag(), msg.getFromUserName(), null); 
		}
	}
 
	@Override
	public void onAppMsgReciveTransferCashMsg(WebWechatClient mClient,
			WechatMsg msg) {
		// TODO Auto-generated method stub
		super.onAppMsgReciveTransferCashMsg(mClient, msg);
		String content=DateUtils.getNowTimeStr()+"收到来自"+msg.getFrom()+"的转账，请在手机上查收\r\n"; 
		getContext().output("收到来自"+msg.getFrom()+"的转账，请在手机上查收");
		FileUtil.appendToTxt("./config/redbag.txt", content);
		if(mClient.getConfig().isSendMsgAfterReceiveRedbag())
		{ 
			mClient.sendTextMsg(mClient.getConfig().getMsgToSendAfterReceiveRedbag(), msg.getFromUserName(), null); 
		}
	}

	@Override
	public void onModContactList(WechatMsg msg) {
		// TODO Auto-generated method stub
		Long lastOptTime=opMap.get(msg.getFromUserName());
		Long currentTime=System.currentTimeMillis();
		if(lastOptTime!=null && (currentTime-lastOptTime)<3000)
		{
			return ;
		}
		super.onModContactList(msg);
//		if(msg.getFromUserName().startsWith("@@"))
//		{
//			return;
//		}
//		WechatMsgRecommendInfo recInfo=msg.getRecommendInfo();
//		if(recInfo==null)
//		{
//			return ;
//		}
		WebWechatClient mClient = (WebWechatClient) getContext(); 
//		Map<String, WechatContact> buddyList=mClient.getWechatStore().getBuddyList(); 
//		Map<String, WechatContact> groupList=mClient.getWechatStore().getGroupList();
//		String userName=recInfo.getUserName();
//		if(userName==null || "".equalsIgnoreCase(userName))
//		{
//			return;
//		}
//		
//		if(userName.startsWith("@@"))
//		{ 
//			groupList.put(userName,recInfo);
//		}else 
//		{
//			buddyList.put(userName,recInfo);
//		}
//		getContext().output("通讯录中添加联系人："+recInfo.getNickName());
//		//
//		Map<Long, WechatMsg> welcomeMsgMap=mClient.getConfig().getWelcomeMsgMap();
//		if(welcomeMsgMap!=null && !welcomeMsgMap.isEmpty())
//		{
//			for(WechatMsg temp : welcomeMsgMap.values())
//			{
////				temp.setToUserName(recInfo.getUserName());
//				switch(temp.getMsgType())
//				{ 
//				case WechatMsgType.MSGTYPE_TEXT:
//					mClient.sendTextMsg(temp.getContent(), recInfo.getUserName(), null);
////					SendMsgTask task =new SendMsgTask(mClient,temp,10);
////					ThreadPoolManager.newInstance().addTask(task);
//					break;
//				case WechatMsgType.MSGTYPE_IMAGE:
//					mClient.sendImgMsg(temp.getContent(), recInfo.getUserName(), null);
//					break;
//				case WechatMsgType.MSGTYPE_VIDEO:
//					mClient.sendVideoMsg(temp.getContent(), recInfo.getUserName(), null);
//					break;
//				case WechatMsgType.MSGTYPE_APP:
//					mClient.sendFileMsg(temp.getContent(), recInfo.getUserName(), null);
//					break;
//				}
//			}
//		}
//		
//		
////		if(mClient.getConfig().isSendTextMsgAfterVerify())
////		{
////			mClient.sendTextMsg(mClient.getConfig().getAfterVerifySendTextMsg(), recInfo.getUserName(), null);
////		}
////		if(mClient.getConfig().isSendImgMsgAfterVerify())
////		{
////			mClient.sendImgMsg(mClient.getConfig().getAfterVerifySendImgMsg(), recInfo.getUserName(), null);
////		} 
//		if(mClient.getConfig().isAfterVeifyInviteToGroup())
//		{
//			addToGroup(mClient,recInfo);
//		}
		ThreadPoolManager.newInstance().addTask(new HandlerOnModContactListTask(mClient,msg));
		opMap.put(msg.getFromUserName(), System.currentTimeMillis());
	}

	@Override
	public void onDelContactList(WechatMsg msg) {
		// TODO Auto-generated method stub
		super.onDelContactList(msg);
		WechatMsgRecommendInfo recInfo=msg.getRecommendInfo();
		if(recInfo==null)
		{
			return ;
		}
		WebWechatClient mClient = (WebWechatClient) getContext(); 
		Map<String, WechatContact> buddyList=mClient.getWechatStore().getBuddyList();
		Map<String, WechatContact> chatRoom=mClient.getWechatStore().getChatRoom();
		Map<String, WechatContact> groupList=mClient.getWechatStore().getGroupList();
		String userName=recInfo.getUserName();
		if(userName==null || "".equalsIgnoreCase(userName))
		{
			return;
		}
		if(userName.startsWith("@@"))
		{
			chatRoom.remove(userName);
			groupList.remove(userName);
		}else 
		{
			buddyList.remove(userName);
		}
		getContext().output("通讯录中删除联系人："+msg.getFrom());
		
	}

	 
	
	
}

