package com.yyk333.sms.version.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="yyk_version")
public class Version implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5572123205302192264L;
	
	/**
	 * 主键ID，数据库为int (10)
	 */
	@Id
	@GeneratedValue
	private Long id;
	/**
	 * 父ID，数据库为int (11)
	 */
	private Long pid;
	/**
	 * 设备类型 0.ios 1.Android
	 * tinyint (1)
	 * 默认值0
	 */
	@Column(name="equipment_type")
	private Integer equipmentType;
	/**
	 * 版本号 vachar (15)
	 */
	@Column(name="version_code")
	private String versionCode;
	/**
	 * 版本说明 vachar (255)
	 */
	@Column(name="version_detail")
	private String versionDetail;
	/**
	 * 版本类型：0.C端 1.B端 
	 * char (1)
	 * tinyint (1)
	 */
	@Column(name="app_type")
	private Integer appType;
	/**
	 * 更新路径
	 * vachar (255)
	 */
	@Column(name="update_path")
	private String updatePath;
	/**
	 * 添加时间
	 * int (11)
	 */
	@Column(name="add_time")
	private Long addTime;
	/**
	 * 状态0.启用 1.禁用
	 * tinyint (1)
	 */
	private Integer status;
	/**
	 * 更新类型0.普通更新 1.强制更新 2.热更新
	 * tinyint (1)
	 * 默认值0
	 */
	@Column(name="update_type")
	private Integer updateType;
	/**
	 * 更新方式0.自定义 1.低于
	 * tinyint (1)
	 */
	@Column(name="update_way")
	private Integer updateWay;
	/**
	 * 更新版本 0.位全部版本
	 * vachar (255)
	 */
	@Column(name="update_version")
	private String updateVersion;
	
	@Transient
	private String patchesCode;
	@Transient
	private String lastVersion;
	
	public Version() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Integer getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(Integer equipmentType) {
		this.equipmentType = equipmentType;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionDetail() {
		return versionDetail;
	}

	public void setVersionDetail(String versionDetail) {
		this.versionDetail = versionDetail;
	}

	public Integer getAppType() {
		return appType;
	}

	public void setAppType(Integer appType) {
		this.appType = appType;
	}

	public String getUpdatePath() {
		return updatePath;
	}

	public void setUpdatePath(String updatePath) {
		this.updatePath = updatePath;
	}

	public Long getAddTime() {
		return addTime;
	}

	public void setAddTime(Long addTime) {
		this.addTime = addTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getUpdateType() {
		return updateType;
	}

	public void setUpdateType(Integer updateType) {
		this.updateType = updateType;
	}

	public Integer getUpdateWay() {
		return updateWay;
	}

	public void setUpdateWay(Integer updateWay) {
		this.updateWay = updateWay;
	}

	public String getUpdateVersion() {
		return updateVersion;
	}

	public void setUpdateVersion(String updateVersion) {
		this.updateVersion = updateVersion;
	}
	
	public String getPatchesCode() {
		return patchesCode;
	}
	
	public void setPatchesCode(String patchesCode) {
		this.patchesCode = patchesCode;
	}

	@Override
	public String toString() {
		return "Version [id=" + id + ", pid=" + pid + ", equipmentType=" + equipmentType + ", versionCode="
				+ versionCode + ", versionDetail=" + versionDetail + ", appType=" + appType + ", updatePath="
				+ updatePath + ", addTime=" + addTime + ", status=" + status + ", updateType=" + updateType
				+ ", updateWay=" + updateWay + ", updateVersion=" + updateVersion + ", patchesCode=" + patchesCode
				+ "]";
	}
	
}
