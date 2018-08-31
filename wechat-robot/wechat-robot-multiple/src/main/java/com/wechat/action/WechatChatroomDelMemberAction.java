package com.wechat.action;

import java.util.List;

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

public class WechatChatroomDelMemberAction extends AbstractHttpAction {
	 private static final Logger LOGGER = LoggerFactory.getLogger(WechatChatroomDelMemberAction.class);
	private List<String> delUserList; 
	private String chatroomName; 
	
	public WechatChatroomDelMemberAction(String chatroomName,List<String> delUserList,QQContext context,
			QQActionListener listener) {
		super(context, listener);
		this.chatroomName=chatroomName;
		this.delUserList=delUserList; 
	}

	

	@Override
	protected QQHttpRequest onBuildRequest() throws QQException, JSONException {
		String fun="delmember";
		String url = getContext().getSession().getBase_uri() + "/webwxupdatechatroom?fun="+fun+"&r=" + DateUtils.nowTimestamp();
		if(delUserList==null || delUserList.isEmpty() || StringHelper.isEmpty(chatroomName))
		{
			 return null;
		}
		QQHttpRequest req = createHttpRequest("POST", url);  
		JSONObject body = new JSONObject();
		body.put("BaseRequest", getContext().getSession().getBaseRequest());
		body.put("ChatRoomName", this.chatroomName);
		
		StringBuffer buffer=new StringBuffer();
		for(String temp : delUserList)
		{
			buffer.append(","+temp);
		} 
		 
		body.put("DelMemberList", buffer.substring(1).toString());
		 
		req.setPostBody(body.toString()); 
		return req;
	}

	/**
	 * 错误码： 
	 * 1205 操作频繁
	 */
	@Override
	protected void onHttpStatusOK(QQHttpResponse response) throws QQException,
			JSONException {
		String res=response.getResponseString();
		JSONObject resJson=JSONKit.parseObject(res);
		JSONObject baseResponse = resJson.get("BaseResponse").asJSONObject();
		int ret =-1;
		if (null != baseResponse) {
			ret=baseResponse.getInt("Ret", -1); 
			if(ret==0)
			{ 
				notifyActionEvent(QQActionEvent.Type.EVT_OK, ret);
				LOGGER.error("群组踢人成功");
				return;
			} 
		} 
		notifyActionEvent(QQActionEvent.Type.EVT_ERROR, ret); 
		LOGGER.error("群组踢人失败："+res);
		return;
		
	}
	
}
