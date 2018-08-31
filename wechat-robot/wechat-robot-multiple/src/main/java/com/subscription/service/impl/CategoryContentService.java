package com.subscription.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.subscription.Subscription;
import com.subscription.content.CategoriedContent;
import com.subscription.content.ContentCategory;
import com.subscription.content.SubscriptionContent;

public class CategoryContentService {

	private void categoryContent(List<SubscriptionContent> contents,CategoriedContent categoriedContent,boolean paid)
	{
		 
		if(contents==null || contents.isEmpty() || categoriedContent==null)
		{
			return ;
		}
		
		 
		for(SubscriptionContent temp : contents)
		{
			if(ContentCategory.PIC.equalsIgnoreCase(temp.getCategory()))
			{
				if(paid || !temp.isNeedPaid())
				{
					categoriedContent.getPicContentPaid().add(temp);
				}else
				{
					//if(temp.isNeedSingle()&& categoriedContent.getPicContentNotPaid()!=null && categoriedContent.getPicContentNotPaid().size()>0)
					//{
					//	categoriedContent.setPicHasNeedSingle(true);
					//}
					categoriedContent.getPicContentNotPaid().add(temp);
				}
				 
			}else if(ContentCategory.VIDEO.equalsIgnoreCase(temp.getCategory()))
			{
				if(paid || !temp.isNeedPaid())
				{
					categoriedContent.getVideoContentPaid().add(temp);
				}else
				{
					//if(temp.isNeedSingle()&& categoriedContent.getVideoContentNotPaid()!=null && categoriedContent.getVideoContentNotPaid().size()>0)
					//{
					//	categoriedContent.setVideoHasNeedSingle(true);
					//}
					categoriedContent.getVideoContentNotPaid().add(temp);
				}
			}else if(ContentCategory.SOFTWARE.equalsIgnoreCase(temp.getCategory()))
			{
				if(paid || !temp.isNeedPaid())
				{
					categoriedContent.getSoftwareContentPaid().add(temp);
				}else
				{
					//if(temp.isNeedSingle()&& categoriedContent.getSoftwareContentNotPaid()!=null && categoriedContent.getSoftwareContentNotPaid().size()>0)
					//{
					//	categoriedContent.setSoftwareHasNeedSingle(true);
					//}
					categoriedContent.getSoftwareContentNotPaid().add(temp);
				}
			}if(ContentCategory.TEXT.equalsIgnoreCase(temp.getCategory()))
			{
				if(paid || !temp.isNeedPaid())
				{
					categoriedContent.getTextContentPaid().add(temp);
				}else
				{ 
					categoriedContent.getTextContentNotPaid().add(temp);
				}
			} 
			if(!paid&&temp.isNeedSingle())
			{
				categoriedContent.setHasNeedSingle(true);
			}
			
		} 
		if(!paid &&categoriedContent.isHasNeedSingle())
		{
			int length=(categoriedContent.getPicContentNotPaid()==null?0:categoriedContent.getPicContentNotPaid().size())
			 +(categoriedContent.getSoftwareContentNotPaid()==null?0:categoriedContent.getSoftwareContentNotPaid().size())
			+(categoriedContent.getVideoContentNotPaid()==null?0:categoriedContent.getVideoContentNotPaid().size());
			if(length<2)
			{
				categoriedContent.setHasNeedSingle(false);
			}
		}
		
		return ;
	}
	
	
	public CategoriedContent categorySubscriptions(List<SubscriptionContent> contents,List<Subscription> subscriptions)
	{
		Map<Long,SubscriptionContent> contentsMap=new HashMap<Long,SubscriptionContent>();
		List<SubscriptionContent> notPaid=null;
		List<SubscriptionContent> paid=new ArrayList<SubscriptionContent>();
		CategoriedContent categoriedContent=new CategoriedContent();
		for(SubscriptionContent temp : contents)
		{
			contentsMap.put(temp.getId(), temp);
		}
		 if(subscriptions!=null)
		 {
			 for(Subscription temp : subscriptions)
				{
					long contentId=temp.getContent()==null?-1:temp.getContent().getId();
					if(contentsMap.get(contentId)!=null)
					{
						paid.add(contentsMap.get(contentId));
						contentsMap.remove(contentId);
					} 
				}
		 }
		
		if(!contentsMap.isEmpty())
		{
			notPaid=new ArrayList<SubscriptionContent>(contentsMap.values());
		}
		if(paid!=null && !paid.isEmpty())
		{
		   this.categoryContent(paid, categoriedContent, true);
		}
		
		if(notPaid!=null && !notPaid.isEmpty())
		{
		   this.categoryContent(notPaid, categoriedContent, false);
		}
		
		return categoriedContent;
	}
	
}
