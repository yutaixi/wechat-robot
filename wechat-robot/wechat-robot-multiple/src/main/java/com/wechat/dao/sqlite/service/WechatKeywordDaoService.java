package com.wechat.dao.sqlite.service;
 
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  
import com.im.db.mybatis.SessionFactory; 
import com.subscription.KeywordVO;
import com.wechat.dao.sqlite.dao.IWechatKeywordDao; 

public class WechatKeywordDaoService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WechatKeywordDaoService.class);
	
	public void saveKeyword(KeywordVO keywordVO)
	{
		SqlSession session=null;
		IWechatKeywordDao wechatKeywordDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			wechatKeywordDao = session.getMapper(IWechatKeywordDao.class);   
			if(keywordVO.getId()==null)
		    {
				wechatKeywordDao.saveKeyword(keywordVO);
		    }else
		    {
		    	wechatKeywordDao.updateKeyword(keywordVO);
		    }
		}catch(Exception e)
		{
			LOGGER.error("保存关键词异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return ;
	}
	
	public void deleteKeyword(KeywordVO keyword)
	{
		if(keyword==null || keyword.getId()==null)
	    {
			return;
	    }
		SqlSession session=null;
		IWechatKeywordDao wechatKeywordDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			wechatKeywordDao = session.getMapper(IWechatKeywordDao.class);   
			wechatKeywordDao.deleteKeyword(keyword);
		}catch(Exception e)
		{
			LOGGER.error("删除关键词异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return ;
	}
	
	public List<KeywordVO> queryAllKeyword()
	{
		List<KeywordVO> result=null;
		SqlSession session=null;
		IWechatKeywordDao wechatKeywordDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			wechatKeywordDao = session.getMapper(IWechatKeywordDao.class);   
			result=wechatKeywordDao.queryAllKeyword();
		    
		}catch(Exception e)
		{
			LOGGER.error("查询关键词异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		} 
		return result;
	}
}
