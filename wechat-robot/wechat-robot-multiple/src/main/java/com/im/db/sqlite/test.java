package com.im.db.sqlite;

import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

/**
 * 池回收线程
 * 
 * @author Allen
 * @date 2016年10月31日
 *
 */
final class Recovery extends SqliteFactory implements Runnable {
    public Recovery() {
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                //print("INFO:[可用链接数:", sList.size(), "]", "[已用连接数:", sRunList.size(), "]");
                Vector<SqlitePojo> list = sRunList;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getCon().isClosed() || new Date().getTime() - list.get(i).getCreateTime() > timeout) {
                        changeRunList(list, 1, i);
                        if (list.size() >= i)
                            --i;
                    }
                }
            } catch (InterruptedException e) {
                sList.clear();
                sRunList.clear();
                print("ERROR:[池回收线程死掉]");
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ExecuteTypeIsException e) {
                e.printStackTrace();
            }
        }
    }
}