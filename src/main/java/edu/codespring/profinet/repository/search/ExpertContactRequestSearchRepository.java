package edu.codespring.profinet.repository.search;


import edu.codespring.profinet.domain.ExpertContactRequest;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ExpertContactRequest entity.
 */
public interface ExpertContactRequestSearchRepository extends ElasticsearchRepository<ExpertContactRequest, Long> {
	
	List<ExpertContactRequest> findByUserRequest_loginAndExpertrequest_id(String username, Long expertid);
	
	Page<ExpertContactRequest> findByExpertrequest_id(Long id, Pageable pageable);

}
