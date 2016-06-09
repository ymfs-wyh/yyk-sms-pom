package com.yyk333.sms.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yyk333.sms.config.entities.Config;

public interface ConfigRepository extends JpaRepository<Config, String> {

}
