package com.yyk333.sms.knowledge.dto;

import java.util.Map;

public class ArticleQueryDTO {

	// 文章id
	private Long artId;
	// 用户id
	private Long userId;
	// 文章分类id
	private Long artCatId;
	// 文章启用状态
	private Integer status;
	// 操作类型1收藏，2分享
	private Integer optType;
	// 文章搜索关键字
	private String keyWord;
	// 排序设置排序方向和排序字段
	private Map<String, Object> orderMap;
	// 分页功能当前页
	private Integer pageNo;
	// 分页功能分页大小
	private Integer pageSize;

	public Long getArtId() {
		return artId;
	}

	public void setArtId(Long artId) {
		this.artId = artId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getArtCatId() {
		return artCatId;
	}

	public void setArtCatId(Long artCatId) {
		this.artCatId = artCatId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOptType() {
		return optType;
	}

	public void setOptType(Integer optType) {
		this.optType = optType;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public Map<String, Object> getOrderMap() {
		return orderMap;
	}

	public void setOrderMap(Map<String, Object> orderMap) {
		this.orderMap = orderMap;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return "ArticleQueryDTO [artId=" + artId + ", userId=" + userId + ", artCatId=" + artCatId + ", status="
				+ status + ", keyWord=" + keyWord + ", orderMap=" + orderMap + ", pageNo=" + pageNo + ", pageSize="
				+ pageSize + "]";
	}

}
