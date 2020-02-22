package com.lovelystudy.module.es.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.lovelystudy.module.es.pojo.TagIndex;

/**
 * 
 */
public interface TagIndexRepository extends ElasticsearchRepository<TagIndex, Integer> {
}
