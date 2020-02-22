package com.lovelystudy.module.es.service;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.lovelystudy.module.es.pojo.TagIndex;
import com.lovelystudy.module.es.repository.TagIndexRepository;
import com.lovelystudy.module.tag.mapper.TagMapper;
import com.lovelystudy.module.tag.pojo.Tag;

/**
 * 
 */
@Service
public class TagSearchService {

	@Autowired
	private TagIndexRepository tagIndexRepository;
	@Autowired
	private TagMapper tagMapper;

	/**
	 * 清除所有的索引
	 */
	public void clearAll() {
		tagIndexRepository.deleteAll();
	}

	/**
	 * 根据id删除索引
	 *
	 * @param id
	 */
	public void deleteById(Integer id) {
		tagIndexRepository.deleteById(id);
	}

	/**
	 * 索引标签
	 *
	 * @param tag
	 */
	public void indexed(Tag tag) {
		TagIndex tagIndex = new TagIndex();
		BeanUtils.copyProperties(tag, tagIndex);
		tagIndexRepository.save(tagIndex);
	}

	/**
	 * 索引全部标签
	 */
	public void indexedAll() {
		List<TagIndex> tagIndices = new ArrayList<>();
		List<Tag> tags = tagMapper.findAll(null, null, null);
		if (tags.size() > 0) {
			tags.forEach(tag -> {
				TagIndex tagIndex = new TagIndex();
				BeanUtils.copyProperties(tag, tagIndex);
				tagIndices.add(tagIndex);
			});
			// 保存前先删除索引
			this.clearAll();
			tagIndexRepository.saveAll(tagIndices);
		}
	}

	/**
	 * 查询索引
	 *
	 * @param keyword
	 * @param limit
	 * @return
	 */
	public List<TagIndex> query(String keyword, Integer limit) {
		Pageable pageable = PageRequest.of(0, limit);
		// TODO fuzzyQuery模糊查询，即使写错了，也可以识别出来，但比较耗费性能，数据量多了不建议使用
		QueryBuilder queryBuilder = QueryBuilders.fuzzyQuery("name", keyword);
		SearchQuery query = new NativeSearchQueryBuilder().withPageable(pageable).withQuery(queryBuilder).build();
		return tagIndexRepository.search(query).getContent();
	}
}
