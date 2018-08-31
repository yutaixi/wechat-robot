 /*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 /**
 * Project  : WebQQCoreAsync
 * Package  : iqq.im.util
 * File     : DateUtils.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2012-9-5
 * License  : Apache License 2.0 
 */
package com.im.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>DateUtils class.</p>
 *
 * @author solosky
 */
public class DateUtils {
   /**
    * <p>parse.</p>
    *
    * @param jsonobj a {@link org.json.JSONObject} object.
    * @throws java.text.ParseException if any.
    * @throws org.json.JSONException if any.
    * @return a {@link java.util.Date} object.
    */
   public static Date parse(JSONObject jsonobj) throws ParseException, JSONException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d = format.parse(jsonobj.getInt("year") + "-" + jsonobj.getInt("month")+ "-" + jsonobj.getInt("day"));
        return d;
    }
   
   /**
    * <p>nowTimestamp.</p>
    *
    * @return a long.
    */
   public static long nowTimestamp(){
	   return System.currentTimeMillis()/1000;
   }
   
   
   public static String getGMT8TimeStr(long time)
	{
		
       TimeZone china = TimeZone.getTimeZone("GMT+:08:00"); 
		Calendar cal = Calendar.getInstance();   
		cal.setTimeZone(china);
		cal.setTimeInMillis(time);
		//Sun Jul 03 2016 22:10:34 GMT+0800 (中国标准时间)
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT+0800 (中国标准时间)'", Locale.US);
		String str = sdf.format(cal.getTime());
		return str;
	}
   public static String formatDate(Date date)
   {
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateStr=format.format(date);
	   return dateStr;
   }
   public static String getNowTimeStr()
 	{ 
       TimeZone china = TimeZone.getTimeZone("GMT+:08:00"); 
 		Calendar cal = Calendar.getInstance();   
 		cal.setTimeZone(china); 
 		//Sun Jul 03 2016 22:10:34 GMT+0800 (中国标准时间)
 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss", Locale.US);
 		String str = sdf.format(cal.getTime());
 		return str;
 	}
   
   public static void main(String[] args)
   { 
	   System.out.println(DateUtils.getNowTimeStr());
   }
   public static String getGMTTimeMillisStr(String dataStr)
   {
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   Date date = null;
	   try {
	    date = format.parse(dataStr);
	   } catch (ParseException e) {
	    e.printStackTrace();
	   }
	   TimeZone china = TimeZone.getTimeZone("GMT+:08:00"); 
 		Calendar cal = Calendar.getInstance();   
 		cal.setTimeZone(china);
 		cal.setTime(date);
	   return String.valueOf(cal.getTimeInMillis());
   }
   
    public static Date getTime(long time)
    {
    	TimeZone china = TimeZone.getTimeZone("GMT+:08:00"); 
		Calendar cal = Calendar.getInstance();   
		cal.setTimeZone(china);
		cal.setTimeInMillis(time);
		return cal.getTime();
    }
}
