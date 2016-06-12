package com.yyk333.sms.pns.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yyk333.sms.pns.services.PnsService;

@Controller
@RequestMapping(value = "/pns")
public class PnsController {

	@Autowired
	private PnsService pnsService;

	@RequestMapping(value = "/smns")
	@ResponseBody
	public String smns(@RequestBody String post) {
		return pnsService.shortMsg_pns(post);
	}

	@RequestMapping(value = "/wxns", method = RequestMethod.POST)
	@ResponseBody
	public String wxns(@RequestBody String post) {
		return pnsService.weiXin_pns(post);
	}

	@RequestMapping(value = "/emns")
	@ResponseBody
	public String emns(@RequestBody String post) {
		return pnsService.email_pns(post);
	}

	@RequestMapping(value = "/umns")
	@ResponseBody
	public String umns(@RequestBody String post) {
		return pnsService.uMengApp_pnss(post);
	}
}
