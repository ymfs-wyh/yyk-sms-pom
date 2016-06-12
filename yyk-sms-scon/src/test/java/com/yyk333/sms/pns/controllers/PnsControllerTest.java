package com.yyk333.sms.pns.controllers;

import static org.junit.Assert.*;

import java.util.HashMap;
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
@SpringApplicationConfiguration(classes=Application.class)
@WebIntegrationTest("server.port:0")
public class PnsControllerTest {

	private String url = "http://192.168.0.89:8090/sms-scon/pns/";
	private RestTemplate template = new TestRestTemplate();
	
	@Test
	public void testSmns() {
		Map<String, Object> map = new HashMap<String, Object>(); 
		map.put("mobiles", "13659854897");
		map.put("content", "测试");
		
		String result = template.postForObject(url+"smns", JSON.toJSONString(map), String.class);
		System.out.println(result);
	}

	@Test
	public void testWxns() {
		fail("Not yet implemented");
	}

	@Test
	public void testEmns() {
		fail("Not yet implemented");
	}

	@Test
	public void testUmns() {
		fail("Not yet implemented");
	}

}
