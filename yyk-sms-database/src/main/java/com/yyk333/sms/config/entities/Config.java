package com.yyk333.sms.config.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "yyk_config")
public class Config implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8317919639271099456L;

	@Id
	@GeneratedValue
	private String name;

	private String type;

	private String value;
	
	private String info;
	
	private String desc;
	
	private String tabId;
	
	private String tabName;
	
	private String gid;
	
	private String sort;
	
	private String status;
	
	public Config() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTabId() {
		return tabId;
	}

	public void setTabId(String tabId) {
		this.tabId = tabId;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Config [name=" + name + ", type=" + type + ", value=" + value + ", info=" + info + ", desc=" + desc
				+ ", tabId=" + tabId + ", tabName=" + tabName + ", gid=" + gid + ", sort=" + sort + ", status=" + status
				+ "]";
	}
	
}
