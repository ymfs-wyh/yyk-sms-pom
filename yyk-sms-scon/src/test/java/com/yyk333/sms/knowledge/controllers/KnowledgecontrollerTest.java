package com.yyk333.sms.knowledge.controllers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.yyk333.sms.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest("server.port:0")
public class KnowledgecontrollerTest {
	
	private String url = "http://192.168.0.89:8090/sms-scon/knowledge/";
	private RestTemplate template = new TestRestTemplate();
	
	@Test
	public void testUserInfoSynchro() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userArtCatIds", "1,2,3,4,5,6,7");
		map.put("userId", "1");
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("artId", "1");
		reqMap.put("optType", "1");
		list.add(reqMap);
		Map<String, Object> reqMap1 = new HashMap<>();
		reqMap1.put("artId", "1");
		reqMap1.put("optType", "2");
		list.add(reqMap1);
		map.put("userArtOpts", list);
		String result = template.postForObject(url+"user_data_syn", JSON.toJSONString(map), String.class);
		System.out.println(result);
	}

	@Test
	public void testArtCatInfoLoad() {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>(); 
//		map.put("userArtCatIds", "1,2,3,4,5,6");
		map.add("userId", "1");
		
		String result = template.postForObject(url+"art_cat_load", map, String.class);
		System.out.println(result);
	}

	@Test
	public void testArtCatArtInfoLoad() {
		fail("Not yet implemented");
	}

	@Test
	public void testArtInfoQuery() {
		fail("Not yet implemented");
	}

	@Test
	public void testArtKeyHotQuery() {
		fail("Not yet implemented");
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
