package com.yyk333.sms.banner.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.yyk333.sms.banner.dto.BannerDTO;
import com.yyk333.sms.banner.entities.Banner;
import com.yyk333.sms.banner.services.BannerService;
import com.yyk333.sms.vo.RespVO;

@Controller
@RequestMapping(value = "/banner")
public class BannerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BannerController.class);

	@Autowired
	private BannerService bannerService;


	/**
	 * 加载轮播图
	 * 
	 * @param bannerlocation
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/banners")
	public RespVO artCatInfoLoad(String bannerlocation) {
		LOGGER.debug("服务消费者接收参数：bannerlocation->" + bannerlocation);

		RespVO respVO = new RespVO();
		
		BannerDTO bannerDTO = new BannerDTO();
		
		Banner banner = new Banner();

		if (StringUtils.isBlank(bannerlocation)) {
			respVO.setStatus("0");
			respVO.setInfo("请求参数bannerlocation不能为空！");
			respVO.setErrCode("500");
			return respVO;
		}
		
		banner.setStatus("0");
		banner.setBannerlocation(bannerlocation);
		bannerDTO.setBanner(banner);

		List<String> listAsc = new ArrayList<>();
		List<String> listDesc = new ArrayList<>();
		listAsc.add("bannerorder");
		listDesc.add("banId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("asc", listAsc);
		map.put("desc", listDesc);
		LOGGER.debug("排序map：map->" + map);
		bannerDTO.setOrderMap(map);

		String postData = JSON.toJSONString(bannerDTO);
		
		String result = bannerService.bannerListLoad(postData);
		respVO = JSON.parseObject(result, RespVO.class);

		return respVO;

	}
	
}
