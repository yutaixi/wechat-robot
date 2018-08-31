package com.im.schedule.timer;

import java.util.concurrent.TimeUnit; 
import com.im.schedule.queue.ThreadQueueTask;

public abstract class ScheduleTask extends ThreadQueueTask {

	private long delay=0L;
	private long period;
	private TimeUnit unit=TimeUnit.SECONDS;
	private String scheduleName;
	
	public abstract void run(); 

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public TimeUnit getUnit() {
		return unit;
	}

	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	 
	
	 
}
