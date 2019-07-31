package edu.codespring.profinet.repository.search;

import edu.codespring.profinet.domain.Keyword;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Keyword entity.
 */
public interface KeywordSearchRepository extends ElasticsearchRepository<Keyword, Long> {
	
	List<Keyword> findByNameLike(String name);
	
}
