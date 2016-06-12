package com.yyk333.upacp.pay;

import org.springframework.stereotype.Controller;

import com.unionpay.acp.sdk.SDKConfig;

@Controller
public class AutoLoadController {
	
	public AutoLoadController() {
		SDKConfig.getConfig().loadPropertiesFromSrc();
	}
	
}
