package com.im.ui.schedule.task.handler;
 
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
 



import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import iqq.im.QQActionListener;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQNotifyEvent; 

import com.im.base.customize.EnglishSignUpVO;
import com.im.base.customize.GroupInviteSearchVO;
import com.im.base.customize.GroupInviteVO; 
import com.im.base.vo.MediaFileVO;
import com.im.base.wechat.FutureMsg;
import com.im.base.wechat.WechatContact;
import com.im.base.wechat.WechatMsg;
import com.im.base.wechat.WechatMsgRecommendInfo;
import com.im.schedule.queue.ThreadPoolManager; 
import com.im.ui.schedule.future.BatchGetContactInfoFutureTask;
import com.im.ui.schedule.task.AutoReplyVideoMsgTask;
import com.im.ui.schedule.task.GroupInviteStatisticsTask; 
import com.im.ui.schedule.task.SendMsgTask; 
import com.im.ui.schedule.task.WechatRemarkTask;
import com.im.ui.service.AutoDistributeService;
import com.im.ui.util.context.WindowContext;
import com.im.ui.wechatui.utils.EnglishSignUpSequence;
import com.im.ui.wechatui.utils.UserUtils;
import com.im.utils.DateUtils;
import com.im.utils.FileUtil;
import com.im.utils.NumberUtils;
import com.im.utils.StringHelper;
import com.im.utils.encrypt.EncryptUtil;
import com.im.utils.ini.IniFileUtil;
import com.im.utils.ini.IniSection;
import com.subscription.KeywordVO;
import com.subscription.content.CategoriedContent;
import com.subscription.content.ContentCategory;
import com.subscription.content.SubscriptionContent;
import com.subscription.service.impl.CategoryContentService;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.action.ActionResponse;
import com.wechat.bean.WechatMsgType; 
import com.wechat.dao.sqlite.service.EnglishSignUpDaoService;
import com.wechat.dao.sqlite.service.GroupInviteStatisticsDaoService;
import com.wechat.dao.sqlite.service.WechatMsgDaoService;
import com.wechat.event.FutureEvent;
import com.wechat.event.FutureEvent.Type;
import com.wechat.service.MediaFileService;

public class WechatMsgEventHandlerTask extends WechatFutureMsgEventHandlerTask {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WechatMsgEventHandlerTask.class);
	
	private MediaFileService mediaFileService=new MediaFileService();
	private WechatMsgDaoService wechatMsgDaoService=new WechatMsgDaoService();
	private AutoDistributeService autoDistributeService =new AutoDistributeService();
	private EnglishSignUpDaoService englishSignUpDaoService=new EnglishSignUpDaoService();
	private GroupInviteStatisticsDaoService groupInviteStatisticsDaoService=new GroupInviteStatisticsDaoService();
	
	public WechatMsgEventHandlerTask(WebWechatClient mClient,
			QQNotifyEvent event) {
		super(mClient, event);
		// TODO Auto-generated constructor stub
	}
 
	@Override
	protected void beforeHandleEvent(QQNotifyEvent event, WechatMsg wechatMsg) {
		// TODO Auto-generated method stub
		super.beforeHandleEvent(event, wechatMsg);
		wechatMsgDaoService.saveMsg(wechatMsg);
	}

	
	
	@Override
	public void onImageMsg(WechatMsg msg) {
		// TODO Auto-generated method stub
		super.onImageMsg(msg);
		boolean hasResponed=this.processFutureEvent(msg);
//		String content=msg.getContent();
//		content=content.replaceAll("&lt;", "<");
//		content=content.replaceAll("&gt;", ">");
//		System.out.println(content);
		
		if(msg.getFromUserName().startsWith("@@"))
		{
			return;
		}
		if(this.mClient.getSession().getUser().getUserName().equalsIgnoreCase(msg.getFromUserName()))
		{
			mediaFileService.saveMediaFile(msg, this.mClient.getSession().getSkey()); 
//			MediaFileVO mediaFile=mediaFileService.findMediaFile("d515787e89866ae9bf88465f14260802");
//			String content=mediaFile.getContent();
//			content=content.replaceAll("&lt;", "<");
//			content=content.replaceAll("&gt;", ">");
//			this.mClient.sendImgContentMsg(content, FILEHELPER, null);
			 
		}
		
	}

	@Override
	public void onTextMsgFromFilterUsers( WechatMsg msg) {
		// TODO Auto-generated method stub
		super.onTextMsgFromFilterUsers(msg);
	}

	@Override
	public void onTextMsgFromMyself(WechatMsg msg) {
		// TODO Auto-generated method stub
		super.onTextMsgFromMyself(msg);
		processMsg(msg);
	}

	@Override
	public void onTextMsgFromGroup(WechatMsg msg) {
		// TODO Auto-generated method stub
		super.onTextMsgFromGroup(msg);
	}

	@Override
	public void onTextMsgDefaultProcess( WechatMsg msg) {
		// TODO Auto-generated method stub
		super.onTextMsgDefaultProcess( msg);
		processMsg(msg);
	}
	
	private void processMsg( WechatMsg wechatMsg) {
 
//		WechatProcessMsgTask processMsgTask=new WechatProcessMsgTask(this.mClient,wechatMsg);
//		ThreadPoolManager.newInstance().addTask(processMsgTask);
		String msg = wechatMsg.getContent();
		boolean hasResponed=false;
		if( StringHelper.isEmpty(msg))
		{
			return;
		}  
		if("这个人物名字未知".equalsIgnoreCase(wechatMsg.getFrom()))
		{
			syncContact(mClient,wechatMsg.getFromUserName(),wechatMsg);
		}
		if("这个人物名字未知".equalsIgnoreCase(wechatMsg.getFrom()))
		{
			return;
		}
		
		hasResponed=this.processFutureEvent(wechatMsg);
		
		hasResponed=processDistributeCode(mClient,wechatMsg); 
		
		if(!hasResponed && mClient.getConfig().isEnglishEnableSignUp() && mClient.getConfig().getEnglishBeginAskMode()==1
				&& !StringHelper.isEmpty(mClient.getConfig().getEnglishBeginAskKeyword())
				&& mClient.getConfig().getEnglishBeginAskKeyword().equalsIgnoreCase(wechatMsg.getContent()))
		{
			hasResponed=true;
			startEnglishSignUp(wechatMsg.getFromUserName(),wechatMsg.getFrom());
		}
		
		if(!hasResponed )
		{
			hasResponed=processKeyword(mClient,wechatMsg); 
		}
		if(!hasResponed)
		{
			hasResponed=processAutoReplyVideo(mClient,wechatMsg);
		}
		if(!hasResponed && !StringHelper.isEmpty(mClient.getConfig().getDefaultReply()))
		{
			mClient.sendDelayedTextMsg(mClient.getConfig().getDefaultReply(), wechatMsg.getFromUserName(),mClient.getConfig().getDefaultReplyDelay(), null);
		}
		
		
	}
	private boolean syncContact(WebWechatClient mClient,String userName,WechatMsg wechatMsg)
	{
		List<WechatContact> contactList=null;
		 Map<String,WechatContact> contacts=new HashMap<String,WechatContact>();
		 WechatContact wechatContact=new WechatContact();
		 wechatContact.setUserName(userName);
		  contacts.put(userName, wechatContact);
		  FutureTask<List<WechatContact>> futureTask=new FutureTask<List<WechatContact>>(new BatchGetContactInfoFutureTask(mClient,contacts));
		  ExecutorService executor=Executors.newFixedThreadPool(1);
		  executor.execute(futureTask); 
		  try {
			   contactList=futureTask.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  if(contactList==null || contactList.isEmpty())
		  {
			  return false;
		  }
		  WechatContact contact=contactList.get(0);
		   
		  mClient.getWechatStore().getBuddyList().put(contact.getUserName(), contact);
		  wechatMsg.setFrom(contact.getNickName());
         return true;
	}

	private boolean processDistributeCode(final WebWechatClient mClient, WechatMsg wechatMsg)
	{ 
		String msg=wechatMsg.getContent();
		if(msg!=null && !msg.equals(mClient.getConfig().getDistributeCommand()))
		{
			return false;
		}
		
		if(mClient.getConfig().getDistributeMsg()!=null && !"".equalsIgnoreCase(mClient.getConfig().getDistributeMsg())
				&& mClient.getConfig().getCodeToDistribute()!=null && !mClient.getConfig().getCodeToDistribute().isEmpty()
				&&mClient.getConfig().getDistributeTimesPerFriendPerDay()>0)
		{
			String dateStr=DateUtils.getNowTimeStr().split(" ")[0]; 
			if(!dateStr.equalsIgnoreCase(mClient.getConfig().getLastCountDate()))
			{
				mClient.getConfig().setDistributeTimeCounter(new ConcurrentHashMap<String,Long>());
				mClient.getConfig().setLastCountDate(dateStr);
			}
			Long distributeTimes=mClient.getConfig().getDistributeTimeCounter().get(wechatMsg.getFrom());
			distributeTimes=distributeTimes==null?0:distributeTimes;
			if(distributeTimes!=null && distributeTimes>=mClient.getConfig().getDistributeTimesPerFriendPerDay())
			{
				mClient.sendTextMsg("今日领取次数已用完，请明天再来。[微笑]", wechatMsg.getFromUserName(), null);
				return true;
			}
			String nextCode=autoDistributeService.getNextCode(mClient.getConfig());
			autoDistributeService.appendCodesDistributed(mClient.getConfig(), nextCode, wechatMsg.getFrom());
			String reply=mClient.getConfig().getDistributeMsg().replace("%s%", nextCode);
			  
			WindowContext.getCodeLeft().setText(String.valueOf(mClient.getConfig().getCodeToDistribute().size()));
			mClient.sendTextMsg(reply, wechatMsg.getFromUserName(), null);
			return true;
			 
		}
		return false;
	}
	private boolean processKeyword(final WebWechatClient mClient, WechatMsg wechatMsg)
	{
		boolean hasResponed=false;
		
		String msg=wechatMsg.getContent();
		List<KeywordVO> possibleKeyword=new ArrayList<KeywordVO>();
		if(mClient.getConfig().getKeywordMap()!=null && !mClient.getConfig().getKeywordMap().isEmpty())
		{
			 for(KeywordVO keyword : mClient.getConfig().getKeywordMap().values())
			 {
				 if(mClient.getConfig().isKeywordExactMatching() && !StringHelper.isEmpty(keyword.getKeyword()) && msg.equals(keyword.getKeyword()))
				 {
					 possibleKeyword.add(keyword);
					  
				 }else if(!mClient.getConfig().isKeywordExactMatching() && !StringHelper.isEmpty(keyword.getKeyword()) && msg.contains(keyword.getKeyword()))
				 {
					 possibleKeyword.add(keyword);
				 }
			 }
		}
		if(possibleKeyword!=null && !possibleKeyword.isEmpty())
		{
			 
			WechatContact contact = mClient.getWechatStore().getBuddyList().get(wechatMsg.getFromUserName());
			CategoriedContent contents = getCategoriedContent(possibleKeyword,msg, contact);
			hasResponed=processCategoriedContent(mClient,wechatMsg,contents);
			 
		}
		return hasResponed;
	}
	
	private CategoriedContent getCategoriedContent(List<KeywordVO> keywordList,String msg,WechatContact contact)
	{
		//缩小关键词范围并去重
		List<SubscriptionContent> contentList=preprocessKeywords(keywordList,msg);
		//对关键词关联的内容进行分组分类 
		CategoryContentService categoryContentService=new CategoryContentService(); 
		CategoriedContent categoriedContent=categoryContentService.categorySubscriptions(contentList, null); 
		return categoriedContent;
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
			} 
			
		}
		if(contentList==null || contentList.isEmpty())
		{
			return null;
		}
		//去重
		 contentList = new ArrayList<SubscriptionContent>(new HashSet<SubscriptionContent>(contentList));
		 return contentList;
	}
 
	private Long getDelayTime(List<SubscriptionContent> contents)
	{
	    Long minDelay=Long.MAX_VALUE;
	    Long maxDelay=Long.MIN_VALUE; 
	    if(contents==null)
	    {
	    	return 0L;
	    }
	    for(SubscriptionContent temp : contents)
	    {
	    	if(temp.getMinDelay()!=null && temp.getMinDelay()<minDelay)
	    	{
	    		minDelay=temp.getMinDelay();
	    	}
	    	if(temp.getMinDelay()!=null && temp.getMinDelay()>maxDelay)
	    	{
	    		maxDelay=temp.getMinDelay();
	    	}
	    	if(temp.getMaxDelay()!=null && temp.getMaxDelay()>maxDelay)
	    	{
	    		maxDelay=temp.getMaxDelay();
	    	}
	    	if(temp.getMaxDelay()!=null && temp.getMaxDelay()<minDelay)
	    	{
	    		minDelay=temp.getMaxDelay();
	    	} 
	    }
	    if(maxDelay==null && minDelay!=null && minDelay>-1)
	    {
	    	return minDelay;
	    }else if(minDelay==null && maxDelay!=null && maxDelay>-1)
	    {
	    	return maxDelay;
	    }else if(minDelay==null && maxDelay==null)
	    {
	    	return 0L;
	    }
	    Random rand = new Random();  
	    long delay=rand.nextInt((int)Math.abs(maxDelay-minDelay)+1)+minDelay;
	    
	    return delay;
		
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
		// 根据关键字匹配文字
		if (contents.getTextContentPaid() != null && !contents.getTextContentPaid().isEmpty()) {
 
			mClient.sendDelayedTextMsg(getTextMsg(contents.getTextContentPaid()), wechatMsg.getFromUserName(), getDelayTime(contents.getTextContentPaid()),null);
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
	
	private String getTextMsg(List<SubscriptionContent> contents)
	{
		if(contents==null || contents.isEmpty())
		{
			return "";
		}
		StringBuffer buffer=new StringBuffer();
		for(SubscriptionContent temp : contents)
		{
			buffer.append(temp.getContent()+"\r\n");
		}
		String msg=buffer.toString();
		if(msg.endsWith("\r\n"))
		{
			msg=msg.substring(0, buffer.lastIndexOf("\r\n"));
		}
		return msg;
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
	 
	
	private boolean processAutoReplyVideo(final WechatClient mClient, WechatMsg wechatMsg)
	{
		boolean hasResponed=false;
		
		if(!mClient.getConfig().isEnableAutoReplyVideo())
		{
			return hasResponed;
		}
		String msg=wechatMsg.getContent();
		if(StringHelper.isEmpty(msg) || !StringUtils.isNumeric(msg))
		{
			return  hasResponed;
		} 
		WechatMsg taskMsg=new WechatMsg();
		taskMsg.setToUserName(wechatMsg.getFromUserName()); 
		taskMsg.setContent(wechatMsg.getContent());
		AutoReplyVideoMsgTask task =new AutoReplyVideoMsgTask(mClient,taskMsg,mClient.getConfig().getAutoReplyVideoDelayTime());
		ThreadPoolManager.newInstance().addTask(task); 
		return true;
	}
	
	@Override
	public void onVerifymsgMsg(final WechatMsg msg) {
		LOGGER.info("MSGTYPE_VERIFYMSG-"+msg.getFrom()+":"+msg.getContent()); 
		if(msg.getRecommendInfo()==null)
		{
			return ;
		}
		 
		final WechatMsgRecommendInfo recInfo=msg.getRecommendInfo();
		mClient.output("收到来自"+recInfo.getNickName()+"的好友请求:"+recInfo.getContent());
		LOGGER.info("收到来自"+recInfo.getNickName()+"的好友请求:"+recInfo.getContent());
		
		if(!mClient.getConfig().isAutoAgreeAddFriend())
		{
			return;
		}
		if(recInfo.getOpCode()==2)
		{ 
//			mClient.aggreeAddFriend(recInfo.getUserName(), recInfo.getTicket(), new QQActionListener(){ 
//				@Override
//				public void onActionEvent(QQActionEvent event) {
//					if (event.getType() == QQActionEvent.Type.EVT_OK) { 
//						mClient.output("已同意"+recInfo.getNickName()+"的好友请求");
//						LOGGER.info("已同意"+recInfo.getNickName()+"的好友请求"); 
//					}else if (event.getType() == QQActionEvent.Type.EVT_ERROR) { 
//						mClient.output("添加好友"+recInfo.getNickName()+"出错："+String.valueOf(event.getTarget()));
//						LOGGER.info("添加好友"+recInfo.getNickName()+"出错："+String.valueOf(event.getTarget()));
//					}
//					
//				}
//				
//			});
			WindowContext.newFriendQueue.offer(recInfo);
		}
	}
	 
	public void addToGroup(final WechatMsgRecommendInfo recInfo)
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
			final String groupName=UserUtils.getUserNameByNickName(this.mClient,temp);
			if(!StringHelper.isEmpty(groupName))
			{
				mClient.updateChatroom(groupName, users, false, new QQActionListener(){

					@Override
					public void onActionEvent(QQActionEvent event) {
						if (event.getType() == QQActionEvent.Type.EVT_OK) { 
							mClient.output("已邀请"+recInfo.getNickName()+"加入群聊"+temp);
							LOGGER.info("已邀请"+recInfo.getNickName()+"加入群聊"+temp);
						}else if (event.getType() == QQActionEvent.Type.EVT_ERROR) {
							ActionResponse actionResponse=(ActionResponse)event.getTarget();
							int code=actionResponse.getStatus();
							if(code==-23)
							{
								inviteUserToGroup(groupName,users,temp,recInfo);
							}else
							{
								mClient.output("邀请"+recInfo.getNickName()+"加入群聊"+temp+"失败:"+actionResponse.getResponseData());
								LOGGER.info("邀请"+recInfo.getNickName()+"加入群聊"+temp+"失败:"+actionResponse.getResponseData());
								
							}
							
						}
						
					}
					
				});
			}else
			{
				mClient.output("邀请加入群聊失败"+"未找到群聊"+temp);
				LOGGER.info("邀请加入群聊失败"+"未找到群聊"+temp);
			}
			
		}
		
	}
	public void inviteUserToGroup(String groupName,List<String> users,final String groupNameStr,final WechatMsgRecommendInfo recInfo)
	{
		mClient.updateChatroom(groupName, users, true,  new QQActionListener(){ 
			@Override
			public void onActionEvent(QQActionEvent event) {
				if (event.getType() == QQActionEvent.Type.EVT_OK) { 
					mClient.output("已邀请"+recInfo.getNickName()+"加入群聊"+groupNameStr);
					LOGGER.info("已邀请"+recInfo.getNickName()+"加入群聊"+groupNameStr);
				}else if (event.getType() == QQActionEvent.Type.EVT_ERROR) { 
					ActionResponse actionResponse=(ActionResponse)event.getTarget();
					mClient.output("邀请"+recInfo.getNickName()+"加入群聊"+groupNameStr+"失败:"+actionResponse.getResponseData());
					LOGGER.info("邀请"+recInfo.getNickName()+"加入群聊"+groupNameStr+"失败:"+actionResponse.getResponseData());
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
		 
		 if(rec.getUserName().indexOf("@@")>-1)
		 {
			 this.mClient.getWechatStore().getGroupList().put(rec.getUserName(), rec);
		 }else
		 {
			 this.mClient.getWechatStore().getBuddyList().put(rec.getUserName(), rec);
		 }
		 this.mClient.output("收到来自"+msg.getFrom()+"的好友推荐:"+rec.getNickName());
		LOGGER.info("收到来自"+msg.getFrom()+"的好友推荐:"+rec.getNickName()); 
		if(this.mClient.getConfig().isAutoAddFriendOnSharecardMsg())
		{
			this.mClient.addFriend(rec, null, null);
			LOGGER.info("已向"+rec.getNickName()+"发送好友邀请");
			this.mClient.output("已向"+rec.getNickName()+"发送好友邀请");
		}
		
		
	}

	 

	@Override
	public void onSysMsgReceiveRedBag(WechatMsg msg) {
		// TODO Auto-generated method stub
		super.onSysMsgReceiveRedBag( msg);
		String content=DateUtils.getNowTimeStr()+"收到来自"+msg.getFrom()+"的红包，请在手机上查收\r\n"; 
		this.mClient.output("收到来自"+msg.getFrom()+"的红包，请在手机上查收");
		FileUtil.appendToTxt("./config/redbag.txt", content);
		
		if(mClient.getConfig().isSendMsgAfterReceiveRedbag())
		{ 
			mClient.sendTextMsg(mClient.getConfig().getMsgToSendAfterReceiveRedbag(), msg.getFromUserName(), null); 
		}
	}
 
	@Override
	public void onAppMsgReciveTransferCashMsg(WechatMsg msg) {
		// TODO Auto-generated method stub
		super.onAppMsgReciveTransferCashMsg( msg);
		String content=DateUtils.getNowTimeStr()+"收到来自"+msg.getFrom()+"的转账，请在手机上查收\r\n"; 
		this.mClient.output("收到来自"+msg.getFrom()+"的转账，请在手机上查收");
		FileUtil.appendToTxt("./config/redbag.txt", content);
		if(mClient.getConfig().isSendMsgAfterReceiveRedbag())
		{ 
			mClient.sendTextMsg(mClient.getConfig().getMsgToSendAfterReceiveRedbag(), msg.getFromUserName(), null); 
		}
	}

	@Override
	public void onModContactList(WechatMsg msg) { 
		super.onModContactList(msg);
		boolean isGroup=false;
		
		WechatMsgRecommendInfo recInfo=msg.getRecommendInfo();
		if(recInfo==null)
		{
			return ;
		}
		recInfo.setNickName(UserUtils.removeEmoji(recInfo.getNickName()));
		String userName=recInfo.getUserName();
		Map<String, WechatContact> buddyList=mClient.getWechatStore().getBuddyList(); 
		Map<String, WechatContact> groupList=mClient.getWechatStore().getGroupList();
		Map<String, WechatContact> chatRoomList=mClient.getWechatStore().getChatRoom();
		Long lastOptTime=WindowContext.getOpMap().get(userName);
		if(lastOptTime!=null || buddyList.get(userName)!=null || groupList.get(userName)!=null || chatRoomList.get(userName)!=null)
		{
			return;
		} 
		WindowContext.getOpMap().put(userName, System.currentTimeMillis());
		 
		if(userName==null || "".equalsIgnoreCase(userName))
		{
			return;
		}
		
		if(userName.startsWith("@@"))
		{
			isGroup=true; 
		}
		
		if(isGroup)
		{ 
			groupList.put(userName,recInfo);
			WindowContext.addNewGroupToList(recInfo);
		}else 
		{
			buddyList.put(userName,recInfo);
			WindowContext.addNewFriendToList(recInfo);
		}
		
		this.mClient.output("通讯录中添加联系人："+recInfo.getNickName());
		if(!isGroup)
		{ 
			//
			Map<Long, WechatMsg> welcomeMsgMap=mClient.getConfig().getWelcomeMsgMap();
			if(welcomeMsgMap!=null && !welcomeMsgMap.isEmpty())
			{
				for(WechatMsg temp : welcomeMsgMap.values())
				{
//					temp.setToUserName(recInfo.getUserName());
					switch(temp.getMsgType())
					{ 
					case WechatMsgType.MSGTYPE_TEXT:
						mClient.sendTextMsg(temp.getContent(), recInfo.getUserName(), null); 
						break;
					case WechatMsgType.MSGTYPE_IMAGE:
						mClient.sendImgMsg(temp.getContent(), recInfo.getUserName(), null);
						break;
					case WechatMsgType.MSGTYPE_VIDEO:
						mClient.sendVideoMsg(temp.getContent(), recInfo.getUserName(), null);
						break;
					case WechatMsgType.MSGTYPE_APP:
						mClient.sendFileMsg(temp.getContent(), recInfo.getUserName(), null);
						break;
					case WechatMsgType.MSGTYPE_IMAGE_FORWARD:
						MediaFileVO mediaFile=mediaFileService.findMediaFile(temp.getContent());
						if(mediaFile==null)
						{
							break;
						}
						String content=mediaFile.getContent();
						content=content.replaceAll("&lt;", "<");
						content=content.replaceAll("&gt;", ">"); 
						mClient.sendImgContentMsg(content, recInfo.getUserName(), null);
						break;
					default:
						mClient.sendTextMsg(temp.getContent(), recInfo.getUserName(), null); 
						break;
					}
				}
			}
	 
			if(mClient.getConfig().isAfterVeifyInviteToGroup() && !this.mClient.getConfig().getInviteToGroupsAfterVeify().isEmpty())
			{
//				addToGroup(recInfo);
				WindowContext.newFriendGroupInviteQueue.offer(recInfo);
			}
			if(mClient.getConfig().isEnglishEnableSignUp() && mClient.getConfig().getEnglishBeginAskMode()==0)
			{
				startEnglishSignUp(recInfo.getUserName(),recInfo.getNickName());
			}
		}
		
 
	}
	
	private void startEnglishSignUp(String userName,String nickName)
	{
		ConcurrentHashMap<Long, FutureMsg> futrueMsgMap=mClient.getConfig().getFutrueMsgMap(); 
		if(futrueMsgMap==null || futrueMsgMap.isEmpty())
		{
			return ;
		}
//		List<Long> keysList=new ArrayList<Long>(futrueMsgMap.keySet());
//		Collections.sort(keysList);
//		FutureMsg futureMsg=futrueMsgMap.get(keysList.get(0));  
		for(FutureMsg futureMsg : futrueMsgMap.values())
		{
			if("问年龄".equalsIgnoreCase(futureMsg.getAction()))
			{
				mClient.sendDelayedTextMsg(futureMsg.getContent(), userName, futureMsg.getDelay(), null);
				EnglishSignUpVO englishSignUpVO=new EnglishSignUpVO();
				englishSignUpVO.setNickName(nickName);
				this.addFutureEvent(userName,Type.ENGLISH_SIGN_UP,futureMsg,0,englishSignUpVO); 
			}
		} 
		
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
		String userName=recInfo.getUserName();
		 
		Map<String, WechatContact> buddyList=mClient.getWechatStore().getBuddyList();
		Map<String, WechatContact> chatRoom=mClient.getWechatStore().getChatRoom();
		Map<String, WechatContact> groupList=mClient.getWechatStore().getGroupList();
		if(userName==null || "".equalsIgnoreCase(userName))
		{
			return;
		}
		if(userName.startsWith("@@"))
		{
			chatRoom.remove(userName);
			groupList.remove(userName);
			WindowContext.removeGroupFromList(recInfo);
		}else 
		{
			buddyList.remove(userName);
			WindowContext.removeriendFromList(recInfo);
		} 
		WindowContext.getOpMap().remove(userName);
		
		this.mClient.output("通讯录中删除联系人："+msg.getFrom());
		
	}

	@Override
	protected boolean processFutureEvent(WechatMsg msg) {
		super.processFutureEvent(msg);
		if(msg==null)
		{
			return false;
		}
		FutureEvent futureEvent=WindowContext.getFutureEventMap().get(msg.getFromUserName());
		if(futureEvent==null)
		{
			return false;
		}
		switch(futureEvent.getType())
		{
		case FILM_WAIT_FOR_PAY:
			break;
		case  SOFTWARE_WAIT_FOR_PAY:
			break;
		case  SUBSCRIPTION_CONTENT_WAIT_TO_RECORD:
			break;
		case   CONTENT_WAIT_TO_CHOOSE:
			break;
		case  ENGLISH_SIGN_UP:
			return processEnglishSignUpFutureAction(futureEvent,msg); 
		}
		
		
		
		return false;
	}
	
	private boolean processEnglishSignUpFutureAction(FutureEvent futureEvent,WechatMsg msg)
	{
		int order=futureEvent.getOrder();
		FutureMsg futureMsg=(FutureMsg)futureEvent.getTarget();
		EnglishSignUpVO englishSignUpVO=(EnglishSignUpVO)futureEvent.getData();
		boolean reponsed=false;
		String className="";
		if("问年龄".equalsIgnoreCase(futureMsg.getAction()))
		{
			Double age=getAgeFromMsg(msg.getContent());
			englishSignUpVO.setAge(age); 
			if(age==2 || age==4)
			{
				ConcurrentHashMap<Long, FutureMsg> msgMap=mClient.getConfig().getFutrueMsgMap();
				for(FutureMsg temp : msgMap.values())
				{
					if("问基础".equalsIgnoreCase(temp.getAction()))
				    { 
						SendMsgTask msgTask=new SendMsgTask(mClient,temp,msg.getFromUserName(),temp.getDelay());
						ThreadPoolManager.newInstance().addTask(msgTask);
						reponsed=true;
						futureEvent.setTarget(temp);  
//					   mClient.sendTextMsg(temp.getContent(), msg.getFromUserName(), null);
				    }
				}
			}else if(age<2)
			{
				className="樱桃班";
				
			}else if(age>2 && age<4)
			{
				className="草莓班";
			}else
			{
				className="苹果班";
			}
			if(!StringHelper.isEmpty(className))
			{
				WechatMsg classNameMsg=new WechatMsg();
				classNameMsg.setMsgType(WechatMsgType.MSGTYPE_TEXT);
				classNameMsg.setContent(className+"哦");
				sendClassSchedule(className,msg.getFromUserName());
				SendMsgTask classNameMsgTask=new SendMsgTask(mClient,classNameMsg,msg.getFromUserName(),0L);
				ThreadPoolManager.newInstance().addTask(classNameMsgTask);
				ConcurrentHashMap<Long, FutureMsg> msgMap=mClient.getConfig().getFutrueMsgMap();
				for(FutureMsg temp : msgMap.values())
				{
					if("报名流程".equalsIgnoreCase(temp.getAction()))
				    { 
						SendMsgTask msgTask=new SendMsgTask(mClient,temp,msg.getFromUserName(),temp.getDelay());
						ThreadPoolManager.newInstance().addTask(msgTask);
						reponsed=true;
						futureEvent.setTarget(temp);   
				    }
				}
				
			}
			
			englishSignUpVO.setClassName(className);
			futureEvent.setData(englishSignUpVO);
			WindowContext.getFutureEventMap().put(msg.getFromUserName(), futureEvent);
		}else if("问基础".equalsIgnoreCase(futureMsg.getAction()))
		{
			 
			 boolean hasBasis=getBasisFromMsg(msg.getContent());
			 englishSignUpVO.setBasis(hasBasis?"有":"没有");
			 if(hasBasis)
			 {
				 if(englishSignUpVO.getAge()==2)
				 {
					 englishSignUpVO.setClassName("草莓班");
				 }else if(englishSignUpVO.getAge()==4)
				 { 
					 englishSignUpVO.setClassName("苹果班");
				 }
			 }else
			 {
				 if(englishSignUpVO.getAge()==2)
				 {
					 englishSignUpVO.setClassName("樱桃班");
				 }else if(englishSignUpVO.getAge()==4)
				 {
					 englishSignUpVO.setClassName("草莓班");
				 }
			 }
			 WechatMsg classNameMsg=new WechatMsg();
				classNameMsg.setContent(englishSignUpVO.getClassName()+"哦");
				classNameMsg.setMsgType(WechatMsgType.MSGTYPE_TEXT);
				SendMsgTask classNameMsgTask=new SendMsgTask(mClient,classNameMsg,msg.getFromUserName(),0L);
				ThreadPoolManager.newInstance().addTask(classNameMsgTask);
				
				sendClassSchedule(englishSignUpVO.getClassName(),msg.getFromUserName());
			 ConcurrentHashMap<Long, FutureMsg> msgMap=mClient.getConfig().getFutrueMsgMap();
				for(FutureMsg temp : msgMap.values())
				{
					if("报名流程".equalsIgnoreCase(temp.getAction()))
				    { 
						SendMsgTask msgTask=new SendMsgTask(mClient,temp,msg.getFromUserName(),temp.getDelay());
						ThreadPoolManager.newInstance().addTask(msgTask);
						reponsed=true;
						futureEvent.setTarget(temp);   
				    }
				}
				futureEvent.setData(englishSignUpVO);
				WindowContext.getFutureEventMap().put(msg.getFromUserName(), futureEvent);
		}else  if("报名流程".equalsIgnoreCase(futureMsg.getAction()) && msg.getMsgType()==WechatMsgType.MSGTYPE_IMAGE)
		{ 
			this.setRemark(englishSignUpVO);
			englishSignUpDaoService.save(englishSignUpVO);
			WechatRemarkTask remarkTask=new WechatRemarkTask(mClient,msg.getFromUserName(),englishSignUpVO.getRemark(),1);
			ThreadPoolManager.newInstance().addTask(remarkTask);
			this.sendFutureMsg("报名成功",msg.getFromUserName()); 
			reponsed=true;
			WindowContext.getFutureEventMap().remove(msg.getFromUserName());
		}
		
		
		return reponsed;
	}
	
	private void setRemark(EnglishSignUpVO englishSignUpVO)
	{
		String sequence=EnglishSignUpSequence.next(englishSignUpVO.getClassName());
		 Calendar cal = Calendar.getInstance();
		 int month = cal.get(Calendar.MONTH) + 1;
		 String remark= englishSignUpVO.getClassName()+month+"月"+sequence;
		 englishSignUpVO.setRemark(remark);
	}
	
	private void sendClassSchedule(String className,String toUserName)
	{
		String action=className+"课表";
		this.sendFutureMsg(action, toUserName);
	}
	
	private FutureMsg sendFutureMsg(String action,String toUserName)
	{
		FutureMsg futureMsg=null;
		ConcurrentHashMap<Long, FutureMsg> msgMap=mClient.getConfig().getFutrueMsgMap();
		for(FutureMsg temp : msgMap.values())
		{
			if(action.equalsIgnoreCase(temp.getAction()))
		    { 
				SendMsgTask msgTask=new SendMsgTask(mClient,temp,toUserName,temp.getDelay());
				ThreadPoolManager.newInstance().addTask(msgTask); 
		    }
		}
		return futureMsg;
	}
	
	private boolean getBasisFromMsg(String msg)
	{
		boolean hasBasis=false;
		if(msg!=null && msg.indexOf("没有")>-1)
		{
			hasBasis=false;
		}else if(msg!=null && msg.indexOf("有")>-1)
		{
			hasBasis=true;
		}
		return hasBasis;
	}
	
	private   Double getAgeFromMsg(String msg)
	{
		String numTarget = ""; 
		Double ageDouble =null;
		int age=0;
		String ageKeywordYear = "岁";
		String ageKeywordMonth = "月";
		String ageKeywordDay = "天"; 
		if (msg.indexOf(ageKeywordYear) > -1) {
			numTarget=getAgeNumberFromStr(msg,msg.indexOf(ageKeywordYear));  
			age=NumberUtils.getNumberFromChineseAndArabicStr(numTarget);
			ageDouble=Double.valueOf(age);
		} else if (msg.indexOf(ageKeywordMonth) > -1) {
			numTarget=getAgeNumberFromStr(msg,msg.indexOf(ageKeywordMonth));  
			age=NumberUtils.getNumberFromChineseAndArabicStr(numTarget);
			ageDouble=age/12.0;
		}else if (msg.indexOf(ageKeywordDay) > -1) {
			numTarget=getAgeNumberFromStr(msg,msg.indexOf(ageKeywordDay)); 
			age=NumberUtils.getNumberFromChineseAndArabicStr(numTarget);
			ageDouble=age/365.0;
		}
		else if(NumberUtils.isNumeric(msg))
		{
			numTarget=msg; 
			age=NumberUtils.getNumberFromChineseAndArabicStr(numTarget);
			ageDouble=Double.valueOf(age);
		}
		System.out.println(numTarget);
		age=NumberUtils.getNumberFromChineseAndArabicStr(numTarget); 
		return ageDouble;
	}
	
	private   String getAgeNumberFromStr(String msg,int index)
	{
		String numChar = "一二三四五六七八九十";
		int searchIndex=index;
		String numTarget = "";
		
		if("两".equalsIgnoreCase(msg.substring(0, msg.length()-1)) && msg.length()>1)
		{
			return "2";
		}
		
		while (searchIndex - 1 >= 0) {
			String temp=msg.substring(searchIndex - 1, searchIndex);
			if (numChar.indexOf(temp) > -1
					|| StringUtils.isNumeric(temp)) {
				numTarget = temp+ numTarget;
			} else if ("个".equalsIgnoreCase(temp) && searchIndex==index) {
				 
				searchIndex--;
				continue;
			}
			else  
			{
				break;
			}
			searchIndex--;
		}
		
		return numTarget;
	}

	@Override
	public void onSysMsgSomeoneHasBeenRemovedOutOfGroup(WechatMsg msg,
			String who, String byWho) { 
		
		if(StringHelper.isEmpty(who) || StringHelper.isEmpty(byWho))
		{
			return;
		}
		super.onSysMsgSomeoneHasBeenRemovedOutOfGroup(msg, who, byWho);
		GroupInviteVO inviteVO=new GroupInviteVO();
		inviteVO.setInviter(byWho);
		inviteVO.setBeInvited(who);
		inviteVO.setGroupName(msg.getFrom());
		inviteVO.setOption("移出群聊");
		groupInviteStatisticsDaoService.save(inviteVO);
		
		if(this.mClient.getSession().getUser().getNickName().equalsIgnoreCase(who) &&
				this.mClient.getConfig().isNoticeInviterWhenRemovedOut())
		{
			GroupInviteSearchVO searchVO=new GroupInviteSearchVO();
			searchVO.setBeInvited(who);
			searchVO.setGroupName(msg.getFrom());
			searchVO.setOption("邀请加入");
			GroupInviteVO inviter=groupInviteStatisticsDaoService.findLastInviter(searchVO);
			if(inviter==null || StringHelper.isEmpty(inviter.getInviter()))
			{
				return;
			}
			String inviterUserName=UserUtils.getUserNameByNickName(this.mClient,inviter.getInviter());
			
			
			searchVO.setInviter(inviter.getInviter());
			searchVO.setOption("移出群聊");
			int removedCount=groupInviteStatisticsDaoService.findFinalOptCount(searchVO);
			String sendMsg="@"+inviter.getInviter()+"\r\n我被"+byWho+"移出群组"+msg.getFrom();
			if(removedCount>-1)
			{
				sendMsg+="，一共被踢了"+removedCount+"个群";
			}
			this.mClient.sendDelayedTextMsg(sendMsg, inviterUserName,1L, null);
			String filePath=this.mClient.getConfig().getGroupCustomizeGroupInviteStatisticsPath();
			String fileName=EncryptUtil.encryptMD5(who);
			
			List<IniSection> sections=new ArrayList<IniSection>();
			IniSection iniSection=new IniSection(EncryptUtil.encryptMD5(inviter.getInviter()));
			iniSection.add("removed", sendMsg);
			sections.add(iniSection);
			IniFileUtil.generate(filePath, fileName+".ini", sections,who);
			String content=IniFileUtil.getIniCotent(filePath, fileName+".ini");
			if(content!=null)
			{
				FileUtil.writeToTxt(filePath+File.separator+fileName+".txt", content);
			}
		}
		
	}

	@Override
	public void onSysMsgNewMemberInGroup(WechatMsg msg, List<String> who,
			String byWho) { 
		if(who==null || who.isEmpty() || StringHelper.isEmpty(byWho))
		{
			return;
		}
		super.onSysMsgNewMemberInGroup(msg, who, byWho);
		GroupInviteStatisticsTask groupInviteStatisticsTask=new GroupInviteStatisticsTask(this.mClient,msg, who,byWho,1);
		ThreadPoolManager.newInstance().addTask(groupInviteStatisticsTask);
	}
	
	 
 
	
}
