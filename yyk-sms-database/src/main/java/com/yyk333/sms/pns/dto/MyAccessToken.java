package com.yyk333.sms.pns.dto;

import java.io.Serializable;
/**
 * 
 * @Title: MyAccessToken.java  
 * Copyright: Copyright (c) 2014 
 * Company:杭州宁居科技有限公司
 * 
 * @author 柴观新
 * 2014-4-21 上午10:24:58
 * @version V1.0
 */
public class MyAccessToken implements Serializable {

    /** */
    private static final long serialVersionUID = -3623391096483885039L;

    /** 获取到的凭证 */
    private String access_token;

    /** 凭证有效时间，单位：秒 */
    private Long expire_time;
    
    private Long add_time;

    @Override
    public String toString() {
        return "AccessToken [access_token=" + access_token + ", expires_in=" + expire_time + "]";
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

	public Long getExpire_time() {
		return expire_time;
	}

	public void setExpire_time(Long expire_time) {
		this.expire_time = expire_time;
	}

	public Long getAdd_time() {
		return add_time;
	}

	public void setAdd_time(Long add_time) {
		this.add_time = add_time;
	}
    
}
