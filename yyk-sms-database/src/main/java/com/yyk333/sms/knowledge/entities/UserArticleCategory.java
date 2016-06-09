package com.yyk333.sms.knowledge.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="yyk_user_art_cat")
public class UserArticleCategory implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 3977499733554749287L;
	
	/**
	 *  id INT(11) NOT NULL AUTO_INCREMENT,
	 */
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long userArtCatId;
	
	/**
	 *  art_cat_id INT(11) NOT NULL COMMENT '文章分类ids',
	 */
	private String artCatIds;
	
	/**
	 *  user_id INT(11) NOT NULL COMMENT '用户id',
	 */
	private Long userId;
	
	/**
	 * create_time int(11) NOT NULL COMMENT '创建时间，默认系统当前时间',
	 */
	private long createTime;

	/**
	 * update_time int(11) NOT NULL COMMENT '更新时间，默认当前时间',
	 */
	private long updateTime;
	
	public UserArticleCategory() {
		// TODO Auto-generated constructor stub
	}

	public Long getUserArtCatId() {
		return userArtCatId;
	}

	public void setUserArtCatId(Long userArtCatId) {
		this.userArtCatId = userArtCatId;
	}

	public String getArtCatIds() {
		return artCatIds;
	}

	public void setArtCatIds(String artCatIds) {
		this.artCatIds = artCatIds;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "UserArticleCategory [userArtCatId=" + userArtCatId + ", artCatIds=" + artCatIds + ", userId=" + userId
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
	
}
