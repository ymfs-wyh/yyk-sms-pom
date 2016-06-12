package com.yyk333.upacp.pay.vo;

public class FileTransferVO {

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

	// 接入类型，0：直连商户 2：平台类商户接入,必填字段
	private String accessType;
	// 商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号,必填字段
	private String merId;
	// 清算日期 M
	private String settleDate;
	// 12. 订单发送时间
	private String txnTime;
	// 13. 文件类型
	// M 依据实际业务情况定义参考 7.1：商户索取的文件类型约定
	private String fileType;

	// 28. 保留域 选填字段
	private String reserved;

	public FileTransferVO() {
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

	public String getSettleDate() {
		return settleDate;
	}

	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}

	public String getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

}
