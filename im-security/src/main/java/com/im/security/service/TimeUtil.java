package com.im.security.service;

import java.io.InputStream;
import java.net.Socket; 
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
public class TimeUtil {
    public static final int DEFAULT_PORT = 37;//NTP服务器端口
    public static final String DEFAULT_HOST = "wwv.nist.gov";//NTP服务器地址 
    private static final String[] hosts={
    	"wwv.nist.gov",	//24.56.178.140 
    	"time.nist.gov",	//132.163.4.102
    	"time-a.nist.gov",	//129.6.15.28
    	"time-b.nist.gov",	//129.6.15.29
    	"time-c.nist.gov",	//129.6.15.30
    	"nist1-macon.macon.ga.us",	//98.175.203.200
    	"wolfnisttime.com",	//66.199.22.67
    	"nist.netservicesgroup.com",	//64.113.32.5
    	"nisttime.carsoncity.k12.mi.us",	//198.111.152.100
    	"nist1-lnk.binary.net",	//216.229.0.179 
    	"time-a.timefreq.bldrdoc.gov",	//132.163.4.101
    	"time-b.timefreq.bldrdoc.gov",	//132.163.4.102
    	"time-c.timefreq.bldrdoc.gov",	//132.163.4.103 
    	"utcnist.colorado.edu",	//128.138.140.44
    	"utcnist2.colorado.edu",	//128.138.141.172
    	"time-nw.nist.gov",	//131.107.13.100
    	"nist-time - server.eoni.com"	//216.228.192.69};
    };
    
    private TimeUtil() {
    };
 
    public static void main(String[] args)
    {
    	System.out.println(getCurrentTimeSync());
    	
    }
    
    public static Date getCurrentTimeSync()
    {
    	TimeZone china = TimeZone.getTimeZone("GMT+:08:00"); 
		Calendar cal = Calendar.getInstance();   
		cal.setTimeZone(china);
		long currentTime=currentTimeMillis(true);
		cal.setTimeInMillis(currentTime);
		return cal.getTime();
    }
    
    public static long currentTimeMillis(Boolean sync) {
        if (sync != null && sync.booleanValue() != true)
            return System.currentTimeMillis();
        try {
            return syncCurrentTime();
        } catch (Exception e) {
            return System.currentTimeMillis();
        }
    }
 
    public static long syncCurrentTime()  throws Exception {
        // The time protocol sets the epoch at 1900,
        // the java Date class at 1970. This number
        // converts between them.
        long differenceBetweenEpochs = 2208988800L;
 
        // If you'd rather not use the magic number uncomment
        // the following section which calculates it directly.
 
        /*
         * TimeZone gmt = TimeZone.getTimeZone("GMT"); Calendar epoch1900 =
         * Calendar.getInstance(gmt); epoch1900.set(1900, 01, 01, 00, 00, 00);
         * long epoch1900ms = epoch1900.getTime().getTime(); Calendar epoch1970
         * = Calendar.getInstance(gmt); epoch1970.set(1970, 01, 01, 00, 00, 00);
         * long epoch1970ms = epoch1970.getTime().getTime();
         * 
         * long differenceInMS = epoch1970ms - epoch1900ms; long
         * differenceBetweenEpochs = differenceInMS/1000;
         */
 
        InputStream raw = null;
        long secondsSince1970=0;
        long msSince1970=0;
        for(String temp : hosts)
        {
        	 try {
            	 
                 Socket theSocket = new Socket(temp, DEFAULT_PORT);
                 raw = theSocket.getInputStream();
      
                 long secondsSince1900 = 0;
                 for (int i = 0; i < 4; i++) {
                     secondsSince1900 = (secondsSince1900 << 8) | raw.read();
                 }
                 if (raw != null)
                     raw.close();
                  secondsSince1970 = secondsSince1900 - differenceBetweenEpochs;
                   msSince1970 = secondsSince1970 * 1000;
                 return msSince1970;
             } catch (Exception e) {
                  
             } 
        }
       throw new Exception("获取时间错误");
    }
}