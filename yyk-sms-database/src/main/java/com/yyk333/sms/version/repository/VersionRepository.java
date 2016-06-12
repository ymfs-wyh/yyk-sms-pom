package com.yyk333.sms.version.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.yyk333.sms.version.entities.Version;

@Repository
public interface VersionRepository extends CrudRepository<Version, Long> {

	@Query(value = "select * from yyk_version "
			+ "where status = ?1 and equipment_type = ?2 and "
			+ "version_code >= ?3 and app_type = ?4", nativeQuery = true)
	public Version findByQueryVo(Integer status, Integer equipmentType, String updateVersion,
			Integer appType);
	
	@Query(value = "select yv2.* from yyk_version yv1, yyk_version yv2 where yv1.id = yv2.pid "
			+ "and yv2.status = ?1 "
			+ "and yv1.equipment_type = ?2 "
			+ "and yv1.version_code = ?3 "
			+ "and yv1.app_type = ?4 ", nativeQuery = true)
	public Version findPVersionByQueryVo(Integer status, Integer equipmentType, String updateVersion,
			Integer appType);
	
	@Query(value = "SELECT * FROM yyk_version where status = 0 and equipment_type = ?1 and app_type = ?2 and version_code = (select max(version_code) lastVersion from yyk_version yv where status = 0 and equipment_type = ?1 and app_type = ?2) ", nativeQuery = true)
	public List<Version> findLastVersions(Integer equipmentType,
			Integer appType);
	
	@Query(value = "select * from yyk_version "
			+ "where status = ?1 and pid = ?2 ", nativeQuery = true)
	public Version findPatchVersion(Integer status, Long pid);

}
