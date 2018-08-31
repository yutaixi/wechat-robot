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
 * Project  : LiteFetion
 * Package  : net.solosky.litefetion.util
 * File     : StringHelper.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-10-3
 * License  : Apache License 2.0 
 */
package com.im.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串工具类
 *
 * @author solosky
 */
public class StringHelper {

	
	static char hexdigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',

		'9', 'a', 'b', 'c', 'd', 'e', 'f' };
	/**
	 * 转义HTML中特殊的字符
	 *
	 * @param html HTML 内容
	 * @return 结果字符串
	 */
	public static String qouteHtmlSpecialChars(String html)
	{
		if(html==null)	return null;
		String[] specialChars = { "&", "\"", "'", "<", ">"};
		String[] qouteChars = {"&amp;", "&quot;", "&apos;", "&lt;", "&gt;"};
		for(int i=0; i<specialChars.length; i++){
			html = html.replace(specialChars[i], qouteChars[i]);
		}
		return html;
	}
	
	/**
	 * 反转义HTML中特殊的字符
	 *
	 * @param html HTML 内容
	 * @return 结果字符串
	 */
	public static String unqouteHtmlSpecialChars(String html)
	{
		if(html==null)	return null;
		String[] specialChars = { "&", "\"", "'", "<", ">", " "};
		String[] qouteChars = {"&amp;", "&quot;", "&apos;", "&lt;", "&gt;", "&nbsp;"};
		for(int i=0; i<specialChars.length; i++){
			html = html.replace(qouteChars[i], specialChars[i]);
		}
		return html;
	}
	
	
	/**
	 * 去掉HTML标签
	 *
	 * @param html HTML 内容
	 * @return 去除HTML标签后的结果
	 */
	public static String stripHtmlSpecialChars(String html)
	{
		if(html==null)	return null;
		 html=html.replaceAll("</?[^>]+>",""); 
		 html=html.replace("&nbsp;"," "); 
		 
		 return html;
	}
	
	
	/**
	 * 以一种简单的方式格式化字符串
	 * 如
	 * <pre>
	 * String s = StringHelper.format("{0} is {1}", "apple", "fruit");
	 * System.out.println(s);
	 * //输出  apple is fruit.
	 * </pre>
	 *
	 * @param pattern 需要进行格式化的字符串
	 * @param args 用于格式化的参数
	 * @return 结果字符串
	 */
	public static String format(String pattern, Object ...args)
	{
		for(int i=0; i<args.length; i++) {
			pattern = pattern.replace("{"+i+"}", args[i].toString());
		}
		return pattern;
	}
	
	/**
	 * 编码URL
	 *
	 * @param url 需要进行编码的URL
	 * @return 编码后的URL
	 */
	public static String urlEncode(String url)
	{
		try {
	        return URLEncoder.encode(url, "utf8");
        } catch (UnsupportedEncodingException e) {
        	return url;
        }
	}
	
	public static boolean isEmpty(String str)
	{
		if(str==null || "".equalsIgnoreCase(str))
		{
			return true;
		}
		return false;
		
	}
	 
	public static boolean isNumeric(String str){ 
		   Pattern pattern = Pattern.compile("[0-9]*"); 
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
	}
	public static void main(String[] args)
	{
		System.out.println(StringHelper.isNumeric("123"));
	}
	
	/**
	 * 
	 * 把byte[]数组转换成十六进制字符串表示形式
	 * 
	 * @param tmp
	 *            要转换的byte[]
	 * 
	 * @return 十六进制字符串表示形式
	 */

	public static String byteToHexString(byte[] tmp) {

		String s;

		// 用字节表示就是 16 个字节

		char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，

		// 所以表示成 16 进制需要 32 个字符

		int k = 0; // 表示转换结果中对应的字符位置

		for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节

			// 转换成 16 进制字符的转换

			byte byte0 = tmp[i]; // 取第 i 个字节

			str[k++] = hexdigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,

			// >>> 为逻辑右移，将符号位一起右移

			str[k++] = hexdigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换

		}

		s = new String(str); // 换后的结果转换为字符串

		return s;

	}
}
