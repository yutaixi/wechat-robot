package com.wechat.action; 
import org.json.JSONException; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  

import com.blade.kit.json.JSONObject;
import com.wechat.bean.WechatSendMsgType;

import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.action.AbstractHttpAction;
import iqq.im.core.QQContext;
import iqq.im.core.QQSession; 
import iqq.im.http.QQHttpRequest;
import iqq.im.http.QQHttpResponse; 

public class WechatSendImgMsgAction extends AbstractHttpAction{

	 private static final Logger LOGGER = LoggerFactory.getLogger(WechatSendImgMsgAction.class);
	
	 private String mediaId;
	 private String toUser;
	 
	public WechatSendImgMsgAction(QQContext context, QQActionListener listener,String mediaId,String toUser) {
		super(context, listener);
		this.mediaId=mediaId;
		this.toUser=toUser;
	}

	

	@Override
	protected QQHttpRequest onBuildRequest() throws QQException {
		String url = getContext().getSession().getBase_uri() + "/webwxsendmsgimg?fun=async&f=json&pass_ticket="+getContext().getSession().getPass_ticket(); 
		String bodyMsg=getPostBodyStr(getContext().getSession());
		QQHttpRequest req = createHttpRequest("POST", url);  
		req.setPostBody(bodyMsg);
		return req;
	}
	 
	@Override
	protected void onHttpStatusOK(QQHttpResponse response) throws QQException,
			JSONException { 
		System.out.println(response.getResponseString());
		super.onHttpStatusOK(response); 
	}



	private String getPostBodyStr(QQSession meta) {
		JSONObject body = new JSONObject(); 
		String fromUser = null;
		JSONObject baseRequest = meta.getBaseRequest();
		String clientMsgId = String.valueOf(System.currentTimeMillis());
		JSONObject Msg = new JSONObject();

		fromUser = meta.getUser().getUserName();
		Msg.put("Type", WechatSendMsgType.IMG_MSG);
		Msg.put("MediaId", this.mediaId);
		Msg.put("Content", "");
		Msg.put("FromUserName", fromUser);
		Msg.put("ToUserName", this.toUser);
		Msg.put("LocalID", clientMsgId);
		Msg.put("ClientMsgId", clientMsgId);
		body.put("BaseRequest", baseRequest);
		body.put("Msg", Msg);
		body.put("Scene", 0);

		String bodyStr = body.toString();
		return bodyStr;
	}
	  
	 
}
