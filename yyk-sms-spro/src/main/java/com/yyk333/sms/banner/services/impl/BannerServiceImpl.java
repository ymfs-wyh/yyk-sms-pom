package com.yyk333.sms.banner.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yyk333.sms.banner.dto.BannerDTO;
import com.yyk333.sms.banner.entities.Banner;
import com.yyk333.sms.banner.repository.BannerRepository;
import com.yyk333.sms.banner.services.BannerService;
import com.yyk333.sms.constants.SystemConstants;
import com.yyk333.sms.vo.RespVO;

import net.spy.memcached.MemcachedClient;

@Service("bannerService")
public class BannerServiceImpl implements BannerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BannerServiceImpl.class);

	@Autowired
	private MemcachedClient client;

	@Autowired
	private BannerRepository bannerRepository;

	/**
	 * 加载轮播图
	 * 
	 * @param bannerlocation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String bannerListLoad(String postData) {
		// TODO Auto-generated method stub
		LOGGER.debug("------------------分类栏目数据加载服务提供者开始-----------------------");
		LOGGER.debug("请求参数：postData：" + postData);
		RespVO respVO = new RespVO();
		if (StringUtils.isNotBlank(postData)) {
			BannerDTO bannerDTO = new BannerDTO();
			try {
				bannerDTO = JSON.parseObject(postData, BannerDTO.class);
			} catch (Exception e) {
				respVO.setStatus("0");
				respVO.setInfo("解析参数失败！");
				respVO.setErrCode("500");
				LOGGER.debug("解析参数失败！");
				return JSON.toJSONString(respVO);
			}

			Banner banner = new Banner();
			banner = bannerDTO.getBanner();
			if (banner == null) {
				respVO.setStatus("0");
				respVO.setInfo("服务提供者接收参数为空！");
				respVO.setErrCode("500");
				LOGGER.debug("服务提供者接收参数为空！");
				return JSON.toJSONString(respVO);
			}

			LOGGER.debug("轮播参数：banner->" + banner);

			// 添加排序
			Map<String, Object> orderMap = bannerDTO.getOrderMap();
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
			
			List<Banner> list = new ArrayList<>();
			// 1.查询轮播列表
			try {
				list = bannerRepository.findAll(new BannerSpecification(banner), sort);
			} catch (Exception e) {
				LOGGER.debug("查询用户栏目异常！");
			}

			respVO.setStatus("1");
			respVO.setInfo("查询轮播列表成功！");
			respVO.setErrCode("200");
			Map<String, Object> map = new HashMap<>();
			List<Banner> bannerList = new ArrayList<>();
			for (Banner banner2 : list) {
				banner2.setBannerimg(SystemConstants.FILE_SERVER_PATH + banner2.getBannerimg());
				banner2.setMore();
				bannerList.add(banner2);
			}
			map.put("bannerList", bannerList);
			respVO.setData(map);
			
		} else {
			respVO.setStatus("0");
			respVO.setInfo("服务提供者接收参数为空！");
			respVO.setErrCode("500");
		}
		LOGGER.debug("------------------分类栏目数据加载服务提供者结束-----------------------");
		return JSON.toJSONString(respVO);
	}

}

class BannerSpecification implements Specification<Banner> {

	private Banner banner;

	public BannerSpecification(Banner banner) {
		this.banner = banner;
	}

	@Override
	public Predicate toPredicate(Root<Banner> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> list = new ArrayList<Predicate>();
		
		if (banner.getStatus()!=null) {
			list.add(cb.equal(root.get("status").as(Integer.class), banner.getStatus()));
		}
		
		if (StringUtils.isNotBlank(banner.getBannerlocation())) {
			list.add(cb.equal(root.get("bannerlocation").as(String.class), banner.getBannerlocation()));
		}
		
		Predicate[] p = new Predicate[list.size()];
		return cb.and(list.toArray(p));
	}

}
