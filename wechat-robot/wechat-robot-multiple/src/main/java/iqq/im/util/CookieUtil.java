package iqq.im.util;

import java.util.List;

import iqq.im.http.QQHttpResponse; 
public class CookieUtil {

	public static String getCookie(QQHttpResponse response) {

		List<String> cookies = response.getHeaders("Set-Cookie");
		StringBuffer sBuffer = new StringBuffer();

		for (String value : cookies) {
			if (value == null) {
				continue;
			}
			String cookie = value.substring(0, value.indexOf(";") + 1);
			sBuffer.append(cookie);
		} 
		return sBuffer.toString();
	}
	
}
