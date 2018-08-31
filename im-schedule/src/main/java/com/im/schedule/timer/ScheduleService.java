package com.im.schedule.timer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors; 
import java.util.concurrent.ScheduledExecutorService;  
 

public class ScheduleService { 
	 
	private static int coreSize=5;
	
	private static boolean scheduled;
	
	private Map<String,ScheduleTask> taskMap=new HashMap<String,ScheduleTask>();
	
	private static ScheduledExecutorService service;
	
	private ScheduleService()
	{
		if(service==null)
		{
			this.service=Executors.newScheduledThreadPool(coreSize);
		}
	}
	
	public static ScheduleService getInstance()
	{
		 return ScheduleServiceHolder.instance;
	}
	  
   public boolean schedule(ScheduleTask task)
   {
	    
	   if(service.isShutdown())
	   {
		   this.service=Executors.newScheduledThreadPool(coreSize);
	   }
	   if(taskMap.get(task.getName())!=null)
		{
		   return false;
		}
	   taskMap.put(task.getName(), task);
	   service.scheduleAtFixedRate(task, task.getDelay(), task.getPeriod(), task.getUnit());
	   scheduled=true;
	   return true;
   }
   
   public void stopAllTask()
   {
	   service.shutdownNow();
	   scheduled=false;
	   taskMap.clear();
   }
//   public void restartAllTask()
//   {
//	   this.stopAllTask();
//	   while(!service.isShutdown())
//	   {
//		   try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	   }
//	   this.service=Executors.newScheduledThreadPool(coreSize);
//	   for(ScheduleTask task : taskMap.values())
//	   {
//		   service.scheduleAtFixedRate(task, task.getDelay(), task.getPeriod(), task.getUnit());
//		  
//	   } 
//   }
   
   public boolean isShutdown()
   {
	   return service.isShutdown();
   }
   
//   public void cleanTaskMap()
//   {
//	   taskMap.clear();
//   }
   
   private static class ScheduleServiceHolder
   {
	    private static ScheduleService instance=new ScheduleService();
   }

   public static boolean isScheduled() {
    	return scheduled;
   }
   
   
}
