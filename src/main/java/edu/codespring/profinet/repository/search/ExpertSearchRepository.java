package edu.codespring.profinet.repository.search;

import edu.codespring.profinet.domain.Expert;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Modifying;

/**
 * Spring Data ElasticSearch repository for the Expert entity.
 */
public interface ExpertSearchRepository extends ElasticsearchRepository<Expert, Long> {

	List<Expert> findByExpertkeywords_name(String keywordname);
}
