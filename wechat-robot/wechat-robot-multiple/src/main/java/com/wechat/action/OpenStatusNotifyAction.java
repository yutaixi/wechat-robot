package com.wechat.action;  
import org.json.JSONException;
import org.json.JSONObject; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.im.utils.DateUtils;
import com.im.utils.StringHelper;

import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.action.AbstractHttpAction; 
import iqq.im.core.QQContext;
import iqq.im.event.QQActionEvent;
import iqq.im.http.QQHttpRequest;
import iqq.im.http.QQHttpResponse; 

public class OpenStatusNotifyAction extends AbstractHttpAction{

	 private static final Logger LOG = LoggerFactory.getLogger(OpenStatusNotifyAction.class);
	public OpenStatusNotifyAction(QQContext context, QQActionListener listener) {
		super(context, listener);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected QQHttpRequest onBuildRequest() throws QQException, JSONException {
		String url = getContext().getSession().getBase_uri() + "/webwxstatusnotify?lang=zh_CN&pass_ticket=" + getContext().getSession().getPass_ticket();

		JSONObject body = new JSONObject();
		body.put("BaseRequest", getContext().getSession().getBaseRequest());
		body.put("Code", 3);
		body.put("FromUserName", getContext().getSession().getUser().getUserName());
		body.put("ToUserName", getContext().getSession().getUser().getUserName());
		body.put("ClientMsgId", DateUtils.nowTimestamp());

		QQHttpRequest req = createHttpRequest("POST", url);   
		req.setPostBody(body.toString());
        return req;
	}

	
	
	@Override
	protected void onHttpStatusOK(QQHttpResponse response) throws QQException
	{
		 try { 
			 String res=response.getResponseString(); 
			 if (StringHelper.isEmpty(res)) {
				 notifyActionEvent(QQActionEvent.Type.EVT_ERROR, "返回为空");
				 LOG.error("返回为空");
				} 
			 JSONObject responseJson=new JSONObject(res);
			 JSONObject BaseResponse = responseJson.getJSONObject("BaseResponse");
				if (null != BaseResponse) {
					int ret = BaseResponse.getInt("Ret");
					if (ret != 0) {
						 LOG.error("状态通知开启失败:"+ret);
						notifyActionEvent(QQActionEvent.Type.EVT_ERROR, ret);
					}else
					{
						notifyActionEvent(QQActionEvent.Type.EVT_OK, ret);
						 LOG.info("状态通知已开启");
					}
					
				}
		 }catch(Exception e)
		 {
			 LOG.error("状态通知开启失败:"+e);
			 notifyActionEvent(QQActionEvent.Type.EVT_ERROR, e);
		 }
	}
}
