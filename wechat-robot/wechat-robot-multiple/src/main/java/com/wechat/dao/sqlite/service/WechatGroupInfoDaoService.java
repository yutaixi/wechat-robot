package com.wechat.dao.sqlite.service;

import java.util.List; 
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import com.im.base.wechat.WechatContact;
import com.im.db.mybatis.SessionFactory; 
import com.wechat.dao.sqlite.dao.IGroupInfoDao;

public class WechatGroupInfoDaoService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WechatGroupInfoDaoService.class);
	
	public void save(WechatContact group) {
		SqlSession session=null;
		IGroupInfoDao groupInfoDao=null; 
		 
		try{
			session = SessionFactory.getInstance().openSession();
			groupInfoDao = session.getMapper(IGroupInfoDao.class); 
			WechatContact contact=groupInfoDao.findGroupByName(group.getNickName());
			if(contact!=null)
			{
				group.setId(contact.getId());
				groupInfoDao.update(group);
			}else
			{
				groupInfoDao.save(group);
			}
			
		}catch(Exception e)
		{
			LOGGER.error("保存群组信息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return ;
	}
	
	public List<WechatContact> query()
	{
		List<WechatContact> result=null;
		SqlSession session=null;
		IGroupInfoDao groupInfoDao=null; 
		try{
			session = SessionFactory.getInstance().openSession();
			groupInfoDao = session.getMapper(IGroupInfoDao.class); 
			result=groupInfoDao.query(); 
		}catch(Exception e)
		{
			LOGGER.error("获取群组信息异常"+e);
		}finally
		{
			SessionFactory.closeSession(session);
		}
		return result;
	}
	
}
