package com.wechat.dao.mysql.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.im.base.wechat.WechatContact;
import com.subscription.KeywordVO;
import com.subscription.Subscription;
import com.subscription.content.SubscriptionContent;

public interface ISubscriptionDao {

	public void saveContent(@Param("content")SubscriptionContent content);
	//public void updateKeywordMatch(@Param("keywords")List<String> keywords,@Param("content")SubscriptionContent content);
	
	public void saveKeyword(@Param("keyword")KeywordVO keyword); 
	
	public void saveKeywordContentMatch(@Param("keyword")KeywordVO keyword,@Param("content")SubscriptionContent content);
	public void saveKeywordExt(@Param("keyword")KeywordVO keyword,@Param("labels")List<KeywordVO> labels);
	

	public List<KeywordVO> getKeywordList(@Param("owner")Long owner);
	 
	public Subscription getSubscription(@Param("contact")WechatContact contact,@Param("content")SubscriptionContent content);
	public List<Subscription> batchGetSubscription(@Param("contact")WechatContact contact,@Param("contents")List<SubscriptionContent> contents);
    public List<Subscription> getSyncPushSubscriptions(@Param("owner")Long owner);
	
    public void saveSubscription(@Param("subscription")Subscription subscription);
}
