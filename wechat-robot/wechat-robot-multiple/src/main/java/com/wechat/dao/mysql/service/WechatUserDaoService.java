package com.wechat.dao.mysql.service;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.db.mybatis.SessionFactory;
import com.wechat.bean.WechatUser;
import com.wechat.dao.mysql.dao.IUserDao;
import com.wechat.dao.service.IWechatUserDaoService;

public class WechatUserDaoService implements IWechatUserDaoService{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WechatUserDaoService.class);
	@Override
	public void saveUser(WechatUser user) {
		SqlSession session=null;
		IUserDao userDao=null;
		try{
			session = SessionFactory.getInstance().openSession();
			userDao = session.getMapper(IUserDao.class); 
			userDao.saveUser(user);
		}catch(Exception e)
		{
			LOGGER.error("保存消息异常"+e);
		}
		finally
		{
			SessionFactory.closeSession(session);
		}
		 
		
	}

}
