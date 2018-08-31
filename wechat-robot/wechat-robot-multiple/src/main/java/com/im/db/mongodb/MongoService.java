package com.im.db.mongodb;

import java.util.Arrays; 
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public  class MongoService {

	private static  MongoClient mongoClient=null;
	
	private static   String mongoServer="localhost";
	private static   int mongoServerPort=27017;
	private static  String userName="weixin";
	private static  String password="weixin";
	private static  String database="weixin";
	
	public static MongoClient getInstance()
	{ 
		 if(mongoClient==null )
		 {
			 MongoCredential credential = MongoCredential.createCredential(userName, database, password.toCharArray());
			 mongoClient = new MongoClient(new ServerAddress(mongoServer, mongoServerPort), Arrays.asList(credential));
		 }
	    return mongoClient;
	   
	}
	 
}
