package com.im.db.mybatis;

import java.io.IOException;
import java.io.Reader; 

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
 

public class SessionFactory {

	private static final String resource = "MyBatis-Configuration.sqlite.xml";
	private static SqlSessionFactory factory;
	
	private static  class ReaderFactory
	{
		public static Reader reader;
		static{
			try {
				reader=Resources.getResourceAsReader(resource);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static SqlSessionFactory getInstance()
	{
		if(factory==null)
		{
			 
		    SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
		    factory = builder.build(ReaderFactory.reader); 
		    
		}
		return factory;
	}
	
	 
	
	public static void closeSession(SqlSession session)
	{
		if(session!=null)
		{
			session.commit();
			session.close();
		}
	}
	
	 
}
