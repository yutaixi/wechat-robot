package com.im.db.mongodb;

import com.mongodb.DB;
import com.mongodb.DBCollection;

public class WeixinDb {

	public static DB getDb()
	{
		 return MongoService.getInstance().getDB("weixin");
	} 
	
	public static DBCollection getCollection(String collectionName)
	{
		if(collectionName==null)
		{
			return null;
		}
		 return MongoService.getInstance().getDB("weixin").getCollection(collectionName);
	}
}
