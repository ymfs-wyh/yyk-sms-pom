package com.yyk333.sms.firing.repsository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yyk333.sms.firing.entities.Firing;

@Repository
public interface FiringRepository extends JpaRepository<Firing, Long>, JpaSpecificationExecutor<Firing> {

	@Query(value = "select * from yyk_firing_category yfc left join yyk_firing yf on yfc.cat_id = yf.cat_id "
			+ "where yfc.cat_key = ?1 and is_original_pic = 1 or yfc.cat_key = ?1 and "
			+ "resolution_ratio_width = ?2 and resolution_ratio_height = ?3 ", nativeQuery = true)
	public List<Firing> findByQueryVo(String catKey, int width, int height);

}
