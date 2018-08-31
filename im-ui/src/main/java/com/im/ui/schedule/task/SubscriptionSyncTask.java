package com.im.ui.schedule.task; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.im.base.wechat.WechatContact;
import com.im.schedule.timer.ScheduleTask;
import com.subscription.Subscription;
import com.subscription.content.CategoriedContent;
import com.subscription.content.ContentCategory;
import com.subscription.content.ContentType;
import com.subscription.content.SubscriptionContent;
import com.wechat.WebWechatClient; 
import com.wechat.WechatClient;
import com.wechat.core.WechatContext;
import com.wechat.core.WechatStore;
import com.wechat.dao.mysql.service.SubscriptionDaoService;

public class SubscriptionSyncTask extends ScheduleTask{

	private WechatClient client;
	
	private Map<Long, WechatContact> contactMap;
	
	private SubscriptionDaoService subscriptionDaoService=new SubscriptionDaoService();
	@Override
	public void run() { 
		if(!getContactMap())
		{
			return;
		}
		
		List<Subscription> subscriptionList=subscriptionDaoService.getSyncPushSubscriptions(client.getSession().getUser().getUin());
		if(subscriptionList==null || subscriptionList.isEmpty())
		{
			return;
		}
		Map<Long,List<SubscriptionContent>>contentMap=new HashMap<Long,List<SubscriptionContent>>();
		for(Subscription temp : subscriptionList)
		{
			if(contentMap.get(temp.getContactId())==null)
			{
				contentMap.put(temp.getContactId(), new ArrayList<SubscriptionContent>());
			}
			contentMap.get(temp.getContactId()).add(temp.getContent());
		}
		Set<Long> kdySet=contentMap.keySet();
		for(Long contactId: kdySet)
		{
			List<SubscriptionContent> tempList=contentMap.get(contactId);
			CategoriedContent categoriedContent=new CategoriedContent();
			for(SubscriptionContent temp : tempList)
			{
				if(ContentCategory.PIC.equalsIgnoreCase(temp.getCategory()))
				{
					categoriedContent.getPicContentPaid().add(temp);
				}else if(ContentCategory.SOFTWARE.equalsIgnoreCase(temp.getCategory()))
				{
					categoriedContent.getSoftwareContentPaid().add(temp);
				}else if(ContentCategory.VIDEO.equalsIgnoreCase(temp.getCategory()))
				{
					categoriedContent.getVideoContentPaid().add(temp);
				}
			}
			WechatContact contact=contactMap.get(contactId);
			if(contact==null)
			{
				continue;
			}
			if(categoriedContent.getPicContentPaid()!=null && !categoriedContent.getPicContentPaid().isEmpty())
			{
				client.sendTextMsg(getTextUrlMsg(categoriedContent.getPicContentPaid()), contact.getUserName(), null);
				savePushVersion(contactId,categoriedContent.getPicContentPaid());
			}
			if(categoriedContent.getSoftwareContentPaid()!=null && !categoriedContent.getSoftwareContentPaid().isEmpty())
			{
				client.sendTextMsg(getTextUrlMsg(categoriedContent.getSoftwareContentPaid()), contact.getUserName(), null);
				savePushVersion(contactId,categoriedContent.getSoftwareContentPaid());
			}
			if(categoriedContent.getVideoContentPaid()!=null && !categoriedContent.getVideoContentPaid().isEmpty())
			{
				client.sendTextMsg(getTextUrlMsg(categoriedContent.getVideoContentPaid()), contact.getUserName(), null);
				savePushVersion(contactId,categoriedContent.getVideoContentPaid());
			}
		}
		 
	}
	
	private void savePushVersion(Long contactId,List<SubscriptionContent> contents)
	{
		if(contents==null || contents.isEmpty())
		{
			return;
		}
		Subscription subscription=null;
		for(SubscriptionContent temp : contents)
		{
			subscription=new Subscription();
			subscription.setContactId(contactId);
			subscription.setContent(temp);
			subscription.setPushVersion(temp.getVersion());
			subscription.setHasPaid(true);
			subscriptionDaoService.saveSubscription(subscription);
		}
		
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
		buffer.append("以上内容有更新，自动发送不用回");
		return buffer.toString();
	}
	public SubscriptionSyncTask(WechatClient client)
	{
		 
		this.client=client;
	}
	
	private boolean getContactMap()
	{
		WechatContext wechatContext=(WechatContext)client;
		WechatStore store=wechatContext.getWechatStore();
		Map<String, WechatContact> contacts=store.getBuddyList();
		if(contacts==null || contacts.isEmpty())
		{
			return false;
		}
		contactMap=new HashMap<Long,WechatContact>();
		for(WechatContact temp : contacts.values())
		{
			contactMap.put(temp.getId(), temp);
		}
		return true;
	}
	 

}
