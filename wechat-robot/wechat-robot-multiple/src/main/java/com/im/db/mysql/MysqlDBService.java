package com.im.db.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlDBService {

	public void executeQuery(String sql)
	{
		 MySQLPool pool = MySQLPool.getInstance();
         Connection conn = pool.getConnection();
         PreparedStatement  preparedStm=null;
         ResultSet rs = null;
         try { 
         	preparedStm = conn.prepareStatement(sql);
             rs = preparedStm.executeQuery();
             while (rs.next()) {
                 System.out.println(rs.getInt(1) + ", "
                         + rs.getString(2)); 
             }
         } catch (SQLException e) {
             e.printStackTrace();
         } finally { 
             if (rs != null) {
                 try {
                     rs.close();
                 } catch (SQLException e) {
                     e.printStackTrace();
                 }
             }
             if (preparedStm != null) {
                 try {
                 	preparedStm.close();
                 } catch (SQLException e) {
                 }
             }
             pool.releaseConnection(conn);
         }
     } 
	
}
