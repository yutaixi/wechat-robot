package com.wechat.action;

import com.im.base.BaseVO;

public class ActionResponse extends BaseVO{

	private Object requestData1;
	private Object requestData2;
	private Object requestData3;
	private Object requestData4;
	
	private Object responseData;
	
	private  int status;

	 

	public Object getRequestData1() {
		return requestData1;
	}

	public void setRequestData1(Object requestData1) {
		this.requestData1 = requestData1;
	}

	public Object getRequestData2() {
		return requestData2;
	}

	public void setRequestData2(Object requestData2) {
		this.requestData2 = requestData2;
	}

	public Object getRequestData3() {
		return requestData3;
	}

	public void setRequestData3(Object requestData3) {
		this.requestData3 = requestData3;
	}

	public Object getRequestData4() {
		return requestData4;
	}

	public void setRequestData4(Object requestData4) {
		this.requestData4 = requestData4;
	}

	public Object getResponseData() {
		return responseData;
	}

	public void setResponseData(Object responseData) {
		this.responseData = responseData;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
