package com.yyk333.upacp.pay.vo;

import java.util.HashMap;
import java.util.Map;

public class ResponseViewObject {
	
	private String status;
	private String errCode;
	private String info;
	private Map<String, Object> data = new HashMap<String, Object>();

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
