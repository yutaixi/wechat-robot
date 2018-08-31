package com.wechat.dao.sqlite.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.base.vo.MediaFileSearchVO;
import com.im.base.vo.MediaFileVO;
import com.im.base.wechat.WechatMsg;
import com.im.db.mybatis.SessionFactory;
import com.wechat.dao.sqlite.dao.IMediaFileDao;
import com.wechat.dao.sqlite.dao.IWelcomeMsgDao;

public class MediaFileDaoService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MediaFileDaoService.class);
	
	public void save(MediaFileVO file)
	{
		SqlSession session=null;
		IMediaFileDao mediaFileDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			mediaFileDao = session.getMapper(IMediaFileDao.class);   
			mediaFileDao.save(file);
		}catch(Exception e)
		{
			LOGGER.error("保存媒体文件信息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return ;
	}
	
	public MediaFileVO find(String md5)
	{
		MediaFileVO result=null;
		SqlSession session=null;
		IMediaFileDao mediaFileDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			mediaFileDao = session.getMapper(IMediaFileDao.class);   
			result=mediaFileDao.find(md5);
		}catch(Exception e)
		{
			LOGGER.error("查询媒体文件信息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return result;
	}
	
	public List<MediaFileVO> query(MediaFileSearchVO searchVO)
	{
		List<MediaFileVO>  result=null;
		SqlSession session=null;
		IMediaFileDao mediaFileDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			mediaFileDao = session.getMapper(IMediaFileDao.class);   
			result=mediaFileDao.query(searchVO);
		}catch(Exception e)
		{
			LOGGER.error("query查询媒体文件信息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return result;
	}
}
