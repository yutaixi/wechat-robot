package com.wechat.dao.sqlite.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.im.base.customize.EnglishSignUpVO; 
import com.im.db.mybatis.SessionFactory;
import com.wechat.dao.sqlite.dao.IEnglishSignUpDao; 

public class EnglishSignUpDaoService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EnglishSignUpDaoService.class);
	
 
	public void save(EnglishSignUpVO signUpVO)
	{
		SqlSession session=null;
		IEnglishSignUpDao englishSignUpDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			englishSignUpDao = session.getMapper(IEnglishSignUpDao.class);   
			englishSignUpDao.save(signUpVO);
		}catch(Exception e)
		{
			LOGGER.error("保存报名信息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return ;
	}
	
	public List<EnglishSignUpVO> queryAll()
	{
		List<EnglishSignUpVO> result=null;
		SqlSession session=null;
		IEnglishSignUpDao englishSignUpDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			englishSignUpDao = session.getMapper(IEnglishSignUpDao.class);   
			result=englishSignUpDao.queryAll();
		}catch(Exception e)
		{
			LOGGER.error("查询报名信息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return result;
	}
}
