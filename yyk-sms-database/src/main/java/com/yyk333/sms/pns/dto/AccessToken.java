package com.yyk333.sms.pns.dto;

import java.io.Serializable;
/**
 * 
 * @Title: AccessToken.java  
 * Copyright: Copyright (c) 2014 
 * Company:杭州宁居科技有限公司
 * 
 * @author 柴观新
 * 2014-4-21 上午10:24:58
 * @version V1.0
 */
public class AccessToken implements Serializable {

    /** */
    private static final long serialVersionUID = -3623391096483885039L;

    /** 获取到的凭证 */
    private String access_token;

    /** 凭证有效时间，单位：秒 */
    private String expires_in;
    
    private String refresh_token;
    
    private String openid;
    
    private String scope;
    
    private String errcode;
    
    private String errmsg;

    @Override
    public String toString() {
        return "AccessToken [access_token=" + access_token + ", expires_in=" + expires_in + "]";
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

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
