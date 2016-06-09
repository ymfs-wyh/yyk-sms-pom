package com.yyk333.sms.knowledge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yyk333.sms.knowledge.entities.UserArticleOption;


@Repository
public interface UserArticleOptionRepository
		extends JpaRepository<UserArticleOption, Long>, JpaSpecificationExecutor<UserArticleOption> {
	public UserArticleOption findByUserIdAndArtIdAndOptType(Long userId, Long artId, Integer optType);
}
