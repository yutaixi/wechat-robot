package iqq.im.dao;

import java.io.Reader; 
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.im.base.vo.ImMsg;
import com.im.base.wechat.WechatMsg;
import com.im.db.mybatis.SessionFactory;
import com.subscription.KeywordVO;
import com.wechat.dao.mysql.dao.IUserDao;
import com.wechat.dao.mysql.dao.IWechatMsgDao;
import com.wechat.dao.mysql.service.SubscriptionDaoService;

public class MyBatisTest {
    
    @Test
    public void msgDaoTest() throws Exception
    {
    	 
        SqlSession session = SessionFactory.getInstance().openSession();
        IWechatMsgDao msgDao = session.getMapper(IWechatMsgDao.class); 
        WechatMsg wechatMsg=new WechatMsg();
        wechatMsg.setMediaId("MediaId");
        wechatMsg.setMsgId("MsgId");
        wechatMsg.setIM("from", "to", "test");
        msgDao.saveMsg(wechatMsg); 
        SessionFactory.closeSession(session);
    }
    @Test
    public void TestKeywordDao()
    {
    	SubscriptionDaoService subscriptionDaoService=new SubscriptionDaoService();
    	
    	List<KeywordVO> keywords=new ArrayList<KeywordVO>();
    	KeywordVO key1=new KeywordVO();
    	key1.setKeyword("test32");
    	keywords.add(key1);
    	
    	KeywordVO key2=new KeywordVO();
    	key2.setKeyword("test42");
    	keywords.add(key2);
    	subscriptionDaoService.saveKeywords(keywords);
    	for(KeywordVO temp : keywords)
    	{
    		System.out.println(temp.getId());
    	}
    }
}
