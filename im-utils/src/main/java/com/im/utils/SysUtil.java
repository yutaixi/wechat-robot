package com.im.utils;

public class SysUtil {

	public static void sleep(long millis)
	{
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
		
	}
}
