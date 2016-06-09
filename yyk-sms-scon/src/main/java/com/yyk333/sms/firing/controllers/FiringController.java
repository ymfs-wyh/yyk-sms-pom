package com.yyk333.sms.firing.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.yyk333.sms.firing.dto.FiringQueryVO;
import com.yyk333.sms.firing.services.FiringService;
import com.yyk333.sms.vo.RespVO;

@Controller
@RequestMapping(value = "/firing")
public class FiringController {

	@Autowired
	private FiringService firingService;

	@RequestMapping(value = "/manage")
	@ResponseBody
	public RespVO checkVersionUpdate(String devTyp, String firAddTime, String width, String height) {
		// 声明返回VO
		RespVO respVO = new RespVO();

		if (StringUtils.isBlank(width)) {
			// 设备类型错误
			respVO.setStatus("1");
			respVO.setErrCode("500");
			respVO.setInfo("Device's width is null！");
			return respVO;
		}
		if (StringUtils.isBlank(height)) {
			// 设备类型错误
			respVO.setStatus("1");
			respVO.setErrCode("500");
			respVO.setInfo("Device's height is null！");
			return respVO;
		}
		if (StringUtils.isBlank(devTyp)) {
			// 设备类型错误
			respVO.setStatus("1");
			respVO.setErrCode("500");
			respVO.setInfo("Device's type is null！");
			return respVO;
		}
		if (StringUtils.isBlank(firAddTime)) {
			// 设备类型错误
			respVO.setStatus("1");
			respVO.setErrCode("500");
			respVO.setInfo("启动页图片添加时间为空！");
			return respVO;
		}

		// 声明查询VO
		FiringQueryVO queryVO = new FiringQueryVO();
		// 启用状态
		queryVO.setStatus(1);

		// 设备类型
		queryVO.setDevice(Integer.parseInt(devTyp));
		
		// 设备的宽度和高度
		queryVO.setResolutionRatioWidth(Integer.parseInt(width));
		queryVO.setResolutionRatioHeight(Integer.parseInt(height));
		
		// 图片更新时间
		queryVO.setUpdateTime(Long.parseLong(firAddTime));

		String post = JSON.toJSONString(queryVO);
		// 调用业务层方法处理 version update 业务
		String result = firingService.checkFiring(post);
		if (StringUtils.isNotBlank(result)) {
			// 封装返回结果
			return JSON.parseObject(result, RespVO.class);
		} else {
			// 设备类型错误
			respVO.setStatus("1");
			respVO.setErrCode("500");
			respVO.setInfo("Request param error or Call server trantsport excoption！");
		}

		return respVO;
	}

}
