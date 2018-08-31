package com.wechat.action;

import java.util.List;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blade.kit.json.JSONKit;
import com.blade.kit.json.JSONObject;
import com.im.utils.DateUtils;

import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.action.AbstractHttpAction;
import iqq.im.core.QQContext;
import iqq.im.event.QQActionEvent;
import iqq.im.http.QQHttpRequest;
import iqq.im.http.QQHttpResponse; 

public class WechatUpdateChatroomAction extends AbstractHttpAction {
	 private static final Logger LOGGER = LoggerFactory.getLogger(WechatUpdateChatroomAction.class);
	private List<String> addUserList; 
	private String chatroomName;
	private boolean needInvite;
	
	public WechatUpdateChatroomAction(String chatroomName,List<String> addUserList,boolean needInvite,QQContext context,
			QQActionListener listener) {
		super(context, listener);
		this.chatroomName=chatroomName;
		this.addUserList=addUserList;
		this.needInvite=needInvite;
	}

	

	@Override
	protected QQHttpRequest onBuildRequest() throws QQException, JSONException {
		String fun=needInvite?"invitemember":"addmember";
		String url = getContext().getSession().getBase_uri() + "/webwxupdatechatroom?fun="+fun+"&r=" + DateUtils.nowTimestamp();
		if(addUserList==null || addUserList.isEmpty())
		{
			 return null;
		}
		QQHttpRequest req = createHttpRequest("POST", url);  
		JSONObject body = new JSONObject();
		body.put("BaseRequest", getContext().getSession().getBaseRequest());
		body.put("ChatRoomName", this.chatroomName);
		
		StringBuffer buffer=new StringBuffer();
		for(String temp : addUserList)
		{
			buffer.append(","+temp);
		} 
		 
		body.put(needInvite?"InviteMemberList":"AddMemberList", buffer.substring(1).toString());
		 
		req.setPostBody(body.toString()); 
		return req;
	}

	/**
	 * 错误码：
	 * -23：需要邀请才能加入
	 * 1：fun错误
	 * -2028：需要群主同意才能邀请
	 * -2013 不是好友
	 * 1205 操作频繁
	 */
	@Override
	protected void onHttpStatusOK(QQHttpResponse response) throws QQException,
			JSONException {
		String res=response.getResponseString();
		JSONObject resJson=JSONKit.parseObject(res);
		JSONObject baseResponse = resJson.get("BaseResponse").asJSONObject();
		ActionResponse actionResponse=new ActionResponse();
		actionResponse.setRequestData1(chatroomName); 
		actionResponse.setRequestData2(addUserList); 
		if (null != baseResponse) {
			int ret = baseResponse.getInt("Ret", -1);
			actionResponse.setStatus(ret);
			if(ret==0)
			{
				actionResponse.setResponseData("邀请发送成功");
				notifyActionEvent(QQActionEvent.Type.EVT_OK, actionResponse);
				return;
			}else
			{
				switch(ret)
				{
				case -2028:
					actionResponse.setResponseData("需要群主同意才能邀请");
					break;
				case -2013:
					actionResponse.setResponseData("非好友关系");
					break;
				case 1205:
					actionResponse.setResponseData("操作频繁，稍后再试");
					break;
				default: actionResponse.setResponseData("群组邀请失败:"+ret);
				}
				 
				
				notifyActionEvent(QQActionEvent.Type.EVT_ERROR, actionResponse);
				LOGGER.error("群组邀请失败："+baseResponse.toString());
				return;
			}
		}
		actionResponse.setResponseData("unkown error");
		actionResponse.setStatus(-100);
		notifyActionEvent(QQActionEvent.Type.EVT_ERROR, actionResponse);//-100
		return;
		
	}
	
}
