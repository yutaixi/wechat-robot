package com.wechat.dao.sqlite.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

import com.im.base.schedule.WechatTimingMsgVO;
import com.im.base.wechat.WechatMsg;
import com.im.db.mybatis.SessionFactory; 
import com.wechat.dao.sqlite.dao.IWechatTimingMsgDao;
import com.wechat.dao.sqlite.dao.IWelcomeMsgDao;

public class WechatTimingMsgService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WechatTimingMsgService.class);
	
	public void saveTimingMsg(WechatTimingMsgVO msg)
	{
		SqlSession session=null;
		IWechatTimingMsgDao wechatTimingMsgDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			wechatTimingMsgDao = session.getMapper(IWechatTimingMsgDao.class);   
			if(msg.getId()==null)
		    {
				wechatTimingMsgDao.saveTimingMsg(msg);
		    }else
		    {
		    	wechatTimingMsgDao.updateTimingMsg(msg);
		    }
		}catch(Exception e)
		{
			LOGGER.error("保存定时消息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return ;
	}
	
	public void deleteTimingMsg(WechatTimingMsgVO msg)
	{
		if(msg==null || msg.getId()==null)
	    {
			return;
	    }
		SqlSession session=null;
		IWechatTimingMsgDao wechatTimingMsgDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			wechatTimingMsgDao = session.getMapper(IWechatTimingMsgDao.class);   
			wechatTimingMsgDao.deleteTimingMsg(msg);
		}catch(Exception e)
		{
			LOGGER.error("删除定时消息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return ;
	}
	
	public List<WechatTimingMsgVO> queryTimingMsg()
	{
		List<WechatTimingMsgVO> result=null;
		SqlSession session=null;
		IWechatTimingMsgDao wechatTimingMsgDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			wechatTimingMsgDao = session.getMapper(IWechatTimingMsgDao.class);   
			result=wechatTimingMsgDao.queryAllTimingMsg();
		    
		}catch(Exception e)
		{
			LOGGER.error("查询定时消息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		} 
		return result;
	}
}
