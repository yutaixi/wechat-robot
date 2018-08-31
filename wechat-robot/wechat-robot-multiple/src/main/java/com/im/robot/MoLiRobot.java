package com.im.robot;
 
import com.blade.kit.http.HttpRequest;
 

public class MoLiRobot implements Robot {

	private String apiUrl;

	public MoLiRobot() {
		 
	}

	@Override
	public String talk(String msg) {
		if(null == this.apiUrl){
			return "机器人未配置";
		}
		String url = apiUrl + "&question=" + msg;
		String result = HttpRequest.get(url).connectTimeout(3000).body();
		return result;
	}

}
