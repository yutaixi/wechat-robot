package com.im.security.service;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
/*
 * 物理地址是48位，别和ipv6搞错了
 */
public class MacUtil {
	 public static void main(String[] args) throws Exception {  
		 getMac(); 
	    }  
  
	 public static String getLocalMacAddress()
	 {
		 InetAddress ia=null; 
		 String mac=null;
		try {
			ia = InetAddress.getLocalHost();
			mac= getMACAddress(ia);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//获取本地IP对象  
        catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mac;
	 }
	 
	 public static String getMac()  
	 {
		 Enumeration<NetworkInterface> interfaces=null;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		if(interfaces==null)
		{
			return "";
		}
		 StringBuffer macBuffer=new StringBuffer();
	        while (interfaces.hasMoreElements()) {  
	            NetworkInterface ni = interfaces.nextElement();  
	            try {
	            	if(ni.getDisplayName().contains("Bluetooth"))
	            	{
	            		continue;
	            	}
	            	if(!ni.getName().startsWith("wlan") && !ni.getName().startsWith("eth"))
	            	{
	            		continue;
	            	}
	            	 byte[] mac =ni.getHardwareAddress();
	            	 if(mac==null)
	            	 {
	            		 continue;
	            	 }
	            	 StringBuffer sb = new StringBuffer();  
	            	  
	                 for(int i=0;i<mac.length;i++){  
	                     if(i!=0){  
	                         sb.append("-");  
	                     }  
	                     //mac[i] & 0xFF 是为了把byte转化为正整数  
	                     String s = Integer.toHexString(mac[i] & 0xFF);  
	                     sb.append(s.length()==1?0+s:s);  
	                 }
	                 if(sb.length()==17)
	                 {
	                	 macBuffer.append(";"+sb.toString().toUpperCase());
	                 }
	                 System.out.println(ni.getDisplayName());  
	                 System.out.println(ni.getName());  
	                 System.out.println(sb.toString().toUpperCase());  
	                   
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	          //  System.out.println(ni);  
	        }   
	        if(macBuffer.length()<=0)
	        {
	        	return "";
	        }
	        return macBuffer.toString().substring(1).toUpperCase();
	 }
	 
     //获取MAC地址的方法  
     private static String getMACAddress(InetAddress ia)throws Exception{  
         //获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。  
         byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();  
           
         //下面代码是把mac地址拼装成String  
         StringBuffer sb = new StringBuffer();  
           
         for(int i=0;i<mac.length;i++){  
             if(i!=0){  
                 sb.append("-");  
             }  
             //mac[i] & 0xFF 是为了把byte转化为正整数  
             String s = Integer.toHexString(mac[i] & 0xFF);  
             sb.append(s.length()==1?0+s:s);  
         }  
           
         //把字符串所有小写字母改为大写成为正规的mac地址并返回  
         return sb.toString().toUpperCase();  
     }  
}