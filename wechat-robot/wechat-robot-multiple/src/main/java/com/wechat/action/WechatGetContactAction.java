package com.wechat.action;

  
import java.util.Map;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blade.kit.DateKit;
import com.blade.kit.StringKit;
import com.blade.kit.json.JSONArray;
import com.blade.kit.json.JSONKit;
import com.blade.kit.json.JSONObject; 
import com.im.base.wechat.WechatContact;
import com.im.utils.JsonUtil;
import com.wechat.core.WechatConstants;
import com.wechat.core.WechatContext;
import com.wechat.core.WechatStore;

import iqq.im.QQActionListener;
import iqq.im.QQException;
import iqq.im.action.AbstractHttpAction;
import iqq.im.core.QQContext;
import iqq.im.event.QQActionEvent;
import iqq.im.http.QQHttpRequest;
import iqq.im.http.QQHttpResponse; 

public class WechatGetContactAction extends AbstractHttpAction{

	 private static final Logger LOG = LoggerFactory.getLogger(WechatGetContactAction.class);
	
	public WechatGetContactAction(QQContext context, QQActionListener listener) {
		super(context, listener);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	protected QQHttpRequest onBuildRequest() throws QQException, JSONException {
		String url = getContext().getSession().getBase_uri() + "/webwxgetcontact?lang=zh_CN&skey="
				+ getContext().getSession().getSkey() + "&r=" + DateKit.getCurrentUnixTime()+"&seq=0"; 
		JSONObject body = new JSONObject();
		body.put("BaseRequest", getContext().getSession().getBaseRequest());
		QQHttpRequest req = createHttpRequest("GET", url); 
		req.setPostBody(body.toString());
		return req;
	}

	@Override
	protected void onHttpStatusOK(QQHttpResponse response) throws QQException,
			JSONException {
		// TODO Auto-generated method stub
		String res=response.getResponseString();
		//System.out.println(res);
		if (StringKit.isBlank(res)) {
			notifyActionEvent(QQActionEvent.Type.EVT_ERROR, null);
			LOG.error("获取联系人信息失败，服务器返回为空");
			return ;
		}
		
		LOG.debug(res);
		
		WechatStore wechatStore = ((WechatContext)this.getContext()).getWechatStore();
		try {
			JSONObject jsonObject = JSONKit.parseObject(res);
			JSONObject BaseResponse = jsonObject.get("BaseResponse").asJSONObject();
			if (null != BaseResponse) {
				int ret = BaseResponse.getInt("Ret", -1);
				if (ret == 0) {
					JSONArray memberList = jsonObject.get("MemberList").asArray();
					Map<String,WechatContact> buddyList =wechatStore.getBuddyList();
					Map<String,WechatContact> groupList =wechatStore.getGroupList();
					Map<String,WechatContact> chatRoom =wechatStore.getChatRoom();
					WechatContact contact=null;
					if (null != memberList) {
						for (int i = 0, len = memberList.size(); i < len; i++) { 
							String contactStr=memberList.get(i).asJSONObject().toString();
							contact=JsonUtil.parseJsonByOrgName(contactStr, WechatContact.class);
							// 订阅号/公众号/服务号
							if (contact.getVerifyFlag() == 8 || contact.getVerifyFlag()==24) {
								continue;
							}
							//微信官方公众号
							if (contact.getVerifyFlag() == 56) {
								continue;
							}
							// 特殊联系人
							if (WechatConstants.FILTER_USERS.contains(contact.getUserName())) {
								continue;
							}
							// 群聊
							if (contact.getUserName().indexOf("@@") > -1) {
								groupList.put(contact.getUserName(), contact);
								//chatRoom.remove(contact.getUserName());
								continue;
							}
							// 自己
							if (contact.getUserName().equals(getContext().getSession().getUser().getUserName())) {
								getContext().getSession().setUser(contact);
								continue;
							}
							
							buddyList.put(contact.getUserName(), contact);
						}   
					}
				}
			}
		} catch (Exception e) {
			notifyActionEvent(QQActionEvent.Type.EVT_ERROR, null);
			return ;
		}
		notifyActionEvent(QQActionEvent.Type.EVT_OK, null);
		LOG.error("已同步联系人信息");
		 return;
	}

}
