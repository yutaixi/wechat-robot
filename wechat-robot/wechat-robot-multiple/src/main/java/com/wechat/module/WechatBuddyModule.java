package com.wechat.module;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blade.kit.json.JSONObject;
import com.im.base.wechat.WechatContact;
import com.im.utils.StringHelper;
import com.wechat.action.WechatBatchGetContactAction;
import com.wechat.action.WechatChatroomDelMemberAction;
import com.wechat.action.WechatChatroomModTopicAction;
import com.wechat.action.WechatGetContactAction; 
import com.wechat.action.WechatOplogAction;
import com.wechat.action.WechatVerifyUserAction;
import com.wechat.bean.OplogCmd;

import iqq.im.QQActionListener; 
import iqq.im.core.QQModule;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQActionFuture;
import iqq.im.event.future.ProcActionFuture;
import iqq.im.module.AbstractModule; 

public class WechatBuddyModule extends AbstractModule {
	private static final Logger LOGGER = LoggerFactory.getLogger(WechatBuddyModule.class);  
	/**
	 * <p>getCategoryList.</p>
	 *
	 * @param listener a {@link iqq.im.QQActionListener} object.
	 * @return a {@link iqq.im.event.QQActionFuture} object.
	 */
	public QQActionFuture getBuddyList(QQActionListener listener) {
		return pushHttpAction(new WechatGetContactAction(getContext(), listener));
	}
	
	public QQActionFuture batchGetContact(QQActionListener listener,Map<String,WechatContact> contacts) {
		return pushHttpAction(new WechatBatchGetContactAction(getContext(), listener,contacts));
	}
	public QQActionFuture getContact(QQActionListener listener,WechatContact user) {
		Map<String,WechatContact> contacts=new HashMap<String,WechatContact>();
		contacts.put(user.getUserName(), user);
		return pushHttpAction(new WechatBatchGetContactAction(getContext(), listener,contacts));
	}
	
	public QQActionFuture modRemark(WechatContact user,String remarkName,QQActionListener listener) {
		return pushHttpAction(new WechatOplogAction(user,OplogCmd.MODREMARKNAME,remarkName,getContext(), listener));
	}
	 
	
	public QQActionFuture batchModRemark(Map<String, WechatContact> contact,QQActionListener listener) {
		final ProcActionFuture future = new ProcActionFuture(listener, true); 
		if(contact==null || contact.isEmpty())
		{
			future.notifyActionEvent(QQActionEvent.Type.EVT_OK, null);
			return future;
		}
		Collection<WechatContact> contacts=contact.values();
		final WechatLoginModule login = getContext().getModule(QQModule.Type.LOGIN);
 
		for( WechatContact temp :contacts)
		{
			//if(!StringHelper.isEmpty(temp.getRemarkName()) || !StringHelper.isEmpty(temp.getAlias()))
			if(!StringHelper.isEmpty(temp.getRemarkName()))
			{
				continue;
			}
			pushHttpAction(new WechatOplogAction(temp,OplogCmd.MODREMARKNAME,temp.getNickName(),getContext(), new QQActionListener(){

				@Override
				public void onActionEvent(QQActionEvent event) {
					if(event.getType()==QQActionEvent.Type.EVT_OK)
					{
						 
						login.pollMsg(null);
						WechatContact user=(WechatContact)event.getTarget(); 
						//getContact(null,user);
						LOGGER.info(user.getNickName()+":"+user.getAlias()+"-remark to:"+user.getRemarkName());
						
					}else if(event.getType()==QQActionEvent.Type.EVT_ERROR)
					{
						 
						LOGGER.error("mod remark failed:"+event.getTarget());
					}
					
				}
				
			}));
			 
		}
		future.notifyActionEvent(QQActionEvent.Type.EVT_OK, null);
		return future;
	}
	
	public QQActionFuture verifyUser(String user,String verifyContent,int opCode,String ticket,QQActionListener listener) {
		return pushHttpAction(new WechatVerifyUserAction(user,verifyContent,opCode,ticket,getContext(), listener));
	}
	
	public QQActionFuture batchVerifyUser(List<WechatContact> contact,String verifyContent,QQActionListener listener) { 
		final ProcActionFuture future = new ProcActionFuture(listener, true); 
		if(contact==null || contact.isEmpty())
		{
			future.notifyActionEvent(QQActionEvent.Type.EVT_OK, null);
			return future;
		}
		for(WechatContact temp : contact )
		{
			pushHttpAction(new WechatVerifyUserAction(temp.getUserName(),verifyContent,WechatVerifyUserAction.addFriend,null,getContext(), new QQActionListener(){

				@Override
				public void onActionEvent(QQActionEvent event) {
					if(event.getType()==QQActionEvent.Type.EVT_OK)
					{
						
					}else if(event.getType()==QQActionEvent.Type.EVT_OK)
					{
						LOGGER.error("Verify User Failed:"+event.getTarget());
					} 
				}
				
			}));
		}
		future.notifyActionEvent(QQActionEvent.Type.EVT_OK, null);
		return future;
	}
	
	public QQActionFuture chatroomModTopic(String newTopic,String chatroomName,QQActionListener listener) {
		return pushHttpAction(new WechatChatroomModTopicAction(newTopic,chatroomName,getContext(), listener));
	}
	
	public QQActionFuture chatroomDelMember(String chatroomName,List<String> delUserList,QQActionListener listener) {
		return pushHttpAction(new WechatChatroomDelMemberAction(chatroomName,delUserList,getContext(), listener));
	}
	
}
