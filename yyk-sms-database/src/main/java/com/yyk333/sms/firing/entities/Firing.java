package com.yyk333.sms.firing.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="yyk_firing")
public class Firing implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3807618053144110806L;
	
	/**
	 * 主键ID MEDIUMINT(8)
	 */
	@Id
	@GeneratedValue
	private Long id;
	/**
	 * 名称 vachar (20)
	 */
	@Column(name="firing_name")
	private String firingName;
	/**
	 * 名称 MEDIUMINT(8)
	 */
	@JoinColumn(name="cat_id", unique=true)
	@OneToOne(fetch=FetchType.EAGER)
	private FiringCategory firingCategory;
	/**
	 * 设备类型1.IOS;2.Android TINYINT(2)
	 */
	private int device;
	/**
	 * 分辨率1.640*640；… TINYINT(4)
	 */
	@Column(name="resolution_ratio_width")
	private int resolutionRatioWidth;
	/**
	 * 分辨率1.640*640；… TINYINT(4)
	 */
	@Column(name="resolution_ratio_height")
	private int resolutionRatioHeight;
	/**
	 * 启动图片路径 vachar (255)
	 */
	@Column(name="firing_pic")
	private String firingPic;
	/**
	 * 状态0.禁用;1.启用 TINYINT(1)
	 */
	private int status;
	
	@Column(name="update_time")
	private Long updateTime;
	
	@Column(name="is_original_pic")
	private int isOriginalPic;

	public Firing() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFiringName() {
		return firingName;
	}

	public void setFiringName(String firingName) {
		this.firingName = firingName;
	}

	public FiringCategory getFiringCategory() {
		return firingCategory;
	}

	public void setFiringCategory(FiringCategory firingCategory) {
		this.firingCategory = firingCategory;
	}

	public int getDevice() {
		return device;
	}

	public void setDevice(int device) {
		this.device = device;
	}

	public int getResolutionRatioWidth() {
		return resolutionRatioWidth;
	}
	
	public void setResolutionRatioWidth(int resolutionRatioWidth) {
		this.resolutionRatioWidth = resolutionRatioWidth;
	}
	
	public int getResolutionRatioHeight() {
		return resolutionRatioHeight;
	}
	
	public void setResolutionRatioHeight(int resolutionRatioHeight) {
		this.resolutionRatioHeight = resolutionRatioHeight;
	}

	public String getFiringPic() {
		return firingPic;
	}

	public void setFiringPic(String firingPic) {
		this.firingPic = firingPic;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public Long getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	
	public int getIsOriginalPic() {
		return isOriginalPic;
	}
	
	public void setIsOriginalPic(int isOriginalPic) {
		this.isOriginalPic = isOriginalPic;
	}

	@Override
	public String toString() {
		return "Firing [id=" + id + ", firingName=" + firingName + ", firingCategory=" + firingCategory + ", device="
				+ device + ", resolutionRatioWidth=" + resolutionRatioWidth + ", resolutionRatioHeight="
				+ resolutionRatioHeight + ", firingPic=" + firingPic + ", status=" + status + ", updateTime="
				+ updateTime + ", isOriginalPic=" + isOriginalPic + "]";
	}
	
}
