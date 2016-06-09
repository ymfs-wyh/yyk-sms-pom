package com.yyk333.sms.version.services.impl;

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
import com.yyk333.sms.utils.MD5Util;
import com.yyk333.sms.version.entities.Version;
import com.yyk333.sms.version.repsository.VersionRepository;
import com.yyk333.sms.version.services.VersionControlService;
import com.yyk333.sms.vo.RespVO;

import net.spy.memcached.MemcachedClient;

/**
 * 版本控制的Service
 * 
 * @author weiyanhao
 *
 */
@Service("versionControlService")
public class VersionControlServiceImpl implements VersionControlService {

	private static final Logger logger = LoggerFactory.getLogger(VersionControlServiceImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private VersionRepository versionRepository;

	@Autowired
	private MemcachedClient client;

	/**
	 * 用于检查版本更新
	 * 
	 * @param postData:是一个JSON字符串
	 * @return 将返回一个JSON字符串
	 */
	@Override
	public String checkComAndForVerUpd(String postData) {
		logger.debug("--------------------查询版本更新开始---------------------------");
		logger.debug("查询参数：" + postData);
		RespVO respVO = new RespVO();
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(postData)) {

			// 查询VO
			Version verQryVO = null;
			try {
				verQryVO = JSON.parseObject(postData, Version.class);
				logger.debug("解析参数成功！");
			} catch (Exception e) {// 解析参数异常
				respVO.setStatus("1");
				respVO.setErrCode("500");
				respVO.setInfo("业务层参数解析异常！");
				logger.debug("解析参数异常！");
				return JSON.toJSONString(respVO);
			}

			// 1.获取最新版本
			Version version = null;
			try {
				version = getCacheLstVersion(verQryVO);
			} catch (Exception e) {
				respVO.setStatus("1");
				respVO.setErrCode("500");
				respVO.setInfo("查询最新版本异常！");
				logger.debug("查询最新版本异常！");
				return JSON.toJSONString(respVO);
			}

			// 1.获取热更新版本
			Version version2 = null;
			try {
				version2 = getCacheHotVersion(verQryVO);
			} catch (Exception e) {
				respVO.setStatus("1");
				respVO.setErrCode("500");
				respVO.setInfo("查询热更新版本异常！");
				logger.debug("查询热更新版本异常！");
				return JSON.toJSONString(respVO);
			}

			// 2.1 传入版本参数
			String versionCode = verQryVO.getVersionCode();
			if (StringUtils.isBlank(versionCode)) {
				respVO.setStatus("1");
				respVO.setErrCode("500");
				respVO.setInfo("业务层接收的版本为空！");
				logger.debug("业务层接收的版本为空！");
				return JSON.toJSONString(respVO);
			}

			if (version != null && versionCompare(version.getVersionCode(), versionCode)) {
				logger.debug("查询的最新版本为："+version.toString());
				// 2.判断更新方式
				Integer updateWay = version.getUpdateWay();
				if (updateWay == 0 && version.getUpdateVersion().contains(versionCode)
						|| updateWay == 1 && versionCompare(version.getUpdateVersion(), versionCode)) {
					// 自动义，最新版本要求更新的版本是否包含传入版本 或者 低于，传入版本是否低于最新版本要求更新的版本；是强制更新
					Map<String, Object> map = new HashMap<>();
					map.put("code", "100");
					map.put("version", version.getVersionCode());
					map.put("title", "版本更新");
					map.put("note", version.getVersionDetail());
					String path = version.getUpdatePath().startsWith("http")?"":SystemConstants.FILE_SERVER_PATH;
					map.put("url", path + version.getUpdatePath());
					map.put("mustUpdate", "1");
					logger.debug("查询强制更新成功：" + map.toString());
					result.put("com_for_data", map);
				} else if (updateWay == 0 && !version.getUpdateVersion().contains(versionCode)
						|| updateWay == 1 && !versionCompare(version.getUpdateVersion(), versionCode)) {
					// 普通更新
					Map<String, Object> map = new HashMap<>();
					map.put("code", "100");
					map.put("version", version.getVersionCode());
					map.put("title", "版本更新");
					map.put("note", version.getVersionDetail());
					String path = version.getUpdatePath().startsWith("http")?"":SystemConstants.FILE_SERVER_PATH;
					map.put("url", path + version.getUpdatePath());
					map.put("mustUpdate", "0");
					logger.debug("查询普通更新成功：" + map.toString());
					result.put("com_for_data", map);

					// 检查是否热更新
					if (version2 != null) {// 查询到热更新版本
						logger.debug("查询的热更新版本为："+version2.toString());
						if (!String.valueOf(verQryVO.getAddTime()).equals(String.valueOf(version2.getAddTime()))) {

							Map<String, Object> hotMap = new HashMap<>();
							hotMap.put("code", "100");
							hotMap.put("version", version2.getVersionCode());
							hotMap.put("title", "版本更新");
							hotMap.put("note", version2.getVersionDetail());
							hotMap.put("patAddTime", version2.getAddTime());
							String path2 = version2.getUpdatePath().startsWith("http")?"":SystemConstants.FILE_SERVER_PATH;
							hotMap.put("url", path2 + version2.getUpdatePath());
							hotMap.put("mustUpdate", "2");
							logger.debug("查询热更新成功：" + hotMap.toString());
							result.put("hot_upd_data", hotMap);
						}
					}

				} else {// 查询的更新版本更新方式错误
					respVO.setStatus("0");
					respVO.setErrCode("200");
					respVO.setInfo("更新方式错误！");
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("code", "101");
					result.put("com_for_data", map);
					respVO.setData(result);
					logger.debug("更新方式错误！");
					return JSON.toJSONString(respVO);
				}
			} else if (version != null && versionCode.equals(version.getVersionCode())) {// 同版本检查是否热更新
				if (version2 != null) {// 查询到热更新版本
					logger.debug("查询的热更新版本为："+version2.toString());
					if (!String.valueOf(verQryVO.getAddTime()).equals(String.valueOf(version2.getAddTime()))) {

						Map<String, Object> map = new HashMap<>();
						map.put("code", "100");
						map.put("version", version2.getVersionCode());
						map.put("title", "版本更新");
						map.put("note", version2.getVersionDetail());
						map.put("patAddTime", version2.getAddTime());
						String path2 = version2.getUpdatePath().startsWith("http")?"":SystemConstants.FILE_SERVER_PATH;
						map.put("url", path2 + version2.getUpdatePath());
						map.put("mustUpdate", "2");
						result.put("hot_upd_data", map);
					}
				} else {// 当前版本无可更新
					respVO.setStatus("0");
					respVO.setErrCode("200");
					respVO.setInfo("无可更新版本！");
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("code", "101");
					result.put("com_for_data", map);
					respVO.setData(result);
					logger.debug("无可更新版本！");
					return JSON.toJSONString(respVO);
				}
			} else {// 当前版本无可更新
				respVO.setStatus("0");
				respVO.setErrCode("200");
				respVO.setInfo("无可更新版本！");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", "101");
				result.put("com_for_data", map);
				respVO.setData(result);
				logger.debug("无可更新版本！");
				return JSON.toJSONString(respVO);
			}
		}
		respVO.setStatus("0");
		respVO.setErrCode("200");
		respVO.setInfo("版本更新查询成功！");
		respVO.setData(result);
		logger.debug("版本更新查询成功！");
		logger.debug("版本更新：" + respVO.toString());
		logger.debug("--------------------查询版本更新结束---------------------------");
		return JSON.toJSONString(respVO);

	}

	private Version getCacheLstVersion(Version verQryVO) {
		StringBuffer sb = new StringBuffer();
		sb.append(verQryVO.getStatus()).append(verQryVO.getEquipmentType()).append(verQryVO.getAppType());

		String mainMemcacheKey = MD5Util.signUtf8(sb.toString());

		Version version = null;
		if (verQryVO.getEquipmentType() == 0 && client.get("iosLstVer_" + mainMemcacheKey) != null) {// ios
			version = (Version) client.get("iosLstVer_" + mainMemcacheKey);
		} else if (verQryVO.getEquipmentType() == 1 && client.get("andLstVer_" + mainMemcacheKey) != null) {
			version = (Version) client.get("andLstVer_" + mainMemcacheKey);
		}

		// 1.查找最新版本
		if (version == null) {// 缓存无数据，查询数据库
			List<Version> versions = versionRepository.findLastVersions(verQryVO.getEquipmentType(),
					verQryVO.getAppType());

			if (versions != null && versions.size() == 1) {
				version = versions.get(0);
				if (version == null) {
					logger.debug("无符合条件的最新版本");
				} else {
					int expir_time = Integer.parseInt(SystemConstants.VERSION_UPDATE_MEMCACHE_EXPIRATION_TIME);
					if (verQryVO.getEquipmentType() == 0) {// ios
						String key = "iosLstVer_" + mainMemcacheKey;
						client.set(key, expir_time, version);

						String cacheKeys = (String) client.get("VersionUpdateCacheKey");
						cacheKeys = cacheKeys == null ? "" : cacheKeys;
						String keys = cacheKeys + SystemConstants.COMM_SPLIT + key;
						if (keys.startsWith(",")) {
							keys = keys.substring(1);
						}
						client.set("VersionUpdateCacheKey", expir_time, keys);
					} else if (verQryVO.getEquipmentType() == 1) {
						String key = "andLstVer_" + mainMemcacheKey;
						client.set("andLstVer_" + mainMemcacheKey, expir_time, version);

						String cacheKeys = (String) client.get("VersionUpdateCacheKey");
						cacheKeys = cacheKeys == null ? "" : cacheKeys;
						String keys = cacheKeys + SystemConstants.COMM_SPLIT + key;
						if (keys.startsWith(",")) {
							keys = keys.substring(1);
						}
						client.set("VersionUpdateCacheKey", expir_time, keys);
					}
				}
			} else {
				logger.debug("查询最新版本结果size:" + versions.size());
			}
		}
		return version;
	}

	private Version getCacheHotVersion(Version verQryVO) {
		StringBuffer sb = new StringBuffer();
		sb.append(verQryVO.getStatus()).append(verQryVO.getEquipmentType()).append(verQryVO.getVersionCode())
				.append(verQryVO.getAppType());

		String hotMemcacheKey = MD5Util.signUtf8(sb.toString());

		Version version = null;
		if (verQryVO.getEquipmentType() == 0 && client.get("iosHotVer_" + hotMemcacheKey) != null) {// ios
			version = (Version) client.get("iosHotVer_" + hotMemcacheKey);
		} else if (verQryVO.getEquipmentType() == 1 && client.get("andHotVer_" + hotMemcacheKey) != null) {
			version = (Version) client.get("andHotVer_" + hotMemcacheKey);
		}

		// 1.查找热更新版本
		if (version == null) {// 缓存无数据，查询数据库
			version = versionRepository.findPVersionByQueryVo(verQryVO.getStatus(), verQryVO.getEquipmentType(),
					verQryVO.getVersionCode(), verQryVO.getAppType());

			if (version != null) {
				int expir_time = Integer.parseInt(SystemConstants.VERSION_UPDATE_MEMCACHE_EXPIRATION_TIME);
				if (verQryVO.getEquipmentType() == 0) {// ios
					String key = "iosHotVer_" + hotMemcacheKey;
					client.set("iosHotVer_" + hotMemcacheKey, expir_time, version);

					String cacheKeys = (String) client.get("VersionUpdateCacheKey");
					cacheKeys = cacheKeys == null ? "" : cacheKeys;
					String keys = cacheKeys + SystemConstants.COMM_SPLIT + key;
					if (keys.startsWith(",")) {
						keys = keys.substring(1);
					}
					client.set("VersionUpdateCacheKey", expir_time, keys);
				} else if (verQryVO.getEquipmentType() == 1) {
					String key = "andHotVer_" + hotMemcacheKey;
					client.set("andHotVer_" + hotMemcacheKey, expir_time, version);

					String cacheKeys = (String) client.get("VersionUpdateCacheKey");
					cacheKeys = cacheKeys == null ? "" : cacheKeys;
					String keys = cacheKeys + SystemConstants.COMM_SPLIT + key;
					if (keys.startsWith(",")) {
						keys = keys.substring(1);
					}
					client.set("VersionUpdateCacheKey", expir_time, keys);
				}
			} else {
				logger.debug("没有符合条件的热更新版本！");
			}
		}
		return version;
	}

	/**
	 * 用于比较版本，oldVer版本大就返回true，否则返回false
	 * 
	 * @param oldVer
	 * @param newVer
	 * @return
	 */
	private boolean versionCompare(String oldVer, String newVer) {

		if (StringUtils.isBlank(oldVer)) {
			return false;
		}

		String[] oldVerArr = oldVer.split("\\.");
		String[] newVerArr = newVer.split("\\.");

		int oldVerArrLength = oldVerArr.length;
		int newVerArrLength = newVerArr.length;

		int index = oldVerArrLength < newVerArrLength ? oldVerArrLength : newVerArrLength;
		boolean flag = false;
		for (int i = 0; i < index; i++) {
			Integer a = Integer.parseInt(oldVerArr[i]);
			Integer b = Integer.parseInt(newVerArr[i]);
			flag = a > b;
			if (!(a == b)) {// 版本第i个数字相同继续比较
				break;
			}
		}
		return flag;
	}

}
