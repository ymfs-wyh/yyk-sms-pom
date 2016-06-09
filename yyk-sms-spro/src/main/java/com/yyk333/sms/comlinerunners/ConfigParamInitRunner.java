package com.yyk333.sms.comlinerunners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import com.yyk333.sms.config.entities.Config;
import com.yyk333.sms.config.repository.ConfigRepository;

import net.spy.memcached.MemcachedClient;

@Order(value = 1) // value数值越小越先执行
public class ConfigParamInitRunner implements CommandLineRunner {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigParamInitRunner.class);
	
	@Autowired
	private MemcachedClient client;
	
	@Autowired
	private ConfigRepository configRepository;
	
	@Override
	public void run(String... args) throws Exception {
		// 将app 文章列表自动刷新时间设置到缓存
		Config config = configRepository.findOne("app_art_list_flesh_time");
		if (config!=null) {
			client.set("app_art_list_flesh_time", 24*60*60*1000, config.getValue());
			LOGGER.debug("app_art_list_flesh_time:"+config.getValue());
		}
		
		// 将推荐分类的id查询出来放入缓存
		config = configRepository.findOne("app_art_rem_cat_id");
		if (config!=null) {
			client.set("app_art_rem_cat_id", 24*60*60*1000, config.getValue());
			LOGGER.debug("app_art_rem_cat_id:"+config.getValue());
		}
	}

}
