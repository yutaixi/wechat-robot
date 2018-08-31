package com.im.utils;

import org.apache.commons.lang3.StringUtils;

public class NumberUtils {

	private static final String numChar = "一二三四五六七八九十";
	
	private static final String[] numArray={"一","二","三","四","五","六","七","八","九"};
	
	public static Long getLongFromObj(Object obj)
	{
		Long result=null;
		if(obj==null)
		{
			return result;
		}
		String numberStr=obj.toString();
		if(StringHelper.isEmpty(numberStr) ||  !StringUtils.isNumeric(numberStr))
		{
			return result;
		}
		result=Long.valueOf(numberStr);
		return result;
	}
	
	public static int getNumberFromChineseAndArabicStr(String number)
	{
		if(StringHelper.isEmpty(number) || !NumberUtils.isNumeric(number))
		{
			return -1;
		} 
		int targetNum=0;
		if(StringUtils.isNumeric(number))
		{
			return Integer.valueOf(number);
		}
		String temp=null;
		for(int i=number.length()-1;i>=0;i--)
		{
			temp=String.valueOf(number.charAt(i));
			if(numChar.indexOf(temp)>-1)
			{
                  if("十".equalsIgnoreCase(temp))
                  {
                	  if(i-1>=0)
                	  {
                		  
                		  targetNum+=10*getIntFromChineseChar(String.valueOf(number.charAt(i-1)));
                		  i--;
                	  }else
                	  {
                		  targetNum+=10;
                	  }
                  }else
                  {
                	  targetNum+=getIntFromChineseChar(temp);
                  }
			}
		}
		return targetNum;
	}
	 
	public static int getIntFromChineseChar(String num)
	{
		 
		for(int i=0;i<numArray.length;i++)
		{
			if(numArray[i].equalsIgnoreCase(num))
			{
				return i+1;
			}
		}
		return -1;
	}
	
	
	public static boolean isNumeric(String num)
	{ 
		for(int i=0;i<num.length();i++)
		{
			if(NumberUtils.numChar.indexOf(num.charAt(i))>-1 || StringUtils.isNumeric(String.valueOf(num.charAt(i))) )
			{
				continue;
			}else
			{
				return false;
			}
		}
		return true;
	}
}
