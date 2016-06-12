package com.yyk333.sms.knowledge.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "yyk_user_art_opt")
public class UserArticleOption implements Serializable {

	/**
	 * 
	 */
	private static final Long serialVersionUID = -48170713629704663L;

	/**
	 * id Integer(11) NOT NULL COMMENT '主键id，自增',
	 */
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long userArtOptId;
	/**
	 * art_id Integer(11) NOT NULL COMMENT '文章id',
	 */
	private Long artId;
	/**
	 * user_id Integer(11) NOT NULL COMMENT '用户id',
	 */
	private Long userId;
	/**
	 * opt_type tinyInteger(1) NOT NULL COMMENT '操作类型：1收藏，2分享，3评论',
	 */
	private Integer optType;
	
	/**
	 * create_time Integer(11) NOT NULL COMMENT '创建时间，默认系统当前时间',
	 */
	private Long createTime;

	/**
	 * update_time Integer(11) NOT NULL COMMENT '更新时间，默认当前时间',
	 */
	private Long updateTime;
	
	public UserArticleOption() {
		// TODO Auto-generated constructor stub
	}

	public Long getUserArtOptId() {
		return userArtOptId;
	}

	public void setUserArtOptId(Long userArtOptId) {
		this.userArtOptId = userArtOptId;
	}

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

	public Integer getOptType() {
		return optType;
	}

	public void setOptType(Integer optType) {
		this.optType = optType;
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
		return "UserArticleOption [userArtOptId=" + userArtOptId + ", artId=" + artId + ", userId=" + userId
				+ ", optType=" + optType + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
	
}
