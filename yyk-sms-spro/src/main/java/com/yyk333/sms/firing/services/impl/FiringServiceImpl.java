package com.yyk333.sms.firing.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yyk333.sms.constants.SystemConstants;
import com.yyk333.sms.firing.dto.FiringQueryVO;
import com.yyk333.sms.firing.entities.Firing;
import com.yyk333.sms.firing.entities.FiringCategory;
import com.yyk333.sms.firing.repsository.FiringRepository;
import com.yyk333.sms.firing.services.FiringService;
import com.yyk333.sms.utils.MD5Util;
import com.yyk333.sms.utils.PropertiesUtil;
import com.yyk333.sms.vo.RespVO;

import net.spy.memcached.MemcachedClient;

@Service("firingService")
public class FiringServiceImpl implements FiringService {

	private static final Logger logger = LoggerFactory.getLogger(FiringServiceImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private FiringRepository firingRepository;

	@Autowired
	private MemcachedClient client;

	@Override
	public String checkFiring(String postData) {
		logger.debug("--------------------查询启动图更新开始---------------------------");
		logger.debug("查询参数：" + postData);
		RespVO respVO = new RespVO();
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(postData)) {

			FiringQueryVO firingQueryVO = null;
			try {
				firingQueryVO = JSON.parseObject(postData, FiringQueryVO.class);
			} catch (Exception e) {
				respVO.setStatus("1");
				respVO.setErrCode("500");
				respVO.setInfo("业务层参数解析异常！");
				logger.debug("解析参数异常！");
				return JSON.toJSONString(respVO);
			}

			// 查看缓存是否存在符合条件的启动图
			Firing firing = null;
			try {
				firing = isExist(firingQueryVO);
			} catch (Exception e) {
				respVO.setStatus("1");
				respVO.setErrCode("500");
				respVO.setInfo("查询启动图异常！");
				logger.debug("查询启动图异常！");
				return JSON.toJSONString(respVO);
			}

			if (firing != null) {
				// 传入的启动页时间和查询结果一致
				if (String.valueOf(firing.getUpdateTime()).equals(String.valueOf(firingQueryVO.getUpdateTime()))) {
					respVO.setStatus("0");
					respVO.setErrCode("200");
					respVO.setInfo("启动图已是最新的！");
					map.put("code", "101");
					respVO.setData(map);
					logger.debug("启动图已是最新的！");
					return JSON.toJSONString(respVO);
				}
				respVO.setStatus("0");
				respVO.setErrCode("200");
				respVO.setInfo("请求启动图成功！");
				map.put("code", "100");
				map.put("title", firing.getFiringName());
				String path = firing.getFiringPic().startsWith("http")?"":SystemConstants.FILE_SERVER_PATH;
				map.put("firingPath", path + firing.getFiringPic());
				map.put("updateTime", String.valueOf(firing.getUpdateTime()));
				respVO.setData(map);
				logger.debug("请求启动图成功！");
				return JSON.toJSONString(respVO);
			} else {
				respVO.setStatus("0");
				respVO.setErrCode("200");
				respVO.setInfo("没有符合设备类型的启动图原始图供生成！");
				map.put("code", "101");
				respVO.setData(map);
				logger.debug("没有符合设备类型的启动图原始图供生成！");
				return JSON.toJSONString(respVO);
			}

		} else {// 传入参数为空
			respVO.setStatus("1");
			respVO.setErrCode("500");
			respVO.setInfo("业务层接收参数为空！");
			logger.debug("服务异常，请重新请求！");
			return JSON.toJSONString(respVO);
		}
	}

	private Firing isExist(FiringQueryVO firingQueryVO) {

		StringBuffer sb = new StringBuffer();
		sb.append(firingQueryVO.getResolutionRatioWidth()).append("*").append(firingQueryVO.getResolutionRatioHeight());

		String firMemcacheKey = MD5Util.signUtf8(sb.toString());

		// 获取启动图
		Firing firing = null;
		int device = firingQueryVO.getDevice();
		if (device == 1 && client.get("iosFiring_" + firMemcacheKey) != null) {// ios
			firing = (Firing) client.get("iosFiring_" + firMemcacheKey);
		} else if (device == 2 && client.get("andFiring_" + firMemcacheKey) != null) {
			firing = (Firing) client.get("andFiring_" + firMemcacheKey);
		}

		// 缓存中没有启动图或者获取缓存启动图为null
		if (firing == null) {
			FiringCategory firingCategory = new FiringCategory();
			if (device == 1) {
				firingCategory.setCatKey(PropertiesUtil.getProperty("IOS_Cat_Key"));
				logger.debug("IOS_Cat_Key:"+PropertiesUtil.getProperty("IOS_Cat_Key"));
			} else if (device == 2) {
				firingCategory.setCatKey(PropertiesUtil.getProperty("Android_Cat_Key"));
				logger.debug("Android_Cat_Key:"+PropertiesUtil.getProperty("Android_Cat_Key"));
			}
			firingQueryVO.setFiringCategory(firingCategory);

			// 查询启动页列表，查询原始图和符合宽高的启动图
			logger.debug("Cat_Key:"+firingQueryVO.getFiringCategory().getCatKey());
			List<Firing> firings = firingRepository.findByQueryVo(firingQueryVO.getFiringCategory().getCatKey(),
					firingQueryVO.getResolutionRatioWidth(), firingQueryVO.getResolutionRatioHeight());

			if (firings != null && firings.size() > 0) {// 查询结果不为空时
				// 根据不同的设备宽高选择相应的启动页，没有时通过原始图切割
				firing = selectFitSizePic(firings, firingQueryVO.getResolutionRatioWidth(),
						firingQueryVO.getResolutionRatioHeight());
				if (firing == null) {
					return null;
				}
				int expir_time = Integer.parseInt(SystemConstants.FIRING_MEMCACHE_EXPIRATION_TIME);
				// 将查询结果放入缓存
				if (device == 1) {
					String key = "iosFiring_" + firMemcacheKey;
					client.set(key, expir_time, firing);

					String cacheKeys = (String) client.get("FiringCacheKey");
					cacheKeys = cacheKeys == null ? "" : cacheKeys;
					String keys = cacheKeys + SystemConstants.COMM_SPLIT + key;
					if (keys.startsWith(",")) {
						keys = keys.substring(1);
					}
					client.set("FiringCacheKey", expir_time, keys);
				} else if (device == 2) {
					String key = "andFiring_" + firMemcacheKey;
					client.set("andFiring_" + firMemcacheKey, expir_time, firing);

					String cacheKeys = (String) client.get("FiringCacheKey");
					cacheKeys = cacheKeys == null ? "" : cacheKeys;
					String keys = cacheKeys + SystemConstants.COMM_SPLIT + key;
					if (keys.startsWith(",")) {
						keys = keys.substring(1);
					}
					client.set("FiringCacheKey", expir_time, keys);
				}
			}
		}

		return firing;
	}

	private Firing selectFitSizePic(List<Firing> firings, int width, int height) {

		for (Firing firing : firings) {
			if (firing.getResolutionRatioWidth() == width && firing.getResolutionRatioHeight() == height) {
				return firing;
			}
		}

		for (Firing firing : firings) {
			if (firing.getIsOriginalPic() == 1) {
				/*
				 * // 输出为文件 String fileName = "/" + UUID.randomUUID().toString()
				 * + ".png"; String dirPathAndName =
				 * SystemConstants.FILE_OSS_STORAGE_PATH + DateUtil.getDate(new
				 * Date()) + fileName; String localPathAndName =
				 * SystemConstants.FILE_LOCAL_STORAGE_PATH + fileName;
				 * 
				 * ImageUtils.scalePic(SystemConstants.FILE_SERVER_PATH +
				 * firing.getFiringPic(), localPathAndName, dirPathAndName,
				 * height, width);
				 * 
				 * Firing fir = new Firing(); fir.setFiringName(width + "*" +
				 * height); fir.setFiringCategory(firing.getFiringCategory());
				 * fir.setDevice(firing.getDevice());
				 * fir.setResolutionRatioWidth(width);
				 * fir.setResolutionRatioHeight(height);
				 * fir.setFiringPic(dirPathAndName); fir.setStatus(0);
				 * fir.setUpdateTime(System.currentTimeMillis() / 1000);
				 * fir.setIsOriginalPic(0); fir = firingRepository.save(fir);
				 */
				StringBuffer sb = new StringBuffer();
				sb.append(SystemConstants.PICTURE_DEAL_INTERFACE_ADDR).append(firing.getFiringPic()).append("@")
						.append(height).append("h").append("_").append(width).append("w").append("_2e");
				firing.setFiringPic(sb.toString());
				return firing;
			}
		}

		return null;
	}

}