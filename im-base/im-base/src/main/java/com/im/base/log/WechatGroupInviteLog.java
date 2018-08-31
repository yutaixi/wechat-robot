package com.im.base.log;

import com.im.base.BaseVO;

public class WechatGroupInviteLog extends BaseVO{

	private String operator;
	
	private String inviteNickName;
	
	private String toGroupName;
	
	private String status;
	
	private String reason;
	
	private int ret=-100;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getInviteNickName() {
		return inviteNickName;
	}

	public void setInviteNickName(String inviteNickName) {
		this.inviteNickName = inviteNickName;
	}

	public String getToGroupName() {
		return toGroupName;
	}

	public void setToGroupName(String toGroupName) {
		this.toGroupName = toGroupName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}
	
	
	
	
}
