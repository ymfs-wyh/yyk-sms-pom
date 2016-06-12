package com.yyk333.upacp.pay.vo;

public class AuthRequestVO {

	// -----------------------通用报文头-------------------------

	// 版本号，全渠道默认值 ，必填字段
	private String version;
	// 字符集编码，可以使用UTF-8,GBK两种方式 ，必填字段
	private String encoding;
	// 证书 ID ，必填字段
	private String certId;
	// 签名 ，必填字段
	private String signature;
	// 签名方法，只支持 01：RSA方式证书加密 ，必填字段
	private String signMethod;
	// 交易类型 ，01：消费 ，必填字段
	private String txnType;
	// 交易子类型， 01：自助消费 ，必填字段
	private String txnSubType;
	// 产品类型，B2C网关支付，手机wap支付 ，必填字段
	private String bizType;
	// 渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机 ，必填字段
	private String channelType;

	// 前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
	// 前台返回商户结果时使用，前台类交易需上送
	private String frontUrl;

	// 后台通知地址（需设置为【外网】能访问
	// http\https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
	// 后台返回商户结果时使用，如上送，则发送商户后台交易结果通知
	private String backUrl;

	// 接入类型，0：直连商户 2：平台类商户接入,必填字段
	private String accessType;
	// 商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号,必填字段
	private String merId;

	// 二级商户代码,条件字段
	private String subMerId;
	// 二级商户全称,条件字段
	private String subMerName;
	// 二级商户简称,条件字段
	private String subMerAbbr;

	// 商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则,必填字段
	private String orderId;
	// 订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效,必填字段
	private String txnTime;
	// 后台类交易且卡号上送；跨行收单且收单机构收集银行卡信息时上送,条件字段
	// 01：银行卡
	// 02：存折
	// 03： IC 卡
	// 默认取值： 01
	// 取值“03”表示以 IC 终端发起的 IC
	// 卡交易， IC 作为普通银行卡进行支
	// 付时，此域填写为“01”
	private String accType;
	// 条件字段
	// 1、 后台类消费交易时上送全卡号或卡号后 4 位
	// 2、 跨行收单且收单机构收集银行信息时上送、
	// 3、前台类交易可通过配置后返回，卡号可选上送
	private String accNo;

	// 交易金额，单位分，不要带小数点,必填字段
	private String txnAmt;
	// 交易币种（境内商户一般是156 人民币）,必填字段
	private String currencyCode;

	// 条件字段，银行卡验证信息及身份信息
	// 1、后台类消费交易时上送
	// 2、认证支付 2.0，后台交易时可选
	// Key=value 格式
	// （具体填写参考数据元说明）
	private String customerInfo;

	// 26. 终端号
	// 选填字段
	private String termId;
	// 27. 请求方保留域原样返回
	// 选填字段
	// 商户自定义保留域，交易应答时会
	private String reqReserved;
	// 28. 保留域 选填字段
	private String reserved;
	// 29. 风险信息域 选填字段
	private String riskRateInfo;
	// 30. 加密证书 ID 条件字段
	private String encryptCertId;
	// 32. 分期付款信息域息时，需上送组合域，填法见数据元说明
	// 条件字段
	// 分期付款交易，商户端选择分期信
	private String instalTransInfo;
	// 34. 发卡机构代码
	// 条件字段
	// 1、 当账号类型为 02-存折时需填写
	// 2、 在前台类交易时填写默认银行
	// 代码，支持直接跳转到网银。 银行
	// 简码列表参考附录： C.1、 C.2， 其
	// 中 C.2 银行列表仅支持借记卡
	private String issInsCode;
	// 36. 终端信息域
	// 选填字段
	// 移动支付业务需要上送
	private String userMac;
	// 37. 持卡人 IP
	// 条件字段
	// 前台交易，有 IP 防钓鱼要求的商户
	// 上送
	private String customerIp;
	// 绑定标识号
	// O 绑定消费 需做绑定时填写 用于唯一标识绑定关系
	private String bindId;
	// 支付卡类型
	// C 绑定消费
	// 特殊商户交易控制用（如借贷分
	// 离
	private String payCardType;
	// 38. 有卡交易信息域
	// 条件字段
	// 有卡交易必填有卡交易信息域
	private String cardTransData;
	// 39. 订单描述
	// 条件字段 移条件字段支付上送
	private String orderDesc;

	public AuthRequestVO() {

	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getCertId() {
		return certId;
	}

	public void setCertId(String certId) {
		this.certId = certId;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignMethod() {
		return signMethod;
	}

	public void setSignMethod(String signMethod) {
		this.signMethod = signMethod;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getTxnSubType() {
		return txnSubType;
	}

	public void setTxnSubType(String txnSubType) {
		this.txnSubType = txnSubType;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	
	public String getFrontUrl() {
		return frontUrl;
	}
	
	public void setFrontUrl(String frontUrl) {
		this.frontUrl = frontUrl;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getSubMerId() {
		return subMerId;
	}

	public void setSubMerId(String subMerId) {
		this.subMerId = subMerId;
	}

	public String getSubMerName() {
		return subMerName;
	}

	public void setSubMerName(String subMerName) {
		this.subMerName = subMerName;
	}

	public String getSubMerAbbr() {
		return subMerAbbr;
	}

	public void setSubMerAbbr(String subMerAbbr) {
		this.subMerAbbr = subMerAbbr;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getTxnAmt() {
		return txnAmt;
	}

	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(String customerInfo) {
		this.customerInfo = customerInfo;
	}

	public String getTermId() {
		return termId;
	}

	public void setTermId(String termId) {
		this.termId = termId;
	}

	public String getReqReserved() {
		return reqReserved;
	}

	public void setReqReserved(String reqReserved) {
		this.reqReserved = reqReserved;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public String getRiskRateInfo() {
		return riskRateInfo;
	}

	public void setRiskRateInfo(String riskRateInfo) {
		this.riskRateInfo = riskRateInfo;
	}

	public String getEncryptCertId() {
		return encryptCertId;
	}

	public void setEncryptCertId(String encryptCertId) {
		this.encryptCertId = encryptCertId;
	}

	public String getInstalTransInfo() {
		return instalTransInfo;
	}

	public void setInstalTransInfo(String instalTransInfo) {
		this.instalTransInfo = instalTransInfo;
	}

	public String getIssInsCode() {
		return issInsCode;
	}

	public void setIssInsCode(String issInsCode) {
		this.issInsCode = issInsCode;
	}

	public String getUserMac() {
		return userMac;
	}

	public void setUserMac(String userMac) {
		this.userMac = userMac;
	}

	public String getCustomerIp() {
		return customerIp;
	}

	public void setCustomerIp(String customerIp) {
		this.customerIp = customerIp;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public String getPayCardType() {
		return payCardType;
	}

	public void setPayCardType(String payCardType) {
		this.payCardType = payCardType;
	}

	public String getCardTransData() {
		return cardTransData;
	}

	public void setCardTransData(String cardTransData) {
		this.cardTransData = cardTransData;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

}
