package com.im.ui.wechatui.utils;

import java.util.List;

import com.im.base.customize.EnglishSignUpVO;
import com.im.utils.StringHelper;

public class EnglishSignUpSequence {

	private static int cherryIndex;
	private static int strawberryIndex;
	private static int appIndex;
	
	public static synchronized String next(String name)
	{
		if(StringHelper.isEmpty(name))
		{
			return "";
		}
		int index=0;
		String preFix="";
		String indexStr=null;
		if("樱桃班".equalsIgnoreCase(name))
		{
			preFix+="C";
			cherryIndex++;
			index=cherryIndex;
		}else if("草莓班".equalsIgnoreCase(name))
		{
			preFix+="S";
			strawberryIndex++;
			index=strawberryIndex;
			
		}else if("苹果班".equalsIgnoreCase(name))
		{
			preFix+="A";
			appIndex++;
			index=appIndex;
		}
		indexStr=index+"";
		if(indexStr.length()<3)
		{
			int length= indexStr.length();
			for(int i=0;i<(3-length);i++)
			{
				indexStr="0"+indexStr;
			}
		}
		
		return preFix+indexStr;
	}
	
	public static synchronized void init(List<EnglishSignUpVO> signUpList)
	{
		cherryIndex=0;
		strawberryIndex=0;
		appIndex=0;
		if(signUpList==null)
		{
			return;
		}
		for(EnglishSignUpVO temp : signUpList)
		{
			if("樱桃班".equalsIgnoreCase(temp.getClassName()))
			{ 
				cherryIndex++; 
			}else if("草莓班".equalsIgnoreCase(temp.getClassName()))
			{ 
				strawberryIndex++; 
				
			}else if("苹果班".equalsIgnoreCase(temp.getClassName()))
			{ 
				appIndex++; 
			}
		}
	}

	public static void main(String[] args)
	{
		System.out.println(EnglishSignUpSequence.next("樱桃班"));
		System.out.println(EnglishSignUpSequence.next("草莓班"));
		
		System.out.println(EnglishSignUpSequence.next("樱桃班"));
		System.out.println(EnglishSignUpSequence.next("樱桃班"));
		
	}
	
	public static int getCherryIndex() {
		return cherryIndex;
	}

	public static void setCherryIndex(int cherryIndex) {
		EnglishSignUpSequence.cherryIndex = cherryIndex;
	}

	public static int getStrawberryIndex() {
		return strawberryIndex;
	}

	public static void setStrawberryIndex(int strawberryIndex) {
		EnglishSignUpSequence.strawberryIndex = strawberryIndex;
	}

	public static int getAppIndex() {
		return appIndex;
	}

	public static void setAppIndex(int appIndex) {
		EnglishSignUpSequence.appIndex = appIndex;
	}
	
	
}
