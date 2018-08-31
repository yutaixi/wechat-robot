package com.im.timer;

import com.im.schedule.timer.ScheduleTask;

public class ScheduleTestTask extends ScheduleTask {

	@Override
	public void run() {
		 System.out.println(this.getName());
	}

}
