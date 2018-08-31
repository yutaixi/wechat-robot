package com.im.db.sqlite;

import java.sql.Connection;

/**
 * 连接池对象容器
 * 
 * @author Allen
 * @date 2016年10月31日
 *
 */
public class SqlitePojo {

    private long createTime;// 时间戳
    private Connection con;// 链接对象

    public SqlitePojo() {
        // TODO Auto-generated constructor stub
    }

    public SqlitePojo(long createTime, Connection con) {
        this.createTime = createTime;
        this.con = con;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

}