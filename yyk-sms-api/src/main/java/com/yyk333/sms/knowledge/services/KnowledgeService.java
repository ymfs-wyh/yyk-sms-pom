package com.yyk333.sms.knowledge.services;

public interface KnowledgeService {
	
	/**
	 * 分类页面文章数据加载
	 * @param postData：JSON
	 * @return JSON
	 */
	public String artCatArtInfoLoad(String postData);
	
	/**
	 * 分类页面栏目分类数据加载
	 * @param postData：JSON
	 * @return JSON
	 */
	public String artCatInfoLoad(String postData);
	
	/**
	 * 文章信息综合查询
	 * @param postData：JSON
	 * @return JSON
	 */
	public String artInfoQuery(String postData);
	
	/**
	 * 热门搜索关键字查询
	 * @param postData：JSON
	 * @return JSON
	 */
	public String artKeyHotQuery(String postData);
	
	/**
	 * 文章详情查询
	 * @param postData：JSON
	 * @return JSON
	 */
	public String artDetInfoQuery(String postData);
	
	/**
	 * 文章详情编辑
	 * @param postData：JSON
	 * @return JSON
	 */
	public String artDetInfoUpdate(String postData);
	
	/**
	 * 用户信息同步
	 * @param postData：JSON
	 * @return JSON
	 */
	public String userInfoSynchro(String postData);
	
}
