package com.yyk333.sms.knowledge.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "yyk_art_hot_sea")
public class ArticleSearchRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8822796839499841090L;

	/**
	 * id int(11) NOT NULL COMMENT '文章id，主键，自增',
	 */
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long artSeaRecId;

	/**
	 * keyWord varchar(255) NOT NULL COMMENT '关键字',
	 */
	private String keyWord;

	/**
	 * user_id int(11) COMMENT '用户id',
	 */
	private Long userId;
	
	/**
	 * search_count int(11) COMMENT '关键字搜索次数',
	 */
	private Long searchCount;
	
	/**
	 * create_time int(11) NOT NULL COMMENT '创建时间，默认系统当前时间',
	 */
	private Long createTime;

	/**
	 * update_time int(11) NOT NULL COMMENT '更新时间，默认当前时间',
	 */
	private Long updateTime;

	public ArticleSearchRecord() {
		// TODO Auto-generated constructor stub
	}

	public Long getArtSeaRecId() {
		return artSeaRecId;
	}

	public void setArtSeaRecId(Long artSeaRecId) {
		this.artSeaRecId = artSeaRecId;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(Long searchCount) {
		this.searchCount = searchCount;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "ArticleSearchRecord [artSeaRecId=" + artSeaRecId + ", keyWord=" + keyWord + ", userId=" + userId
				+ ", searchCount=" + searchCount + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}

}
