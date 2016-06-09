package com.yyk333.sms.firing.dto;

import com.yyk333.sms.firing.entities.Firing;
import com.yyk333.sms.firing.entities.FiringCategory;

public class FiringQueryVO extends Firing {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8733052031616874662L;
	
	private FiringCategory firingCategory;

	public FiringCategory getVersionUpdate() {
		return firingCategory;
	}

	public void setVersionUpdate(FiringCategory firingCategory) {
		this.firingCategory = firingCategory;
	}

}
