package com.yyk333.sms.banner.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.yyk333.sms.utils.DateUtil;

@Entity
@Table(name = "yyk_banner")
public class Banner implements Serializable {

	/**
	 * 序列话id
	 */
	private static final long serialVersionUID = -4559760952297668013L;

	/**
	 * id int(11) NOT NULL AUTO_INCREMENT,
	 */
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long banId;

	/**
	 * bannertitle varchar(200) DEFAULT NULL COMMENT '标题',
	 */
	private String bannertitle;

	/**
	 * bannerimg varchar(200) DEFAULT NULL COMMENT 'banner图片',
	 */
	private String bannerimg;

	/**
	 * bannerurl varchar(200) DEFAULT NULL COMMENT '跳转链接',
	 */
	private String bannerurl;

	/**
	 * bannerlocation varchar(1) DEFAULT NULL COMMENT 'banner位置，1、首页，2、商城',
	 */
	private String bannerlocation;

	/**
	 * bannerorder int(11) DEFAULT NULL COMMENT '显示顺序',
	 */
	private Integer bannerorder;

	/**
	 * creater varchar(20) DEFAULT NULL COMMENT '创建人id',
	 */
	private String creater;

	/**
	 * createdate datetime DEFAULT NULL COMMENT '创建时间',
	 */
	private Date createdate;

	/**
	 * status varchar(1) DEFAULT NULL COMMENT '0、启用；1、禁用',
	 */
	private String status;
	
	/**
	 * createdate '创建时间' 的字符串格式,
	 */
	@Transient
	private String createDateStr;

	public Banner() {
		// TODO Auto-generated constructor stub
	}
	
	public void setMore(){
		this.setCreateDateStr(DateUtil.dateToString(this.createdate, "yyyy-MM-dd HH:mm:ss"));
	}

	public Long getBanId() {
		return banId;
	}

	public void setBanId(Long banId) {
		this.banId = banId;
	}

	public String getBannertitle() {
		return bannertitle;
	}

	public void setBannertitle(String bannertitle) {
		this.bannertitle = bannertitle;
	}

	public String getBannerimg() {
		return bannerimg;
	}

	public void setBannerimg(String bannerimg) {
		this.bannerimg = bannerimg;
	}

	public String getBannerurl() {
		return bannerurl;
	}

	public void setBannerurl(String bannerurl) {
		this.bannerurl = bannerurl;
	}

	public String getBannerlocation() {
		return bannerlocation;
	}

	public void setBannerlocation(String bannerlocation) {
		this.bannerlocation = bannerlocation;
	}

	public Integer getBannerorder() {
		return bannerorder;
	}

	public void setBannerorder(Integer bannerorder) {
		this.bannerorder = bannerorder;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	@Override
	public String toString() {
		return "Banner [banId=" + banId + ", bannertitle=" + bannertitle + ", bannerimg=" + bannerimg + ", bannerurl="
				+ bannerurl + ", bannerlocation=" + bannerlocation + ", bannerorder=" + bannerorder + ", creater="
				+ creater + ", createdate=" + createdate + ", status=" + status + ", createDateStr=" + createDateStr
				+ "]";
	}

}
