package com.yyk333.upacp.pay.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.unionpay.acp.sdk.AcpService;
import com.unionpay.acp.sdk.SDKConfig;
import com.yyk333.upacp.pay.constants.SystemConstants;
import com.yyk333.upacp.pay.services.TradeService;
import com.yyk333.upacp.pay.utils.DateUtil;
import com.yyk333.upacp.pay.utils.IdGenerator;
import com.yyk333.upacp.pay.vo.AuthRequestVO;
import com.yyk333.upacp.pay.vo.BackTradeQueryVO;
import com.yyk333.upacp.pay.vo.BackTradeRequestVO;
import com.yyk333.upacp.pay.vo.FileTransferVO;
import com.yyk333.upacp.pay.vo.FrontTradeRequestVO;
import com.yyk333.upacp.pay.vo.ResponseViewObject;

@Service
public class TradeServiceImpl implements TradeService {

	private static final Logger logger = LoggerFactory.getLogger(TradeServiceImpl.class);

	@Override
	public ResponseViewObject tradeDeal(Map<String, String> reqMap) throws Exception {

		ResponseViewObject object = new ResponseViewObject();

		/** 请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面 **/
		Map<String, String> submitFromData = AcpService.sign(reqMap, SystemConstants.ENCODING_UTF8); // 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。

		String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl(); // 获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
		String html = AcpService.createAutoFormHtml(requestFrontUrl, submitFromData, SystemConstants.ENCODING_UTF8); // 生成自动跳转的Html表单

		object.setStatus("0");
		object.setErrCode("200");
		object.setInfo("交易成功！");
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("html", html);
		object.setData(data);
		logger.debug("打印请求HTML，此为请求报文，为联调排查问题的依据：" + html);
		return object;
	}
	
	@Override
	public ResponseViewObject tradeAppDeal(Map<String, String> reqMap) {
		
		ResponseViewObject object = new ResponseViewObject();
		
		/**对请求参数进行签名并发送http post请求，接收同步应答报文**/
		Map<String, String> reqData = AcpService.sign(reqMap, SystemConstants.ENCODING_UTF8);// 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String requestAppUrl = SDKConfig.getConfig().getAppRequestUrl(); // 交易请求url从配置文件读取对应属性文件acp_sdk.properties中的
		Map<String, String> rspData = AcpService.post(reqData, requestAppUrl, SystemConstants.ENCODING_UTF8);// 发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
		
		logger.debug("打印请求HTML，此为请求报文，为联调排查问题的依据：" + rspData);
		
		/**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
		//应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, SystemConstants.ENCODING_UTF8)){
				logger.debug("验证签名成功");
				String respCode = rspData.get("respCode") ;
				if(("00").equals(respCode)){
					//成功,获取tn号
					String tn = rspData.get("tn");
					object.setStatus("0");
					object.setErrCode("200");
					object.setInfo("验证签名成功！");
					HashMap<String, Object> data = new HashMap<String, Object>();
					data.put("tn", tn);
					object.setData(data);
				}else{
					//其他应答码为失败请排查原因或做失败处理
					object.setStatus("1");
					object.setErrCode("500");
					object.setInfo("验证签名失败！");
					HashMap<String, Object> data = new HashMap<String, Object>();
					data.put("respCode", respCode);
					data.put("respMsg", rspData.get("respMsg"));
					object.setData(data);
				}
			}else{
				//TODO 检查验证签名失败的原因
				object.setStatus("1");
				object.setErrCode("500");
				object.setInfo("验证签名失败！");
			}
		}else{
			//未返回正确的http状态
			logger.debug("未获取到返回报文或返回http状态码非200");
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("未获取到返回报文或返回http状态码非200！");
		}
		return object;
	}
	
	@Override
	public ResponseViewObject tradeCancel(Map<String, String> reqMap) throws Exception {

		ResponseViewObject object = new ResponseViewObject();

		/** 请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文 **/
		Map<String, String> reqData = AcpService.sign(reqMap, SystemConstants.ENCODING_UTF8);// 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String reqUrl = SDKConfig.getConfig().getBackRequestUrl();// 交易请求url从配置文件读取对应属性文件acp_sdk.properties中的
																	// acpsdk.backTransUrl
		// acpsdk.backTransUrl

		Map<String, String> rspData = AcpService.post(reqData, reqUrl, SystemConstants.ENCODING_UTF8);// 发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

		logger.debug("打印请求HTML，此为请求报文，为联调排查问题的依据：" + rspData);

		/** 对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考-------------> **/

		// 应答码规范参考open.unionpay.com帮助中心 下载 产品接口规范 《平台接入接口规范-第5部分-附录》
		if (!rspData.isEmpty()) {
			if (AcpService.validate(rspData, SystemConstants.ENCODING_UTF8)) {
				logger.debug("验证签名成功");
				object.setStatus("0");
				object.setErrCode("200");
				object.setInfo("验证签名成功！");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("rspData", rspData);
				object.setData(data);
			} else {
				logger.debug("验证签名失败");
				// TODO 检查验证签名失败的原因
				object.setStatus("1");
				object.setErrCode("500");
				object.setInfo("验证签名失败！");
			}
		} else {
			// 未返回正确的http状态
			logger.debug("未获取到返回报文或返回http状态码非200");
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("未获取到返回报文或返回http状态码非200！");
		}
		return object;
	}
	
	@Override
	public ResponseViewObject tradeQuery(Map<String, String> reqMap) throws Exception {

		ResponseViewObject object = new ResponseViewObject();

		/** 请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文 **/
		Map<String, String> reqData = AcpService.sign(reqMap, SystemConstants.ENCODING_UTF8);// 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String reqUrl = SDKConfig.getConfig().getSingleQueryUrl();// 交易请求url从配置文件读取对应属性文件acp_sdk.properties中的
																	// acpsdk.singleQueryUrl

		Map<String, String> rspData = AcpService.post(reqData, reqUrl, SystemConstants.ENCODING_UTF8);// 发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

		logger.debug("打印请求HTML，此为请求报文，为联调排查问题的依据：" + rspData);

		/** 对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考-------------> **/

		// 应答码规范参考open.unionpay.com帮助中心 下载 产品接口规范 《平台接入接口规范-第5部分-附录》
		if (!rspData.isEmpty()) {
			if (AcpService.validate(rspData, SystemConstants.ENCODING_UTF8)) {
				logger.debug("验证签名成功");
				object.setStatus("0");
				object.setErrCode("200");
				object.setInfo("验证签名成功！");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("rspData", rspData);
				object.setData(data);
			} else {
				logger.debug("验证签名失败");
				// TODO 检查验证签名失败的原因
				object.setStatus("1");
				object.setErrCode("500");
				object.setInfo("验证签名失败！");
			}
		} else {
			// 未返回正确的http状态
			logger.debug("未获取到返回报文或返回http状态码非200");
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("未获取到返回报文或返回http状态码非200！");
		}
		return object;
	}
	
	@Override
	public ResponseViewObject tradeAuth(Map<String, String> reqMap) throws Exception {
		ResponseViewObject object = new ResponseViewObject();

		/** 请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面 **/
		Map<String, String> submitFromData = AcpService.sign(reqMap, SystemConstants.ENCODING_UTF8); // 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。

		String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl(); // 获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
		String html = AcpService.createAutoFormHtml(requestFrontUrl, submitFromData, SystemConstants.ENCODING_UTF8); // 生成自动跳转的Html表单

		object.setStatus("0");
		object.setErrCode("200");
		object.setInfo("交易成功！");
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("html", html);
		object.setData(data);
		logger.debug("打印请求HTML，此为请求报文，为联调排查问题的依据：" + html);
		return object;
	}
	
	@Override
	public ResponseViewObject fileTrans(Map<String, String> reqMap) throws Exception {
		ResponseViewObject object = new ResponseViewObject();

		/** 请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文 **/
		Map<String, String> reqData = AcpService.sign(reqMap, SystemConstants.ENCODING_UTF8);// 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String url = SDKConfig.getConfig().getFileTransUrl(); // 获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.fileTransUrl

		Map<String, String> rspData = AcpService.post(reqData, url, SystemConstants.ENCODING_UTF8);// 发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

		logger.debug("打印请求HTML，此为请求报文，为联调排查问题的依据：" + rspData);

		/** 对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考-------------> **/

		// 应答码规范参考open.unionpay.com帮助中心 下载 产品接口规范 《平台接入接口规范-第5部分-附录》
		if (!rspData.isEmpty()) {
			if (AcpService.validate(rspData, SystemConstants.ENCODING_UTF8)) {
				logger.debug("验证签名成功");
				String respCode = rspData.get("respCode");
				if ("00".equals(respCode)) {
					// 交易成功，解析返回报文中的fileContent并落地
					AcpService.deCodeFileContent(rspData, SystemConstants.DECODE_FILE_PATH,
							SystemConstants.ENCODING_UTF8);
				}
				object.setStatus("0");
				object.setErrCode("200");
				object.setInfo("验证签名成功！");
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("rspData", rspData);
				object.setData(data);
			} else {
				logger.debug("验证签名失败");
				// TODO 检查验证签名失败的原因
				object.setStatus("1");
				object.setErrCode("500");
				object.setInfo("验证签名失败！");
			}
		} else {
			// 未返回正确的http状态
			logger.debug("未获取到返回报文或返回http状态码非200");
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("未获取到返回报文或返回http状态码非200！");
		}
		return object;
	}
	
	@Override
	public Map<String, String> objectToMap(FrontTradeRequestVO frontTradeVO) throws Exception {

		Map<String, String> requestData = new HashMap<String, String>();
		/*** 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改 ***/
		requestData.put("version", SystemConstants.VERSION); // 版本号，全渠道默认值
		requestData.put("encoding", SystemConstants.ENCODING_UTF8); // 字符集编码，可以使用UTF-8,GBK两种方式
		requestData.put("signMethod", SystemConstants.SIGN_METHOD); // 签名方法，只支持01：RSA方式证书加密

		requestData.put("txnType", frontTradeVO.getTxnType()); // 交易类型，01：消费
		requestData.put("txnSubType", frontTradeVO.getTxnSubType()); // 交易子类型，01：自助消费

		requestData.put("bizType", frontTradeVO.getBizType()); // 产品类型，B2C网关支付，手机wap支付

		// 渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机
		requestData.put("channelType", frontTradeVO.getChannelType());

		// 前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面
		// 点击“返回商户”按钮的时候将异步通知报文post到该地址
		// 如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
		// 异步通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知
		requestData.put("frontUrl", frontTradeVO.getFrontUrl());

		// 后台通知地址（需设置为【外网】能访问 http
		// https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
		// 后台通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知
		// 注意:1.需设置为外网能访问，否则收不到通知 2.http https均可
		// 3.收单后台通知后需要10秒内返回http200或302状态码
		// 4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
		// 5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d
		// 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
		requestData.put("backUrl", frontTradeVO.getBackUrl());

		/*** 商户接入参数 ***/

		// 接入类型，0：直连商户 2:平台类商户接入
		requestData.put("accessType", frontTradeVO.getAccessType()); 
		// 商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
		requestData.put("merId", frontTradeVO.getMerId()); 

		if ("2".equals(frontTradeVO.getAccessType())) {// 平台类商户接入
			requestData.put("subMerId", frontTradeVO.getSubMerId()); // 二级商户代码

			requestData.put("subMerName", frontTradeVO.getSubMerName()); // 二级商户全称

			requestData.put("subMerAbbr", frontTradeVO.getSubMerAbbr()); // 二级商户简称
		}
		
		requestData.put("orderId", IdGenerator.getId()); // 商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
		requestData.put("txnTime", DateUtil.getDate()); // 订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效

		requestData.put("currencyCode", frontTradeVO.getCurrencyCode()); // 交易币种（境内商户一般是156
		// 人民币）

		requestData.put("txnAmt", frontTradeVO.getTxnAmt()); // 交易金额，单位分，不要带小数点

		return requestData;
	}

	@Override
	public ResponseViewObject paramsValidate(FrontTradeRequestVO frontTradeVO) throws Exception {

		ResponseViewObject object = new ResponseViewObject();

		if (StringUtils.isBlank(frontTradeVO.getTxnType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易类型不能为空！");
			return object;
		}

		if (StringUtils.isBlank(frontTradeVO.getTxnSubType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易子类型不能为空！");
			return object;
		}

		if (StringUtils.isBlank(frontTradeVO.getBizType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("业务类型不能为空！");
			return object;
		}

		if (StringUtils.isBlank(frontTradeVO.getChannelType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("渠道类型不能为空！");
			return object;
		}

		if (StringUtils.isBlank(frontTradeVO.getMerId())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("商户号码不能为空！");
			return object;
		}

		if (StringUtils.isBlank(frontTradeVO.getAccessType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("接入类型不能为空！");
			return object;
		}

		if ("2".equals(frontTradeVO.getAccessType())) {// 平台类商户接入
			if (StringUtils.isBlank(frontTradeVO.getSubMerId())) {
				object.setStatus("1");
				object.setErrCode("500");
				object.setInfo("二级商户代码不能为空！");
				return object;
			}

			if (StringUtils.isBlank(frontTradeVO.getSubMerName())) {
				object.setStatus("1");
				object.setErrCode("500");
				object.setInfo("二级商户全称不能为空！");
				return object;
			}

			if (StringUtils.isBlank(frontTradeVO.getSubMerAbbr())) {
				object.setStatus("1");
				object.setErrCode("500");
				object.setInfo("二级商户简称不能为空！");
				return object;
			}
		}

		if (StringUtils.isBlank(frontTradeVO.getCurrencyCode())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易币种不能为空！");
			return object;
		}

		if (StringUtils.isBlank(frontTradeVO.getTxnAmt())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易金额不能为空！");
			return object;
		}
		
		if (StringUtils.isBlank(frontTradeVO.getFrontUrl())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("前台通知地址不能为空！");
			return object;
		}
		
		if (StringUtils.isBlank(frontTradeVO.getBackUrl())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("后台通知地址不能为空！");
			return object;
		}
		
		/*
		 * if (StringUtils.isBlank(gatewaysTradeQueryVO.getOrderId())) {
		 * object.setStatus("1"); object.setErrCode("500");
		 * object.setInfo("商户订单号不能为空！"); return object; }
		 */

		return object;
	}
	
	@Override
	public Map<String, String> objectToMap(BackTradeRequestVO backTradeRequestVO) throws Exception {

		Map<String, String> requestData = new HashMap<String, String>();
		/*** 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改 ***/
		requestData.put("version", SystemConstants.VERSION); // 版本号，全渠道默认值
		requestData.put("encoding", SystemConstants.ENCODING_UTF8); // 字符集编码，可以使用UTF-8,GBK两种方式
		requestData.put("signMethod", SystemConstants.SIGN_METHOD); // 签名方法，只支持01：RSA方式证书加密

		requestData.put("txnType", backTradeRequestVO.getTxnType()); // 交易类型，01：消费
		requestData.put("txnSubType", backTradeRequestVO.getTxnSubType()); // 交易子类型，01：自助消费

		requestData.put("bizType", backTradeRequestVO.getBizType()); // 产品类型，B2C网关支付，手机wap支付

		// 渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机
		requestData.put("channelType", backTradeRequestVO.getChannelType());

		// 后台通知地址（需设置为【外网】能访问 http
		// https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
		// 后台通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知
		// 注意:1.需设置为外网能访问，否则收不到通知 2.http https均可
		// 3.收单后台通知后需要10秒内返回http200或302状态码
		// 4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
		// 5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d
		// 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
		requestData.put("backUrl", backTradeRequestVO.getBackUrl());

		/*** 商户接入参数 ***/

		// 接入类型，0：直连商户 2:平台类商户接入
		requestData.put("accessType", backTradeRequestVO.getAccessType());
		// 商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
		requestData.put("merId", backTradeRequestVO.getMerId());

		if ("2".equals(backTradeRequestVO.getAccessType())) {// 平台类商户接入
			requestData.put("subMerId", backTradeRequestVO.getSubMerId()); // 二级商户代码

			requestData.put("subMerName", backTradeRequestVO.getSubMerName()); // 二级商户全称

			requestData.put("subMerAbbr", backTradeRequestVO.getSubMerAbbr()); // 二级商户简称
		}

		requestData.put("orderId", backTradeRequestVO.getOrderId()); // 商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
		requestData.put("origQryId", backTradeRequestVO.getOrigQryId()); // 原始消费交易的
																			// queryId
		requestData.put("txnTime", backTradeRequestVO.getTxnTime()); // 订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效

		requestData.put("txnAmt", backTradeRequestVO.getTxnAmt()); // 交易金额，单位分，不要带小数点

		return requestData;
	}

	@Override
	public ResponseViewObject paramsValidate(BackTradeRequestVO backTradeRequestVO) throws Exception {

		ResponseViewObject object = new ResponseViewObject();

		if (StringUtils.isBlank(backTradeRequestVO.getTxnType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易类型不能为空！");
			return object;
		}

		if (StringUtils.isBlank(backTradeRequestVO.getTxnSubType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易子类型不能为空！");
			return object;
		}

		if (StringUtils.isBlank(backTradeRequestVO.getBizType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("业务类型不能为空！");
			return object;
		}

		if (StringUtils.isBlank(backTradeRequestVO.getChannelType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("渠道类型不能为空！");
			return object;
		}

		if (StringUtils.isBlank(backTradeRequestVO.getMerId())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("商户号码不能为空！");
			return object;
		}

		if (StringUtils.isBlank(backTradeRequestVO.getAccessType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("接入类型不能为空！");
			return object;
		}

		if ("2".equals(backTradeRequestVO.getAccessType())) {// 平台类商户接入
			if (StringUtils.isBlank(backTradeRequestVO.getSubMerId())) {
				object.setStatus("1");
				object.setErrCode("500");
				object.setInfo("二级商户代码不能为空！");
				return object;
			}

			if (StringUtils.isBlank(backTradeRequestVO.getSubMerName())) {
				object.setStatus("1");
				object.setErrCode("500");
				object.setInfo("二级商户全称不能为空！");
				return object;
			}

			if (StringUtils.isBlank(backTradeRequestVO.getSubMerAbbr())) {
				object.setStatus("1");
				object.setErrCode("500");
				object.setInfo("二级商户简称不能为空！");
				return object;
			}
		}

		if (StringUtils.isBlank(backTradeRequestVO.getTxnAmt())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易金额不能为空！");
			return object;
		}

		if (StringUtils.isBlank(backTradeRequestVO.getOrderId())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("订单号不能为空！");
			return object;
		}

		if (StringUtils.isBlank(backTradeRequestVO.getOrigQryId())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("原始交易流水号不能为空！");
			return object;
		}

		if (StringUtils.isBlank(backTradeRequestVO.getTxnTime())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("订单发送时间不能为空！");
			return object;
		}

		if (StringUtils.isBlank(backTradeRequestVO.getBackUrl())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("后台通知地址不能为空！");
			return object;
		}

		/*
		 * if (StringUtils.isBlank(gatewaysTradeQueryVO.getOrderId())) {
		 * object.setStatus("1"); object.setErrCode("500");
		 * object.setInfo("商户订单号不能为空！"); return object; }
		 */

		return object;
	}

	@Override
	public ResponseViewObject paramsQueryValidate(BackTradeQueryVO backTradeQueryVO) throws Exception {
		ResponseViewObject object = new ResponseViewObject();

		if (StringUtils.isBlank(backTradeQueryVO.getTxnType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易类型不能为空！");
			return object;
		}

		if (StringUtils.isBlank(backTradeQueryVO.getTxnSubType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易子类型不能为空！");
			return object;
		}

		if (StringUtils.isBlank(backTradeQueryVO.getBizType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("业务类型不能为空！");
			return object;
		}

		if (StringUtils.isBlank(backTradeQueryVO.getAccessType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("接入类型不能为空！");
			return object;
		}

		if (StringUtils.isBlank(backTradeQueryVO.getMerId())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("商户号码不能为空！");
			return object;
		}

		if (StringUtils.isBlank(backTradeQueryVO.getQueryId()) && (StringUtils.isBlank(backTradeQueryVO.getOrderId())
				|| StringUtils.isBlank(backTradeQueryVO.getTxnTime()))) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易查询流水号与订单号和订单交易时间不能同时为空！");
			return object;
		}

		/*
		 * if (StringUtils.isBlank(gatewaysTradeQueryVO.getOrderId())) {
		 * object.setStatus("1"); object.setErrCode("500");
		 * object.setInfo("商户订单号不能为空！"); return object; }
		 */

		return object;
	}

	@Override
	public Map<String, String> queryVOToMap(BackTradeQueryVO backTradeQueryVO) throws Exception {
		Map<String, String> requestData = new HashMap<String, String>();
		/*** 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改 ***/
		requestData.put("version", SystemConstants.VERSION); // 版本号，全渠道默认值
		requestData.put("encoding", SystemConstants.ENCODING_UTF8); // 字符集编码，可以使用UTF-8,GBK两种方式
		requestData.put("signMethod", SystemConstants.SIGN_METHOD); // 签名方法，只支持01：RSA方式证书加密

		requestData.put("txnType", backTradeQueryVO.getTxnType()); // 交易类型，01：消费
		requestData.put("txnSubType", backTradeQueryVO.getTxnSubType()); // 交易子类型，01：自助消费

		requestData.put("bizType", backTradeQueryVO.getBizType()); // 产品类型，B2C网关支付，手机wap支付

		/*** 商户接入参数 ***/

		// 接入类型，0：直连商户 2:平台类商户接入
		requestData.put("accessType", backTradeQueryVO.getAccessType());
		// 商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
		requestData.put("merId", backTradeQueryVO.getMerId());

		requestData.put("orderId", backTradeQueryVO.getOrderId()); // 商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
																	// queryId
		requestData.put("txnTime", backTradeQueryVO.getTxnTime()); // 订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效

		if (StringUtils.isNotBlank(backTradeQueryVO.getQueryId())) {
			requestData.put("queryId", backTradeQueryVO.getQueryId()); // 交易查询流水号
		}

		return requestData;
	}

	@Override
	public ResponseViewObject paramsAuthValidate(AuthRequestVO authRequestVO) throws Exception {
		ResponseViewObject object = new ResponseViewObject();

		if (StringUtils.isBlank(authRequestVO.getTxnType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易类型不能为空！");
			return object;
		}

		if (StringUtils.isBlank(authRequestVO.getTxnSubType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易子类型不能为空！");
			return object;
		}

		if (StringUtils.isBlank(authRequestVO.getBizType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("业务类型不能为空！");
			return object;
		}

		if (StringUtils.isBlank(authRequestVO.getChannelType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("渠道类型不能为空！");
			return object;
		}

		if (StringUtils.isBlank(authRequestVO.getMerId())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("商户号码不能为空！");
			return object;
		}

		if (StringUtils.isBlank(authRequestVO.getAccessType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("接入类型不能为空！");
			return object;
		}

		if ("2".equals(authRequestVO.getAccessType())) {// 平台类商户接入
			if (StringUtils.isBlank(authRequestVO.getSubMerId())) {
				object.setStatus("1");
				object.setErrCode("500");
				object.setInfo("二级商户代码不能为空！");
				return object;
			}

			if (StringUtils.isBlank(authRequestVO.getSubMerName())) {
				object.setStatus("1");
				object.setErrCode("500");
				object.setInfo("二级商户全称不能为空！");
				return object;
			}

			if (StringUtils.isBlank(authRequestVO.getSubMerAbbr())) {
				object.setStatus("1");
				object.setErrCode("500");
				object.setInfo("二级商户简称不能为空！");
				return object;
			}
		}

		if (StringUtils.isBlank(authRequestVO.getCurrencyCode())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易币种不能为空！");
			return object;
		}

		if (StringUtils.isBlank(authRequestVO.getTxnAmt())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易金额不能为空！");
			return object;
		}

		if (StringUtils.isBlank(authRequestVO.getBackUrl())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("后台通知地址不能为空！");
			return object;
		}

		/*
		 * if (StringUtils.isBlank(gatewaysTradeQueryVO.getOrderId())) {
		 * object.setStatus("1"); object.setErrCode("500");
		 * object.setInfo("商户订单号不能为空！"); return object; }
		 */

		return object;
	}

	@Override
	public Map<String, String> authVOToMap(AuthRequestVO authRequestVO) throws Exception {
		Map<String, String> requestData = new HashMap<String, String>();
		/*** 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改 ***/
		requestData.put("version", SystemConstants.VERSION); // 版本号，全渠道默认值
		requestData.put("encoding", SystemConstants.ENCODING_UTF8); // 字符集编码，可以使用UTF-8,GBK两种方式
		requestData.put("signMethod", SystemConstants.SIGN_METHOD); // 签名方法，只支持01：RSA方式证书加密

		requestData.put("txnType", authRequestVO.getTxnType()); // 交易类型，01：消费
		requestData.put("txnSubType", authRequestVO.getTxnSubType()); // 交易子类型，01：自助消费

		requestData.put("bizType", authRequestVO.getBizType()); // 产品类型，B2C网关支付，手机wap支付

		// 渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机
		requestData.put("channelType", authRequestVO.getChannelType());
		
		if (StringUtils.isNotBlank(authRequestVO.getFrontUrl())) {
			requestData.put("frontUrl", authRequestVO.getFrontUrl());
		}
		// 后台通知地址（需设置为【外网】能访问 http
		// https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
		// 后台通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知
		// 注意:1.需设置为外网能访问，否则收不到通知 2.http https均可
		// 3.收单后台通知后需要10秒内返回http200或302状态码
		// 4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
		// 5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d
		// 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
		requestData.put("backUrl", authRequestVO.getBackUrl());

		/*** 商户接入参数 ***/

		// 接入类型，0：直连商户 2:平台类商户接入
		requestData.put("accessType", authRequestVO.getAccessType());
		// 商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
		requestData.put("merId", authRequestVO.getMerId());

		if ("2".equals(authRequestVO.getAccessType())) {// 平台类商户接入
			requestData.put("subMerId", authRequestVO.getSubMerId()); // 二级商户代码

			requestData.put("subMerName", authRequestVO.getSubMerName()); // 二级商户全称

			requestData.put("subMerAbbr", authRequestVO.getSubMerAbbr()); // 二级商户简称
		}

		requestData.put("orderId", IdGenerator.getId()); // 商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
		requestData.put("txnTime", DateUtil.getDate()); // 订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效

		requestData.put("currencyCode", authRequestVO.getCurrencyCode()); // 交易币种（境内商户一般是156
		// 人民币）

		requestData.put("txnAmt", authRequestVO.getTxnAmt()); // 交易金额，单位分，不要带小数点

		return requestData;
	}

	@Override
	public ResponseViewObject paramsFileValidate(FileTransferVO fileTransferVO) throws Exception {
		
		ResponseViewObject object = new ResponseViewObject();
		
		if (StringUtils.isBlank(fileTransferVO.getTxnType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易类型不能为空！");
			return object;
		}
		// ，01：消费
		if (StringUtils.isBlank(fileTransferVO.getTxnSubType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("交易子类型不能为空！");
			return object;
		}
		// 01：自助消费
		if (StringUtils.isBlank(fileTransferVO.getBizType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("业务类型不能为空！");
			return object;
		}
		

		/*** 商户接入参数 ***/
		if (StringUtils.isBlank(fileTransferVO.getMerId())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("商户号码不能为空！");
			return object;
		}
		
		if (StringUtils.isBlank(fileTransferVO.getAccessType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("接入类型不能为空！");
			return object;
		}
		
		if (StringUtils.isBlank(fileTransferVO.getSettleDate())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("清算日期不能为空！");
			return object;
		}
		if (StringUtils.isBlank(fileTransferVO.getFileType())) {
			object.setStatus("1");
			object.setErrCode("500");
			object.setInfo("文件类型不能为空！");
			return object;
		}
		return null;
	}

	@Override
	public Map<String, String> fileObjToMap(FileTransferVO fileTransferVO) throws Exception {
		Map<String, String> requestData = new HashMap<String, String>();
		/*** 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改 ***/
		requestData.put("version", SystemConstants.VERSION); // 版本号，全渠道默认值
		requestData.put("encoding", SystemConstants.ENCODING_UTF8); // 字符集编码，可以使用UTF-8,GBK两种方式
		requestData.put("signMethod", SystemConstants.SIGN_METHOD); // 签名方法，只支持01：RSA方式证书加密
		requestData.put("txnType", fileTransferVO.getTxnType()); // 交易类型
		requestData.put("txnSubType", fileTransferVO.getTxnSubType()); // 交易子类型，
		
		requestData.put("bizType", fileTransferVO.getBizType()); // 业务类型，B2C网关支付，手机wap支付
		requestData.put("accessType", fileTransferVO.getAccessType()); // 接入类型，0：直连商户
		requestData.put("merId", fileTransferVO.getMerId()); // 商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
		
		requestData.put("settleDate", fileTransferVO.getSettleDate()); // 清算日期，如果使用正式商户号测试则要修改成自己想要获取对账文件的日期，
		// 测试环境如果使用700000000000001商户号则固定填写0119
		requestData.put("txnTime", DateUtil.getDate()); // 订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		requestData.put("fileType", fileTransferVO.getFileType()); // 文件类型，一般商户填写00即可
		
		return requestData;

	}

	

}
