package com.lovelystudy.module.es.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.lovelystudy.module.es.pojo.TopicIndex;

/**
 * 
 */
public interface TopicIndexRepository extends ElasticsearchRepository<TopicIndex, Integer> {
}
