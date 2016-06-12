package com.yyk333.sms.banner.dto;

import java.util.Map;

import com.yyk333.sms.banner.entities.Banner;

public class BannerDTO {
	
	private Banner banner;
	
	// 排序设置排序方向和排序字段
	private Map<String, Object> orderMap;

	public BannerDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public Banner getBanner() {
		return banner;
	}

	public void setBanner(Banner banner) {
		this.banner = banner;
	}

	public Map<String, Object> getOrderMap() {
		return orderMap;
	}

	public void setOrderMap(Map<String, Object> orderMap) {
		this.orderMap = orderMap;
	}

	@Override
	public String toString() {
		return "BannerDTO [banner=" + banner + ", orderMap=" + orderMap + "]";
	}

}
