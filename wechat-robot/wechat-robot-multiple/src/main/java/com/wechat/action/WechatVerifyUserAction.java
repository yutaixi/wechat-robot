package com.wechat.action;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blade.kit.json.JSONArray;
import com.blade.kit.json.JSONKit;
import com.blade.kit.json.JSONObject;
import com.im.utils.DateUtils;
import com.im.utils.StringHelper;
import com.wechat.WebWechatClient;

import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.action.AbstractHttpAction;
import iqq.im.core.QQContext;
import iqq.im.event.QQActionEvent;
import iqq.im.http.QQHttpRequest;
import iqq.im.http.QQHttpResponse; 

public class WechatVerifyUserAction extends AbstractHttpAction{

	private static final Logger LOG = LoggerFactory.getLogger(WechatVerifyUserAction.class);
	
	public static final int addFriend=2;
	public static final int aggreeAddFriend=3;
	
	private String userName;
	private String verifyContent;
	private int opCode;
	private String verifyUserTicket;
	
	public WechatVerifyUserAction(String userName,String verifyContent,int opCode,String verifyUserTicket,QQContext context, QQActionListener listener) {
		super(context, listener);
		this.userName=userName;
		this.opCode=opCode;
		this.verifyUserTicket=verifyUserTicket;
		if(StringHelper.isEmpty(verifyContent) && opCode==addFriend)
		{  
			verifyContent="我是"+getContext().getSession().getUser().getNickName();
		}
		this.verifyContent=verifyContent;
	}
	
	@Override
	protected QQHttpRequest onBuildRequest() throws QQException, JSONException {
		String url = getContext().getSession().getBase_uri() + "/webwxverifyuser?r=" + DateUtils.nowTimestamp();
		QQHttpRequest req = createHttpRequest("POST", url);  
		JSONObject body = new JSONObject();
		body.put("BaseRequest", getContext().getSession().getBaseRequest());
		body.put("Opcode", this.opCode);
		//设置VerifyUserList
		body.put("VerifyUserListSize", 1); 
		JSONArray userList=new JSONArray();
		JSONObject user=new JSONObject();
		user.put("Value", this.userName);
		user.put("VerifyUserTicket", this.verifyUserTicket==null?"":this.verifyUserTicket);
		userList.add(user);
		body.put("VerifyUserList", userList);
		//设置验证内容
		body.put("VerifyContent", this.verifyContent);
		//设置sceneList
		body.put("SceneListCount", 1); 
		JSONArray sceneList=new JSONArray();
		sceneList.add(33); 
		body.put("SceneList", sceneList);
		//设置skey
		body.put("skey", getContext().getSession().getBaseRequest().get("Skey"));
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
			 notifyActionEvent(QQActionEvent.Type.EVT_ERROR, baseResponse.getString("ErrMsg"));
			 LOG.error("好友验证异常："+baseResponse.getInt("Ret"));
			 return;
		}
		super.onHttpStatusOK(response);
	}

	
	
	

}
