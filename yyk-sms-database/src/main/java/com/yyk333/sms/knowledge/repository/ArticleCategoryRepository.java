package com.yyk333.sms.knowledge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yyk333.sms.knowledge.entities.ArticleCategory;

@Repository
public interface ArticleCategoryRepository
		extends JpaRepository<ArticleCategory, Long>, JpaSpecificationExecutor<ArticleCategory> {

	/**
	 * 根据userId查询用户定制栏目列表
	 */
	@Query(value = "select * from yyk_article_category where id in (:artCatIds) and status = :status order by cat_Order, update_Time desc, create_Time desc", nativeQuery = true)
	public List<ArticleCategory> findUserArticleCategoryList(@Param("artCatIds") String artCatIds,
			@Param("status") Integer status);

}
