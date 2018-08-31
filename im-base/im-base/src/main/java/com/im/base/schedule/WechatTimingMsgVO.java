package com.im.base.schedule;

import java.util.concurrent.TimeUnit;

import com.im.base.wechat.WechatMsg;

public class WechatTimingMsgVO extends WechatMsg{
	
	private Long period;
	private TimeUnit unit=TimeUnit.SECONDS;

	 
	public TimeUnit getUnit() {
		return unit;
	}

	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}

	 

	public Long getPeriod() {
		return period;
	}

	public void setPeriod(Long period) {
		this.period = period;
	}

	 
	 
}
