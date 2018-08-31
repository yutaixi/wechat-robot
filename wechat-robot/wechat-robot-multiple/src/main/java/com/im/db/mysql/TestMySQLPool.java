package com.im.db.mysql;

/**
 * 测试类
 */ 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 
public class TestMySQLPool {
    private static volatile int a;
 
    private synchronized static void incr() {
        a++;
    }
 
    public static void main(String[] args) throws InterruptedException {
        int times = 10;
        long start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            new Thread(new Runnable() {
 
                @Override
                public void run() {
 
                    MySQLPool pool = MySQLPool.getInstance();
                    Connection conn = pool.getConnection();
                    PreparedStatement  preparedStm=null;
                    ResultSet rs = null;
                    try {
                    	String sql="select id, user_name from weixin.wx_user";
                    	preparedStm = conn.prepareStatement(sql);
                        rs = preparedStm.executeQuery();
                        while (rs.next()) {
                            System.out.println(rs.getInt(1) + ", "
                                    + rs.getString(2));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        incr();
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
            }).start();
        }
        while (true) {
            if (a == times) {
                System.out.println("finished, time:"
                        + (System.currentTimeMillis() - start));
                break;
            }
            Thread.sleep(100);
        }
    }
}
 