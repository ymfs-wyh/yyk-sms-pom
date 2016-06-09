package com.yyk333.sms.knowledge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yyk333.sms.knowledge.entities.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

	/**
	 * 根据id和文章状态查询文章信息
	 * 
	 * @param artId
	 * @param status
	 * @return
	 */
	public Article findByArtIdAndStatus(Long artId, Integer status);

	/**
	 * 查询启用的最大文章id
	 * 
	 * @return
	 */
	@Query(value = "select max(id) from yyk_article where status = 1", nativeQuery = true)
	public Long findArtMaxId();

}
