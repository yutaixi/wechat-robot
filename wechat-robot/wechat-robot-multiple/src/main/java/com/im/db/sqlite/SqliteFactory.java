package com.im.db.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 

/**
 * sqlite连接池
 * 
 * @author Allen
 * @date 2016-10-31
 *
 */
public class SqliteFactory {
    private static Log logger = LogFactory.getLog("interceptorLog");
    private static int max = 200;// 最大池链接
    private static int min = 10;// 初始池链接
    private static int step = 10;// 每次最大补充线程数量
    private static String dbPath;// 数据库链接路径
    private static String timeFlag;// 日期时间戳
    private static String path;
    
    protected static long timeout = 10000;// 超时线程回收
    protected static Vector<SqlitePojo> sList = new Vector<SqlitePojo>();// 链接容器
    protected static Vector<SqlitePojo> sRunList = new Vector<SqlitePojo>();// 已分配的链接容器
    static {
        ini();
        new Thread(new Recovery()).start();
    }

    /**
     * sqlite对象获取异常
     * 
     * @author Allen
     * @date 2016年10月31日
     *
     */
    final class SqlitePojoException extends Exception {
        private static final long serialVersionUID = 1L;

        @Override
        public void printStackTrace() {
            print("ERROR:[池对象创建异常]");
            super.printStackTrace();
        }
    }

    /**
     * 池溢出
     * 
     * @author Allen
     * @date 2016年10月31日
     *
     */
    final class SqliteMaxException extends Exception {
        private static final long serialVersionUID = -3970424418466308414L;

        @Override
        public void printStackTrace() {
            print("ERROR:[连接池总数超过最大设置 max:", max);
            super.printStackTrace();
        }
    }

    /**
     * 对已分配容器的操作类型不存在
     * 
     * @author Allen
     * @date 2016年10月31日
     *
     */
    final class ExecuteTypeIsException extends Exception {
        private static final long serialVersionUID = -3970424418466308414L;

        @Override
        public void printStackTrace() {
            print("ERROR:[对已分配容器的操作类型不存在]");
            super.printStackTrace();
        }
    }

    /**
     * 初始化连接池
     * 
     * @author Allen
     * @throws SQLException
     * @date 2016年10月31日
     */
    private static void ini() {
        Connection con = null;
        Statement stmt = null;
        try {
            setClass();
            if (timeFlag == null) {
                // 启动后为时间戳设置一个当前日期
                timeFlag = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                //String path = new StringBuffer(PropertiesUtils.sourceMap.get("sqlite.dbpath")).append(PropertiesUtils.sourceMap.get("sqlite.dbfile").replace("{?}", timeFlag)).toString();
                dbPath = path;
                createDb(con, stmt);
            }
            addD(min);
        } catch (Exception e) {
            sList.clear();
            print("ERROR:[池初始化失败][池容器已清空]");
            e.printStackTrace();
        } finally {
            try {
                if (con != null)
                    con.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                print("ERROR:[池初始化失败][池容器已清空]");
                sList.clear();
                e.printStackTrace();
            }
        }

    }

    /**
     * 设置驱动
     * 
     * @throws ClassNotFoundException
     * @author Allen
     * @date 2016年10月31日
     */
    private static void setClass() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
    }

    /**
     * 创建一个新的db文件并为其创建数据库
     * 
     * @param con
     * @param stmt
     * @param path
     * @author Allen
     * @throws SQLException
     * @date 2016年10月31日
     */
    private static void createDb(Connection con, Statement stmt) throws SQLException {
        File f = new File(dbPath);
        // 如果文件不存在则创建
        // 创建新的db文件并新建表
        if (!f.isFile()) {
            con = DriverManager.getConnection(new StringBuffer("jdbc:sqlite:").append(dbPath).toString());
            stmt = con.createStatement();
            String sql = "CREATE TABLE log ("+
                         "id INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL,"+
                         "action_userId   INT           NOT NULL,"+
                         "action_loginId  VARCHAR (100) NOT NULL,"+
                         "action_userName VARCHAR (20)  NOT NULL,"+
                         "action_name     VARCHAR (30)  NOT NULL,"+
                         "action_param    TEXT          NOT NULL,"+
                         "action_result   VARCHAR (20)  NOT NULL,"+
                         "action_time     TIME (25)     NOT NULL DEFAULT (datetime('now', 'localtime') ),"+
                         "action_ip       VARCHAR (20),"+
                         "action_role     INTEGER (2) "+
                         ");";
            stmt.executeUpdate(sql);
        }
    }

    /**
     * 获取链接
     * 
     * @return
     * @author Allen
     * @throws Exception
     * @throws SQLException
     * @throws SqliteMaxException
     * @throws ExecuteTypeIsException
     * @date 2016年10月31日
     */
    public Connection getCon() throws SQLException, SqlitePojoException, SqliteMaxException, ExecuteTypeIsException {
        /**
         * 先进先出原则
         */
        SqlitePojo sPojo = null;
        checkLogDay();
        synchronized (sList) {
            checkList();
            sPojo = sList.get(0);
            sList.remove(0);
            changeRunList(sRunList, 0, sPojo);
        }
        if (sPojo == null || sPojo.getCon().isClosed())
            throw new SqlitePojoException();
        return sPojo.getCon();
    }

    /**
     * 同步对已分配容器进行操作
     * 
     * @param list
     * @param type
     *            0 add 1 remove
     * @param Object
     *            ob
     * @throws ExecuteTypeIsException
     * @author Allen
     * @date 2016年10月31日
     */
    protected synchronized void changeRunList(Vector<SqlitePojo> list, int type, Object... ob) throws ExecuteTypeIsException {
        switch (type) {
        case 0:
            // add
            list.add((SqlitePojo) ob[0]);
            break;
        case 1:
            // del
            list.remove(Integer.parseInt(ob[0].toString()));
            break;
        default:
            throw new ExecuteTypeIsException();
        }
    }

    /**
     * 检查容器是否满足使用需求
     * 
     * @throws SqliteMaxException
     * @author Allen
     * @throws SQLException
     * @date 2016年10月31日
     */
    private void checkList() throws SqliteMaxException, SQLException {
        if (sList.size() == 0) {
            // 超过最大线程数
            int num = 0;
            if ((num = max - sRunList.size() - sList.size()) <= 0)
                throw new SqliteMaxException();
            // 剩余可增加线程数
            num = num > step ? step : num;
            addD(num);
        }
    }

    /**
     * 检查日志日期
     * 
     * @author Allen
     * @throws SQLException
     * @date 2016年10月31日
     */
    private synchronized static void checkLogDay() {
        String tempTime = null;
        Connection con = null;
        Statement stmt = null;
        try {
            if (!timeFlag.equals((tempTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date())))) {
                // 时间戳已过期
                timeFlag = tempTime;
               // dbPath = new StringBuffer(PropertiesUtils.sourceMap.get("sqlite.dbpath")).append(PropertiesUtils.sourceMap.get("sqlite.dbfile").replace("{?}", timeFlag)).toString();
                createDb(con, stmt);
                for (int i = 0; i < sList.size(); i++) {
                    // 更新历史时间的list
                    sList.get(i).setCon(DriverManager.getConnection(new StringBuffer("jdbc:sqlite:").append(dbPath).toString()));
                }
            }
        } catch (Exception e) {
            print("ERROR:[新日期检查更新失败][池容器已清空]");
            e.printStackTrace();
        } finally {
            try {
                if (con != null)
                    con.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 添加新的链接到容器
     * 
     * @param num
     * @throws SQLException
     * @author Allen
     * @date 2016年10月31日
     */
    private static void addD(int num) throws SQLException {
        for (int i = 0; i < num; i++) {
            checkLogDay();
            sList.add(new SqlitePojo(new Date().getTime(), DriverManager.getConnection(new StringBuffer("jdbc:sqlite:").append(dbPath).toString())));
        }
    }

    /**
     * 字符串组装
     * 
     * @param str
     * @return
     * @author Allen
     * @date 2016年10月31日
     */
    protected static void print(Object... ob) {
        StringBuffer sbf = new StringBuffer();
        for (Object temp : ob)
            sbf.append(temp);
        logger.info(sbf.toString());
    }

    public static void main(String[] args) {
        try {
            while (true) {
                Scanner sc = new Scanner(System.in);
                if (sc.next() != null) {
                    long s = System.currentTimeMillis();
                    new SqliteFactory().getCon();
                    System.out.println(System.currentTimeMillis() - s + "ms");

                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}