package com.wechat.dao.sqlite.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

import com.im.base.wechat.WechatMsg;
import com.im.db.mybatis.SessionFactory; 
import com.wechat.dao.sqlite.dao.IWelcomeMsgDao;

public class WelcomeMsgService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WelcomeMsgService.class);
	
	public void saveWelcomeMsg(WechatMsg msg)
	{
		SqlSession session=null;
		IWelcomeMsgDao welcomeMsgDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			welcomeMsgDao = session.getMapper(IWelcomeMsgDao.class);   
			if(msg.getId()==null)
		    {
				welcomeMsgDao.saveWelcomeMsg(msg);
		    }else
		    {
		    	welcomeMsgDao.updateWelcomeMsg(msg);
		    }
		}catch(Exception e)
		{
			LOGGER.error("保存欢迎语异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return ;
	}
	
	public void deleteWelcomeMsg(WechatMsg msg)
	{
		if(msg==null || msg.getId()==null)
	    {
			return;
	    }
		SqlSession session=null;
		IWelcomeMsgDao welcomeMsgDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			welcomeMsgDao = session.getMapper(IWelcomeMsgDao.class);   
			welcomeMsgDao.deleteWelcomeMsg(msg);
		}catch(Exception e)
		{
			LOGGER.error("删除欢迎语异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return ;
	}
	
	public List<WechatMsg> queryWelcomeMsg()
	{
		List<WechatMsg> result=null;
		SqlSession session=null;
		IWelcomeMsgDao welcomeMsgDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			welcomeMsgDao = session.getMapper(IWelcomeMsgDao.class);   
			result=welcomeMsgDao.queryAllWelcomeMsg();
		    
		}catch(Exception e)
		{
			LOGGER.error("查询欢迎语异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		} 
		return result;
	}
}
