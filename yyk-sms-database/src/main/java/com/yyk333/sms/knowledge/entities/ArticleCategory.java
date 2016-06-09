package com.yyk333.sms.knowledge.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
@Table(name = "yyk_article_category")
public class ArticleCategory implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 2793526311571465819L;

	/**
	 * id Integer(11) NOT NULL COMMENT '主键id，自增',
	 */
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long artCatId;

	/**
	 * art_cat_name varchar(45) NOT NULL COMMENT '文章分类名称，非空',
	 */
	private String artCatName;

	/**
	 * sup_cat_id Integer(11) NOT NULL DEFAULT '0' COMMENT '父项分类id',
	 */
	private Long supCatId;

	/**
	 * cat_order tinyInteger(3) NOT NULL DEFAULT '0' COMMENT
	 * '分类排序字段，非空，取值范围：0-999',
	 */
	private Integer catOrder;

	/**
	 * status tinyInteger(1) NOT NULL DEFAULT '0' COMMENT '启用状态：0不启用；1启用',
	 */
	private Integer status;

	/**
	 * is_allow_edit tinyInteger(1) NOT NULL DEFAULT '0' COMMENT '是否允许编辑：0否；1是',
	 */
	private Integer isAllowEdit;

	/**
	 * cat_icon varchar(255) NOT NULL COMMENT '分类图标',
	 */
	private String catIcon;

	/**
	 * cat_desc varchar(255) DEFAULT NULL COMMENT '分类描述',
	 */
	private String catDesc;

	/**
	 * create_time Integer(11) NOT NULL COMMENT '创建时间，默认系统当前时间',
	 */
	private Long createTime;

	/**
	 * update_time Integer(11) NOT NULL COMMENT '更新时间，默认当前时间',
	 */
	private Long updateTime;

	// 映射单向 1-n 的关联关系
	// 使用 @OneToMany 来映射 1-n 的关联关系
	// 使用 @JoinColumn 来映射外键列的名称
	// 可以使用 @OneToMany 的 fetch 属性来修改默认的加载策略
	// 可以通过 @OneToMany 的 cascade 属性来修改默认的删除策略.
	// 注意: 若在 1 的一端的 @OneToMany 中使用 mappedBy 属性, 则 @OneToMany 端就不能再使用
	// @JoinColumn 属性了.
	@JSONField(serialize=false)
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE }, mappedBy = "articleCategory")
	private Set<Article> articles = new HashSet<Article>();

	@Transient
	private String[] artCatIdArr;

	public ArticleCategory() {
		// TODO Auto-generated constructor stub
	}

	public Long getArtCatId() {
		return artCatId;
	}

	public void setArtCatId(Long artCatId) {
		this.artCatId = artCatId;
	}

	public String getArtCatName() {
		return artCatName;
	}

	public void setArtCatName(String artCatName) {
		this.artCatName = artCatName;
	}

	public Long getSupCatId() {
		return supCatId;
	}

	public void setSupCatId(Long supCatId) {
		this.supCatId = supCatId;
	}

	public Integer getCatOrder() {
		return catOrder;
	}

	public void setCatOrder(Integer catOrder) {
		this.catOrder = catOrder;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsAllowEdit() {
		return isAllowEdit;
	}

	public void setIsAllowEdit(Integer isAllowEdit) {
		this.isAllowEdit = isAllowEdit;
	}

	public String getCatIcon() {
		return catIcon;
	}

	public void setCatIcon(String catIcon) {
		this.catIcon = catIcon;
	}

	public String getCatDesc() {
		return catDesc;
	}

	public void setCatDesc(String catDesc) {
		this.catDesc = catDesc;
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

	public String[] getArtCatIdArr() {
		return artCatIdArr;
	}

	public void setArtCatIdArr(String[] artCatIdArr) {
		this.artCatIdArr = artCatIdArr;
	}

	public Set<Article> getArticles() {
		return articles;
	}

	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	@Override
	public String toString() {
		return "ArticleCategory [artCatId=" + artCatId + ", artCatName=" + artCatName + ", supCatId=" + supCatId
				+ ", catOrder=" + catOrder + ", status=" + status + ", isAllowEdit=" + isAllowEdit + ", catIcon="
				+ catIcon + ", catDesc=" + catDesc + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}

}
