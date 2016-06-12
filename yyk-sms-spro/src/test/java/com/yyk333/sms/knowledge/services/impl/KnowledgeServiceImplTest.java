package com.yyk333.sms.knowledge.services.impl;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.yyk333.sms.knowledge.entities.Article;
import com.yyk333.sms.knowledge.entities.ArticleCategory;
import com.yyk333.sms.knowledge.repository.ArticleCategoryRepository;
import com.yyk333.sms.knowledge.repository.ArticleRepository;
import com.yyk333.sms.knowledge.services.KnowledgeService;
import com.yyk333.sms.vo.RespVO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class KnowledgeServiceImplTest {
	
	@Autowired
	private KnowledgeService knowledgeService;
	
	@Autowired
	private ArticleCategoryRepository articleCategoryRepository;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Test
	public void testInsertData() {
		String nameStr = "医学人文,康复医学,临床营养,营养配餐,饮食搭配,营养食材,营养菜谱,中医食疗,时令饮食,药膳食疗,营养信息,营养活动,食品安全";
		String[] split = nameStr.split(",");
		int i = 11;
		for (String string : split) {
			ArticleCategory entity = new ArticleCategory();
			entity.setArtCatName(string);
			entity.setCatDesc(string+"描述");
			entity.setCatIcon("Public/Uploads/575798f818cc6.png");
			entity.setCatOrder(i++);
			entity.setCreateTime(System.currentTimeMillis()/1000);
			entity.setIsAllowEdit(1);
			entity.setStatus(1);
			entity.setSupCatId(0L);
			entity.setUpdateTime(System.currentTimeMillis()/1000);
			articleCategoryRepository.save(entity);
		}
	}
	
	@Test
	public void testInsertArtData() {
		List<Article> list = new ArrayList<>();
		List<String> strl = new ArrayList<>();
		strl.add("Public/Uploads/2016-05-20/573e77729eab2.png");
		strl.add("Public/Uploads/2016-05-20/573e778c8b2f2.png");
		strl.add("Public/Uploads/2016-05-20/573e77a096504.png");
		strl.add("Public/Uploads/2016-05-20/573e77b771851.png");
		strl.add("Public/Uploads/2016-05-20/573e77d27b476.png");
		strl.add("Public/Uploads/2016-05-20/573e78a1d87dc.png");
		int j = 0;
		long artcatid = 2L;
		for (int m = 0; m < 23; m++) {
			ArticleCategory articleCategory = new ArticleCategory();
			articleCategory.setArtCatId(artcatid++);
			for (int i = 0; i < 30; i++) {
				Article article = new Article();
				article.setContent(i+"内容"+UUID.randomUUID()+"内容");
				article.setCreateTime(System.currentTimeMillis()/1000+i*100);
				article.setUpdateTime(System.currentTimeMillis()/1000+i*100);
				article.setDetailPic(strl.get(j++));
				article.setIntro("简介"+UUID.randomUUID());
				article.setIsRecommend(0);
				article.setIsTop(0);
				article.setKeyWords("关键字"+UUID.randomUUID());
				article.setStatus(1);
				article.setThumbnail(strl.get(j++));
				article.setTitle("标题"+UUID.randomUUID());
				article.setTotalCollection(i*j);
				article.setTotalComments(i*j^2);
				article.setTotalShare(i*j^3);
				article.setArticleCategory(articleCategory);
				list.add(article);
				if (j==4) {
					j=0;
				}
			}
		}
		articleRepository.save(list);
	}

	
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
