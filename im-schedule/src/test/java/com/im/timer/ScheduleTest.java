package com.im.timer;

import com.im.schedule.timer.ScheduleService;

public class ScheduleTest {

	public static void test()
	{
		ScheduleTestTask task=new ScheduleTestTask();
		task.setName("ScheduleTestTask");
		task.setPeriod(3);
	    ScheduleService.getInstance().schedule(task);
	     try {
	    	 System.out.println("sleep begain");
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     System.out.println("sleep end");
	     System.out.println("stop All Task");
	     ScheduleService.getInstance().stopAllTask();
	     System.out.println("restart All Task");
	     
	}
	
	public static void main(String[] args)
	{
		test();
	}
}
