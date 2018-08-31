package com.wechat.dao.sqlite.service; 
import java.util.List; 
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  
import com.im.base.wechat.FutureMsg; 
import com.im.db.mybatis.SessionFactory; 
import com.wechat.dao.sqlite.dao.IFutureMsgDao; 

public class FutureMsgDaoService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FutureMsgDaoService.class);
	
	public void saveFutureMsg(FutureMsg msg)
	{
		SqlSession session=null;
		IFutureMsgDao futureMsgDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			futureMsgDao = session.getMapper(IFutureMsgDao.class);   
			if(msg.getId()==null)
		    {
				futureMsgDao.saveFutureMsg(msg);
		    }else
		    {
		    	futureMsgDao.updateFutureMsg(msg);
		    }
		}catch(Exception e)
		{
			LOGGER.error("保存事件消息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return ;
	}
	
	public void deleteFutureMsg(FutureMsg msg)
	{
		if(msg==null || msg.getId()==null)
	    {
			return;
	    }
		SqlSession session=null;
		IFutureMsgDao futureMsgDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			futureMsgDao = session.getMapper(IFutureMsgDao.class);   
			futureMsgDao.deleteFutureMsg(msg);
		}catch(Exception e)
		{
			LOGGER.error("删除事件消息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return ;
	}
	
	public List<FutureMsg> queryAllFutureMsg()
	{
		List<FutureMsg> result=null;
		SqlSession session=null;
		IFutureMsgDao futureMsgDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			futureMsgDao = session.getMapper(IFutureMsgDao.class);   
			result=futureMsgDao.queryAllFutureMsg();
		    
		}catch(Exception e)
		{
			LOGGER.error("查询事件消息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		} 
		return result;
	}
}
