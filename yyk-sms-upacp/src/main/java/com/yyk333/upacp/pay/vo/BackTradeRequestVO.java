package com.yyk333.upacp.pay.vo;

public class BackTradeRequestVO {

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

	// -----------------------后台交易报文头-------------------------

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
	//原始交易流水号 origQryId ,必填字段 原始消费交易的 queryId
	private String origQryId;
	
	// 订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效,必填字段
	private String txnTime;
	// 交易金额，单位分，不要带小数点,必填字段
	private String txnAmt;
	// 26. 终端号
	// 选填字段
	private String termId;
	// 27. 请求方保留域原样返回
	// 选填字段
	// 商户自定义保留域，交易应答时会
	private String reqReserved;
	// 28. 保留域 选填字段
	private String reserved;
	
	public BackTradeRequestVO() {
		// TODO Auto-generated constructor stub
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

	public String getOrigQryId() {
		return origQryId;
	}

	public void setOrigQryId(String origQryId) {
		this.origQryId = origQryId;
	}

	public String getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}

	public String getTxnAmt() {
		return txnAmt;
	}

	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
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
	
}
