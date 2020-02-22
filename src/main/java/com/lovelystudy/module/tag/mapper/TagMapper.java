package com.lovelystudy.module.tag.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lovelystudy.module.tag.pojo.Tag;

public interface TagMapper {
	int count();

	int deleteByPrimaryKey(Integer id);

	// 自定义方法
	List<Tag> findAll(@Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize,
			@Param("orderBy") String orderBy);

	Tag findByName(String name);

	List<Tag> findByNameLike(@Param("name") String name, @Param("pageNo") Integer pageNo,
			@Param("pageSize") Integer pageSize);

	List<Tag> findByTopicId(Integer topicId);

	int insert(Tag record);

	int insertSelective(Tag record);

	Tag selectByPrimaryKey(Integer id);

	int updateByPrimaryKey(Tag record);

	int updateByPrimaryKeySelective(Tag record);
}