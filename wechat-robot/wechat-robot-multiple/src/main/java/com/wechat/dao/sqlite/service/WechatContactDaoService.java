package com.wechat.dao.sqlite.service; 
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.base.wechat.WechatContact;
import com.im.db.mybatis.SessionFactory; 
import com.wechat.dao.sqlite.dao.IContactDao;

public class WechatContactDaoService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WechatContactDaoService.class);
	
	public WechatContact queryContact() {
		SqlSession session=null;
		IContactDao contactDao=null;
		WechatContact wechatContact=null;
		 
		try{
			session = SessionFactory.getInstance().openSession();
			contactDao = session.getMapper(IContactDao.class);   
		    wechatContact=contactDao.getContact();  
		}catch(Exception e)
		{
			LOGGER.error("获取通讯录异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return wechatContact;
	}
	
	public static void main(String[] args)
	{
		WechatContactDaoService wechatContactDaoService=new WechatContactDaoService();
		wechatContactDaoService.queryContact();
	}
	
}
