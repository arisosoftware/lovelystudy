package com.lovelystudy.module.es.service;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.lovelystudy.module.es.pojo.TopicIndex;
import com.lovelystudy.module.es.repository.TopicIndexRepository;
import com.lovelystudy.module.topic.mapper.TopicMapper;
import com.lovelystudy.module.topic.pojo.TopicWithBLOBs;
import com.lovelystudy.module.user.pojo.User;
import com.lovelystudy.module.user.service.UserService;

/**
 * 
 */
@Service
public class TopicSearchService {

	@Autowired
	private TopicIndexRepository topicIndexRepository;
	@Autowired
	private TopicMapper topicMapper;
	@Autowired
	private UserService userService;

	/**
	 * 清除所有的索引
	 */
	public void clearAll() {
		topicIndexRepository.deleteAll();
	}

	/**
	 * 根据id删除索引
	 *
	 * @param id
	 */
	public void deleteById(Integer id) {
		topicIndexRepository.deleteById(id);
	}

	/**
	 * 索引话题
	 *
	 * @param topic
	 * @param username
	 */
	public void indexed(TopicWithBLOBs topic, String username) {
		TopicIndex topicIndex = new TopicIndex();
		topicIndex.setId(topic.getId());
		topicIndex.setTitle(topic.getTitle());
		topicIndex.setTag(topic.getTag());
		topicIndex.setContent(topic.getContent());
		topicIndex.setInTime(topic.getInTime());
		topicIndex.setUsername(username);
		topicIndexRepository.save(topicIndex);
	}

	/**
	 * 索引全部话题
	 */
	public void indexedAll() {
		List<TopicIndex> topicIndices = new ArrayList<>();
		List<TopicWithBLOBs> topics = topicMapper.findAll();
		if (topics.size() > 0) {
			topics.forEach(topic -> {
				TopicIndex topicIndex = new TopicIndex();
				topicIndex.setId(topic.getId());
				topicIndex.setTitle(topic.getTitle());
				topicIndex.setTag(topic.getTag());
				topicIndex.setContent(topic.getContent());
				topicIndex.setInTime(topic.getInTime());

				User user = userService.findById(topic.getUserId());
				topicIndex.setUsername(user.getUsername());
				topicIndices.add(topicIndex);
			});
			// 保存前先删除索引
			this.clearAll();
			topicIndexRepository.saveAll(topicIndices);
		}
	}

	/**
	 * 查询索引
	 *
	 * @param keyword
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<TopicIndex> query(String keyword, Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.multiMatchQuery(keyword, "title", "content")));
		SearchQuery query = queryBuilder.withPageable(pageable).build();
		return topicIndexRepository.search(query);
	}
}
