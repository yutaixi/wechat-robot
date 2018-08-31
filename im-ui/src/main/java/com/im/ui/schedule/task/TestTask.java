package com.im.ui.schedule.task;

import com.im.schedule.queue.ThreadPoolManager;
import com.im.schedule.queue.ThreadQueueTask;

public class TestTask extends ThreadQueueTask{

	@Override
	public void run() {
		System.out.println("sleep开始");
		
		for(int i=0;i<100;i++)
		{
			try {
				Thread.sleep(1*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("循环"+i);
		}
		
		System.out.println("sleep结束");
		
	}

	public static void main(String[] args) throws InterruptedException
	{
		
		TestTask task=new TestTask();
		task.start();
		System.out.println(task.getState().toString());
		System.out.println(task.isAlive());
		Thread.sleep(5000);
		task.stop();
		System.out.println(task.getState().toString());
		System.out.println(task.isAlive());
	}
}
