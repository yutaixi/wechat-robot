package com.wechat.dao.sqlite.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.db.mybatis.SessionFactory;

public class DBService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WechatContactDaoService.class);
	
	public static void runScript()
	{
		 SqlSession session=SessionFactory.getInstance().openSession();
		 Connection conn =session.getConnection();
		 ScriptRunner runner = new ScriptRunner(conn);
		 Resources.setCharset(Charset.forName("GBK")); //设置字符集,不然中文乱码插入错误
		 runner.setLogWriter(null);//设置是否输出日志
		 
		 try {
			runner.runScript(Resources.getResourceAsReader("wx_im_msg.sql"));
			
			runner.closeConnection();
			conn.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			 SessionFactory.closeSession(session);
		}
		 
		 
         
        
	}

}
