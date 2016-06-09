package com.yyk333.sms.knowledge.services.impl;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSON;
import com.yyk333.sms.Application;
import com.yyk333.sms.knowledge.dto.ArticleQueryDTO;
import com.yyk333.sms.knowledge.services.KnowledgeService;
import com.yyk333.sms.vo.RespVO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class KnowledgeServiceImplTest {
	
	@Autowired
	private KnowledgeService knowledgeService;
	
	@Test
	public void testUserInfoSynchro() {
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("userArtCatIds", "1,2");
		reqMap.put("userId", 1);
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map_1 = new HashMap<>();
		map_1.put("artId", 1);
		map_1.put("optType", 1);
		list.add(map_1);
		String result_1 = knowledgeService.userInfoSynchro(JSON.toJSONString(reqMap));
		System.out.println(result_1);
		
	}

	@Test
	public void testArtCatInfoLoad() {
		fail("Not yet implemented");
	}

	@Test
	public void testArtCatArtInfoLoad() {
		Long artCatId = 1L;
//		Long artId= 6L;
		Integer pageNo = 3;
		Integer pageSize = 3;
		
		ArticleQueryDTO articleDTO = new ArticleQueryDTO();
//		articleDTO.setArtId(artId);
		articleDTO.setStatus(1);
		articleDTO.setArtCatId(artCatId);
		articleDTO.setPageNo(pageNo);
		articleDTO.setPageSize(pageSize);

		if (articleDTO.getPageSize() == null) {
			articleDTO.setPageSize(10);
		}

		List<String> listDesc = new ArrayList<>();
		listDesc.add("isTop");
		listDesc.add("isRecommend");
		listDesc.add("totalComments");
		listDesc.add("totalCollection");
		listDesc.add("totalShare");
		listDesc.add("updateTime");
		listDesc.add("createTime");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("desc", listDesc);
		articleDTO.setOrderMap(map);

		String postData = JSON.toJSONString(articleDTO);
		String result = knowledgeService.artCatArtInfoLoad(postData);
		System.out.println(result);
	}

	@Test
	public void testArtInfoQuery() {
		fail("Not yet implemented");
	}

	@Test
	public void testArtKeyHotQuery() {
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
		articleDTO.setOrderMap(map);

		String postData = JSON.toJSONString(articleDTO);
		String result = knowledgeService.artKeyHotQuery(postData);
		System.out.println(result);
	}

	@Test
	public void testArtDetInfoQuery() {
		fail("Not yet implemented");
	}

	@Test
	public void testArtDetInfoUpdate() {
		fail("Not yet implemented");
	}

}
