package com.wechat.action;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blade.kit.json.JSONKit;
import com.blade.kit.json.JSONObject;
import com.im.base.wechat.WechatContact;
import com.im.utils.DateUtils;

import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.action.AbstractHttpAction;
import iqq.im.core.QQContext;
import iqq.im.event.QQActionEvent;
import iqq.im.http.QQHttpRequest;
import iqq.im.http.QQHttpResponse; 

public class WechatOplogAction extends AbstractHttpAction{

	private static final Logger LOGGER = LoggerFactory.getLogger(WechatOplogAction.class);
	
	private WechatContact user;
	private int cmdId;
	private String remarkName;
	public WechatOplogAction(WechatContact user,int cmdId,String remarkName,QQContext context, QQActionListener listener) {
		super(context, listener);
		 this.user=user;
		 this.cmdId=cmdId;
		 this.remarkName=remarkName;
	}
	
	@Override
	protected QQHttpRequest onBuildRequest() throws QQException, JSONException {
		//https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxoplog
		String url = getContext().getSession().getBase_uri() + "/webwxoplog?r=" + DateUtils.nowTimestamp();
		QQHttpRequest req = createHttpRequest("POST", url);  
		JSONObject body = new JSONObject();
		body.put("BaseRequest", getContext().getSession().getBaseRequest());
		body.put("CmdId", this.cmdId);
		body.put("RemarkName", this.remarkName);
		body.put("UserName", this.user.getUserName());
		req.setPostBody(body.toString()); 
		return req;
	}
	
	@Override
	protected void onHttpStatusOK(QQHttpResponse response) throws QQException,
			JSONException {
		String res=response.getResponseString();
		
		JSONObject resJson= JSONKit.parseObject(res); 
		JSONObject baseResponse=resJson.get("BaseResponse").asJSONObject();
		if(baseResponse.getInt("Ret")!=0)
		{
			 notifyActionEvent(QQActionEvent.Type.EVT_ERROR, "修改备注名失败:"+baseResponse.getInt("Ret"));
			 LOGGER.error("修改备注名失败:"+baseResponse.getInt("Ret"));
			 return;
		}else
		{
			LOGGER.error("修改备注名成功");
		}
		user.setRemarkName(this.remarkName);
		 notifyActionEvent(QQActionEvent.Type.EVT_OK, this.user);
	}
	
	
	

}
