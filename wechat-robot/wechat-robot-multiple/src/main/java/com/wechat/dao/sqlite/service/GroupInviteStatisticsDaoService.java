package com.wechat.dao.sqlite.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.base.customize.GroupInviteSearchVO;
import com.im.base.customize.GroupInviteVO;
import com.im.base.log.WechatGroupInviteLog;
import com.im.base.log.WechatGroupInviteLogSearchVO;
import com.im.base.schedule.WechatTimingMsgVO;
import com.im.db.mybatis.SessionFactory;
import com.wechat.dao.sqlite.dao.IGroupInviteJobLogDao;
import com.wechat.dao.sqlite.dao.IGroupInviteStatisticsDao;
import com.wechat.dao.sqlite.dao.IWechatTimingMsgDao;

public class GroupInviteStatisticsDaoService {

private static final Logger LOGGER = LoggerFactory.getLogger(GroupInviteStatisticsDaoService.class);
	
	public void save(GroupInviteVO inviteVO)
	{
		SqlSession session=null;
		IGroupInviteStatisticsDao groupInviteStatisticsDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			groupInviteStatisticsDao = session.getMapper(IGroupInviteStatisticsDao.class);   
			groupInviteStatisticsDao.save(inviteVO);
		}catch(Exception e)
		{
			LOGGER.error("保存群组邀请信息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return ;
	}
	public List<GroupInviteVO> query(GroupInviteSearchVO searchVO)
	{
		List<GroupInviteVO> result=null;
		SqlSession session=null;
		IGroupInviteStatisticsDao groupInviteStatisticsDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			groupInviteStatisticsDao = session.getMapper(IGroupInviteStatisticsDao.class);   
			result=groupInviteStatisticsDao.query(searchVO);
		    
		}catch(Exception e)
		{
			LOGGER.error("查询群组邀请信息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		} 
		return result;
	}
	public GroupInviteVO findLastInviter(GroupInviteSearchVO searchVO)
	{
		GroupInviteVO result=null;
		SqlSession session=null;
		IGroupInviteStatisticsDao groupInviteStatisticsDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			groupInviteStatisticsDao = session.getMapper(IGroupInviteStatisticsDao.class);   
			result=groupInviteStatisticsDao.findLastInviter(searchVO);
		    
		}catch(Exception e)
		{
			LOGGER.error("查询群组邀请人信息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		} 
		return result;
	}
	
	
	public int findInviteCount(GroupInviteSearchVO searchVO)
	{
		int result=-1;
		SqlSession session=null;
		IGroupInviteStatisticsDao groupInviteStatisticsDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			groupInviteStatisticsDao = session.getMapper(IGroupInviteStatisticsDao.class);   
			result=groupInviteStatisticsDao.findInviteCount(searchVO);
		    
		}catch(Exception e)
		{
			LOGGER.error("查询群组邀请数量信息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		} 
		return result;
	}
	 
	
	public int findFinalOptCount(GroupInviteSearchVO searchVO)
	{
		int result=-1;
		SqlSession session=null;
		IGroupInviteStatisticsDao groupInviteStatisticsDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			groupInviteStatisticsDao = session.getMapper(IGroupInviteStatisticsDao.class);   
			result=groupInviteStatisticsDao.findFinalOptCount(searchVO);
		    
		}catch(Exception e)
		{
			LOGGER.error("查询群组最终操作数量信息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		} 
		return result;
	}
}
