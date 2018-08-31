package com.wechat.dao.sqlite.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.base.log.WechatGroupInviteLog;
import com.im.base.log.WechatGroupInviteLogSearchVO;
import com.im.base.schedule.WechatTimingMsgVO;
import com.im.db.mybatis.SessionFactory;
import com.wechat.dao.sqlite.dao.IGroupInviteJobLogDao;
import com.wechat.dao.sqlite.dao.IWechatTimingMsgDao;

public class WechatGroupInviteJobLogDaoService {

private static final Logger LOGGER = LoggerFactory.getLogger(WechatGroupInviteJobLogDaoService.class);
	
	public void saveLog(WechatGroupInviteLog log)
	{
		SqlSession session=null;
		IGroupInviteJobLogDao groupInviteLogDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			groupInviteLogDao = session.getMapper(IGroupInviteJobLogDao.class);   
			groupInviteLogDao.saveLog(log);
		}catch(Exception e)
		{
			LOGGER.error("保存群组邀请日志异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return ;
	}
	public List<WechatGroupInviteLog> queryGroupInviteLog(WechatGroupInviteLogSearchVO searchVO)
	{
		List<WechatGroupInviteLog> result=null;
		SqlSession session=null;
		IGroupInviteJobLogDao groupInviteLogDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			groupInviteLogDao = session.getMapper(IGroupInviteJobLogDao.class);   
			result=groupInviteLogDao.queryGroupInviteLog(searchVO);
		    
		}catch(Exception e)
		{
			LOGGER.error("查询群组邀请日志异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		} 
		return result;
	}
	
	public  WechatGroupInviteLog findFirstRecord(WechatGroupInviteLogSearchVO searchVO)
	{
		WechatGroupInviteLog result=null;
		SqlSession session=null;
		IGroupInviteJobLogDao groupInviteLogDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			groupInviteLogDao = session.getMapper(IGroupInviteJobLogDao.class);   
			result=groupInviteLogDao.findFirstRecord(searchVO);
		    
		}catch(Exception e)
		{
			LOGGER.error("查询群组邀请单条日志异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		} 
		return result;
	}
}
