package com.yyk333.sms.knowledge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yyk333.sms.knowledge.entities.ArticleSearchRecord;

@Repository
public interface ArticleSearchRecordRepository
		extends JpaRepository<ArticleSearchRecord, Long>, JpaSpecificationExecutor<ArticleSearchRecord> {
	public ArticleSearchRecord findByKeyWord(String keyWord);
}
