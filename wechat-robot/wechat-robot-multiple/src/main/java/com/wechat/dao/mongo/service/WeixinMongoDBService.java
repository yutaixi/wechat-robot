package com.wechat.dao.mongo.service;
  
import com.im.base.vo.ImMsg;
import com.im.base.wechat.WechatMsg;
import com.im.db.mongodb.WeixinDb;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.wechat.dao.service.IWechatMsgDaoService;
 

public class WeixinMongoDBService implements IWechatMsgDaoService{

	public  void saveMsg(WechatMsg wechatMsg)
	{
		DBCollection coll=WeixinDb.getCollection("weixinMsg");
		if(coll==null)
		{
			return;
		}
		 
		BasicDBObject doc = 
				new BasicDBObject("MsgId", wechatMsg.getMsgId())
		.append("MsgType",wechatMsg.getMsgType() )
		.append("FromUserName",wechatMsg.getFromUserName())
		.append("ToUserName", wechatMsg.getToUserName())
		.append("Content",wechatMsg.getContent() )
		.append("FileName", wechatMsg.getFileName())
		.append("Url", wechatMsg.getUrl())
		.append("From", wechatMsg.getFrom())
		.append("To", wechatMsg.getTo())
		.append("ImType", wechatMsg.getType());
		coll.insert(doc);
		
	}
	
}
