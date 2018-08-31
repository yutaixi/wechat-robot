package com.wechat.dao.sqlite.dao; 
import java.util.List; 
import org.apache.ibatis.annotations.Param; 
import com.im.base.wechat.FutureMsg; 

public interface IFutureMsgDao {

public void saveFutureMsg(@Param("msg")FutureMsg msg);
	
	public void updateFutureMsg(@Param("msg")FutureMsg msg);
	
	public void deleteFutureMsg(@Param("msg")FutureMsg msg);
	
	public List<FutureMsg> queryAllFutureMsg();
	
}
