package com.wechat.dao.mysql.service; 
import org.apache.ibatis.session.SqlSession; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.base.wechat.WechatMsg;
import com.im.db.mybatis.SessionFactory;
import com.wechat.dao.mysql.dao.IWechatMsgDao;
import com.wechat.dao.service.IWechatMsgDaoService;
import com.wechat.service.WechatEventHandler;

public class WechatMsgDaoService implements IWechatMsgDaoService{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WechatMsgDaoService.class);
	
	public void saveMsg(WechatMsg msg)
	{ 
		 SqlSession session=null;
		 IWechatMsgDao msgDao=null;
		try{
			session = SessionFactory.getInstance().openSession();
		    msgDao = session.getMapper(IWechatMsgDao.class); 
			msgDao.saveMsg(msg);
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
