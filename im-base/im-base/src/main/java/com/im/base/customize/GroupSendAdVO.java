package com.im.base.customize;

import com.im.base.wechat.WechatContact;

public class GroupSendAdVO extends WechatContact{

	private int times;
	
	private long lastSendTime;

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public long getLastSendTime() {
		return lastSendTime;
	}

	public void setLastSendTime(long lastSendTime) {
		this.lastSendTime = lastSendTime;
	}
	
	
	
}
