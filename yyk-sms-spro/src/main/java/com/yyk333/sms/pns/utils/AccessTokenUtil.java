package com.yyk333.sms.pns.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.yyk333.sms.pns.constants.WeiXinConstants;
import com.yyk333.sms.pns.dto.AccessToken;
import com.yyk333.sms.pns.dto.MyAccessToken;
import com.yyk333.sms.utils.DateUtil;
import com.yyk333.sms.utils.PropertiesUtil;

/**
 * 
 * @Title: AccessTokenUtil.java Copyright: Copyright (c) 2014 Company:杭州宁居科技有限公司
 * 
 * @author 柴观新 2014-4-21 上午10:24:58
 * @version V1.0
 */
public class AccessTokenUtil {

	private static final Logger log = LoggerFactory.getLogger(AccessTokenUtil.class);

	private static AccessToken accessToken = new AccessToken();

	/**
	 * 
	 * init(初始化方法)
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void init() throws Exception {
	}

	/**
	 * 
	 * getAccessToken(获取内存中的AccessToken)
	 * 
	 * @return AccessToken AccessToken
	 */
	public static AccessToken getAccessToken() {
		try {
			MyAccessToken myAccessToken = getAccessTokenByFile();
			if (myAccessToken == null) {
				accessToken = getAccessTokenByUrl();
				log.debug("access_token.json为空时token截止时间到期，根据url获取，有效时间毫秒{} ； token:{}", accessToken.getExpires_in(),
						accessToken.getAccess_token());
				// 保存accessToken到文件
				saveAccessTokenToFile(accessToken);
			} else {
				log.debug("获取access_token成功，有效时间毫秒{} ； token:{}", myAccessToken.getExpire_time(),
						myAccessToken.getAccess_token());
				Long expireTime = DateUtil.convertPHPDateToJava(myAccessToken.getExpire_time()).getTime() - 200 * 1000;
				Long currTime = System.currentTimeMillis();
				if (expireTime <= currTime) {
					accessToken = getAccessTokenByUrl();
					log.debug("token截止时间到期，根据url获取，有效时间毫秒{} ； token:{}", accessToken.getExpires_in(),
							accessToken.getAccess_token());
					// 保存accessToken到文件
					saveAccessTokenToFile(accessToken);
				} else {
					accessToken.setAccess_token(myAccessToken.getAccess_token());
				}
			}
		} catch (Exception e) {
			log.error("AccessTokenUtil:获取AccessToken失败", e);
		}
		return accessToken;
	}

	/**
	 * 
	 * getAccessTokenByUrl(获取到AccessToken对象)
	 *
	 * @Title: getAccessToken
	 * @return AccessToken AccessToken
	 * @throws Exception
	 *             异常
	 */
	public static AccessToken getAccessTokenByUrl() throws Exception {
		// 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&" + "appid="
				+ WeiXinConstants.APPID + "&secret=" + WeiXinConstants.SECRET;
		String thhpRequestStr = HttpRequestUtil.httpRequestSSL(url, "GET", null);
		log.debug("--thhpRequestStr-:" + thhpRequestStr);
		return JSON.parseObject(thhpRequestStr, AccessToken.class);
	}

	/**
	 * 
	 * getAccessTokenByFile(根据文件获取MyAccessToken对象)
	 *
	 * @Title: getAccessTokenByFile
	 * @return MyAccessToken MyAccessToken
	 * @throws Exception
	 *             异常
	 */
	public static MyAccessToken getAccessTokenByFile() throws Exception {
		String tokenFileDir = PropertiesUtil.getProperty("token_dir");
		String tokenJson = FileUtil.readFirstLine(tokenFileDir);
		if (StringUtils.isBlank(tokenJson)) {
			return null;
		}
		return JSON.parseObject(tokenJson, MyAccessToken.class);
	}

	/**
	 * 
	 * saveAccessTokenToFile(把微信的Token保存到文件中)
	 *
	 * @Title: saveAccessTokenToFile
	 * @throws Exception
	 *             异常
	 */
	public static void saveAccessTokenToFile(AccessToken accessToken) throws Exception {
		String tokenFileDir = PropertiesUtil.getProperty("token_dir");
		MyAccessToken myAccessToken = new MyAccessToken();
		myAccessToken.setAccess_token(accessToken.getAccess_token());
		Long curTime = System.currentTimeMillis();
		myAccessToken.setAdd_time(DateUtil.convertDateToPHP(curTime));
		myAccessToken
				.setExpire_time(DateUtil.convertDateToPHP(curTime + Long.valueOf(accessToken.getExpires_in()) * 1000));
		FileUtil.writeFile(tokenFileDir, JSON.toJSONString(myAccessToken));
		log.debug("保存Token到文件成功");
	}

}
