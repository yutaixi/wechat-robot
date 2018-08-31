package com.wechat.action; 
import org.json.JSONException;  
import org.json.JSONObject;

import com.im.utils.DateUtils;
import com.im.utils.MathUtil;

import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.action.AbstractHttpAction;
import iqq.im.core.QQContext;
import iqq.im.event.QQActionEvent;
import iqq.im.http.QQHttpRequest; 
import iqq.im.http.QQHttpResponse; 

public class WechatSendTextMsgAction extends AbstractHttpAction{

	private String msg;
	private String toUser; 
	private Long delay;
	
	public WechatSendTextMsgAction(QQContext context, QQActionListener listener,String msg,String toUser) {
		super(context, listener);
		this.msg=msg;
		this.toUser=toUser;
	}
	
	public WechatSendTextMsgAction(QQContext context, QQActionListener listener,String msg,String toUser,Long delay) {
		super(context, listener);
		this.msg=msg;
		this.toUser=toUser;
		this.delay=delay;
	}
	
	private void preProcessParam()
	{
		if(msg==null)
		{
			msg="";
			return ;
		}
		if(this.msg.indexOf("\\r\\n")>-1)
		{
			this.msg=this.msg.replaceAll("\\\\r\\\\n", "\\\r\\\n");
		}else if(this.msg.indexOf("\\n")>-1)
		{
			this.msg=this.msg.replaceAll("\\\\n", "\\\r\\\n");
		}  
	}
	 
	@Override
	protected QQHttpRequest onBuildRequest() throws QQException, JSONException {
		String url = getContext().getSession().getBase_uri() + "/webwxsendmsg?lang=zh_CN&pass_ticket=" + getContext().getSession().getPass_ticket();
		JSONObject body = new JSONObject();
		preProcessParam();
		String clientMsgId = DateUtils.nowTimestamp() + MathUtil.genFixedLengthRandomNum(5);
		JSONObject Msg = new JSONObject();
		Msg.put("Type", 1);
		Msg.put("Content", this.msg);
		Msg.put("FromUserName", getContext().getSession().getUser().getUserName());
		Msg.put("ToUserName", this.toUser);
		Msg.put("LocalID", clientMsgId);
		Msg.put("ClientMsgId", clientMsgId); 
		body.put("BaseRequest", getContext().getSession().getBaseRequest());
		body.put("Msg", Msg);
		QQHttpRequest req = createHttpRequest("POST", url);  
		req.setPostBody(body.toString());
		 
		if(this.delay!=null && this.delay>0)
		{
			try {
			Thread.sleep(this.delay*1000);
		    } catch (InterruptedException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
		    }
		}

		
		return req;
	}

	@Override
	protected void onHttpStatusOK(QQHttpResponse response) throws QQException,
			JSONException {
		// TODO Auto-generated method stub
		super.onHttpStatusOK(response); 
	}

	
}
