package com.lovelystudy.module.security.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lovelystudy.module.security.pojo.AdminUser;

public interface AdminUserMapper {
	int count();

	int deleteByPrimaryKey(Integer id);

	// 自定义方法
	AdminUser findAdminUser(@Param("token") String token, @Param("username") String username);

	List<AdminUser> findAll(@Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize,
			@Param("orderBy") String orderBy);

	int insert(AdminUser record);

	int insertSelective(AdminUser record);

	AdminUser selectByPrimaryKey(Integer id);

	int updateByPrimaryKey(AdminUser record);

	int updateByPrimaryKeySelective(AdminUser record);
}