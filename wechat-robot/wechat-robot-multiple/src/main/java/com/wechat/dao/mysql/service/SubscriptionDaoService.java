package com.wechat.dao.mysql.service; 
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

import com.im.base.wechat.WechatContact;
import com.im.db.mybatis.SessionFactory;
import com.subscription.KeywordVO;
import com.subscription.Subscription;
import com.subscription.content.SubscriptionContent; 
import com.wechat.dao.mysql.dao.ISubscriptionDao;

public class SubscriptionDaoService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SubscriptionDaoService.class);
	 
	public void insertOrUpdateContent(SubscriptionContent content) {
		SqlSession session=null;
		ISubscriptionDao subscriptionContentDao=null;
		if(content==null  )
		{
			return ;
		}
		 
		try{
			session = SessionFactory.getInstance().openSession();
			subscriptionContentDao = session.getMapper(ISubscriptionDao.class); 
			subscriptionContentDao.saveContent(content); 
		}catch(Exception e)
		{
			LOGGER.error("保存订阅内容异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		
	}
	
//	public void updateKeywordMatch(List<String> keywords,SubscriptionContent content) {
//		SqlSession session=null;
//		ISubscriptionDao subscriptionContentDao=null;
//		if(content==null || keywords==null || keywords.isEmpty() )
//		{
//			return ;
//		}
//		 
//		try{
//			session = SessionFactory.getInstance().openSession();
//			subscriptionContentDao = session.getMapper(ISubscriptionDao.class);  
//			subscriptionContentDao.updateKeywordMatch(keywords,content); 
//		}catch(Exception e)
//		{
//			LOGGER.error("保存关键词异常"+e);
//		}finally
//		{
//			SessionFactory.closeSession(session);
//		}
//		
//	}
	
	public List<KeywordVO> getKeywordList(Long owner) {
		SqlSession session=null;
		ISubscriptionDao subscriptionContentDao=null;
		List<KeywordVO> keywords=null;
		 
		try{
			session = SessionFactory.getInstance().openSession();
			subscriptionContentDao = session.getMapper(ISubscriptionDao.class); 
			keywords=subscriptionContentDao.getKeywordList(owner);
		}catch(Exception e)
		{
			LOGGER.error("获取关键词异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return keywords;
	}
	
	public Subscription getSubscription(WechatContact contact,SubscriptionContent content)
	{
		SqlSession session=null;
		ISubscriptionDao subscriptionContentDao=null;
		Subscription subscription=null;
		 
		try{
			session = SessionFactory.getInstance().openSession();
			subscriptionContentDao = session.getMapper(ISubscriptionDao.class); 
			subscription=subscriptionContentDao.getSubscription(contact, content);
		}catch(Exception e)
		{
			LOGGER.error("获取订阅信息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return subscription;
	}
	
	public List<Subscription> batchGetSubscription(WechatContact contact,List<SubscriptionContent> contents)
	{
		SqlSession session=null;
		ISubscriptionDao subscriptionContentDao=null;
		 List<Subscription> subscriptions=null;
		 
		try{
			session = SessionFactory.getInstance().openSession();
			subscriptionContentDao = session.getMapper(ISubscriptionDao.class); 
			subscriptions=subscriptionContentDao.batchGetSubscription(contact, contents);
		}catch(Exception e)
		{
			LOGGER.error("批量获取订阅信息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return subscriptions;
	}
	
	public List<Subscription> getSyncPushSubscriptions(Long owner)
	{
		SqlSession session=null;
		ISubscriptionDao subscriptionContentDao=null;
		 List<Subscription> subscriptions=null;
		 
		try{
			session = SessionFactory.getInstance().openSession();
			subscriptionContentDao = session.getMapper(ISubscriptionDao.class); 
			subscriptions=subscriptionContentDao.getSyncPushSubscriptions(owner);
		}catch(Exception e)
		{
			LOGGER.error("批量获取订阅信息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return subscriptions;
	}
	
	public boolean saveSubscription(Subscription subscription)
	{
		SqlSession session=null;
		ISubscriptionDao subscriptionContentDao=null; 
		 boolean success=false;
		try{
			session = SessionFactory.getInstance().openSession();
			subscriptionContentDao = session.getMapper(ISubscriptionDao.class); 
			subscriptionContentDao.saveSubscription(subscription);
			success=true;
		}catch(Exception e)
		{
			LOGGER.error("保存订阅信息异常"+e); 
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return success;
	}
	
	public boolean saveKeyword(KeywordVO keyword)
	{
		SqlSession session=null;
		ISubscriptionDao subscriptionContentDao=null; 
		 boolean success=false;
		try{
			session = SessionFactory.getInstance().openSession();
			subscriptionContentDao = session.getMapper(ISubscriptionDao.class); 
			subscriptionContentDao.saveKeyword(keyword); 
			success=true;
		}catch(Exception e)
		{
			LOGGER.error("保存关键词信息异常"+e); 
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return success;
	}
	
	public boolean saveKeywords(List<KeywordVO> keywords) {
		boolean success = false;
		for (KeywordVO temp : keywords) {
			success = saveKeyword(temp);
		} 
		return success;
	}
	
	public boolean saveKeywordContentMatch(KeywordVO keyword,SubscriptionContent content)
	{
		SqlSession session=null;
		ISubscriptionDao subscriptionContentDao=null; 
		 boolean success=false;
		try{
			session = SessionFactory.getInstance().openSession();
			subscriptionContentDao = session.getMapper(ISubscriptionDao.class); 
			subscriptionContentDao.saveKeywordContentMatch(keyword, content);
			success=true;
		}catch(Exception e)
		{
			LOGGER.error("保存关键词和内容关系异常"+e); 
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return success;
	}
	
	public boolean saveKeywordExt(KeywordVO keyword,List<KeywordVO> labels)
	{
		SqlSession session=null;
		ISubscriptionDao subscriptionContentDao=null; 
		 boolean success=false;
		try{
			session = SessionFactory.getInstance().openSession();
			subscriptionContentDao = session.getMapper(ISubscriptionDao.class); 
			subscriptionContentDao.saveKeywordExt(keyword, labels);
			success=true;
		}catch(Exception e)
		{
			LOGGER.error("保存关键词ext关系异常"+e); 
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return success;
	}
	
}
