package com.wechat;
  
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Project  : WebQQCore
 * Package  : iqq.im
 * File     : WebQQClient.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2012-8-1
 * License  : Apache License 2.0
 */ 
import iqq.im.QQActionListener; 
import iqq.im.QQException;
import iqq.im.QQNotifyListener;
import iqq.im.actor.QQActor;
import iqq.im.actor.QQActorDispatcher;
import iqq.im.bean.*; 
import iqq.im.core.*;   
import iqq.im.event.QQNotifyEvent;
import iqq.im.event.QQNotifyEvent.Type;  
import iqq.im.service.ApacheHttpService;   

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  

import com.blade.kit.json.JSONObject;
import com.im.base.wechat.WechatContact;
import com.im.base.wechat.WechatMsg;
import com.im.ui.QRCodeFrame;
import com.im.utils.LogUtil;
import com.wechat.action.WechatVerifyUserAction;
import com.wechat.core.WechatContext;  
import com.wechat.core.WechatStore;
import com.wechat.module.WechatBuddyModule;
import com.wechat.module.WechatChatModule;
import com.wechat.module.WechatGroupModule;
import com.wechat.module.WechatLoginModule;
import com.wechat.module.WechatProcModule;
import com.wechat.service.IWechatEventHandler;
import com.wechat.service.WechatEventHandler; 

import java.util.HashMap; 
import java.util.List;
import java.util.Map;

/**
 * WebQQ客户的实现
 *
 * @author solosky
 */
public class WebWechatClient implements WechatClient, WechatContext {
    private static final Logger LOG = LoggerFactory.getLogger(WebWechatClient.class);
    private Map<QQService.Type, QQService> services;
    private Map<QQModule.Type, QQModule> modules;
    private QQActorDispatcher actorDispatcher;
    private WechatConfig wechatConfig;
    private QQAccount account;
    private QQSession session;
    private QQStore qqStore;
    private WechatStore store;
    private LogUtil logUtil;
    private QQNotifyListener notifyListener;  
    private QRCodeFrame qrCodeFrame;
    public WebWechatClient(QQNotifyListener notifyListener, QQActorDispatcher actorDispatcher) {
        this("", "", notifyListener, actorDispatcher,null,null);
        System.setProperty("https.protocols", "TLSv1");
		System.setProperty("jsse.enableSNIExtension", "false");
    }
    
    public WebWechatClient(QQNotifyListener notifyListener, QQActorDispatcher actorDispatcher,LogUtil logUtil) {
        this("", "", notifyListener, actorDispatcher,logUtil,null); 
        System.setProperty("https.protocols", "TLSv1");
		System.setProperty("jsse.enableSNIExtension", "false");
    }
    
    public WebWechatClient(QQNotifyListener notifyListener, QQActorDispatcher actorDispatcher,LogUtil logUtil,WechatEventHandler eventHandler) {
        this("", "", notifyListener, actorDispatcher,logUtil,eventHandler); 
        System.setProperty("https.protocols", "TLSv1");
		System.setProperty("jsse.enableSNIExtension", "false");
    }
      
    /**
     * 构造方法，初始化模块和服务
     * 账号/密码    监听器     线程执行器
     *
     * @param username        a {@link java.lang.String} object.
     * @param password        a {@link java.lang.String} object.
     * @param notifyListener  a {@link iqq.im.QQNotifyListener} object.
     * @param actorDispatcher a {@link iqq.im.actor.QQActorDispatcher} object.
     */
    public WebWechatClient(String username, String password,
                       QQNotifyListener notifyListener, QQActorDispatcher actorDispatcher,LogUtil logUtil,
                       WechatEventHandler eventHandler) {
        this.modules = new HashMap<QQModule.Type, QQModule>();
        this.services = new HashMap<QQService.Type, QQService>();

        this.modules.put(QQModule.Type.LOGIN, new WechatLoginModule());
        this.modules.put(QQModule.Type.PROC, new WechatProcModule());
        this.modules.put(QQModule.Type.CHAT, new WechatChatModule());
        this.modules.put(QQModule.Type.BUDDY, new WechatBuddyModule()); 
        this.modules.put(QQModule.Type.GROUP, new WechatGroupModule()); 
        
        this.services.put(QQService.Type.HTTP, new ApacheHttpService());
        this.services.put(QQService.Type.EVENTHANDLER, eventHandler==null?new WechatEventHandler():eventHandler);
        
        this.account = new QQAccount();
        this.account.setUsername(username);
        this.account.setPassword(password);
        this.session = new QQSession();
        this.qqStore = new QQStore();
        this.store=new WechatStore();
        
        this.logUtil=logUtil; 
        this.notifyListener = notifyListener;
        this.actorDispatcher = actorDispatcher;

        this.init();
    }

    /**
     * {@inheritDoc}
     * <p/>
     * 获取某个类型的模块，QQModule.Type
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends QQModule> T getModule(QQModule.Type type) {
        return (T) modules.get(type);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * 获取某个类型的服务，QQService.Type
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends QQService> T getSerivce(QQService.Type type) {
        return (T) services.get(type);
    }

     @Override 
    public QRCodeFrame getQrCodeFrame() {
		return qrCodeFrame;
	}
     @Override 
	public void setQrCodeFrame(QRCodeFrame qrCodeFrame) {
		this.qrCodeFrame = qrCodeFrame;
	}

	/**
     * {@inheritDoc}
     * <p/>
     * 获取自己的账号实体
     */
    @Override
    public QQAccount getAccount() {
        return account;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * 获取QQ存储信息，包括获取过后的好友/群好友
     * 还有一些其它的认证信息
     */
    @Override
    public QQStore getStore() {
        return qqStore;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * 放入一个QQActor到队列，将会在线程执行器里面执行
     */
    @Override
    public void pushActor(QQActor actor) {
        actorDispatcher.pushActor(actor);
    }

    /**
     * 初始化所有模块和服务
     */
    private void init() {
        try {
            for (QQService.Type type : services.keySet()) {
                QQService service = services.get(type);
                service.init(this);
            }

            for (QQModule.Type type : modules.keySet()) {
                QQModule module = modules.get(type);
                module.init(this);
            }

            actorDispatcher.init(this);
            qqStore.init(this);
            store.init(this);
        } catch (QQException e) {
            LOG.warn("init error:", e);
        }
    }

    /**
     * 销毁所有模块和服务
     */
    public void destroy() {
        try {
            for (QQModule.Type type : modules.keySet()) {
                QQModule module = modules.get(type);
                module.destroy();
            }

            for (QQService.Type type : services.keySet()) {
                QQService service = services.get(type);
                service.destroy();
            }

            actorDispatcher.destroy();
            qqStore.destroy();
            store.destroy();
        } catch (QQException e) {
            LOG.warn("destroy error:", e);
        }
    }

     

    

    /**
     * <p>getCaptcha.</p>
     *
     * @param listener a {@link iqq.im.QQActionListener} object.
     */
     

    /**
     * {@inheritDoc}
     * <p/>
     * 获取会话信息
     */
    @Override
    public QQSession getSession() {
        return session;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * 通知事件
     */
    @Override
    public void fireNotify(QQNotifyEvent event) {
        if (notifyListener != null) {
            try {
                notifyListener.onNotifyEvent(event);
            } catch (Throwable e) {
                LOG.warn("fireNotify Error!!", e);
            }
        }
        // 重新登录成功，重新poll
        if (event.getType() == Type.RELOGIN_SUCCESS) {
            beginPollMsg();
        }
    }

    /**
     * {@inheritDoc}
     * <p/>
     * 轮询QQ消息
     */
    @Override
    public void beginPollMsg() {
        if (session.getState() == QQSession.State.OFFLINE) {
            throw new IllegalArgumentException("client is aready offline !!!");
        }

        WechatProcModule procModule = (WechatProcModule) getModule(QQModule.Type.PROC);
        
        procModule.doPollMsg(); 
        // 轮询邮件
        // EmailModule emailModule = (EmailModule) getModule(QQModule.Type.EMAIL);
        // emailModule.doPoll();
    }

     
    

    /**
     * 获取登录二维码
     *
     * @param qqActionListener
     */
    @Override
    public void getQRcode(QQActionListener qqActionListener) {
    	WechatProcModule module = getModule(QQModule.Type.PROC);
    	module.getQRCode(qqActionListener);
    }
    @Override
    public void getUuid(QQActionListener qqActionListener) {
        WechatLoginModule login = getModule(QQModule.Type.LOGIN);
        login.getUuid(qqActionListener);
    }

    /**
     * 检测登录二维码是否已经扫描
     *
     * @param qqActionListener
     */
    @Override
    public void checkQRCode(QQActionListener qqActionListener) {
    	WechatProcModule module = getModule(QQModule.Type.PROC);
        module.checkQRCode(qqActionListener);
    }

	 

	@Override
	public void uploadFile(String fileName,String toUser, QQActionListener listener) {
		 
		WechatChatModule module = getModule(QQModule.Type.CHAT);
        module.uploadFile(fileName,toUser,listener);
	}

	@Override
	public void handleEvent(QQNotifyEvent event) {
		WechatEventHandler service=getSerivce(QQService.Type.EVENTHANDLER);
		service.handleEvent(event); 
	}

	@Override
	public void sendTextMsg(String msg, String toUser,QQActionListener listener) {
		WechatChatModule module = getModule(QQModule.Type.CHAT);
		module.sendTextMsg(msg, toUser, listener);
	}

	@Override
	public void sendDelayedTextMsg(String msg, String toUser, Long delay,
			QQActionListener listener) { 
		WechatChatModule module = getModule(QQModule.Type.CHAT);
		module.sendDelayedTextMsg(msg, toUser, delay,listener);
	}
	
	@Override
	public void sendImgMsg(String img, String toUser, QQActionListener listener) {
		WechatChatModule module = getModule(QQModule.Type.CHAT);
		module.sendImgMsg(img, toUser, listener);
	}
	
	@Override
	public void sendImgContentMsg(String content, String toUser,
			QQActionListener listener) {
		WechatChatModule module = getModule(QQModule.Type.CHAT);
		module.sendImgContentMsg(content, toUser, listener);
	}
	
	@Override
	public void sendVideoMsg(String video, String toUser,
			QQActionListener listener) {
		WechatChatModule module = getModule(QQModule.Type.CHAT);
		module.sendVideoMsg(video, toUser, listener);
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sendAppMsg(WechatMsg msg, QQActionListener listener) { 
		WechatChatModule module = getModule(QQModule.Type.CHAT);
		module.sendAppMsg(msg, listener);
	}

	@Override
	public void sendFileMsg(String file, String toUser,
			QQActionListener listener) {
		WechatChatModule module = getModule(QQModule.Type.CHAT);
		module.sendFileMsg(file, toUser, listener);
		
	}

	@Override
	public WechatStore getWechatStore() { 
		return store;
	}

	@Override
	public void syncContactInfo(QQActionListener listener) {
		WechatBuddyModule module = getModule(QQModule.Type.BUDDY);
		module.getBuddyList(listener);
	}

	@Override
	public void batchGetContactInfo(Map<String,WechatContact> contacts,QQActionListener listener) {
		WechatBuddyModule module = getModule(QQModule.Type.BUDDY);
		module.batchGetContact(listener,contacts);
		
	}

	@Override
	public void batchModRemark(Map<String, WechatContact> contact,
			QQActionListener listener) {
		WechatBuddyModule module = getModule(QQModule.Type.BUDDY); 
		module.batchModRemark(contact, listener);
	}

	@Override
	public void batchAddFriend(List<WechatContact> users, String verifyContent,
			QQActionListener listener) {
		WechatBuddyModule module = getModule(QQModule.Type.BUDDY);
		module.batchVerifyUser(users, verifyContent, listener);
	}

	@Override
	public void aggreeAddFriend(String userName, String ticket,
			QQActionListener listener) {
		WechatBuddyModule module = getModule(QQModule.Type.BUDDY);
		module.verifyUser(userName, null, WechatVerifyUserAction.aggreeAddFriend, ticket, listener);
	}

	@Override
	public void createChatroom(String topic, List<String> users,
			QQActionListener listener) {
		WechatChatModule module = getModule(QQModule.Type.CHAT);
		module.createChatroom(topic, users, listener);
		
	}

	@Override
	public void updateChatroom(String chatroomName, List<String> addUsers,boolean needInvite,
			QQActionListener listener) {
		WechatChatModule module = getModule(QQModule.Type.CHAT);
		module.updateChatroom(chatroomName, addUsers,needInvite, listener);
	}
	
	
	@Override
	public void chatroomModTopic(String newTopic, String chatroomName,QQActionListener listener) {
		WechatGroupModule module = getModule(QQModule.Type.GROUP);
		module.chatroomModTopic(newTopic, chatroomName, listener);
		 
		
	}

	@Override
	public void chatroomDelMember(String chatroomName, List<String> delUserList,QQActionListener listener) {
		WechatGroupModule module = getModule(QQModule.Type.GROUP);
		module.chatroomDelMember(chatroomName, delUserList, listener);
	}
	

	@Override
	public void addFriend(WechatContact users, String verifyContent,
			QQActionListener listener) {
		WechatBuddyModule module = getModule(QQModule.Type.BUDDY);
		module.verifyUser(users.getUserName(), verifyContent, WechatVerifyUserAction.addFriend, null, listener);
	}

	@Override
	public void fireNotify(String uid, QQNotifyEvent event) {
		 if (notifyListener != null) {
	            try {
	                notifyListener.onNotifyEvent(uid, event);
	            } catch (Throwable e) {
	                LOG.warn("fireNotify Error!!", e);
	            }
	        }
	        // 重新登录成功，重新poll
	        if (event.getType() == Type.RELOGIN_SUCCESS) {
	            beginPollMsg();
	        }
		
	}

	@Override
	public void modRemark(WechatContact contact,String remark,QQActionListener listener) {
		WechatBuddyModule module = getModule(QQModule.Type.BUDDY); 
		module.modRemark(contact, remark, listener);
		
	}

	@Override
	public void output(String log) {
		if(logUtil==null)
		{
			return;
		}
		logUtil.log(log);
		
	}

	public WechatConfig getConfig() {
		return wechatConfig;
	}

	public void setConfig(WechatConfig wechatConfig) {
		this.wechatConfig = wechatConfig;
	}

	@Override
	public void refreshCodeLeft(int num) {
		// TODO Auto-generated method stub
		if(logUtil==null)
		{
			return;
		}
		logUtil.refreshCodeLeft(num);
	}

	@Override
	public void getMsgImg(String msgId, String skey, QQActionListener listener) {
		WechatChatModule module = getModule(QQModule.Type.CHAT);
		module.getMsgImg(msgId, skey, listener); 
	}

	@Override
	public void getHeadImg(String headImgUrl,QQActionListener listener) {
		WechatLoginModule login = getModule(QQModule.Type.LOGIN);
		login.getHeadImg(listener, headImgUrl);
	}

	

	

	

	

	 
}
