package com.wechat.dao.mysql.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.base.wechat.WechatContact;
import com.im.db.mybatis.SessionFactory;
import com.wechat.dao.mysql.dao.IContactDao;
import com.wechat.dao.service.IWechatContactDaoService;

public class WechatContactDaoService implements IWechatContactDaoService{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WechatContactDaoService.class);
	
	@Override
	public void insertOrUpdateContact(Map<String,WechatContact> contactMap, Long owner) {
		SqlSession session=null;
		IContactDao contactDao=null;
		if(contactMap==null || contactMap.isEmpty())
		{
			return ;
		}
		Collection<WechatContact> contactCollection=contactMap.values();
		try{
			session = SessionFactory.getInstance().openSession();
			contactDao = session.getMapper(IContactDao.class); 
			for(WechatContact contact : contactCollection)
			{
				contactDao.insertOrUpdateContact(contact, owner);
			} 
		}catch(Exception e)
		{
			LOGGER.error("保存通讯录异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		
	}
	
	 

}
