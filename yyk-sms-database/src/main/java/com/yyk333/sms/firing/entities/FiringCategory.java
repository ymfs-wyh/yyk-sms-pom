package com.yyk333.sms.firing.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="yyk_firing_category")
public class FiringCategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6573148840341230139L;

	/**
	 * 主键ID MEDIUMINT(8)
	 */
	@Id
	@GeneratedValue
	private Long catId;
	/**
	 * CHAR(20)
	 */
	@Column(name="cat_name")
	private String catName;
	/**
	 * CHAR(20)
	 */
	@Column(name="cat_key")
	private String catKey;
	
	public FiringCategory() {
		// TODO Auto-generated constructor stub
	}

	public Long getCatId() {
		return catId;
	}

	public void setCatId(Long catId) {
		this.catId = catId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getCatKey() {
		return catKey;
	}

	public void setCatKey(String catKey) {
		this.catKey = catKey;
	}
	
}
