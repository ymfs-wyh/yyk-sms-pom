package com.yyk333.sms.knowledge.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.yyk333.sms.knowledge.dto.ArticleQueryDTO;
import com.yyk333.sms.knowledge.services.KnowledgeService;
import com.yyk333.sms.vo.RespVO;

@Controller
@RequestMapping(value = "/knowledge")
public class KnowledgeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(KnowledgeController.class);

	@Autowired
	private KnowledgeService knowledgeService;

	/**
	 * 用户数据同步
	 * 
	 * @param postData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/user_data_syn")
	public RespVO userInfoSynchro(@RequestBody String postData) {
		LOGGER.debug("服务消费者接收参数：->" + postData);
		RespVO respVO = new RespVO();

		Map<String, Object> reqMap = JSON.parseObject(postData, Map.class);
		if (reqMap != null) {

			String userId = (String) reqMap.get("userId");
			LOGGER.debug("userId：->" + userId);
			if (!StringUtils.isNumeric(userId)) {
				respVO.setStatus("0");
				respVO.setInfo("用户id为空或者不是数字！");
				respVO.setErrCode("500");
				return respVO;
			}

			String userArtCatIds = (String) reqMap.get("userArtCatIds");
			LOGGER.debug("userArtCatIds：->" + userArtCatIds);
			List<Map<String, Object>> userArtOpts = (List<Map<String, Object>>) reqMap.get("userArtOpts");
			LOGGER.debug("userArtOpts：->" + userArtOpts);

			if (StringUtils.isBlank(userArtCatIds) && (userArtOpts == null || userArtOpts.size() == 0)) {
				respVO.setStatus("0");
				respVO.setInfo("无同步内容！");
				respVO.setErrCode("500");
				return respVO;
			}

			String result = knowledgeService.userInfoSynchro(postData);
			respVO = JSON.parseObject(result, RespVO.class);

		} else {
			respVO.setStatus("0");
			respVO.setInfo("请求参数为空！");
			respVO.setErrCode("500");
		}
		return respVO;

	}

	/**
	 * 加载栏目
	 * 
	 * @param userId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/art_cat_load")
	public RespVO artCatInfoLoad(String userId) {
		LOGGER.debug("服务消费者接收参数：userId->" + userId);

		RespVO respVO = null;

		ArticleQueryDTO articleDTO = new ArticleQueryDTO();

		if (StringUtils.isNumeric(userId)) {
			articleDTO.setUserId(Long.parseLong(userId));
		}
		articleDTO.setStatus(1);

		List<String> listAsc = new ArrayList<>();
		List<String> listDesc = new ArrayList<>();
		listAsc.add("catOrder");
		listDesc.add("updateTime");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("asc", listAsc);
		map.put("desc", listDesc);
		LOGGER.debug("排序map：map->" + map);
		articleDTO.setOrderMap(map);

		String postData = JSON.toJSONString(articleDTO);
		String result = knowledgeService.artCatInfoLoad(postData);
		respVO = JSON.parseObject(result, RespVO.class);

		return respVO;

	}

	/**
	 * 加载文章
	 * 
	 * @param artCatId
	 * @param artId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/art_load")
	public RespVO artCatArtInfoLoad(String artCatId, String artId, String pageNo, String pageSize) {
		LOGGER.debug("服务消费者接收参数：artCatId->" + artCatId);
		LOGGER.debug("服务消费者接收参数：artId->" + artId);
		LOGGER.debug("服务消费者接收参数：pageNo->" + pageNo);
		LOGGER.debug("服务消费者接收参数：pageSize->" + pageSize);

		RespVO respVO = new RespVO();

		if (!StringUtils.isNumeric(artCatId)) {
			respVO.setStatus("0");
			respVO.setInfo("请求参数artCatId为空或者不是数字！");
			respVO.setErrCode("500");
			return respVO;
		}

		if (!StringUtils.isNumeric(pageNo)) {
			respVO.setStatus("0");
			respVO.setInfo("请求参数pageNo为空或者不是数字！");
			respVO.setErrCode("500");
			return respVO;
		}

		ArticleQueryDTO articleDTO = new ArticleQueryDTO();
		if (StringUtils.isNumeric(artId)) {
			articleDTO.setArtId(Long.parseLong(artId));
		}
		articleDTO.setStatus(1);
		articleDTO.setArtCatId(Long.parseLong(artCatId));
		articleDTO.setPageNo(Integer.parseInt(pageNo));
		if (StringUtils.isNumeric(pageSize)) {
			articleDTO.setPageSize(Integer.parseInt(pageSize));
		} else {
			articleDTO.setPageSize(10);
		}

		List<String> listDesc = new ArrayList<>();
		listDesc.add("isTop");
		listDesc.add("updateTime");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("desc", listDesc);
		LOGGER.debug("排序map：->" + map);
		articleDTO.setOrderMap(map);

		String postData = JSON.toJSONString(articleDTO);
		String result = knowledgeService.artCatArtInfoLoad(postData);
		respVO = JSON.parseObject(result, RespVO.class);

		return respVO;

	}

	/**
	 * 热门搜索关键字列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/hot_sea_keys")
	public RespVO artKeyHotQuery() {

		RespVO respVO = new RespVO();
		Integer pageNo = 1;
		Integer pageSize = 4;

		ArticleQueryDTO articleDTO = new ArticleQueryDTO();
		articleDTO.setPageNo(pageNo);
		articleDTO.setPageSize(pageSize);

		List<String> listDesc = new ArrayList<>();
		listDesc.add("searchCount");
		listDesc.add("updateTime");
		listDesc.add("createTime");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("desc", listDesc);
		LOGGER.debug("排序map：->" + map);
		articleDTO.setOrderMap(map);

		String postData = JSON.toJSONString(articleDTO);
		String result = knowledgeService.artKeyHotQuery(postData);
		respVO = JSON.parseObject(result, RespVO.class);
		return respVO;

	}

	/**
	 * 文章详情
	 * 
	 * @param artId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/art_detail")
	public RespVO artDetInfoQuery(String artId, String userId) {
		LOGGER.debug("服务消费者接收参数：artId->" + artId);
		RespVO respVO = new RespVO();

		if (!StringUtils.isNumeric(artId)) {
			respVO.setStatus("0");
			respVO.setInfo("请求参数artId为空或者不是数字！");
			respVO.setErrCode("500");
			return respVO;
		}
		

		ArticleQueryDTO articleDTO = new ArticleQueryDTO();

		articleDTO.setStatus(1);
		if (StringUtils.isNumeric(userId)) {
			articleDTO.setUserId(Long.parseLong(userId));
		}
		articleDTO.setArtId(Long.parseLong(artId));

		String postData = JSON.toJSONString(articleDTO);
		String result = knowledgeService.artDetInfoQuery(postData);

		respVO = JSON.parseObject(result, RespVO.class);
		return respVO;

	}

	/**
	 * 文章编辑
	 * 
	 * @param artId
	 * @param userId
	 * @param optType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/art_upd")
	public RespVO artDetInfoUpdate(String artId, String userId, String optType) {
		LOGGER.debug("服务消费者接收参数：artId->" + artId);
		LOGGER.debug("服务消费者接收参数：userId->" + userId);
		LOGGER.debug("服务消费者接收参数：optType->" + optType);

		RespVO respVO = new RespVO();

		if (!StringUtils.isNumeric(artId)) {
			respVO.setStatus("0");
			respVO.setInfo("请求参数artId为空或者不是数字！");
			respVO.setErrCode("500");
			return respVO;
		}
		if (!StringUtils.isNumeric(userId)) {
			respVO.setStatus("0");
			respVO.setInfo("请求参数userId为空或者不是数字！");
			respVO.setErrCode("500");
			return respVO;
		}
		if (!StringUtils.isNumeric(optType)) {
			respVO.setStatus("0");
			respVO.setInfo("请求参数optType为空或者不是数字！");
			respVO.setErrCode("500");
			return respVO;
		}

		ArticleQueryDTO articleDTO = new ArticleQueryDTO();

		articleDTO.setStatus(1);
		articleDTO.setArtId(Long.parseLong(artId));
		articleDTO.setOptType(Integer.parseInt(optType));
		articleDTO.setUserId(Long.parseLong(userId));

		String postData = JSON.toJSONString(articleDTO);
		String result = knowledgeService.artDetInfoUpdate(postData);

		respVO = JSON.parseObject(result, RespVO.class);
		return respVO;

	}

	/**
	 * 搜索功能暂不实现
	 * 
	 * @param keyWord
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/search")
	public RespVO artInfoQuery(String keyWord, String pageNo, String pageSize) {

		RespVO respVO = new RespVO();

		if (StringUtils.isBlank(keyWord)) {
			respVO.setStatus("0");
			respVO.setInfo("请求搜索关键字不能为空！");
			respVO.setErrCode("500");
			return respVO;
		}
		if (!StringUtils.isNumeric(pageNo)) {
			respVO.setStatus("0");
			respVO.setInfo("请求参数pageNo为空或者不是数字！");
			respVO.setErrCode("500");
			return respVO;
		}

		ArticleQueryDTO articleDTO = new ArticleQueryDTO();

		articleDTO.setStatus(1);
		articleDTO.setKeyWord(keyWord);
		articleDTO.setPageNo(Integer.parseInt(pageNo));
		if (StringUtils.isNumeric(pageSize)) {
			articleDTO.setPageSize(Integer.parseInt(pageSize));
		} else {
			articleDTO.setPageSize(10);
		}

		String postData = JSON.toJSONString(articleDTO);
		String result = knowledgeService.artInfoQuery(postData);
		respVO = JSON.parseObject(result, RespVO.class);
		return respVO;

	}

}
