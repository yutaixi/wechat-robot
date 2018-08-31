package com.wechat.module; 
import java.util.List; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import com.wechat.action.WechatChatroomDelMemberAction;
import com.wechat.action.WechatChatroomModTopicAction; 
import iqq.im.QQActionListener;  
import iqq.im.event.QQActionFuture; 
import iqq.im.module.AbstractModule; 

public class WechatGroupModule extends AbstractModule {
	private static final Logger LOGGER = LoggerFactory.getLogger(WechatGroupModule.class);  
	 
	public QQActionFuture chatroomModTopic(String newTopic,String chatroomName,QQActionListener listener) {
		return pushHttpAction(new WechatChatroomModTopicAction(newTopic,chatroomName,getContext(), listener));
	}
	
	public QQActionFuture chatroomDelMember(String chatroomName,List<String> delUserList,QQActionListener listener) {
		return pushHttpAction(new WechatChatroomDelMemberAction(chatroomName,delUserList,getContext(), listener));
	}
	
}
