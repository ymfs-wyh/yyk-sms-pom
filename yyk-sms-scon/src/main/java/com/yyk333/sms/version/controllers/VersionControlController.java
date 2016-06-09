package com.yyk333.sms.version.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.yyk333.sms.version.entities.Version;
import com.yyk333.sms.version.services.VersionControlService;
import com.yyk333.sms.vo.RespVO;

@Controller
@RequestMapping(value = "/version")
public class VersionControlController {

	@Autowired
	private VersionControlService versionControlService;

	/**
	 * 普通更新、强制更新、热更新
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public RespVO checkComAndForVerUpd(String emtTyp, String appType, String verCod, String patAddTime) {
		// 声明返回VO
		RespVO respVO = new RespVO();

		if (StringUtils.isBlank(emtTyp)) {
			respVO.setStatus("1");
			respVO.setErrCode("500");
			respVO.setInfo("设备类型不能为空！");
			return respVO;
		}
		
		if (StringUtils.isBlank(verCod)) {
			respVO.setStatus("1");
			respVO.setErrCode("500");
			respVO.setInfo("版本号不能为空！");
			return respVO;
		}
		
		if (StringUtils.isBlank(appType)) {
			respVO.setStatus("1");
			respVO.setErrCode("500");
			respVO.setInfo("版本类型不能为空！");
			return respVO;
		}

		if (StringUtils.isBlank(patAddTime)) {
			respVO.setStatus("1");
			respVO.setErrCode("500");
			respVO.setInfo("补丁添加时间不能为空！");
			return respVO;
		}

		// 声明查询VO
		Version queryVO = new Version();
		// 封装查询VO
		// 启用状态
		queryVO.setStatus(0);
		// 设备类型
		queryVO.setEquipmentType(Integer.parseInt(emtTyp));
		// 版本号
		queryVO.setVersionCode(verCod);
		// 补丁添加时间
		queryVO.setAddTime(Long.parseLong(patAddTime));
		// 版本类型0.C端 1.B端
		queryVO.setAppType(Integer.parseInt(appType));

		// 调用业务层方法处理 version update 业务
		String result = versionControlService.checkComAndForVerUpd(JSON.toJSONString(queryVO));
		if (StringUtils.isNotBlank(result)) {
			// 封装返回结果
			return JSON.parseObject(result, RespVO.class);
		} else {
			// 设备类型错误
			respVO.setStatus("1");
			respVO.setErrCode("500");
			respVO.setInfo("Call server exception！");
			return respVO;
		}

	}

}
