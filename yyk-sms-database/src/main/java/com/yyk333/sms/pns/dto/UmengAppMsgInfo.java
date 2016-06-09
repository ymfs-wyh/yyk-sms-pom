package com.yyk333.sms.pns.dto;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;


public class UmengAppMsgInfo {

	private String appKey;
	private Long timestamp = System.currentTimeMillis();
	private String type;
	private String deviceTokens;
	private String aliasType;
	private String alias;
	private String fileId;
	private String filter;
	private String payload;
	private String policy;
	private String productionMode;
	private String description;
	private String thirdpartyId;
	private String appMasterSecret;

	public UmengAppMsgInfo() {

	}

	public Properties getProperties() {

		Properties pro = new Properties();
		if (StringUtils.isNotBlank(appKey)) {
			pro.put("appkey", this.appKey);
		}
		pro.put("timestamp", this.timestamp);
		if (StringUtils.isNotBlank(type)) {
			pro.put("type", this.type);
		}
		if (StringUtils.isNotBlank(deviceTokens)) {
			pro.put("device_tokens", this.deviceTokens);
		}
		if (StringUtils.isNotBlank(aliasType)) {
			pro.put("alias_type", this.aliasType);
		}
		if (StringUtils.isNotBlank(alias)) {
			pro.put("alias", this.alias);
		}
		if (StringUtils.isNotBlank(fileId)) {
			pro.put("file_id", this.fileId);
		}
		if (StringUtils.isNotBlank(filter)) {
			pro.put("filter", JSON.parseObject(this.filter, Map.class));
		}
		if (StringUtils.isNotBlank(payload)) {
			pro.put("payload", JSON.parseObject(this.payload, Map.class));
		}
		if (StringUtils.isNotBlank(policy)) {
			pro.put("policy", JSON.parseObject(this.policy, Map.class));
		}
		if (StringUtils.isNotBlank(productionMode)) {
			pro.put("production_mode", this.productionMode);
		}
		if (StringUtils.isNotBlank(description)) {
			pro.put("description", this.description);
		}
		if (StringUtils.isNotBlank(thirdpartyId)) {
			pro.put("thirdparty_id", this.thirdpartyId);
		}
		return pro;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeviceTokens() {
		return deviceTokens;
	}

	public void setDeviceTokens(String deviceTokens) {
		this.deviceTokens = deviceTokens;
	}

	public String getAliasType() {
		return aliasType;
	}

	public void setAliasType(String aliasType) {
		this.aliasType = aliasType;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getProductionMode() {
		return productionMode;
	}

	public void setProductionMode(String productionMode) {
		this.productionMode = productionMode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThirdpartyId() {
		return thirdpartyId;
	}

	public void setThirdpartyId(String thirdpartyId) {
		this.thirdpartyId = thirdpartyId;
	}

	public String getAppMasterSecret() {
		return appMasterSecret;
	}

	public void setAppMasterSecret(String appMasterSecret) {
		this.appMasterSecret = appMasterSecret;
	}

	@Override
	public String toString() {
		return "UmengAppMsgInfo [appKey=" + appKey + ", timestamp=" + timestamp + ", type=" + type + ", deviceTokens="
				+ deviceTokens + ", aliasType=" + aliasType + ", alias=" + alias + ", fileId=" + fileId + ", filter="
				+ filter + ", payload=" + payload + ", policy=" + policy + ", productionMode=" + productionMode
				+ ", description=" + description + ", thirdpartyId=" + thirdpartyId + ", appMasterSecret="
				+ appMasterSecret + "]";
	}

}
