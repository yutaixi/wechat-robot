package com.wechat.module;
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
 * Package  : iqq.im.module
 * File     : CoreModule.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2012-7-31
 * License  : Apache License 2.0
 */
 
import com.wechat.action.CheckQRCodeAction;
import com.wechat.action.GetQRCodeAction;
import com.wechat.action.GetUuidAction;
import com.wechat.action.LoginAction;
import com.wechat.action.OpenStatusNotifyAction;
import com.wechat.action.PollMsgAction;
import com.wechat.action.SyncCheckAction;
import com.wechat.action.WechatGetHeadImgAction;
import com.wechat.action.WechatInitAction;

import iqq.im.QQActionListener;  
import iqq.im.event.QQActionFuture;
import iqq.im.module.AbstractModule;

/**
 * 登录模块，处理登录和退出
 *
 * @author solosky
 */
public class WechatLoginModule extends AbstractModule {

    /**
     * <p>get QRCode</p>
     *
     * @param listener a {@link iqq.im.QQActionListener} object.
     * @return a {@link iqq.im.event.QQActionFuture} object.
     */
    public QQActionFuture getQRCode(QQActionListener listener) {
        return pushHttpAction(new GetQRCodeAction(getContext(), listener));
    }

    
    public QQActionFuture getUuid(QQActionListener listener) {
        return pushHttpAction(new GetUuidAction(getContext(), listener));
    }
     
    public QQActionFuture checkQRCode(QQActionListener listener) {
        return pushHttpAction(new CheckQRCodeAction(getContext(), listener));
    } 
    public QQActionFuture login(QQActionListener listener) {
        return pushHttpAction(new LoginAction(getContext(), listener));
    } 
    
    public QQActionFuture initWechat(QQActionListener listener) {
        return pushHttpAction(new WechatInitAction(getContext(), listener));
    } 
    public QQActionFuture openStatusNotify(QQActionListener listener) {
        return pushHttpAction(new OpenStatusNotifyAction(getContext(), listener));
    }
    
    
    public QQActionFuture pollMsg(QQActionListener listener) {
        return pushHttpAction(new PollMsgAction(getContext(), listener));
    }
    
    public QQActionFuture SyncCheck(QQActionListener listener) {
        return pushHttpAction(new SyncCheckAction(getContext(), listener));
    }
    public QQActionFuture getHeadImg(QQActionListener listener,String headImgUrl) {
        return pushHttpAction(new WechatGetHeadImgAction(getContext(), listener,headImgUrl));
    }
    
}
