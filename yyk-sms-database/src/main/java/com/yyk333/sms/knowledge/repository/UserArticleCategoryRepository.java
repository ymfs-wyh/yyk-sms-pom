package com.yyk333.sms.knowledge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yyk333.sms.knowledge.entities.UserArticleCategory;

@Repository
public interface UserArticleCategoryRepository
		extends JpaRepository<UserArticleCategory, Long>, JpaSpecificationExecutor<UserArticleCategory> {
	
	/**
	 * 根据userId查询UserArticleCategory实体
	 * @param userId
	 * @return
	 */
	public UserArticleCategory findByUserId(Long userId);
}
