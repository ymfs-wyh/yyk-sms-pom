package com.yyk333.sms.pns.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * 证书信任管理器（用于https请求）
 * 
 * @Title: MyX509TrustManager.java Copyright: Copyright (c) 2014
 *         Company:杭州宁居科技有限公司
 * 
 * @author 柴观新 2014-4-21 上午11:16:11
 * @version V1.0
 */
public class MyX509TrustManager implements X509TrustManager {
    /**
     * @param chain X509Certificate[]
     * @param authType String
     * @throws CertificateException CertificateException
     */
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }
    
    /**
     * @param chain X509Certificate[]
     * @param authType String
     * @throws CertificateException CertificateException
     */
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
