package com.yyk333.sms.knowledge.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.yyk333.sms.utils.DateUtil;

@Entity
@Table(name = "yyk_article")
public class Article implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 2761073187509246298L;

	/**
	 * id int(11) NOT NULL COMMENT '文章id，主键，自增',
	 */
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long artId;

	/**
	 * title varchar(255) NOT NULL COMMENT '文章标题',
	 */
	private String title;

	/**
	 * intro varchar(255) DEFAULT NULL COMMENT '文章简介',
	 */
	private String intro;

	/**
	 * content varchar(4000) DEFAULT NULL COMMENT '文章内容',
	 */
	private String content;

	/**
	 * key_words varchar(255) DEFAULT NULL COMMENT '文章关键字，多个以英文逗号隔开',
	 */
	private String keyWords;

	/**
	 * art_cat_id int(11) NOT NULL COMMENT '文章分类id',
	 */
	// 映射单向 n-1 的关联关系
	// 使用 @ManyToOne 来映射多对一的关联关系
	// 使用 @JoinColumn 来映射外键.
	// 可使用 @ManyToOne 的 fetch 属性来修改默认的关联属性的加载策略
	@JoinColumn(name = "art_cat_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private ArticleCategory articleCategory;

	/**
	 * is_top tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否置顶：0不置顶；1置顶',
	 */
	private Integer isTop;

	/**
	 * is_recommend tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否推荐：0否；1是',
	 */
	private Integer isRecommend;
	
	/**
	 * status tinyint(1) NOT NULL DEFAULT '0' COMMENT '启用状态：0不启用；1启用',
	 */
	private Integer status;

	/**
	 * thumbnail varchar(255) NOT NULL COMMENT '缩略图，用于列表显示',
	 */
	private String thumbnail;

	/**
	 * detail_pic varchar(255) DEFAULT NULL COMMENT '详情图',
	 */
	private String detailPic;

	/**
	 * total_collection int(11) NOT NULL DEFAULT '0' COMMENT '总收藏数',
	 */
	private Integer totalCollection;

	/**
	 * total_comments int(11) NOT NULL DEFAULT '0' COMMENT '总评论数',
	 */
	private Integer totalComments;

	/**
	 * total_share int(11) NOT NULL DEFAULT '0' COMMENT '总分享数',
	 */
	private Integer totalShare;

	/**
	 * create_time int(11) NOT NULL COMMENT '创建时间，默认系统当前时间',
	 */
	private Long createTime;

	/**
	 * update_time int(11) NOT NULL COMMENT '更新时间，默认当前时间',
	 */
	private Long updateTime;
	
	@Transient
	private String url;

	@Transient
	private String keyWord;

	@Transient
	private String artCatName;
	
	@Transient
	private Integer isCollection;
	
	@Transient
	private String updateTimeStr;

	public Article() {
		// TODO Auto-generated constructor stub
	}
	
	public void setMore(){
		this.setArtCatName(this.getArticleCategory().getArtCatName());
		this.setUpdateTimeStr(DateUtil.dateToString(new Date(this.updateTime), "MM-dd"));
	}

	public Long getArtId() {
		return artId;
	}

	public void setArtId(Long artId) {
		this.artId = artId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public ArticleCategory getArticleCategory() {
		return articleCategory;
	}

	public void setArticleCategory(ArticleCategory articleCategory) {
		this.articleCategory = articleCategory;
	}

	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	public Integer getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(Integer isRecommend) {
		this.isRecommend = isRecommend;
	}
	
	public Integer getIsCollection() {
		return isCollection;
	}

	public void setIsCollection(Integer isCollection) {
		this.isCollection = isCollection;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getDetailPic() {
		return detailPic;
	}

	public void setDetailPic(String detailPic) {
		this.detailPic = detailPic;
	}

	public Integer getTotalCollection() {
		return totalCollection;
	}

	public void setTotalCollection(Integer totalCollection) {
		this.totalCollection = totalCollection;
	}

	public Integer getTotalComments() {
		return totalComments;
	}

	public void setTotalComments(Integer totalComments) {
		this.totalComments = totalComments;
	}

	public Integer getTotalShare() {
		return totalShare;
	}

	public void setTotalShare(Integer totalShare) {
		this.totalShare = totalShare;
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
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getArtCatName() {
		return artCatName;
	}

	public void setArtCatName(String artCatName) {
		this.artCatName = artCatName;
	}
	
	public String getUpdateTimeStr() {
		return updateTimeStr;
	}

	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}

	@Override
	public String toString() {
		return "Article [artId=" + artId + ", title=" + title + ", intro=" + intro + ", content=" + content
				+ ", keyWords=" + keyWords + ", articleCategory=" + articleCategory + ", isTop=" + isTop
				+ ", isRecommend=" + isRecommend + ", status=" + status + ", thumbnail=" + thumbnail + ", detailPic="
				+ detailPic + ", totalCollection=" + totalCollection + ", totalComments=" + totalComments
				+ ", totalShare=" + totalShare + ", createTime=" + createTime + ", updateTime=" + updateTime + ", url="
				+ url + ", keyWord=" + keyWord + ", artCatName=" + artCatName + ", isCollection=" + isCollection
				+ ", updateTimeStr=" + updateTimeStr + "]";
	}
	
}
