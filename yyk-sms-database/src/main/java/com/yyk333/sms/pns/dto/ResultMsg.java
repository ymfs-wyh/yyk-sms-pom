package com.yyk333.sms.pns.dto;
/**
 * 封装提交post返回信息
 * @author 柴观新
 *
 */
public class ResultMsg {
	private String errcode;
	private String errmsg;

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

}
