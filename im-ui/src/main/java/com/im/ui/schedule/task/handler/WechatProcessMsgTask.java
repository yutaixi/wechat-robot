package com.im.ui.schedule.task.handler;

import java.util.ArrayList;
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

import com.im.base.wechat.WechatContact;
import com.im.base.wechat.WechatMsg;
import com.im.schedule.queue.ThreadPoolManager;
import com.im.schedule.queue.ThreadQueueTask;
import com.im.ui.schedule.future.BatchGetContactInfoFutureTask;
import com.im.ui.schedule.task.AutoReplyVideoMsgTask;
import com.im.ui.service.AutoDistributeService;
import com.im.ui.util.context.WindowContext;
import com.im.utils.DateUtils;
import com.im.utils.StringHelper;
import com.subscription.KeywordVO;
import com.subscription.content.CategoriedContent;
import com.subscription.content.ContentCategory;
import com.subscription.content.SubscriptionContent;
import com.subscription.service.impl.CategoryContentService;
import com.wechat.WebWechatClient;
import com.wechat.WechatClient;
import com.wechat.core.WechatContext;
import com.wechat.event.FutureEvent;

public class WechatProcessMsgTask extends ThreadQueueTask{

	private WebWechatClient mClient;
	
	private WechatMsg wechatMsg;
	private AutoDistributeService autoDistributeService =new AutoDistributeService();
	
	public WechatProcessMsgTask(WebWechatClient mClient,WechatMsg wechatMsg)
	{
		this.mClient=mClient;
		this.wechatMsg=wechatMsg;
	}
	
	@Override
	public void run() {
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
		
		
		hasResponed=processDistributeCode(mClient,wechatMsg); 
		
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
				 if(keyword.getKeyword()!=null && msg.contains(keyword.getKeyword()))
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
	
	protected void addFutureEvent(String userName,FutureEvent.Type type,Object target,int order)
	{
		FutureEvent futureEvent=new FutureEvent(userName,type,target,order);
		WindowContext.getFutureEventMap().put(userName, futureEvent); 
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
}
