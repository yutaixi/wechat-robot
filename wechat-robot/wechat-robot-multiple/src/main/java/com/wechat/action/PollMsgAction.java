package com.wechat.action;

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
 * Project  : WebQQCoreAsync
 * Package  : iqq.im
 * File     : WebQQClientTest.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2013-2-17
 * License  : Apache License 2.0
 */ 

import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.action.AbstractHttpAction;
import iqq.im.bean.*;
import iqq.im.core.*;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQNotifyEvent;
import iqq.im.http.QQHttpRequest;
import iqq.im.http.QQHttpResponse;
import iqq.im.module.DiscuzModule;
import iqq.im.module.GroupModule;
import iqq.im.module.UserModule;   

import org.json.JSONException; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;   

import com.blade.kit.json.JSONArray;
import com.blade.kit.json.JSONKit;
import com.blade.kit.json.JSONObject;
import com.im.base.wechat.WechatMsg;
import com.im.base.wechat.WechatMsgRecommendInfo;
import com.im.utils.DateUtils;
import com.im.utils.JsonUtil;
import com.wechat.bean.WechatMsgType;

import java.util.ArrayList; 
import java.util.List;

/**
 * 轮询Poll消息
 * <p/>
 * 增加更多的消息处理（群被踢、群文件共享、加好友请求）
 *
 * @author solosky
 * @author elthon
 */
public class PollMsgAction extends AbstractHttpAction {

    private static final Logger LOG = LoggerFactory.getLogger(PollMsgAction.class);

    /**
     * <p>Constructor for PollMsgAction.</p>
     *
     * @param context  a {@link iqq.im.core.QQContext} object.
     * @param listener a {@link iqq.im.QQActionListener} object.
     */
    public PollMsgAction(QQContext context, QQActionListener listener) {
        super(context, listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected QQHttpRequest onBuildRequest() throws QQException, JSONException {
        
        String url = getContext().getSession().getBase_uri() + "/webwxsync?skey=" + getContext().getSession().getSkey() 
        		+ "&sid=" + getContext().getSession().getWxsid();
		
		JSONObject body = new JSONObject();
		body.put("BaseRequest", getContext().getSession().getBaseRequest());
		body.put("SyncKey", getContext().getSession().getSyncKey());
		body.put("rr", DateUtils.nowTimestamp());
		 
        QQHttpRequest req = createHttpRequest("POST", url);
        req.addHeader("Cookie", getContext().getSession().getCookie()); 
        req.setPostBody(body.toString());
        req.setReadTimeout(70 * 1000);
        req.setConnectTimeout(10 * 1000); 
        return req;
    }

     
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onHttpStatusOK(QQHttpResponse response) throws QQException,
            JSONException {
        QQStore store = getContext().getStore();
        List<QQNotifyEvent> notifyEvents = new ArrayList<QQNotifyEvent>();
        JSONObject resJson = JSONKit.parseObject(response.getResponseString());  
        JSONObject baseResponse = resJson.get("BaseResponse").asJSONObject();
        //更新synckey
        if (null != baseResponse) {
        	int ret = baseResponse.getInt("Ret");
        	if (ret == 0) { 
        		getContext().getSession().setSyncKey(resJson.get("SyncKey").asJSONObject());
				StringBuffer synckey = new StringBuffer(); 
				JSONArray list = getContext().getSession().getSyncKey().get("List").asArray();
				for (int i = 0, len = list.size(); i < len; i++) {
					JSONObject item = list.get(i).asJSONObject();
					synckey.append("|" + item.getInt("Key") + "_" + item.getInt("Val"));
				}
				getContext().getSession().setSynckey(synckey.substring(1)); 
				 
			}
        }
        notifyEvents.addAll(processAddMsgList(resJson)); 
        notifyActionEvent(QQActionEvent.Type.EVT_OK, notifyEvents);
    }

    
    /**
     * <p>processBuddyMsg.</p>
     *
     * @param pollData a {@link org.json.JSONObject} object.
     * @return a {@link iqq.im.event.QQNotifyEvent} object.
     * @throws org.json.JSONException if any.
     * @throws iqq.im.QQException     if any.
     */
    public List<QQNotifyEvent> processAddMsgList(JSONObject resJson) throws JSONException
    { 
    	 List<QQNotifyEvent> notifyEvents = new ArrayList<QQNotifyEvent>();
    	 QQNotifyEvent event=null;
    	//处理新增消息
        JSONArray addMsgList = resJson.get("AddMsgList").asArray();
        if(addMsgList!=null && !addMsgList.isEmpty())
        {
        	LOG.info("有AddMsgList消息");
        }
        for (int i = 0, len = addMsgList.size(); i < len; i++) {
        	JSONObject msg = addMsgList.get(i).asJSONObject(); 
        	WechatMsg wechatMsg=JsonUtil.parseJsonByOrgName(msg.toString(), WechatMsg.class); 
        	switch(wechatMsg.getMsgType())
        	{
        	case WechatMsgType.MSGTYPE_APP:
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MSGTYPE_APP,wechatMsg);
        		notifyEvents.add(event);
        		break;
        	case WechatMsgType.MSGTYPE_TEXT:
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MSGTYPE_TEXT,wechatMsg);
        		notifyEvents.add(event);
        		break;
        	case WechatMsgType.MSGTYPE_IMAGE:
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MSGTYPE_IMAGE,wechatMsg);
        		notifyEvents.add(event);
        		break;
        	case WechatMsgType.MSGTYPE_VOICE:
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MSGTYPE_VOICE,wechatMsg);
        		notifyEvents.add(event);
        		break;
        	case WechatMsgType.MSGTYPE_VIDEO:
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MSGTYPE_VIDEO,wechatMsg);
        		notifyEvents.add(event);
        		break;
        	case WechatMsgType.MSGTYPE_MICROVIDEO:
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MSGTYPE_MICROVIDEO,wechatMsg);
        		notifyEvents.add(event);
        		break;
        	case WechatMsgType.MSGTYPE_EMOTICON: 
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MSGTYPE_EMOTICON,wechatMsg);
        		notifyEvents.add(event);
        		break;
        	case WechatMsgType.MSGTYPE_VOIPMSG:
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MSGTYPE_VOIPMSG,wechatMsg);
        		notifyEvents.add(event);
        		break;
        	case WechatMsgType.MSGTYPE_VOIPNOTIFY:
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MSGTYPE_VOIPNOTIFY,wechatMsg);
        		notifyEvents.add(event);
        		break;
        	case WechatMsgType.MSGTYPE_VOIPINVITE:
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MSGTYPE_VOIPINVITE,wechatMsg);
        		notifyEvents.add(event);
        		break;
        	case WechatMsgType.MSGTYPE_LOCATION:
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MSGTYPE_LOCATION,wechatMsg);
        		notifyEvents.add(event);
        		break;
        	case WechatMsgType.MSGTYPE_STATUSNOTIFY:
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MSGTYPE_STATUSNOTIFY,wechatMsg);
        		notifyEvents.add(event);
        		break;
        	case WechatMsgType.MSGTYPE_SYSNOTICE:
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MSGTYPE_SYSNOTICE,wechatMsg);
        		notifyEvents.add(event);
        		break;
        	case WechatMsgType.MSGTYPE_POSSIBLEFRIEND_MSG:
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MSGTYPE_POSSIBLEFRIEND_MSG,wechatMsg);
        		notifyEvents.add(event);
        		break;
        	case WechatMsgType.MSGTYPE_VERIFYMSG:
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MSGTYPE_VERIFYMSG,wechatMsg);
        		notifyEvents.add(event);
        		break;
        	case WechatMsgType.MSGTYPE_SHARECARD:
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MSGTYPE_SHARECARD,wechatMsg);
        		notifyEvents.add(event);
        		break;
        	case WechatMsgType.MSGTYPE_SYS:
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MSGTYPE_SYS,wechatMsg);
        		notifyEvents.add(event);
        		break;
        	case WechatMsgType.MSGTYPE_RECALLED:
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MSGTYPE_RECALLED,wechatMsg);
        		notifyEvents.add(event);
        		break;
        	
        	}
        }
        JSONArray modContactList = resJson.get("ModContactList").asArray();
        if(modContactList!=null && !modContactList.isEmpty())
        {
        	for(int i=0;i<modContactList.size();i++)
        	{
        		JSONObject contactObj=modContactList.get(i).asJSONObject();
        		WechatMsgRecommendInfo contactInfo=JsonUtil.parseJsonByOrgName(contactObj.toString(), WechatMsgRecommendInfo.class); 
        		WechatMsg wechatMsg=new WechatMsg(); 
        		wechatMsg.setFromUserName(contactInfo.getUserName());
        		wechatMsg.setToUserName(this.getContext().getSession().getUser().getUserName());
        		wechatMsg.setRecommendInfo(contactInfo);
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MOD_CONTACTLIST,wechatMsg);
        		notifyEvents.add(event);
        		
        	}
        	LOG.info("有modContactList消息");
        }
        JSONArray delContactList = resJson.get("DelContactList").asArray();
        if(delContactList!=null && !delContactList.isEmpty())
        {
        	for(int i=0;i<delContactList.size();i++)
        	{
        		JSONObject contactObj=delContactList.get(i).asJSONObject();
        		WechatMsgRecommendInfo contactInfo=JsonUtil.parseJsonByOrgName(contactObj.toString(), WechatMsgRecommendInfo.class); 
        		WechatMsg wechatMsg=new WechatMsg(); 
        		wechatMsg.setFromUserName(contactInfo.getUserName());
        		wechatMsg.setToUserName(this.getContext().getSession().getUser().getUserName());
        		wechatMsg.setRecommendInfo(contactInfo);
        		event=new QQNotifyEvent(QQNotifyEvent.Type.DEL_CONTACTLIST,wechatMsg);
        		notifyEvents.add(event);
        		
        	}
        	LOG.info("有delContactList消息");
        }
        JSONArray modChatRoomMemberList = resJson.get("ModChatRoomMemberList").asArray();
        if(modChatRoomMemberList!=null && !modChatRoomMemberList.isEmpty())
        {
        	for(int i=0;i<modChatRoomMemberList.size();i++)
        	{
        		JSONObject contactObj=modChatRoomMemberList.get(i).asJSONObject();
        		WechatMsgRecommendInfo contactInfo=JsonUtil.parseJsonByOrgName(contactObj.toString(), WechatMsgRecommendInfo.class); 
        		WechatMsg wechatMsg=new WechatMsg(); 
        		wechatMsg.setFromUserName(contactInfo.getUserName());
        		wechatMsg.setToUserName(this.getContext().getSession().getUser().getUserName());
        		wechatMsg.setRecommendInfo(contactInfo);
        		event=new QQNotifyEvent(QQNotifyEvent.Type.MOD_CHATROOMMEMBERLIST,wechatMsg);
        		notifyEvents.add(event);
        		
        	}
        	LOG.info("有ModChatRoomMemberList消息");
        }
         return notifyEvents;
    }

   
}
