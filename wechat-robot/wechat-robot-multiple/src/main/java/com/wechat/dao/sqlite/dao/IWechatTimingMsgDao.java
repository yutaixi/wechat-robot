package com.wechat.dao.sqlite.dao; 
import java.util.List; 
import org.apache.ibatis.annotations.Param; 
import com.im.base.schedule.WechatTimingMsgVO; 

public interface IWechatTimingMsgDao {

	public void saveTimingMsg(@Param("msg")WechatTimingMsgVO msg);
	
    public void updateTimingMsg(@Param("msg")WechatTimingMsgVO msg);
	
	public void deleteTimingMsg(@Param("msg")WechatTimingMsgVO msg);
	
	public List<WechatTimingMsgVO> queryAllTimingMsg();
}
