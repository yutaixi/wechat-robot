package com.im.utils;

import java.util.Random;

public class MathUtil {

	/* 
	 * 返回长度为【strLength】的随机数，在前面补0 
	 */  
	public static String getFixLenthString(int strLength) {  
	      
	    Random rm = new Random();  
	      
	    // 获得随机数  
	    double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);  
	  
	    // 将获得的获得随机数转化为字符串  
	    String fixLenthString = String.valueOf(pross);  
	  
	    // 返回固定的长度的随机数  
	    return fixLenthString.substring(2, strLength + 1);  
	}  
	
	public static int getRandomNum(int max)
	{
		return MathUtil.getRandomNum(0, max);
	}
	 
	
	public static String genFixedLengthRandomNum(int pwd_len){
	    //35是因为数组是从0开始的，26个字母+10个数字
	    final int maxNum = 36;
	    int i; //生成的随机数
	    int count = 0; //生成的密码的长度
	    char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	    StringBuffer pwd = new StringBuffer("");
	    Random r = new Random();
	    while(count < pwd_len){
	     //生成随机数，取绝对值，防止生成负数，
	   
	     i = Math.abs(r.nextInt(maxNum)); //生成的数最大为36-1
	   
	     if (i >= 0 && i < str.length) {
	      pwd.append(str[i]);
	      count ++;
	     }
	    }
	    return pwd.toString();
	 }
	public static int getRandomNum(int min,int max)
	{
		if(min==0 && max==0)
		{
			return 0;
		}
		 Random r = new Random();
		 return r.nextInt(Math.abs(max-min))+min;
	}
	
	public static void main(String[] args) {
	   String fd=""; 
	   for(int l=0;l<100;l++)
	   {
	       System.out.println(genFixedLengthRandomNum(18)); 
	       //System.out.println(getFixLenthString(18));
	   }
	   
	}
}
