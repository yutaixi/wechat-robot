package com.wechat.action;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blade.kit.json.JSONKit;
import com.blade.kit.json.JSONObject;
import com.im.utils.DateUtils;
import com.im.utils.StringHelper;

import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.action.AbstractHttpAction;
import iqq.im.core.QQContext;
import iqq.im.event.QQActionEvent;
import iqq.im.http.QQHttpRequest;
import iqq.im.http.QQHttpResponse;

public class WechatChatroomModTopicAction  extends AbstractHttpAction{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WechatChatroomModTopicAction.class);

	private String newTopic;
	private String chatroomName;
	
	public WechatChatroomModTopicAction(String newTopic,String chatroomName,QQContext context,QQActionListener listener) 
	{
		super(context, listener);
		 this.newTopic=newTopic;
		 this.chatroomName=chatroomName;
	}

	@Override
	protected QQHttpRequest onBuildRequest() throws QQException, JSONException {
		String fun="modtopic"; 
		String url = getContext().getSession().getBase_uri() + "/webwxupdatechatroom?fun="+fun+"&lang=zh_CN&pass_ticket=" + this.getContext().getSession().getPass_ticket();
		if(StringHelper.isEmpty(newTopic) || StringHelper.isEmpty(chatroomName))
		{
			 return null;
		}
		QQHttpRequest req = createHttpRequest("POST", url);  
		JSONObject body = new JSONObject();
		body.put("BaseRequest", getContext().getSession().getBaseRequest());
		body.put("ChatRoomName", this.chatroomName);
		body.put("NewTopic", newTopic); 
		req.setPostBody(body.toString()); 
		return req;
	}

	
	@Override
	protected void onHttpStatusOK(QQHttpResponse response) throws QQException,
			JSONException {
		String res=response.getResponseString();
		JSONObject resJson=JSONKit.parseObject(res);
		JSONObject baseResponse = resJson.get("BaseResponse").asJSONObject();
		int ret=-1;
		if (null != baseResponse) {
			ret = baseResponse.getInt("Ret", -1);
			if(ret==0)
			{ 
				notifyActionEvent(QQActionEvent.Type.EVT_OK, ret);
				return;
			} 
		}
		notifyActionEvent(QQActionEvent.Type.EVT_ERROR, ret);
	}

	
	
	
	
}
