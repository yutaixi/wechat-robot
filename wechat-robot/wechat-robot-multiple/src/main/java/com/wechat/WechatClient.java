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
import java.util.List;
import java.util.Map;
 
import com.im.base.wechat.WechatContact;
import com.im.base.wechat.WechatMsg;
import com.im.ui.QRCodeFrame;  

import iqq.im.QQActionListener; 
import iqq.im.core.QQSession;
import iqq.im.event.QQNotifyEvent;

/**
 * WebQQ客户的实现
 *
 * @author solosky
 */
public interface WechatClient  {
	/**
     * <p>destroy.</p>
     */
    public void destroy();

    public void getQRcode(QQActionListener qqActionListener);
    public void getUuid(QQActionListener qqActionListener);
    public QQSession getSession(); 
    public void uploadFile(String fileName,String toUser,QQActionListener qqActionListener); 
    public void checkQRCode(QQActionListener qqActionListener);
    public void handleEvent(QQNotifyEvent event);
    public void beginPollMsg();
    public void getHeadImg(String headImgUrl,QQActionListener listener);
    
    public void getMsgImg(String msgId,String skey,QQActionListener listener);
    
    public void sendTextMsg(String msg,String toUser,QQActionListener listener);
    public void sendImgMsg(String img,String toUser,QQActionListener listener);
    public void sendImgContentMsg(String content,String toUser,QQActionListener listener);
    public void sendVideoMsg(String video,String toUser,QQActionListener listener);
    public void sendAppMsg(WechatMsg msg,QQActionListener listener);
    public void sendFileMsg(String file,String toUser,QQActionListener listener);
    
    
    public void sendDelayedTextMsg(String msg,String toUser,Long delay,QQActionListener listener);
//    public void sendDelayedImgMsg(String img,String toUser,Long delay,QQActionListener listener);
//    public void sendDelayedVideoMsg(String video,String toUser,Long delay,QQActionListener listener);
//    public void sendDelayedAppMsg(WechatMsg msg,Long delay,QQActionListener listener);
//    public void sendDelayedFileMsg(String file,String toUser,Long delay,QQActionListener listener);
    
    public void syncContactInfo(QQActionListener listener);
    public void batchGetContactInfo(Map<String,WechatContact> contacts,QQActionListener listener);
     
    
    public void batchModRemark(Map<String,WechatContact> contact,QQActionListener listener);
    public void modRemark(WechatContact contact,String remark,QQActionListener listener);
    public void batchAddFriend(List<WechatContact> users,String verifyContent,QQActionListener listener);
    public void addFriend(WechatContact users,String verifyContent,QQActionListener listener);
    
    public void aggreeAddFriend(String userName,String ticket,QQActionListener listener);
    
    public void createChatroom(String topic,List<String> users,QQActionListener listener);
    public void updateChatroom(String chatroomName,List<String> addUsers,boolean needInvite,QQActionListener listener);
    public void chatroomModTopic(String newTopic,String chatroomName,QQActionListener listener);
    public void chatroomDelMember(String chatroomName,List<String> delUserList,QQActionListener listener);
    
    
    public QRCodeFrame getQrCodeFrame();
	public void setQrCodeFrame(QRCodeFrame qrCodeFrame); 
	
	public WechatConfig getConfig() ;

	public void setConfig(WechatConfig wechatConfig) ;
	
	 
}
