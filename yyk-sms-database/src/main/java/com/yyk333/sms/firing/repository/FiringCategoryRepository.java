package com.yyk333.sms.firing.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.yyk333.sms.firing.entities.FiringCategory;

@Repository
public interface FiringCategoryRepository extends CrudRepository<FiringCategory, Long> {

}
