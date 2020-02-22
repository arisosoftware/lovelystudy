package com.lovelystudy.module.security.mapper;

import java.util.List;

import com.lovelystudy.module.security.pojo.Permission;

public interface PermissionMapper {
	int deleteByPrimaryKey(Integer id);

	List<Permission> findByPid(Integer pid);

	List<Permission> findByPidGreaterThan(Integer pid);

	// 自定义方法
	List<Permission> findByUserId(Integer userId);

	int insert(Permission record);

	int insertSelective(Permission record);

	Permission selectByPrimaryKey(Integer id);

	int updateByPrimaryKey(Permission record);

	int updateByPrimaryKeySelective(Permission record);

}