package com.yyk333.sms.knowledge.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yyk333.sms.config.entities.Config;
import com.yyk333.sms.config.repository.ConfigRepository;
import com.yyk333.sms.knowledge.dto.ArticleQueryDTO;
import com.yyk333.sms.knowledge.entities.Article;
import com.yyk333.sms.knowledge.entities.ArticleCategory;
import com.yyk333.sms.knowledge.entities.ArticleSearchRecord;
import com.yyk333.sms.knowledge.entities.UserArticleCategory;
import com.yyk333.sms.knowledge.entities.UserArticleOption;
import com.yyk333.sms.knowledge.repository.ArticleCategoryRepository;
import com.yyk333.sms.knowledge.repository.ArticleRepository;
import com.yyk333.sms.knowledge.repository.ArticleSearchRecordRepository;
import com.yyk333.sms.knowledge.repository.UserArticleCategoryRepository;
import com.yyk333.sms.knowledge.repository.UserArticleOptionRepository;
import com.yyk333.sms.knowledge.services.KnowledgeService;
import com.yyk333.sms.vo.RespVO;

import net.spy.memcached.MemcachedClient;

@Service("knowledgeService")
public class KnowledgeServiceImpl implements KnowledgeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(KnowledgeServiceImpl.class);

	@Autowired
	private MemcachedClient client;

	@Autowired
	private ArticleCategoryRepository articleCategoryRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private UserArticleCategoryRepository userArticleCategoryRepository;

	@Autowired
	private UserArticleOptionRepository userArticleOptionRepository;

	@Autowired
	private ArticleSearchRecordRepository articleSearchRecordRepository;

	@Autowired
	private ConfigRepository configRepository;

	/**
	 * 用户数据同步
	 * 
	 * @param postData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public String userInfoSynchro(String postData) {
		LOGGER.debug("------------------用户数据同步服务提供者开始-----------------------");
		LOGGER.debug("请求参数：postData：" + postData);
		RespVO respVO = new RespVO();
		if (StringUtils.isNotBlank(postData)) {
			Map<String, Object> reqMap = new HashMap<>();
			try {
				reqMap = JSON.parseObject(postData, Map.class);
			} catch (Exception e) {
				respVO.setStatus("0");
				respVO.setInfo("解析参数失败！");
				respVO.setErrCode("500");
				LOGGER.debug("解析参数失败！");
				return JSON.toJSONString(respVO);
			}

			String user_Id = (String) reqMap.get("userId");
			LOGGER.debug("用户Id：userId->" + user_Id);
			Long userId = null;
			if (StringUtils.isNumeric(user_Id)) {
				userId = Long.parseLong(user_Id);
			}

			// 1.查询文章分类下文章列表
			if (userId != null) {// 如果userId不为空则开始同步
				String artCatIds = (String) reqMap.get("userArtCatIds");
				List<Map<String, Object>> list = (List<Map<String, Object>>) reqMap.get("userArtOpts");

				try {
					if (StringUtils.isNotBlank(artCatIds)) {
						UserArticleCategory userArticleCategory = userArticleCategoryRepository.findByUserId(userId);
						if (userArticleCategory != null) {
							userArticleCategory.setArtCatIds(artCatIds);
							userArticleCategory.setUpdateTime(System.currentTimeMillis() / 1000);
							userArticleCategoryRepository.save(userArticleCategory);
						} else {
							userArticleCategory = new UserArticleCategory();
							userArticleCategory.setArtCatIds(artCatIds);
							userArticleCategory.setUserId(userId);
							userArticleCategory.setCreateTime(System.currentTimeMillis() / 1000);
							userArticleCategory.setUpdateTime(System.currentTimeMillis() / 1000);
							userArticleCategoryRepository.save(userArticleCategory);
						}
					}

					if (list != null && list.size() > 0) {
						for (Map<String, Object> map : list) {
							String artId_ = (String) map.get("artId");
							String optType_ = (String) map.get("optType");
							if (StringUtils.isNumeric(artId_) && StringUtils.isNumeric(optType_)) {// 如果artId不为空则查询文章详情
								Article article = new Article();
								article.setStatus(1);
								Long artId = Long.parseLong(artId_);
								Integer optType = Integer.parseInt(optType_);

								Article article2 = articleRepository.findByArtIdAndStatus(artId, article.getStatus());

								LOGGER.debug("查询文章详情结果：" + article2);
								if (article2 != null) {
									if (optType == 1) {
										UserArticleOption userArticleOption = null;
										try {
											userArticleOption = userArticleOptionRepository.findByUserIdAndArtIdAndOptType(userId, artId, 1);
										} catch (Exception e) {
											respVO.setStatus("0");
											respVO.setInfo("服务提供者查询用户文章操作记录异常！");
											respVO.setErrCode("500");
											return JSON.toJSONString(respVO);
										}
										if (userArticleOption!=null) {
											userArticleOptionRepository.delete(userArticleOption.getUserArtOptId());
											article2.setTotalCollection(article2.getTotalCollection() - 1);
										}else{
											article2.setTotalCollection(article2.getTotalCollection() + 1);
											
											userArticleOption = new UserArticleOption();
											userArticleOption.setArtId(artId);
											userArticleOption.setUserId(userId);
											userArticleOption.setOptType(optType);
											userArticleOption.setCreateTime(System.currentTimeMillis() / 1000);
											userArticleOption.setUpdateTime(System.currentTimeMillis() / 1000);
											userArticleOptionRepository.save(userArticleOption);
										}
									} else if (optType == 2) {
										article2.setTotalShare(article2.getTotalShare() + 1);
										UserArticleOption userArticleOption = new UserArticleOption();
										userArticleOption.setArtId(artId);
										userArticleOption.setUserId(userId);
										userArticleOption.setOptType(optType);
										userArticleOption.setCreateTime(System.currentTimeMillis() / 1000);
										userArticleOption.setUpdateTime(System.currentTimeMillis() / 1000);
										userArticleOptionRepository.save(userArticleOption);
									}
									articleRepository.save(article2);
								}
							}
						}
					}
				} catch (Exception e) {
					respVO.setStatus("0");
					respVO.setInfo("服务提供者同步用户数据异常！");
					respVO.setErrCode("500");
					return JSON.toJSONString(respVO);
				}

				respVO.setStatus("1");
				respVO.setInfo("同步用户数据成功！");
				respVO.setErrCode("200");
			} else {
				respVO.setStatus("0");
				respVO.setInfo("服务提供者接收userId参数为空！");
				respVO.setErrCode("500");
			}

		} else {
			respVO.setStatus("0");
			respVO.setInfo("服务提供者接收参数为空！");
			respVO.setErrCode("500");
		}
		LOGGER.debug("------------------用户数据同步服务提供者结束-----------------------");
		return JSON.toJSONString(respVO);
	}

	/**
	 * 加载栏目
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String artCatInfoLoad(String postData) {
		LOGGER.debug("------------------分类栏目数据加载服务提供者开始-----------------------");
		LOGGER.debug("请求参数：postData：" + postData);
		RespVO respVO = new RespVO();
		if (StringUtils.isNotBlank(postData)) {
			ArticleQueryDTO articleDTO = new ArticleQueryDTO();
			try {
				articleDTO = JSON.parseObject(postData, ArticleQueryDTO.class);
			} catch (Exception e) {
				respVO.setStatus("0");
				respVO.setInfo("解析参数失败！");
				respVO.setErrCode("500");
				LOGGER.debug("解析参数失败！");
				return JSON.toJSONString(respVO);
			}

			Long userId = articleDTO.getUserId();
			LOGGER.debug("用户：userId->" + userId);

			// 添加排序
			Map<String, Object> orderMap = articleDTO.getOrderMap();
			List<Order> orderList = new ArrayList<Order>();
			if (orderMap != null) {
				List<String> ascList = (List<String>) orderMap.get("asc");
				if (ascList != null && ascList.size() > 0) {
					for (String string : ascList) {
						orderList.add(new Order(Direction.ASC, string));
					}
				}
				List<String> descList = (List<String>) orderMap.get("desc");
				if (descList != null && descList.size() > 0) {
					for (String string : descList) {
						orderList.add(new Order(Direction.DESC, string));
					}
				}
			}

			LOGGER.debug("排序字段：orderList->" + orderList);
			Sort sort = null;
			if (orderList != null && orderList.size() > 0) {
				sort = new Sort(orderList);
			}

			// 1.查询文章分类列表
			ArticleCategory articleCategory = new ArticleCategory();
			articleCategory.setStatus(articleDTO.getStatus());
			List<ArticleCategory> userArtCatList = new ArrayList<>();
			if (userId != null) {// 如果userId不为空则查询用户定制栏目
				try {
					UserArticleCategory userArticleCategory = userArticleCategoryRepository.findByUserId(userId);

					String artCatIds = userArticleCategory.getArtCatIds();
					if (StringUtils.isNotBlank(artCatIds)) {
						articleCategory.setArtCatIdArr(artCatIds.split(","));
					}

					userArtCatList = articleCategoryRepository
							.findAll(new ArticleCategorySpecification(articleCategory), sort);

				} catch (Exception e) {
					LOGGER.debug("查询用户栏目异常！");
				}
			}

			// 查询系统所有栏目分类
			List<ArticleCategory> artCatList = new ArrayList<>();
			try {
				artCatList = articleCategoryRepository.findAll(new ArticleCategorySpecification(articleCategory), sort);
			} catch (Exception e) {
				respVO.setStatus("0");
				respVO.setInfo("服务提供者查询系统文章分类异常！");
				respVO.setErrCode("500");
				return JSON.toJSONString(respVO);
			}
			respVO.setStatus("1");
			respVO.setInfo("查询文章分类成功！");
			respVO.setErrCode("200");
			Map<String, Object> map = new HashMap<>();
			List<ArticleCategory> artCatResList = new ArrayList<>();
			for (ArticleCategory articleCategory2 : artCatList) {
				articleCategory2.setStatus(null);
				articleCategory2.setCreateTime(null);
				artCatResList.add(articleCategory2);
			}
			map.put("artCatList", artCatResList);
			if (userId != null && userArtCatList != null && userArtCatList.size() > 0) {
				List<ArticleCategory> userArtCatResList = new ArrayList<>();
				for (ArticleCategory articleCategory2 : userArtCatList) {
					articleCategory2.setStatus(null);
					articleCategory2.setCreateTime(null);
					userArtCatResList.add(articleCategory2);
				}
				map.put("userArtCatList", userArtCatResList);
			}
			respVO.setData(map);

		} else {
			respVO.setStatus("0");
			respVO.setInfo("服务提供者接收参数为空！");
			respVO.setErrCode("500");
		}
		LOGGER.debug("------------------分类栏目数据加载服务提供者结束-----------------------");
		return JSON.toJSONString(respVO);

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
	@SuppressWarnings("unchecked")
	@Override
	public String artCatArtInfoLoad(String postData) {
		LOGGER.debug("------------------分类页面文章数据加载服务提供者开始-----------------------");
		LOGGER.debug("请求参数：postData：" + postData);
		RespVO respVO = new RespVO();
		if (StringUtils.isNotBlank(postData)) {
			ArticleQueryDTO articleDTO = new ArticleQueryDTO();
			try {
				articleDTO = JSON.parseObject(postData, ArticleQueryDTO.class);
			} catch (Exception e) {
				respVO.setStatus("0");
				respVO.setInfo("解析参数失败！");
				respVO.setErrCode("500");
				LOGGER.debug("解析参数失败！");
				return JSON.toJSONString(respVO);
			}

			Long artCatId = articleDTO.getArtCatId();
			LOGGER.debug("分类Id：artCatId->" + artCatId);

			// 添加排序
			Map<String, Object> orderMap = articleDTO.getOrderMap();
			List<Order> orderList = new ArrayList<Order>();
			if (orderMap != null) {
				List<String> ascList = (List<String>) orderMap.get("asc");
				if (ascList != null && ascList.size() > 0) {
					for (String string : ascList) {
						orderList.add(new Order(Direction.ASC, string));
					}
				}
				List<String> descList = (List<String>) orderMap.get("desc");
				if (descList != null && descList.size() > 0) {
					for (String string : descList) {
						orderList.add(new Order(Direction.DESC, string));
					}
				}
			}

			LOGGER.debug("文章排序字段：orderList->" + orderList);
			Page<Article> page = null;
			// 1.查询文章分类下文章列表
			if (artCatId != null) {// 如果artCatId不为空则查询
				Article article = new Article();
				article.setStatus(articleDTO.getStatus());
				article.setArtId(articleDTO.getArtId());

				if (article.getStatus() == null) {
					article.setStatus(1);
				}

				LOGGER.debug("分类Id：artCatId->" + artCatId);

				if (articleDTO.getPageNo() == null || articleDTO.getPageNo() <= 0) {
					respVO.setStatus("0");
					respVO.setInfo("服务提供者接收pageNo参数不正确！");
					respVO.setErrCode("500");
					return JSON.toJSONString(respVO);
				}

				Integer pageNo = articleDTO.getPageNo() - 1;
				LOGGER.debug("分页pageNo->" + pageNo);

				if (pageNo >= 1 && article.getArtId() == null) {
					respVO.setStatus("0");
					respVO.setInfo("服务提供者接收artId参数为空！");
					respVO.setErrCode("500");
					return JSON.toJSONString(respVO);
				} else if (pageNo == 0) {// 当pageNo==0时即传入pageNo==1时请求最新数据
					article.setArtId(null);
				}

				Integer pageSize = articleDTO.getPageSize() != null ? articleDTO.getPageSize() : 10;
				LOGGER.debug("分页pageSize->" + pageSize);

				Sort sort = null;
				if (orderList != null && orderList.size() > 0) {
					sort = new Sort(orderList);
				}

				PageRequest pageable = new PageRequest(pageNo, pageSize, sort);
				try {
					Long remArtCatId = (Long) client.get("app_art_rem_cat_id");
					if (remArtCatId == null) {
						Config config = configRepository.findOne("app_art_rem_cat_id");
						if (config != null) {
							remArtCatId = Long.parseLong(config.getValue());
							client.set("app_art_list_flesh_time", 24 * 60 * 60 * 1000, remArtCatId);
						}
					}
					if (artCatId == remArtCatId) {// artCatId为0时为推荐
						LOGGER.debug("查询推荐文章列表");
						article.setIsRecommend(1);
						page = articleRepository.findAll(new ArticleSpecification(article), pageable);
					} else {
						LOGGER.debug("查询文章分类：" + artCatId);
						ArticleCategory articleCategory = new ArticleCategory();
						articleCategory.setArtCatId(remArtCatId);
						article.setArticleCategory(articleCategory);
						page = articleRepository.findAll(new ArticleSpecification(article), pageable);
					}
				} catch (Exception e) {
					respVO.setStatus("0");
					respVO.setInfo("服务提供者查询文章列表异常！");
					respVO.setErrCode("500");
					return JSON.toJSONString(respVO);
				}

				List<Article> list = new ArrayList<>();
				list = page.getContent();
				int totalPages = page.getTotalPages();
				LOGGER.debug("查询文章列表结果：" + list);
				int size = list.size();
				if (size > 0) {
					respVO.setStatus("1");
					respVO.setInfo("查询分类文章列表成功！");
					respVO.setErrCode("200");
					Map<String, Object> result = new HashMap<>();
					if (pageNo == 0) {
						Long artMaxId = articleRepository.findArtMaxId();
						result.put("lstArtId", artMaxId);
					}
					result.put("remPage", String.valueOf(totalPages - pageNo - 1));
					List<Article> resList = new ArrayList<>();
					for (Article article2 : list) {
						article2.setStatus(null);
						article2.setCreateTime(null);
						article2.setMore();
						article2.setArticleCategory(null);
						resList.add(article2);
					}
					result.put("artList", resList);
					respVO.setData(result);
				} else {
					respVO.setStatus("1");
					respVO.setInfo("查询分类文章列表成功！");
					respVO.setErrCode("200");
					Map<String, Object> result = new HashMap<>();
					if (pageNo == 0) {
						Long artMaxId = articleRepository.findArtMaxId();
						result.put("lstArtId", artMaxId);
					}
					result.put("remPage", 0);
					result.put("artList", list);
					respVO.setData(result);
				}
			} else {
				respVO.setStatus("0");
				respVO.setInfo("服务提供者接收artCatId参数为空！");
				respVO.setErrCode("500");
			}

		} else {
			respVO.setStatus("0");
			respVO.setInfo("服务提供者接收参数为空！");
			respVO.setErrCode("500");
		}
		LOGGER.debug("------------------分类页面数据加载服务提供者结束-----------------------");
		return JSON.toJSONString(respVO);
	}

	/**
	 * 搜索功能暂不实现
	 * 
	 * @param keyWord
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String artInfoQuery(String postData) {
		LOGGER.debug("------------------文章信息综合查询服务提供者开始-----------------------");
		LOGGER.debug("请求参数：postData：" + postData);
		RespVO respVO = new RespVO();
		if (StringUtils.isNotBlank(postData)) {
			ArticleQueryDTO articleDTO = null;
			try {
				articleDTO = JSON.parseObject(postData, ArticleQueryDTO.class);
			} catch (Exception e) {
				respVO.setStatus("0");
				respVO.setInfo("解析参数失败！");
				respVO.setErrCode("500");
				LOGGER.debug("解析参数失败！");
				return JSON.toJSONString(respVO);
			}

			String keyWord = articleDTO.getKeyWord();
			LOGGER.debug("搜索关键字：keyWord->" + keyWord);

			// 添加排序
			Map<String, Object> orderMap = articleDTO.getOrderMap();
			List<Order> orderList = new ArrayList<Order>();
			if (orderMap != null) {
				List<String> ascList = (List<String>) orderMap.get("asc");
				if (ascList != null && ascList.size() > 0) {
					for (String string : ascList) {
						orderList.add(new Order(Direction.ASC, string));
					}
				}
				List<String> descList = (List<String>) orderMap.get("desc");
				if (descList != null && descList.size() > 0) {
					for (String string : descList) {
						orderList.add(new Order(Direction.DESC, string));
					}
				}
			}

			LOGGER.debug("文章排序字段：orderList->" + orderList);
			Page<Article> page = null;
			// 1.查询文章分类下文章列表
			if (keyWord != null) {// 如果artCatId不为空则查询
				Article article = new Article();
				article.setStatus(articleDTO.getStatus());
				article.setKeyWord(keyWord);
				if (article.getStatus() == null) {
					article.setStatus(1);
				}

				if (articleDTO.getPageNo() == null || articleDTO.getPageNo() == 0) {
					respVO.setStatus("0");
					respVO.setInfo("服务提供者接收pageNo参数不正确！");
					respVO.setErrCode("500");
					return JSON.toJSONString(respVO);
				}

				Integer pageNo = articleDTO.getPageNo() - 1;
				LOGGER.debug("分页pageNo->" + pageNo);
				Integer pageSize = articleDTO.getPageSize() != null ? articleDTO.getPageSize() : 10;
				LOGGER.debug("分页pageSize->" + pageSize);

				Sort sort = null;
				if (orderList != null && orderList.size() > 0) {
					sort = new Sort(orderList);
				}

				PageRequest pageable = new PageRequest(pageNo, pageSize, sort);
				try {
					page = articleRepository.findAll(new ArticleSpecification(article), pageable);
				} catch (Exception e) {
					respVO.setStatus("0");
					respVO.setInfo("服务提供者搜索文章异常！");
					respVO.setErrCode("500");
					return JSON.toJSONString(respVO);
				}

				List<Article> list = page.getContent();
				LOGGER.debug("查询文章列表结果：" + list);
				respVO.setStatus("1");
				respVO.setInfo("查询分类文章列表成功！");
				respVO.setErrCode("200");
				Map<String, Object> result = new HashMap<>();
				List<Article> resList = new ArrayList<>();
				for (Article article2 : list) {
					article2.setStatus(null);
					article2.setCreateTime(null);
					article2.setMore();
					article2.setArticleCategory(null);
					resList.add(article2);
				}
				result.put("artList", resList);
				respVO.setData(result);
				if (list != null && list.size() > 0) {
					ArticleSearchRecord entity = articleSearchRecordRepository.findByKeyWord(keyWord);
					if (entity != null) {
						entity.setSearchCount(entity.getSearchCount()+1);
						entity.setUpdateTime(System.currentTimeMillis()/1000);
					}else{
						entity = new ArticleSearchRecord();
						entity.setKeyWord(keyWord);
						entity.setSearchCount(1L);
						entity.setCreateTime(System.currentTimeMillis()/1000);
						entity.setUpdateTime(System.currentTimeMillis()/1000);
					}
					articleSearchRecordRepository.save(entity);
				}
			} else {
				respVO.setStatus("0");
				respVO.setInfo("服务提供者接收keyWord参数为空！");
				respVO.setErrCode("500");
			}

		} else {
			respVO.setStatus("0");
			respVO.setInfo("服务提供者接收参数为空！");
			respVO.setErrCode("500");
		}
		LOGGER.debug("------------------文章信息综合查询服务提供者结束-----------------------");
		return JSON.toJSONString(respVO);
	}

	/**
	 * 热门搜索关键字列表
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String artKeyHotQuery(String postData) {
		LOGGER.debug("------------------查询热门搜索服务提供者开始-----------------------");
		LOGGER.debug("请求参数：postData：" + postData);
		RespVO respVO = new RespVO();
		if (StringUtils.isNotBlank(postData)) {
			ArticleQueryDTO articleDTO = null;
			try {
				articleDTO = JSON.parseObject(postData, ArticleQueryDTO.class);
			} catch (Exception e) {
				respVO.setStatus("0");
				respVO.setInfo("解析参数失败！");
				respVO.setErrCode("500");
				LOGGER.debug("解析参数失败！");
				return JSON.toJSONString(respVO);
			}

			// 添加排序
			Map<String, Object> orderMap = articleDTO.getOrderMap();
			List<Order> orderList = new ArrayList<Order>();
			if (orderMap != null) {
				List<String> ascList = (List<String>) orderMap.get("asc");
				if (ascList != null && ascList.size() > 0) {
					for (String string : ascList) {
						orderList.add(new Order(Direction.ASC, string));
					}
				}
				List<String> descList = (List<String>) orderMap.get("desc");
				if (descList != null && descList.size() > 0) {
					for (String string : descList) {
						orderList.add(new Order(Direction.DESC, string));
					}
				}
			}

			LOGGER.debug("排序字段：orderList->" + orderList);
			Page<ArticleSearchRecord> page = null;

			Integer pageNo = articleDTO.getPageNo() - 1;
			LOGGER.debug("分页pageNo->" + pageNo);
			Integer pageSize = articleDTO.getPageSize() != null ? articleDTO.getPageSize() : 10;
			LOGGER.debug("分页pageSize->" + pageSize);

			Sort sort = null;
			if (orderList != null && orderList.size() > 0) {
				sort = new Sort(orderList);
			}

			PageRequest pageable = new PageRequest(pageNo, pageSize, sort);
			try {
				page = articleSearchRecordRepository.findAll(pageable);
			} catch (Exception e) {
				respVO.setStatus("0");
				respVO.setInfo("服务提供者查询热门搜索关键字异常！");
				respVO.setErrCode("500");
				return JSON.toJSONString(respVO);
			}

			List<ArticleSearchRecord> list = page.getContent();
			List<String> hotSeaList = new ArrayList<>();
			if (list != null) {
				for (ArticleSearchRecord articleSearchRecord : list) {
					hotSeaList.add(articleSearchRecord.getKeyWord());
				}
			}
			LOGGER.debug("查询热门搜索历史记录列表结果：" + list);
			respVO.setStatus("1");
			respVO.setInfo("查询热门搜索关键字成功！");
			respVO.setErrCode("200");
			Map<String, Object> result = new HashMap<>();
			result.put("hotSeaList", hotSeaList);
			respVO.setData(result);

		} else {
			respVO.setStatus("0");
			respVO.setInfo("服务提供者接收参数为空！");
			respVO.setErrCode("500");
		}
		LOGGER.debug("------------------分类页面数据加载服务提供者结束-----------------------");
		return JSON.toJSONString(respVO);
	}

	/**
	 * 文章详情
	 * 
	 * @param artId
	 * @return
	 */
	@Override
	public String artDetInfoQuery(String postData) {
		LOGGER.debug("------------------查询文章详情服务提供者开始-----------------------");
		LOGGER.debug("请求参数：postData：" + postData);
		RespVO respVO = new RespVO();
		if (StringUtils.isNotBlank(postData)) {
			ArticleQueryDTO articleDTO = null;
			try {
				articleDTO = JSON.parseObject(postData, ArticleQueryDTO.class);
			} catch (Exception e) {
				respVO.setStatus("0");
				respVO.setInfo("解析参数失败！");
				respVO.setErrCode("500");
				LOGGER.debug("解析参数失败！");
				return JSON.toJSONString(respVO);
			}

			Long artId = articleDTO.getArtId();
			LOGGER.debug("文章id：artId->" + artId);

			// 1.查询文章分类下文章列表
			if (artId != null) {// 如果artId不为空则查询文章详情
				Article article = new Article();
				article.setStatus(articleDTO.getStatus());
				if (article.getStatus() == null) {
					article.setStatus(1);
				}
				Article article2 = null;
				try {
					article2 = articleRepository.findByArtIdAndStatus(artId, article.getStatus());
				} catch (Exception e) {
					respVO.setStatus("0");
					respVO.setInfo("服务提供者搜索文章异常！");
					respVO.setErrCode("500");
					return JSON.toJSONString(respVO);
				}
				Long userId = articleDTO.getUserId();
				
				UserArticleOption userArticleOption = null;
				try {
					if (userId!=null) {
						userArticleOption = userArticleOptionRepository.findByUserIdAndArtIdAndOptType(userId, artId, 1);
					}
				} catch (Exception e) {
					respVO.setStatus("0");
					respVO.setInfo("服务提供者查询用户文章操作记录异常！");
					respVO.setErrCode("500");
					return JSON.toJSONString(respVO);
				}

				LOGGER.debug("查询文章详情结果：" + article2);
				if (article2 != null) {
					article2.setArtCatName(article2.getArticleCategory().getArtCatName());
					respVO.setStatus("1");
					respVO.setInfo("查询文章详情成功！");
					respVO.setErrCode("200");
					Map<String, Object> result = new HashMap<>();
					if (userArticleOption!=null) {
						article2.setIsCollection(1);
					}else{
						article2.setIsCollection(0);
					}
					article2.setMore();
					article2.setArticleCategory(null);
					article2.setStatus(null);
					article2.setCreateTime(null);
					result.put("article", article2);
					respVO.setData(result);
				} else {
					respVO.setStatus("0");
					respVO.setInfo("此文章被禁用或不存在！");
					respVO.setErrCode("404");
				}
			} else {
				respVO.setStatus("0");
				respVO.setInfo("服务提供者接收artId参数为空！");
				respVO.setErrCode("500");
			}

		} else {
			respVO.setStatus("0");
			respVO.setInfo("服务提供者接收参数为空！");
			respVO.setErrCode("500");
		}
		LOGGER.debug("------------------查询文章详情服务提供者结束-----------------------");
		return JSON.toJSONString(respVO);
	}

	/**
	 * 文章编辑
	 * 
	 * @param artId
	 * @param userId
	 * @param optType
	 * @return
	 */
	@Transactional
	@Override
	public String artDetInfoUpdate(String postData) {
		LOGGER.debug("------------------文章更新编辑服务提供者开始-----------------------");
		LOGGER.debug("请求参数：postData：" + postData);
		RespVO respVO = new RespVO();
		if (StringUtils.isNotBlank(postData)) {
			ArticleQueryDTO articleDTO = null;
			try {
				articleDTO = JSON.parseObject(postData, ArticleQueryDTO.class);
			} catch (Exception e) {
				respVO.setStatus("0");
				respVO.setInfo("解析参数失败！");
				respVO.setErrCode("500");
				LOGGER.debug("解析参数失败！");
				return JSON.toJSONString(respVO);
			}

			Long artId = articleDTO.getArtId();
			Long userId = articleDTO.getUserId();
			Integer optType = articleDTO.getOptType();
			LOGGER.debug("文章id：artId->" + artId);
			LOGGER.debug("用户id：userId->" + userId);
			LOGGER.debug("操作类型：optType->" + optType);

			// 1.查询文章信息
			if (artId != null && userId != null && optType != null) {// 如果artId不为空则查询文章详情
				Article article = new Article();
				article.setStatus(articleDTO.getStatus());
				if (article.getStatus() == null) {
					article.setStatus(1);
				}
				Article article2 = null;
				try {
					article2 = articleRepository.findByArtIdAndStatus(artId, article.getStatus());
				} catch (Exception e) {
					respVO.setStatus("0");
					respVO.setInfo("服务提供者搜索文章异常！");
					respVO.setErrCode("500");
					return JSON.toJSONString(respVO);
				}

				LOGGER.debug("查询文章详情结果：" + article2);
				if (article2 != null) {
					if (optType == 1) {
						UserArticleOption userArticleOption = null;
						try {
							userArticleOption = userArticleOptionRepository.findByUserIdAndArtIdAndOptType(userId, artId, 1);
						} catch (Exception e) {
							respVO.setStatus("0");
							respVO.setInfo("服务提供者查询用户文章操作记录异常！");
							respVO.setErrCode("500");
							return JSON.toJSONString(respVO);
						}
						if (userArticleOption!=null) {
							userArticleOptionRepository.delete(userArticleOption.getUserArtOptId());
							article2.setTotalCollection(article2.getTotalCollection() - 1);
						}else{
							article2.setTotalCollection(article2.getTotalCollection() + 1);
							
							userArticleOption = new UserArticleOption();
							userArticleOption.setArtId(artId);
							userArticleOption.setUserId(userId);
							userArticleOption.setOptType(optType);
							userArticleOption.setCreateTime(System.currentTimeMillis() / 1000);
							userArticleOption.setUpdateTime(System.currentTimeMillis() / 1000);
							userArticleOptionRepository.save(userArticleOption);
						}
					} else if (optType == 2) {
						article2.setTotalShare(article2.getTotalShare() + 1);
						UserArticleOption userArticleOption = new UserArticleOption();
						userArticleOption.setArtId(artId);
						userArticleOption.setUserId(userId);
						userArticleOption.setOptType(optType);
						userArticleOption.setCreateTime(System.currentTimeMillis() / 1000);
						userArticleOption.setUpdateTime(System.currentTimeMillis() / 1000);
						userArticleOptionRepository.save(userArticleOption);
					}
					articleRepository.save(article2);
					
					respVO.setStatus("1");
					respVO.setInfo("更新文章操作成功！");
					respVO.setErrCode("200");
				} else {
					respVO.setStatus("0");
					respVO.setInfo("操作文章被禁用或不存在！");
					respVO.setErrCode("404");
				}
			} else {
				respVO.setStatus("0");
				respVO.setInfo("服务提供者接收artId参数为" + artId + ",userId为" + userId + ",optType为" + optType);
				respVO.setErrCode("500");
			}

		} else {
			respVO.setStatus("0");
			respVO.setInfo("服务提供者接收参数为空！");
			respVO.setErrCode("500");
		}
		LOGGER.debug("------------------文章更新编辑服务提供者结束-----------------------");
		return JSON.toJSONString(respVO);
	}

}

class ArticleCategorySpecification implements Specification<ArticleCategory> {

	private ArticleCategory articleCategory;

	public ArticleCategorySpecification(ArticleCategory articleCategory) {
		this.articleCategory = articleCategory;
	}

	@Override
	public Predicate toPredicate(Root<ArticleCategory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> list = new ArrayList<Predicate>();

		if (articleCategory.getArtCatIdArr() != null && articleCategory.getArtCatIdArr().length > 0) {
			In<Long> in = cb.in(root.get("artCatId").as(Long.class));
			String[] artCatIdsArr = articleCategory.getArtCatIdArr();
			for (String artCatId : artCatIdsArr) {
				if (StringUtils.isNumeric(artCatId)) {
					in.value(Long.parseLong(artCatId));
				}
			}
			list.add(in);
		}
		if (articleCategory.getStatus() != null) {
			list.add(cb.equal(root.get("status").as(Integer.class), articleCategory.getStatus()));
		}
		Predicate[] p = new Predicate[list.size()];
		return cb.and(list.toArray(p));
	}

}

class ArticleSpecification implements Specification<Article> {

	private Article article;

	public ArticleSpecification(Article article) {
		this.article = article;
	}

	@Override
	public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> list = new ArrayList<Predicate>();

		if (StringUtils.isNotBlank(article.getKeyWord())) {
			list.add(cb.or(cb.like(root.get("title").as(String.class), "%" + article.getKeyWord() + "%"),
					cb.like(root.get("keyWords").as(String.class), "%" + article.getKeyWord() + "%")));
		}
		if (article.getArtId() != null) {// 用于查询文章列表上拉加载数据文章id小于最后文章id
			list.add(cb.lt(root.get("artId").as(Integer.class), article.getArtId()));
		}
		if (article.getStatus() != null) {
			list.add(cb.equal(root.get("status").as(Integer.class), article.getStatus()));
		}
		if (article.getIsRecommend() != null) {
			list.add(cb.equal(root.get("isRecommend").as(Integer.class), article.getIsRecommend()));
		}
		Predicate[] p = new Predicate[list.size()];
		return cb.and(list.toArray(p));
	}

}
