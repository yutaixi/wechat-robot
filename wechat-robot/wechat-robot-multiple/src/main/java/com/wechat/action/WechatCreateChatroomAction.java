package com.wechat.action;

import java.util.List;

import org.json.JSONException;

import com.blade.kit.json.JSONArray;
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

public class WechatCreateChatroomAction extends AbstractHttpAction{

	private List<String> users;
	private String topic;
	public WechatCreateChatroomAction(String topic,List<String> users,QQContext context,
			QQActionListener listener) {
		super(context, listener);
		this.users=users;
		this.topic=topic;
	}

	@Override
	protected QQHttpRequest onBuildRequest() throws QQException, JSONException {
		String url = getContext().getSession().getBase_uri() + "/webwxcreatechatroom?r=" + DateUtils.nowTimestamp();
		if(users==null || users.isEmpty())
		{
			return null;
		}
		QQHttpRequest req = createHttpRequest("POST", url);  
		JSONObject body = new JSONObject();
		body.put("BaseRequest", getContext().getSession().getBaseRequest());
		body.put("Topic", this.topic);
		body.put("MemberCount", users.size());
		
		JSONArray members=new JSONArray();
		JSONObject member=null;
		for(String temp : users)
		{
			member=new JSONObject();
			member.put("UserName", temp);
			members.add(member);
		} 
		body.put("MemberList", members);
		req.setPostBody(body.toString()); 
		return req;
	}
	
	
	@Override
	protected void onHttpStatusOK(QQHttpResponse response) throws QQException,
			JSONException {
		String res=response.getResponseString();
		JSONObject resJson=JSONKit.parseObject(res);
		JSONObject baseResponse=resJson.get("BaseResponse").asJSONObject();
		if(baseResponse.getInt("Ret")!=0)
		{
			notifyActionEvent(QQActionEvent.Type.EVT_ERROR, null);
			return;
		}
		String chatRoomName=resJson.getString("ChatRoomName");
		
		notifyActionEvent(QQActionEvent.Type.EVT_OK, chatRoomName);
	}

	

	
	
	
}
